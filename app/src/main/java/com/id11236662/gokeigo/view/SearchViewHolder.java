package com.id11236662.gokeigo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.id11236662.gokeigo.R;
import com.id11236662.gokeigo.data.Entry;

/**
 * This ViewHolder sets the GUI elements of a row with the values of the Entry-type object
 */
public class SearchViewHolder extends RecyclerView.ViewHolder {

    private final TextView mWordInKanjiKanaTextView;
    private final TextView mWordInRomajiTextView;

    public SearchViewHolder(View itemView) {
        super(itemView);
        // Initialise all the GUI elements from the xml layout of the one row
        mWordInKanjiKanaTextView = (TextView) itemView.findViewById(R.id.item_search_word_in_kanji_kana);
        mWordInRomajiTextView = (TextView) itemView.findViewById(R.id.item_search_word_in_romaji);
    }

    /**
     * Sets the GUI elements of a row with the values of entry
     * @param entry the object to bind to a View
     */
    public void bind(Entry entry) {
        mWordInKanjiKanaTextView.setText(entry.getWordInKanjiKana());
        mWordInRomajiTextView.setText(entry.getWordInLowerCaseRomaji());
    }
}
