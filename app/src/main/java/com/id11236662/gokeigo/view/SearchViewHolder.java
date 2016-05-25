package com.id11236662.gokeigo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.data.Word;

/**
 * This ViewHolder sets the GUI elements of a row with the values of the Word-type object
 */
public class SearchViewHolder extends RecyclerView.ViewHolder {

    private final TextView mWord;
    private final TextView mReading;

    public SearchViewHolder(View itemView) {
        super(itemView);
        // Initialise all the GUI elements from the xml layout of the one row
        mWord = (TextView) itemView.findViewById(R.id.item_search_word);
        mReading = (TextView) itemView.findViewById(R.id.item_search_reading);
    }

    /**
     * Sets the GUI elements of a row with the values of word
     * @param word the object to bind to a View
     */
    public void bind(Word word) {
        mWord.setText(word.getWord());
        mReading.setText(word.getReading());
    }
}
