package com.id11236662.gokeigo.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.Constants;

/**
 * This fragment displays a Recycler View list.
 * Shows a list of entries that have non-blank notes.
 */

public class NotesFragment extends Fragment {

    private final EntryManager mEntryManager = EntryManager.getInstance();
    private RecyclerView mRecyclerView;
    private EntryAdapter mAdapter;

    public NotesFragment() {

        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // Set title of the activity.

        CharSequence title = getString(R.string.title_notes);
        getActivity().setTitle(title);

        // Initialise the recycler view field.

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_notes_recycler_view);

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

        // Setup Recycler View and an adapter for it. Don't show date, do show the notes.

        FragmentActivity activity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new EntryAdapter(mEntryManager.getNoteAddedEntries(), false, true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        Log.d(Constants.TAG, "NotesFragment.onResume");
        super.onResume();

        // There's a chance the entries got edited upon return, such as the star states and note text.
        // Refresh the recycler view. It worked before, but not anymore. :(

        mAdapter.notifyDataSetChanged();
        mRecyclerView.invalidate();
        mRecyclerView.requestLayout();
    }
}
