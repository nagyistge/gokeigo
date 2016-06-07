package com.id11236662.gokeigo.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.model.EntryManager;
import com.id11236662.gokeigo.util.Constants;

import java.util.List;

/**
 * This adapter turns a list of Entry-type objects into Views for a RecyclerView.
 * These views are added, removed and moved around depending on the given list of objects.
 */
public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private List<Entry> mEntries;
    private int mRowLayoutId;

    public EntryAdapter(List<Entry> entries, int rowLayoutId) {
        mEntries = entries;
        mRowLayoutId = rowLayoutId;
    }

    /**
     * @param parent   The RecyclerView
     * @param viewType unused argument
     * @return a EntryViewHolder inflated with the row layout xml
     */

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mRowLayoutId, parent, false);
        return new EntryViewHolder(itemView);
    }

    /**
     * Binds the row with Entry-type object.
     *
     * @param holder   unused argument
     * @param position index of the row
     */

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        final Entry entry = mEntries.get(position);
        holder.bind(entry);
    }

    /**
     * Returns the total number of items in the entry set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    /**
     * Removes, adds and moves around Views so they correspond to the objects in the list.
     * The order is important to keep track of indexes.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredEntry list of objects that have already been filtered
     */

    public void animateTo(List<Entry> filteredEntry) {
        applyAndAnimateRemovals(filteredEntry);
        applyAndAnimateAdditions(filteredEntry);
        applyAndAnimateMovedItems(filteredEntry);
    }

    /**
     * Removes the View if its object IS NOT in the filtered list.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredEntry list of objects that have already been filtered
     */

    private void applyAndAnimateRemovals(List<Entry> filteredEntry) {
        for (int i = mEntries.size() - 1; i >= 0; i--) {
            final Entry data = mEntries.get(i);
            if (!filteredEntry.contains(data)) {
                removeItem(i);
            }
        }
    }

    /**
     * Adds the View if its object IS in the filtered list.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredEntry list of objects that have already been filtered
     */

    private void applyAndAnimateAdditions(List<Entry> filteredEntry) {
        for (int i = 0, count = filteredEntry.size(); i < count; i++) {
            final Entry data = filteredEntry.get(i);
            if (!mEntries.contains(data)) {
                addItem(i, data);
            }
        }
    }

    /**
     * Reorders the Views so they correspond with the originally ordered objects.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredEntry list of objects that have already been filtered
     */

    private void applyAndAnimateMovedItems(List<Entry> filteredEntry) {
        for (int toPosition = filteredEntry.size() - 1; toPosition >= 0; toPosition--) {
            final Entry data = filteredEntry.get(toPosition);
            final int fromPosition = mEntries.indexOf(data);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    /**
     * Removes the item from the list and notifies the Recycler View.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param position index in the list
     */

    private void removeItem(int position) {
        mEntries.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Adds the item from the list and notifies the Recycler View.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param position index in the list
     */

    private void addItem(int position, Entry data) {
        mEntries.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * Reorders the item in the list.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param fromPosition old index in the list
     * @param toPosition   new index in the list
     */

    private void moveItem(int fromPosition, int toPosition) {
        final Entry data = mEntries.remove(fromPosition);
        mEntries.add(toPosition, data);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * This ViewHolder sets the GUI elements of a row with the values of the Entry-type object.
     * Ensure it is static to avoid memory problems if more than one EntryViewHolder is created.
     */
    public static class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mWordTextView;
        private final TextView mReadingTextView;
        private final TextView mDefinitionTextView;
        private final TextView mCommonStatusTextView;
        private final TextView mRespectfulLevelTextView;
        private final TextView mHumbleLevelTextView;
        private final ImageView mStarredImageView;
        private final ImageView mUnstarredImageView;
        private Entry mEntry = null;

        public EntryViewHolder(final View itemView) {
            super(itemView);

            // Initialise all the GUI elements from the xml layout of the one row.

            mWordTextView = (TextView) itemView.findViewById(R.id.item_entry_word_text_view);
            mReadingTextView = (TextView) itemView.findViewById(R.id.item_entry_reading_text_view);
            mDefinitionTextView = (TextView) itemView.findViewById(R.id.item_entry_definition_text_view);
            mCommonStatusTextView = (TextView) itemView.findViewById(R.id.item_entry_common_status_text_view);
            mRespectfulLevelTextView = (TextView) itemView.findViewById(R.id.item_entry_respectful_level_text_view);
            mHumbleLevelTextView = (TextView) itemView.findViewById(R.id.item_entry_humble_level_text_view);

            // Initialise the imageview fields and set OnClickListener on them.

            mStarredImageView = (ImageView) itemView.findViewById(R.id.item_entry_starred_image_view);
            mStarredImageView.setOnClickListener(this);
            mUnstarredImageView = (ImageView) itemView.findViewById(R.id.item_entry_unstarred_image_view);
            mUnstarredImageView.setOnClickListener(this);

            // Set listener to the row.

            RelativeLayout row = (RelativeLayout) itemView.findViewById(R.id.item_entry);
            row.setOnClickListener(this);
        }

        /**
         * Sets the GUI elements of a row with the values of entry.
         *
         * @param entry the object to bind to a View
         */

        public void bind(Entry entry) {

            // Save the entry.

            mEntry = entry;

            // Set values to all the text views from entry.

            mWordTextView.setText(entry.getWord());
            mReadingTextView.setText(entry.getReading());
            mDefinitionTextView.setText(entry.getDefinitionToShow());

            // Show common text view if is common, hide if not common.

            if (entry.getIsCommon()) {
                mCommonStatusTextView.setVisibility(View.VISIBLE);
            } else {
                mCommonStatusTextView.setVisibility(View.INVISIBLE);
            }

            // Show the appropriate text view depending on the keigo level.

            if (entry.getIsRespectful()) {
                mRespectfulLevelTextView.setVisibility(View.VISIBLE);
            } else {
                mRespectfulLevelTextView.setVisibility(View.GONE);
            }

            // Show the appropriate text view depending on the keigo level.

            if (entry.getIsHumble()) {
                mHumbleLevelTextView.setVisibility(View.VISIBLE);
            } else {
                mHumbleLevelTextView.setVisibility(View.GONE);
            }

            // Set visibility of the star related image views depending on starred state of entry.

            setVisibleStarImage(mEntry.getIsStarred());
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.item_entry:

                    if (mEntry != null) {

                        // Start the EntryActivity with an intent that contains the selected Entry row
                        // as a parcelable Entry.

                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, EntryActivity.class);
                        intent.putExtra(Constants.INTENT_SELECTED_ENTRY, mEntry);
                        context.startActivity(intent);
                    }

                    break;

                case R.id.item_entry_starred_image_view:

                    if (mEntry != null) {

                        // Switch the starred state of the entry and save it.

                        mEntry.switchIsStarredState();
                        EntryManager.getInstance().saveEntry(mEntry);

                        // Since the starred image was clickable, hide it and replace it with the unstarred image.

                        setVisibleStarImage(false);
                    }

                    break;

                case R.id.item_entry_unstarred_image_view:

                    if (mEntry != null) {

                        // Switch the starred state of the entry and save it.

                        mEntry.switchIsStarredState();
                        EntryManager.getInstance().saveEntry(mEntry);

                        // Since the unstarred image was clickable, hide it and replace it with the starred image.

                        setVisibleStarImage(true);
                    }

                    break;
            }
        }

        /**
         * Set the visibility of the star image views. If starred image view should be visible,
         * hide the unstarred image view. And vice versa.
         *
         * @param isVisible the visibility state of the Search Results related views.
         */

        private void setVisibleStarImage(boolean isVisible) {

            // Set visibility states to the views.

            mStarredImageView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            mUnstarredImageView.setVisibility(isVisible ? View.GONE : View.VISIBLE);

            // Let the system explicitly know that the changed views have to be redrawn.

            mStarredImageView.invalidate();
            mStarredImageView.requestLayout();
            mUnstarredImageView.invalidate();
            mUnstarredImageView.requestLayout();
        }
    }
}
