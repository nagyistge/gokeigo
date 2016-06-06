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
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private EntryManager mEntryManager = EntryManager.getInstance();
    private RecyclerView mRecyclerView;
    private EntryAdapter mAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_history, container, false);


        // Set title of the activity.

        CharSequence title = getString(R.string.title_history);
        getActivity().setTitle(title);

        // Initialise the recycler view field.

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_history_recycler_view);

        // Add ItemDecoration to recycler view. // TODO: Move the DividerItemDecoration elsewhere if reused a lot.

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

        // Setup Recycler View and an adapter for it.

        FragmentActivity activity = getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new EntryAdapter(mEntryManager.getPreviouslyAccessedEntries(), R.layout.item_entry);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        Log.d(Constants.TAG, "HistoryFragment.onResume");
        super.onResume();

        // There's a change the entries got edited upon return, such as the star states.
        // Refresh the recycler view.

        mAdapter.notifyDataSetChanged();
        mRecyclerView.invalidate();
    }
}
