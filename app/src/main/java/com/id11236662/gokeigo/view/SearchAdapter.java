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

/**
 * This adapter turns a list of Entry-type objects into Views for a RecyclerView.
 * These Views are added, removed and moved around depending on the given list of Entries.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private final LayoutInflater mInflater;
    private List<Entry> mEntries;

    public SearchAdapter(Context context, List<Entry> entries) {
        mInflater = LayoutInflater.from(context);
        mEntries = new ArrayList<>(entries);
    }

    /**
     * @param parent The RecyclerView
     * @param viewType unused argument
     * @return a SearchViewHolder inflated with the row layout xml
     */
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(itemView);
    }

    /**
     * Binds the row with Entry-type object
     * @param holder unused argument
     * @param position index of the row
     */
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        final Entry entry = mEntries.get(position);
        holder.bind(entry);
    }

    /**
     * @return the total number of items in the list
     */
    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    /**
     * Removes, adds and moves around Views so they correspond to the objects in the list.
     * The order is important to keep track of indexes.
     * @param filteredEntries list of Entries that have already been filtered
     */
    public void animateTo(List<Entry> filteredEntries) {
        applyAndAnimateRemovals(filteredEntries);
        applyAndAnimateAdditions(filteredEntries);
        applyAndAnimateMovedItems(filteredEntries);
    }

    /**
     * Removes the View if its object IS NOT in the filtered list
     * @param filteredEntries list of Entries that have already been filtered
     */
    private void applyAndAnimateRemovals(List<Entry> filteredEntries) {
        for (int i = mEntries.size() - 1; i >= 0; i--) {
            final Entry entry = mEntries.get(i);
            if (!filteredEntries.contains(entry)) {
                removeItem(i);
            }
        }
    }

    /**
     * Adds the View if its object IS in the filtered list
     * @param filteredEntries list of Entries that have already been filtered
     */
    private void applyAndAnimateAdditions(List<Entry> filteredEntries) {
        for (int i = 0, count = filteredEntries.size(); i < count; i++) {
            final Entry entry = filteredEntries.get(i);
            if (!mEntries.contains(entry)) {
                addItem(i, entry);
            }
        }
    }

    /**
     * Reorders the Views so they correspond with the originally ordered objects
     * @param filteredEntries list of Entries that have already been filtered
     */
    private void applyAndAnimateMovedItems(List<Entry> filteredEntries) {
        for (int toPosition = filteredEntries.size() - 1; toPosition >= 0; toPosition--) {
            final Entry entry = filteredEntries.get(toPosition);
            final int fromPosition = mEntries.indexOf(entry);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    /**
     * Removes the item from the list and notifies the Recycler View
     * @param position index in the list
     */
    public void removeItem(int position) {
        mEntries.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Adds the item from the list and notifies the Recycler View
     * @param position index in the list
     */
    public void addItem(int position, Entry entry) {
        mEntries.add(position, entry);
        notifyItemInserted(position);
    }

    /**
     * Reorders the item in the list.
     * @param fromPosition old index in the list
     * @param toPosition new index in the list
     */
    public void moveItem(int fromPosition, int toPosition) {
        final Entry model = mEntries.remove(fromPosition);
        mEntries.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
