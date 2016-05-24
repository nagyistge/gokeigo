package com.id11236662.gokeigo.data;

import com.orm.SugarRecord;

/**
 * This class represents a container that holds a respectful/polite/humble word, its definition,
 * and a link to the polite version of the word if it isn't the polite version already.
 */
public class Entry extends SugarRecord {
    private String mWordInKanjiKana;
    private String mWordInLowerCaseRomaji;
//    private Long mPoliteWordId;
    private int mLevel;
    private String mDefinition;

    public Entry() {
        // Required empty public constructor so the table can be created
    }

    public Entry(String wordInKanjiKana, String wordInRomaji, int level, String definition) {
        mWordInKanjiKana = wordInKanjiKana;
        // Set the romaji in lower case for easier search filtering
        mWordInLowerCaseRomaji = wordInRomaji.toLowerCase();
        mLevel = level;
        mDefinition = definition;
    }

    public String getWordInKanjiKana() {
        return mWordInKanjiKana;
    }

    public String getWordInRomaji() {
        return mWordInLowerCaseRomaji;
    }

//    public Long getPoliteWordId() {
//        return mPoliteWordId;
//    }
//
//    public void setPoliteWord(Entry politeWord) {
//        assert politeWord != null;
//        mPoliteWordId = politeWord.getId();
//    }

    public int getLevel() {
        return mLevel;
    }

    public String getDefinition() {
        return mDefinition;
    }
}
