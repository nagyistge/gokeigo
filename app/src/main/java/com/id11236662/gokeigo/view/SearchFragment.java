package com.id11236662.gokeigo.view;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.controller.SearchAdapter;
import com.id11236662.gokeigo.model.EntriesResponse;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.util.ApiClient;
import com.id11236662.gokeigo.util.ApiInterface;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.MenuTint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * This fragment displays a Recycler View list.
 * Has a search action on the toolbar, which has query functionality implemented on it.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private TextView mResultsTextView;
    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private SearchView mSearchView;
    private String mSearchString;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Initialise textview field and recycler view and set an item decoration to recycler view.
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

        // Initialise the text view field
        mResultsTextView = (TextView) view.findViewById(R.id.fragment_search_results_text_view);

        // Initialise the recycler view field with the value before returning it.
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_recycler_view);

        // Add ItemDecoration to recycler view.
        mRecyclerView.addItemDecoration(new SearchAdapter.DividerItemDecoration(getActivity()));

        // If saved something on outState, can recover them here.
        if (savedInstanceState != null) {
            mSearchString = savedInstanceState.getString(Constants.SEARCH_KEYWORD);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSearchString = mSearchView.getQuery().toString();
        outState.putString(Constants.SEARCH_KEYWORD, mSearchString);
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
        mAdapter = new SearchAdapter(new ArrayList<Entry>(), R.layout.item_search);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        // Manually tint the search icon as it's provided separately from AppCompat.
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuTint.colorMenuItem(searchItem, Color.WHITE, null);

        // Set QueryTextListener on search view.
        // TODO: make the searchview longer. it's so short on tablets
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        // If mSearchString has been saved, restore it. Most likely the orientation got changed.
        if (mSearchString != null && !mSearchString.isEmpty()) {
            searchItem.expandActionView();
            mSearchView.setQuery(mSearchString, true);
        }
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param query the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener. TODO: revise this
     */
    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }

    /**
     * Called when the query text is submitted by the user.
     * Call the API to search up possible entries with the query text.
     *
     * @param query the new content of the query text field.
     * @return true, always.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        new SearchJishoAsyncTask().execute(query);
        return true;
    }

    /**
     * This AsyncTask calls the API to search up a keyword, and shows the returned results
     * on the recycler view.
     */
    private class SearchJishoAsyncTask extends AsyncTask<String, Void, List<Entry>> {

        private ProgressDialog mProgressDialog;

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
         * Call the API for a list of entries by supplying a keyword for the URI query.
         * Execute search process.
         *
         * @param params Keyword for the URI query.
         * @return list of entries retrieved from the API.
         */
        @Override
        protected List<Entry> doInBackground(String... params) {
            String keyword = params[0];
            // TODO: if not connected to the network... https://futurestud.io/blog/retrofit-2-simple-error-handling | https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<EntriesResponse> call = apiInterface.getEntries(keyword);
            try {
                return call.execute().body().getEntries();
            } catch (IOException e) {
                Log.e(Constants.TAG, e.toString());
            }
            return new ArrayList<>();
        }

        /**
         * Dismiss the progress dialogue after execution. Show the entries in the list and how
         * many there are.
         *
         * @param entries list of entries returned by doInBackground()
         */
        @Override
        protected void onPostExecute(List<Entry> entries) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            // Show the entries in the list.
            mAdapter.animateTo(entries);

            // Show how many entries were found.
            int results = entries.size();
            Locale currentLocale = Locale.ENGLISH; // TODO: Get from the preferences
            mResultsTextView.setText(String.format(currentLocale,
                    getString(R.string.message_entries_found), results));
        }
    }
}
