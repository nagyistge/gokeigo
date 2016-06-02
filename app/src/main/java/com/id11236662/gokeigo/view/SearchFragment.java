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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
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

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private RelativeLayout mSearchViewRelativeLayout;
    private SearchView mSearchView;
    private MenuItem mSearchActionMenuItem;
    private CheckBox mIncludeRespectfulCheckbox;
    private CheckBox mIncludeHumbleCheckbox;
    private RelativeLayout mSearchResultsRelativeLayout;
    private TextView mResultsTextView;
    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;

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

        // Set query hint.

        mSearchView.setQueryHint(getString(R.string.hint_search_query));

        // Set OnClickListener to the GO button.
        Button goButton = (Button) view.findViewById(R.id.fragment_search_go_button);
        goButton.setOnClickListener(this);

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

        // Manually tint the search icon as it's provided separately from AppCompat.

        mSearchActionMenuItem = menu.findItem(R.id.action_search);
        MenuTint.colorMenuItem(mSearchActionMenuItem, Color.WHITE, null);

        // Hide the layout that contains views which display the search results.

        SetVisibleSearchResults(false);

        // Set OnClick listener on search view.

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchActionMenuItem);
        searchView.setOnQueryTextListener(this);

        // Enable a submit button on the search view.

        searchView.setSubmitButtonEnabled(true);

        // Set query hint.

        searchView.setQueryHint(getString(R.string.hint_search_results_query));

        // Hide the Search Result Layout and show the Search View Layout again

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {

            /**
             * Called when a menu item is expanded.
             *
             * @param item Item that was expanded
             * @return true so the item expands
             */

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            /**
             * Called when a menu item is collapsed.
             *
             * @param item Item that was collapsed
             * @return true so the item collapses
             */

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        mSearchView.setQuery(searchView.getQuery(), false);
                        SetVisibleSearchResults(false);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Set the visibility of the search action menu, the search results layout and the search view
     * layout. If the search view layout is visible, hide the search results layout and vice versa.
     *
     * @param isVisible the visibility state of the Search Results related views.
     */
    private void SetVisibleSearchResults(boolean isVisible) {
        mSearchActionMenuItem.setVisible(isVisible);
        mSearchResultsRelativeLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mSearchViewRelativeLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);

        // Let the system explicitly know that the changed views have to be redrawn.

        mSearchResultsRelativeLayout.invalidate();
        mSearchResultsRelativeLayout.requestLayout();
        mSearchViewRelativeLayout.invalidate();
        mSearchViewRelativeLayout.requestLayout();
    }

    /**
     * Called when the query text is changed by the user. // TODO: Show past searches.
     *
     * @param query the new content of the query text field.
     * @return always true as the action is handled and overrides the default action.
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

            SetVisibleSearchResults(true);

            if (data != null) {

                // Show the data in the list.

                mAdapter.animateTo(data);

                // Show how many data were found.

                int number = data.size();
                Locale currentLocale = Locale.ENGLISH;
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
