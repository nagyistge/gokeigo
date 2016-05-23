package com.id11236662.gokeigo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private final LayoutInflater mInflater;
    private List<Entry> mEntries;

    public SearchAdapter(Context context, List<Entry> entries) {
        mInflater = LayoutInflater.from(context);
        mEntries = new ArrayList<>(entries);
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        final Entry entry = mEntries.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public void animateTo(List<Entry> entries) {
        applyAndAnimateRemovals(entries);
        applyAndAnimateAdditions(entries);
        applyAndAnimateMovedItems(entries);
    }

    private void applyAndAnimateRemovals(List<Entry> newEntries) {
        for (int i = mEntries.size() - 1; i >= 0; i--) {
            final Entry entry = mEntries.get(i);
            if (!newEntries.contains(entry)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Entry> newEntries) {
        for (int i = 0, count = newEntries.size(); i < count; i++) {
            final Entry entry = newEntries.get(i);
            if (!mEntries.contains(entry)) {
                addItem(i, entry);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Entry> newEntries) {
        for (int toPosition = newEntries.size() - 1; toPosition >= 0; toPosition--) {
            final Entry entry = newEntries.get(toPosition);
            final int fromPosition = mEntries.indexOf(entry);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Entry removeItem(int position) {
        final Entry entry = mEntries.remove(position);
        notifyItemRemoved(position);
        return entry;
    }

    public void addItem(int position, Entry entry) {
        mEntries.add(position, entry);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Entry model = mEntries.remove(fromPosition);
        mEntries.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
