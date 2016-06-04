package com.id11236662.gokeigo.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
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
        Log.d(Constants.TAG, "EntryAdapter.const - entries.size: " + entries.size());
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
     * This ViewHolder sets the GUI elements of a row with the values of the Entry-type object.
     * Ensure it is static to avoid memory problems if more than one EntryViewHolder is created.
     */
    public static class EntryViewHolder extends RecyclerView.ViewHolder {

        private final TextView mWordTextView;
        private final TextView mReadingTextView;
        private final TextView mDefinitionTextView;
        private final TextView mCommonStatusTextView;
        private final TextView mRespectfulLevelTextView;
        private final TextView mHumbleLevelTextView;
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

            // Set listener to the row.

            RelativeLayout row = (RelativeLayout) itemView.findViewById(R.id.item_entry);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEntry != null) {
                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, EntryActivity.class);
                        intent.putExtra(Constants.INTENT_SELECTED_ENTRY, mEntry);
                        context.startActivity(intent);
                    }
                }
            });
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
            mDefinitionTextView.setText(entry.getBlurb());

            // Show common text view if is common, hide if not common.

            if (entry.getIsCommon()) {
                mCommonStatusTextView.setVisibility(View.VISIBLE);
            } else {
                mCommonStatusTextView.setVisibility(View.GONE);
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
        }
    }
}
