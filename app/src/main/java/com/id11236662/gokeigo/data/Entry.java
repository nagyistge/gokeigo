package com.id11236662.gokeigo.data;

import com.orm.SugarRecord;

/**
 * This class represents an entry slide that contains a word, its definition, example sentences
 * and the respectful and humber forms of the word.
 */
public class Entry extends SugarRecord {
    private String mWordInKanjiKana;
    private String mWordInRomaji;
//    private String mDefinition;
//    private ArrayList<String> mExampleSentences;
//    private int mRespectfulEntryId;
//    private int mHumbleEntryId;

    public Entry(String wordInKanjiKana, String wordInRomaji) {
        mWordInKanjiKana = wordInKanjiKana;
        mWordInRomaji = wordInRomaji;
    }


    public String getWordInKanjiKana() {
        return mWordInKanjiKana;
    }

    public String getWordInRomaji() {
        return mWordInRomaji;
    }
}
