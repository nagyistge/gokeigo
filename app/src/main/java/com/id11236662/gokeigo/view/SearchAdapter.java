package com.id11236662.gokeigo.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.model.Entry;
import com.id11236662.gokeigo.model.ParcelableEntry;
import com.id11236662.gokeigo.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter turns a list of Entry-type objects into Views for a RecyclerView.
 * These Views are added, removed and moved around depending on the given list of objects.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Entry> mEntries;
    private int mRowLayoutId;

    public SearchAdapter(List<Entry> entries, int rowLayoutId) {
        mEntries = new ArrayList<>(entries);
        mRowLayoutId = rowLayoutId;
    }

    /**
     * @param parent   The RecyclerView
     * @param viewType unused argument
     * @return a SearchViewHolder inflated with the row layout xml
     */
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mRowLayoutId, parent, false);
        return new SearchViewHolder(itemView);
    }

    /**
     * Binds the row with Entry-type object.
     * @param holder   unused argument
     * @param position index of the row
     */
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        final Entry entry = mEntries.get(position);
        holder.bind(entry);
    }

    /**
     * @return the total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    /**
     * Removes, adds and moves around Views so they correspond to the objects in the list.
     * The order is important to keep track of indexes.
     * @param filteredEntries list of objects that have already been filtered
     */
    public void animateTo(List<Entry> filteredEntries) {
        applyAndAnimateRemovals(filteredEntries);
        applyAndAnimateAdditions(filteredEntries);
        applyAndAnimateMovedItems(filteredEntries);
    }

    /**
     * Removes the View if its object IS NOT in the filtered list.
     * @param filteredEntries list of objects that have already been filtered
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
     * Adds the View if its object IS in the filtered list.
     * @param filteredEntries list of objects that have already been filtered
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
     * Reorders the Views so they correspond with the originally ordered objects.
     * @param filteredEntries list of objects that have already been filtered
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
     * Removes the item from the list and notifies the Recycler View.
     * @param position index in the list
     */
    public void removeItem(int position) {
        mEntries.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Adds the item from the list and notifies the Recycler View.
     * @param position index in the list
     */
    public void addItem(int position, Entry entry) {
        mEntries.add(position, entry);
        notifyItemInserted(position);
    }

    /**
     * Reorders the item in the list.
     * @param fromPosition old index in the list
     * @param toPosition   new index in the list
     */
    public void moveItem(int fromPosition, int toPosition) {
        final Entry entry = mEntries.remove(fromPosition);
        mEntries.add(toPosition, entry);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * This ViewHolder sets the GUI elements of a row with the values of the Entry-type object.
     * Ensure it is static to avoid memory problems if more than one SearchViewHolder is created.
     */
    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        private final TextView mWordTextView;
        private final TextView mReadingTextView;
        private final TextView mDefinitionTextView;
        private final TextView mCommonStatusTextView;
        private Entry mEntry = null;

        public SearchViewHolder(final View itemView) {
            super(itemView);
            // Initialise all the GUI elements from the xml layout of the one row.
            mWordTextView = (TextView) itemView.findViewById(R.id.item_search_word);
            mReadingTextView = (TextView) itemView.findViewById(R.id.item_search_reading);
            mDefinitionTextView = (TextView) itemView.findViewById(R.id.item_search_definition);
            mCommonStatusTextView = (TextView) itemView.findViewById(R.id.item_search_common_status);

            // Set listener to the row.
            RelativeLayout row = (RelativeLayout) itemView.findViewById(R.id.item_search);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEntry != null) {
                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, EntryActivity.class);
                        ParcelableEntry parcelableEntry = ParcelableEntry.parse(mEntry);
                        intent.putExtra(Constants.INTENT_SELECTED_ENTRY, parcelableEntry);
                        context.startActivity(intent);
                    }
                }
            });
        }

        /**
         * Sets the GUI elements of a row with the values of entry.
         * @param entry the object to bind to a View
         */
        public void bind(Entry entry) {
            // Save the entry.
            mEntry = entry;

            mWordTextView.setText(entry.getWord());
            mReadingTextView.setText(entry.getReading());
            mDefinitionTextView.setText(entry.getDefinition());
            mCommonStatusTextView.setText(entry.getCommonStatus());
        }
    }

    /**
     * This ItemDecoration draws a generic line in-between items in a Recycler View.
     * Ensure it is static to avoid memory problems if more than one DividerItemDecoration
     * is created.
     */
    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            mDivider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
