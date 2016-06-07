package com.id11236662.gokeigo.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Data;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter turns a list of Data-type objects into Views for a RecyclerView.
 * These Views are added, removed and moved around depending on the given list of objects.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private final List<Data> mData;

    public DataAdapter(List<Data> data) {
        mData = new ArrayList<>(data);
    }

    /**
     * @param parent   The RecyclerView
     * @param viewType unused argument
     * @return a DataViewHolder inflated with the row layout xml
     */

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the row of the recycler view with the item xml.

        int mRowLayoutId = R.layout.item_data;
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mRowLayoutId, parent, false);

        return new DataViewHolder(itemView);
    }

    /**
     * Binds the row with Data-type object.
     *
     * @param holder   unused argument
     * @param position index of the row
     */

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        final Data data = mData.get(position);
        holder.bind(data);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Removes, adds and moves around Views so they correspond to the objects in the list.
     * The order is important to keep track of indexes.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredData list of objects that have already been filtered
     */

    public void animateTo(List<Data> filteredData) {
        applyAndAnimateRemovals(filteredData);
        applyAndAnimateAdditions(filteredData);
        applyAndAnimateMovedItems(filteredData);
    }

    /**
     * Removes the View if its object IS NOT in the filtered list.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredData list of objects that have already been filtered
     */

    private void applyAndAnimateRemovals(List<Data> filteredData) {
        for (int i = mData.size() - 1; i >= 0; i--) {
            final Data data = mData.get(i);
            if (!filteredData.contains(data)) {
                removeItem(i);
            }
        }
    }

    /**
     * Adds the View if its object IS in the filtered list.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredData list of objects that have already been filtered
     */

    private void applyAndAnimateAdditions(List<Data> filteredData) {
        for (int i = 0, count = filteredData.size(); i < count; i++) {
            final Data data = filteredData.get(i);
            if (!mData.contains(data)) {
                addItem(i, data);
            }
        }
    }

    /**
     * Reorders the Views so they correspond with the originally ordered objects.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param filteredData list of objects that have already been filtered
     */

    private void applyAndAnimateMovedItems(List<Data> filteredData) {
        for (int toPosition = filteredData.size() - 1; toPosition >= 0; toPosition--) {
            final Data data = filteredData.get(toPosition);
            final int fromPosition = mData.indexOf(data);
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
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Adds the item from the list and notifies the Recycler View.
     * Source: http://stackoverflow.com/a/30429439/1007496
     *
     * @param position index in the list
     */

    private void addItem(int position, Data data) {
        mData.add(position, data);
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
        final Data data = mData.remove(fromPosition);
        mData.add(toPosition, data);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * This ViewHolder sets the GUI elements of a row with the values of the Data-type object.
     * Ensure it is static to avoid memory problems if more than one DataViewHolder is created.
     */

    public static class DataViewHolder extends RecyclerView.ViewHolder {

        private final TextView mWordTextView;
        private final TextView mReadingTextView;
        private final TextView mDefinitionTextView;
        private final TextView mCommonStatusTextView;
        private final TextView mRespectfulLevelTextView;
        private final TextView mHumbleLevelTextView;
        private Data mData = null;

        public DataViewHolder(final View itemView) {
            super(itemView);

            // Initialise all the GUI elements from the xml layout of the one row.

            mWordTextView = (TextView) itemView.findViewById(R.id.item_data_word_text_view);
            mReadingTextView = (TextView) itemView.findViewById(R.id.item_data_reading_text_view);
            mDefinitionTextView = (TextView) itemView.findViewById(R.id.item_data_definition_text_view);
            mCommonStatusTextView = (TextView) itemView.findViewById(R.id.item_data_common_status_text_view);
            mRespectfulLevelTextView = (TextView) itemView.findViewById(R.id.item_data_respectful_level_text_view);
            mHumbleLevelTextView = (TextView) itemView.findViewById(R.id.item_data_humble_level_text_view);

            // Set listener to the row.

            RelativeLayout row = (RelativeLayout) itemView.findViewById(R.id.item_search);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mData != null) {
                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, EntryActivity.class);
                        Entry entry = Entry.parse(mData);
                        intent.putExtra(Constants.INTENT_SELECTED_ENTRY, entry);
                        context.startActivity(intent);
                    }
                }
            });
        }

        /**
         * Sets the GUI elements of a row with the values of data.
         *
         * @param data the object to bind to a View
         */

        public void bind(Data data) {

            // Save the data.

            mData = data;

            // Set values to all the text views from data.

            mWordTextView.setText(data.getWord());
            mReadingTextView.setText(data.getReading());
            mDefinitionTextView.setText(data.getDefinition());

            // Show common text view if is common, hide  if not common.

            if (data.getIsCommon()) {
                mCommonStatusTextView.setVisibility(View.VISIBLE);
            } else {
                mCommonStatusTextView.setVisibility(View.INVISIBLE);
            }

            // Show the appropriate text view depending on the keigo level.

            if (data.getIsRespectful()) {
                mRespectfulLevelTextView.setVisibility(View.VISIBLE);
            } else {
                mRespectfulLevelTextView.setVisibility(View.GONE);
            }

            // Show the appropriate text view depending on the keigo level.

            if (data.getIsHumble()) {
                mHumbleLevelTextView.setVisibility(View.VISIBLE);
            } else {
                mHumbleLevelTextView.setVisibility(View.GONE);
            }
        }
    }
}
