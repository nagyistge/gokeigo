package com.id11236662.gokeigo.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.UserDictionary;
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

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.data.Entry;
import com.id11236662.gokeigo.data.EntriesResponse;
import com.id11236662.gokeigo.util.ApiClient;
import com.id11236662.gokeigo.util.ApiInterface;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.MenuTint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This fragment displays a Recycler View list.
 * Has a search action on the toolbar, which has query functionality implemented on it.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    /**
     * @param inflater           used to inflate views in this fragment
     * @param container          this fragment
     * @param savedInstanceState unused argument
     * @return the view hierarchy associated with this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialise the recycler view field with the value before returning it
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_recycler_view);

        // Add ItemDecoration to recycler view
        mRecyclerView.addItemDecoration(new SearchAdapter.DividerItemDecoration(getActivity()));
        return view;
    }

    /**
     * @param view               returned result of onCreateView
     * @param savedInstanceState unused argument
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Display the actions on the toolbar
        setHasOptionsMenu(true);

        // Setup Recycler View
        FragmentActivity activity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present
        inflater.inflate(R.menu.main, menu);

        // Manually tint the search icon as it's provided separately from AppCompat
        final MenuItem item = menu.findItem(R.id.action_search);
        MenuTint.colorMenuItem(item, Color.WHITE, null);

        // Set QueryTextListener on search view
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
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
        // TODO: Make async call when fetching suggestions somehow
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
            // TODO: if not connected to the network...
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<EntriesResponse> call = apiInterface.getEntries(keyword);
            try {
                return call.execute().body().getData();
            } catch (IOException e) {
                Log.e(Constants.TAG, e.toString());
            }
            return null;
        }

        /**
         * Dismiss the progress dialogue after execution and set the adapter on to recycler view.
         *
         * @param entries list of entries returned by doInBackground()
         */
        @Override
        protected void onPostExecute(List<Entry> entries) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (entries != null) {
                SearchAdapter adapter = new SearchAdapter(entries, R.layout.item_search);
                mRecyclerView.setAdapter(adapter);
            } else {
                // TODO: show message that no results were found
            }
        }
    }
}
