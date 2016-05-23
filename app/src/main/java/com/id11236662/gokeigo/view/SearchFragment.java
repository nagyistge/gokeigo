package com.id11236662.gokeigo.view;

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

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.data.Entry;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private List<Entry> mEntries; // TODO: something tells me the entry manager should handle this

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Initialise the recycler view field with the value before returning it
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        FragmentActivity activity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        mEntries = new ArrayList<>(); // TODO: replace with entry manager
        mEntries.add(new Entry("見る", "miru"));
        mEntries.add(new Entry("行く", "iku"));
        mEntries.add(new Entry("書く", "kaku"));
        mEntries.add(new Entry("来る", "kuru"));

        mAdapter = new SearchAdapter(activity, mEntries);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
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
        final List<Entry> filteredEntryList = filter(mEntries, query);
        mAdapter.animateTo(filteredEntryList);
        // Ensure user can always see all items when searching for something
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Entry> filter(List<Entry> entries, String query) {
        query = query.toLowerCase();

        final List<Entry> filteredEntryList = new ArrayList<>();
        for (Entry entry : entries) {
            final String kanjiKana = entry.getWordInKanjiKana();
            final String romaji = entry.getWordInRomaji();
            if (kanjiKana.contains(query) || romaji.contains(query)) {
                filteredEntryList.add(entry);
            }
        }
        return filteredEntryList;
    }
}
