package com.id11236662.gokeigo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.id11236662.gokeigo.data.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter turns a list of Word-type objects into Views for a RecyclerView.
 * These Views are added, removed and moved around depending on the given list of models.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private List<Word> mModels;
    private int mRowLayoutId;

    public SearchAdapter(List<Word> models, int rowLayoutId) {
        mModels = new ArrayList<>(models);
        mRowLayoutId = rowLayoutId;
    }

    /**
     * @param parent The RecyclerView
     * @param viewType unused argument
     * @return a SearchViewHolder inflated with the row layout xml
     */
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(mRowLayoutId, parent, false);
        return new SearchViewHolder(itemView);
    }

    /**
     * Binds the row with Word-type object
     * @param holder unused argument
     * @param position index of the row
     */
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        final Word word = mModels.get(position);
        holder.bind(word);
    }

    /**
     * @return the total number of items in the list
     */
    @Override
    public int getItemCount() {
        return mModels.size();
    }

    /**
     * Removes, adds and moves around Views so they correspond to the objects in the list.
     * The order is important to keep track of indexes.
     * @param filteredModels list of models that have already been filtered
     */
    public void animateTo(List<Word> filteredModels) {
        applyAndAnimateRemovals(filteredModels);
        applyAndAnimateAdditions(filteredModels);
        applyAndAnimateMovedItems(filteredModels);
    }

    /**
     * Removes the View if its object IS NOT in the filtered list
     * @param filteredModels list of models that have already been filtered
     */
    private void applyAndAnimateRemovals(List<Word> filteredModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Word word = mModels.get(i);
            if (!filteredModels.contains(word)) {
                removeItem(i);
            }
        }
    }

    /**
     * Adds the View if its object IS in the filtered list
     * @param filteredModels list of models that have already been filtered
     */
    private void applyAndAnimateAdditions(List<Word> filteredModels) {
        for (int i = 0, count = filteredModels.size(); i < count; i++) {
            final Word word = filteredModels.get(i);
            if (!mModels.contains(word)) {
                addItem(i, word);
            }
        }
    }

    /**
     * Reorders the Views so they correspond with the originally ordered objects
     * @param filteredModels list of models that have already been filtered
     */
    private void applyAndAnimateMovedItems(List<Word> filteredModels) {
        for (int toPosition = filteredModels.size() - 1; toPosition >= 0; toPosition--) {
            final Word word = filteredModels.get(toPosition);
            final int fromPosition = mModels.indexOf(word);
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
        mModels.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Adds the item from the list and notifies the Recycler View
     * @param position index in the list
     */
    public void addItem(int position, Word word) {
        mModels.add(position, word);
        notifyItemInserted(position);
    }

    /**
     * Reorders the item in the list.
     * @param fromPosition old index in the list
     * @param toPosition new index in the list
     */
    public void moveItem(int fromPosition, int toPosition) {
        final Word model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
