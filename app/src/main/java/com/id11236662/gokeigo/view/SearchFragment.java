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
import com.id11236662.gokeigo.model.DataResponse;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.JishoClient;
import com.id11236662.gokeigo.util.JishoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * This fragment displays a Recycler View list.
 * Has a search action on the toolbar, which has query functionality implemented on it.
 */

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private RelativeLayout mSearchViewRelativeLayout;
    private SearchView mSearchView;
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

        mSearchView = (SearchView) view.findViewById(R.id.fragment_search_view);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnClickListener(this);

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

        mRecyclerView.addItemDecoration(new DataAdapter.DividerItemDecoration(getActivity()));

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
        mAdapter = new DataAdapter(new ArrayList<Data>(), R.layout.item_data);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        inflater.inflate(R.menu.options, menu);

        // Initialise the menu item field, the one that contains the search view on the toolbar.

        mSearchViewActionMenuItem = menu.findItem(R.id.action_search);

        // Hide the layout that contains views which display the search results.

        setVisibleSearchResults(false);

        // Set OnClick listener on search view.

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchViewActionMenuItem);
        searchView.setOnQueryTextListener(this);

        // Enable a submit button on the search view.

        searchView.setSubmitButtonEnabled(true);

        // Set an arbitrary high number for the width of the search view so it expands properly on tablets.
        // Source: http://stackoverflow.com/a/34050959/1007496

        searchView.setMaxWidth(Integer.MAX_VALUE);
    }

    /**
     * Set the visibility of the search action menu, the search results layout and the search view
     * layout. If the search view layout is visible, hide the search results layout and vice versa.
     * Also show the appropriate title.
     *
     * @param isVisible the visibility state of the Search Results related views.
     */

    private void setVisibleSearchResults(boolean isVisible) {

        // Set visibilty states to the views.

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
     *
     * @param query the new content of the query text field.
     * @return always true as the action is handled and overrides the default action.
     */

    @Override
    public boolean onQueryTextSubmit(String query) {
        Activity activity = getActivity();
        if (isDeviceOnline(activity)) {
            boolean includeRespectful = mIncludeRespectfulCheckbox.isChecked();
            boolean includeHumble = mIncludeHumbleCheckbox.isChecked();
            new SearchJishoAsyncTask(includeRespectful, includeHumble, query).execute();
        } else {
            Toast.makeText(activity, R.string.message_no_network_available, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
     * Check if the device has any network connectivity
     * @param activity to get the system's connectivity service from.
     * @return true is there is network connectivity. False if there is none.
     */
    private static boolean isDeviceOnline(Activity activity) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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

                // Expands the search view no matter where you click it.

                mSearchView.onActionViewExpanded();
                break;
            case R.id.fragment_search_go_button:

                // Submit the query in the main search view.

                mSearchView.setQuery(mSearchView.getQuery(), true);
                break;
        }
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

            // If "Include Respectful" option has been checked, grab respectful-related entries.

            if (mIncludeRespectful) {
                Call<DataResponse> call = jishoService.getData(Constants.KEYWORD_PREFIX_RESPECTFUL + mQuery);
                try {
                    results.addAll(call.execute().body().getData());
                } catch (IOException e) {
                    return null;
                }
            }

            // If "Include Humble" option has been checked, grab humble-related entries.

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

            setVisibleSearchResults(true);

            if (data != null) {

                // Show the data in the list.

                mAdapter.animateTo(data);

                // Show how many data were found.

                int number = data.size();
                Locale currentLocale = getResources().getConfiguration().locale; // TODO: get from settings.
                String results = String.format(currentLocale, getString(R.string.message_entries_found_for), mQuery) +
                        ": " + String.format(currentLocale, getString(R.string.message_number_of_entries), number);
                mResultsTextView.setText(results);
            } else {

                // If data is null, it's because there's an exception in calling the server.

                mResultsTextView.setText(R.string.message_search_exception);
            }
        }
    }
}
