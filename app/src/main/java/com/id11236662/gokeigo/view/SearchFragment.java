package com.id11236662.gokeigo.view;

import android.graphics.Color;
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

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.data.Entry;
import com.id11236662.gokeigo.data.EntriesResponse;
import com.id11236662.gokeigo.util.ApiClient;
import com.id11236662.gokeigo.util.ApiInterface;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.MenuTint;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This fragment displays a Recycler View list and maintains a Entry Manager that manages entries
 * in the application. Added a search action on to the toolbar and implemented query functionality
 * on to it. The search is a filter type.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    /**
     * @param inflater used to inflate views in this fragment
     * @param container this fragment
     * @param savedInstanceState unused argument
     * @return the view hierarchy associated with this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Initialise the recycler view field with the value before returning it
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_recycler_view);
        // Add ItemDecoration to recycler view
        mRecyclerView.addItemDecoration(new SearchAdapter.DividerItemDecoration(getActivity()));
        return view;
    }

    /**
     * @param view returned result of onCreateView
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

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EntriesResponse> call = apiInterface.getEntries("miru");
        call.enqueue(new Callback<EntriesResponse>() {
            @Override
            public void onResponse(Call<EntriesResponse> call, Response<EntriesResponse> response) {
                List<Entry> entries = response.body().getData();
                mAdapter = new SearchAdapter(entries, R.layout.item_search);
                mRecyclerView.setAdapter(mAdapter);
                Log.d(Constants.TAG, "Number of entries received: " + entries.size());
            }

            @Override
            public void onFailure(Call<EntriesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
            }
        });
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
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String query) {
        // TODO: Make async call when fetching information
//        final List<Entry> filteredWordList = filter(mEntryManager.getEntries(), query);
//        mAdapter.animateTo(filteredWordList);
//        // Ensure user can always see all items when searching for something
//        mRecyclerView.scrollToPosition(0);
//        return true;
        return false;
    }

    /**
     * Called when the query text is submitted by the user. Does nothing special though.
     * @param query the new content of the query text field.
     * @return false, always.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO: Make async call when fetching information
        return false;
    }

    /**
     * Filters entries with the provided query string
     * @param entries list of entries to filter through
     * @param query the string to match with any part of the kanji/kana or romaji of every entry
     * @return a list of entries that have been filtered
     */
    private static List<Entry> filter(List<Entry> entries, String query) {
        query = query.toLowerCase();

        final List<Entry> filteredEntryList = new ArrayList<>();
//        for (Entry word : entries) {
//            final String kanjiKana = word.getWordInKanjiKana();
//            final String romaji = word.getWordInLowerCaseRomaji();
//            if (kanjiKana.contains(query) || romaji.contains(query)) {
//                filteredEntryList.add(word);
//            }
//        }
        return filteredEntryList;
    }
}
