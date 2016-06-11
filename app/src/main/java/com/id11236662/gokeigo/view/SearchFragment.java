package com.id11236662.gokeigo.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.Constants;

import java.util.List;

/**
 * This fragment displays a Recycler View list.
 * Has a search action on the toolbar, which has query functionality implemented on it.
 */

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private EntryAdapter mAdapter;

    public SearchFragment() {
        // Required empty public constructor.
    }

    /**
     * Check if the device has any network connectivity
     *
     * @param activity to get the system's connectivity service from.
     * @return true is there is network connectivity. False if there isn't.
     */

    private static boolean isDeviceOnline(Activity activity) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Initialise search view field, checkbox fields, textview field, and recycler view field
     * and set an item decoration to recycler view.
     *
     * @param inflater           used to inflate views in this fragment
     * @param container          this fragment
     * @param savedInstanceState unused argument
     * @return the view hierarchy associated with this fragment
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment.

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialise the recycler view field.

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_recycler_view);

        // Add ItemDecoration to recycler view.

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        return view;
    }

    /**
     * @param view               returned result of onCreateView
     * @param savedInstanceState unused argument
     */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Display the actions, which is just a search action, on the toolbar.

        setHasOptionsMenu(true);

        // Set activity title.

        FragmentActivity activity = getActivity();
        activity.setTitle(getString(R.string.app_name));

        // Setup Recycler View and an adapter that fetches entries for it.

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        // Try to fetch entries.

        if (isDeviceOnline(activity)) {

            // If the device is connected to the Internet, fetch the list of respectful and honourable words.

            new FetchJishoEntriesAsyncTask().execute();

        } else {

            // If the device is offline, notify the user.

            Toast.makeText(activity, R.string.message_no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.options, menu);

        // Initialise the menu item field, the one that contains the search view on the toolbar.

        MenuItem mSearchViewActionMenuItem = menu.findItem(R.id.action_search);

        // Set OnQueryTextListener listener on search view.

        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchViewActionMenuItem);
        mSearchView.setOnQueryTextListener(this);

        // Set an arbitrary high number for the width of the search view so it expands properly on tablets.
        // Source: http://stackoverflow.com/a/34050959/1007496

        mSearchView.setMaxWidth(Integer.MAX_VALUE);
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param query the new content of the query text field.
     * @return always true as the default action should not be used.
     */

    @Override
    public boolean onQueryTextChange(String query) {
        Log.d(Constants.TAG, "onQueryTextChange");

        final List<Entry> filteredEntryList = EntryManager.getInstance().filterEntries(query);
        mAdapter.animateTo(filteredEntryList);

        // Ensure user can always see all items when searching for something.

        mRecyclerView.scrollToPosition(0);

        return true;
    }

    /**
     * Called when the query text is submitted by the user.
     *
     * @param query the new content of the query text field.
     * @return always false as the default action should be used.
     */

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * This AsyncTask calls the API to search up a keyword, and shows the returned results
     * on the recycler view.
     */

    private class FetchJishoEntriesAsyncTask extends AsyncTask<Void, Void, List<Entry>> {

        private ProgressDialog mProgressDialog;

        /**
         * Before executing new thread, show dialogue that the search process will soon execute.
         */

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.message_downloading) + "...");
            mProgressDialog.show();
        }

        /**
         * Use the entry manager singleton to fetch entries relating to the search query.
         *
         * @param params not used
         * @return list of entries retrieved from the entry manager singleton. If null, there's been an exception.
         */

        @Override
        protected List<Entry> doInBackground(Void... params) {

            return EntryManager.getInstance().fetchKeigoEntries();
        }

        /**
         * Dismiss the progress dialogue after execution. Show the data in the list and how
         * many there are. If something's wrong with the result, show 'cannot load results' message.
         *
         * @param entries list of data returned by doInBackground()
         */

        @Override
        protected void onPostExecute(List<Entry> entries) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            // Show the entries in the list adapter. and set the adapter to the view.

            mAdapter = new EntryAdapter(entries, false, false);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
