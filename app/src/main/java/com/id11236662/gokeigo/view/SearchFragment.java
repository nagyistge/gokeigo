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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Data;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This fragment displays a Recycler View list.
 * Has a search action on the toolbar, which has query functionality implemented on it.
 */

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private RelativeLayout mSearchViewRelativeLayout;
    private SearchView mSearchOnView;
    private SearchView mSearchOnResults;
    private MenuItem mSearchViewActionMenuItem;
    private CheckBox mIncludeRespectfulCheckbox;
    private CheckBox mIncludeHumbleCheckbox;
    private RelativeLayout mSearchResultsRelativeLayout;
    private TextView mResultsTextView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;

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

        // Initialise the relative layout fields.

        mSearchViewRelativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_search_view_relative_layout);
        mSearchResultsRelativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_search_results_relative_layout);

        // Initialise the search view field and set OnQueryTextListener and OnClickListener to it.

        mSearchOnView = (SearchView) view.findViewById(R.id.fragment_search_view);
        mSearchOnView.setOnQueryTextListener(this);
        mSearchOnView.setOnClickListener(this);

        // Set OnClickListener to the GO button.

        Button goButton = (Button) view.findViewById(R.id.fragment_search_go_button);
        goButton.setOnClickListener(this);

        // Initialise the check box fields.

        mIncludeRespectfulCheckbox = (CheckBox) view.findViewById(R.id.fragment_search_include_respectful_check_box);
        mIncludeHumbleCheckbox = (CheckBox) view.findViewById(R.id.fragment_search_include_humble_check_box);

        // Initialise the text view field.

        mResultsTextView = (TextView) view.findViewById(R.id.fragment_search_results_text_view);

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

        // Setup Recycler View and an adapter for it.

        FragmentActivity activity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new DataAdapter(new ArrayList<Data>());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.options, menu);

        // Initialise the menu item field, the one that contains the search view on the toolbar.

        mSearchViewActionMenuItem = menu.findItem(R.id.action_search);

        // Set OnClick listener on search view.

        mSearchOnResults = (SearchView) MenuItemCompat.getActionView(mSearchViewActionMenuItem);
        mSearchOnResults.setOnQueryTextListener(this);

        // Set an arbitrary high number for the width of the search view so it expands properly on tablets.
        // Source: http://stackoverflow.com/a/34050959/1007496

        mSearchOnResults.setMaxWidth(Integer.MAX_VALUE);

        // Hide the layout that contains views which display the search results.

        setVisibleSearchResults(false);
    }

    @Override
    public void onStop() {
        // TODO: Figure out how to hide the action query screen when home is pressed ... else, if it's really too hard, just make a new activity omg
        // Easier to go back to search results too! Yeah, just make a new activity
        Log.d(Constants.TAG, "onStop");
        super.onStop();
    }

    /**
     * Set the visibility of the search action menu, the search results layout and the search view
     * layout. If the search view layout is visible, hide the search results layout and vice versa.
     * Also show the appropriate title.
     *
     * @param isVisible the visibility state of the Search Results related views.
     */

    private void setVisibleSearchResults(boolean isVisible) {

        // If still on the view screen, persist the search query by passing the query from
        // the search on the search view screen to the search on the search result screen.

        if (mSearchViewRelativeLayout.getVisibility() == View.VISIBLE) {
            mSearchViewActionMenuItem.expandActionView();
            mSearchOnResults.setQuery(mSearchOnView.getQuery(), false);

            // Clear the focus after expanding and setting the query to get rid of the keyboard.

            mSearchOnResults.clearFocus();
        }

        // Set visibility states to the views.

        mSearchViewActionMenuItem.setVisible(isVisible);
        mSearchResultsRelativeLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mSearchViewRelativeLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);

        // Set title of the activity.

        CharSequence title = isVisible ? getString(R.string.title_search_results) : getString(R.string.app_name);
        getActivity().setTitle(title);

        // Let the system explicitly know that the changed views have to be redrawn.

        mSearchResultsRelativeLayout.invalidate();
        mSearchResultsRelativeLayout.requestLayout();
        mSearchViewRelativeLayout.invalidate();
        mSearchViewRelativeLayout.requestLayout();
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param query the new content of the query text field.
     * @return always false as the default action should be used.
     */

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    /**
     * Called when the query text is submitted by the user.
     * Call the API to search up possible entries with the query text.
     * Save all submitted queries.
     *
     * @param query the new content of the query text field.
     * @return always true as the action is handled and overrides the default action.
     */

    @Override
    public boolean onQueryTextSubmit(String query) {

        Activity activity = getActivity();

        if (isDeviceOnline(activity)) {

            // If the device is connected to the Internet, search with the keigo level filters
            // considered in another thread.

            boolean includeRespectful = mIncludeRespectfulCheckbox.isChecked();
            boolean includeHumble = mIncludeHumbleCheckbox.isChecked();
            new SearchJishoAsyncTask(includeRespectful, includeHumble, query).execute();
        } else {

            // If the device is offline, notify the user.

            Toast.makeText(activity, R.string.message_no_connection, Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_search_view:

                // Expand the search view no matter where you click it.

                mSearchOnView.onActionViewExpanded();
                break;
            case R.id.fragment_search_go_button:

                // Submit the query in the main search view.

                mSearchOnView.setQuery(mSearchOnView.getQuery(), true);
                break;
        }
    }

    /**
     * This AsyncTask calls the API to search up a keyword, and shows the returned results
     * on the recycler view.
     */

    private class SearchJishoAsyncTask extends AsyncTask<Void, Void, List<Data>> {

        private final boolean mIncludeRespectful;
        private final boolean mIncludeHumble;
        private final String mQuery;
        private ProgressDialog mProgressDialog;

        public SearchJishoAsyncTask(boolean includeRespectful, boolean includeHumble, String query) {
            mIncludeRespectful = includeRespectful;
            mIncludeHumble = includeHumble;
            mQuery = query;
        }

        /**
         * Before executing new thread, show dialogue that the search process will soon execute.
         */

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.message_searching) + "...");
            mProgressDialog.show();
        }

        /**
         * Use the entry manager singleton to fetch entries relating to the search query.
         *
         * @param params not used
         * @return list of entries retrieved from the entry manager singleton. If null, there's been an exception.
         */

        @Override
        protected List<Data> doInBackground(Void... params) {

            return EntryManager.getInstance().searchData(mIncludeRespectful, mIncludeHumble, mQuery);
        }

        /**
         * Dismiss the progress dialogue after execution. Show the data in the list and how
         * many there are. If something's wrong with the result, show 'cannot load results' message.
         *
         * @param data list of data returned by doInBackground()
         */

        @Override
        protected void onPostExecute(List<Data> data) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (data != null) {
                Log.d(Constants.TAG, "AsyncTask.onPostExecute - data is not null");

                // Show the data in the list.

                mAdapter.animateTo(data);

                // Show how many data were found.

                int number = data.size();
                Locale currentLocale = getResources().getConfiguration().locale;
                String results = String.format(currentLocale, getString(R.string.message_number_of_entries), number);
                mResultsTextView.setText(results);
            } else {
                Log.d(Constants.TAG, "AsyncTask.onPostExecute - data is null");

                // If data is null, something's wrong with the data so it cannot be loaded.

                mResultsTextView.setText(R.string.message_cannot_load_results);
            }

            // Show the search result-related views.

            setVisibleSearchResults(true);
        }
    }
}
