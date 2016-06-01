package com.id11236662.gokeigo.view;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Data;
import com.id11236662.gokeigo.model.DataResponse;
import com.id11236662.gokeigo.util.ActivityConfigurator;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.JishoClient;
import com.id11236662.gokeigo.util.JishoService;
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

    private CheckBox mIncludeRespectfulCheckbox;
    private CheckBox mIncludeHumbleCheckbox;
    private TextView mResultsTextView;
    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;

    public SearchFragment() {
        // Required empty public constructor.
    }

    /**
     * Initialise checkbox fields, textview field, and recycler view field
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

        // Initialise the check box fields.

        mIncludeRespectfulCheckbox = (CheckBox) view.findViewById(R.id.fragment_search_include_respectful_check_box);
        mIncludeHumbleCheckbox = (CheckBox) view.findViewById(R.id.fragment_search_include_humble_check_box);

        // Initialise the text view field.

        mResultsTextView = (TextView) view.findViewById(R.id.fragment_search_results_text_view);

        // Initialise the recycler view field with the value before returning it.

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_recycler_view);

        // Add ItemDecoration to recycler view.

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

        // Display the actions, which is just a search action, on the toolbar.

        setHasOptionsMenu(true);

        // Setup Recycler View and an adapter for it.

        FragmentActivity activity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new SearchAdapter(new ArrayList<Data>(), R.layout.item_search);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.main, menu);
        menu.setGroupVisible(R.id.main_search_group, true);
        menu.setGroupVisible(R.id.main_save_group, false);

        // Manually tint the search icon as it's provided separately from AppCompat.

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuTint.colorMenuItem(searchItem, Color.WHITE, null);

        // Set QueryTextListener on search view.

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        // TODO: make the searchview longer. it's so short on tablets

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
        Activity activity = getActivity();
        if (ActivityConfigurator.isDeviceOnline(activity)) {
            boolean includeRespectful = mIncludeRespectfulCheckbox.isChecked();
            boolean includeHumble = mIncludeHumbleCheckbox.isChecked();
            new SearchJishoAsyncTask(includeRespectful, includeHumble, query).execute();
        } else {
            Toast.makeText(activity, R.string.message_no_network_available, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
     * This AsyncTask calls the API to search up a keyword, and shows the returned results
     * on the recycler view.
     */

    private class SearchJishoAsyncTask extends AsyncTask<Void, Void, List<Data>> {

        private ProgressDialog mProgressDialog;
        private boolean mIncludeRespectful;
        private boolean mIncludeHumble;
        private String mQuery;

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
         * Call the API for a list of entries by supplying a keyword for the URI query.
         * Execute search process.
         *
         * @param params Keyword for the URI query.
         * @return list of entries retrieved from the API. If null, there's been an exception.
         */

        @Override
        protected List<Data> doInBackground(Void... params) {

            // If none of the options is checked, don't bother searching and return 0 results.

            List<Data> results = new ArrayList<>();

            // Create client to access jisho's web service.

            JishoService jishoService = JishoClient.getClient().create(JishoService.class);

            // If "Include Respectful" option has been checked, grab relevant entries about it.

            if (mIncludeRespectful) {
                Call<DataResponse> call = jishoService.getData(Constants.KEYWORD_PREFIX_RESPECTFUL + mQuery);
                try {
                    results.addAll(call.execute().body().getData());
                } catch (IOException e) {
                    return null;
                }
            }

            // If "Include Humble" option has been checked, grab relevant entries about it.

            if (mIncludeHumble) {
                Call<DataResponse> call = jishoService.getData(Constants.KEYWORD_PREFIX_HUMBLE + mQuery);
                try {
                    results.addAll(call.execute().body().getData());
                } catch (IOException e) {
                    return null;
                }
            }

            return results;
        }

        /**
         * Dismiss the progress dialogue after execution. Show the data in the list and how
         * many there are.
         *
         * @param data list of data returned by doInBackground()
         */

        @Override
        protected void onPostExecute(List<Data> data) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (data != null) {

                // Show the data in the list.

                mAdapter.animateTo(data);

                // Show how many data were found.

                int results = data.size();
                Locale currentLocale = Locale.ENGLISH;
                String message1 = String.format(currentLocale, getString(R.string.message_entries_found_for), mQuery);
                String message2 = String.format(currentLocale, getString(R.string.message_number_of_entries), results);
                mResultsTextView.setText((message1 + ": " + message2));
            } else {

                // If data is null, it's set to that when there's an exception.
                // Most likely network-related

                mResultsTextView.setText(R.string.message_no_network_available);
            }
        }
    }
}
