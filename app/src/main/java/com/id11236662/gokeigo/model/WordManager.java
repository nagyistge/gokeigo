package com.id11236662.gokeigo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class manages CRUD operations in relation to Words
 */
public class WordManager {
    private static WordManager mInstance = null;
    private List<Word> mWords;

    private WordManager() {
        // Hide constructor to ensure singleton pattern
    }

    public static WordManager getInstance() {
        if (mInstance == null) {
            mInstance = new WordManager();
        }
        return mInstance;
    }

    public void addWord(String wordInKanjiKana, String wordInRomaji, String definition) {
//        Word word = new Word();
//        getWords().add(word);
//        word.insert();
    }

    /**
     * @return a list of Words from the DB via lazy loading
     */
    public List<Word> getWords() {
        if (mWords == null) {
            mWords = new ArrayList<>();
            // TODO: Make the transaction be Async
//            mWords = SQLite.select().from(Word.class).queryList();
// TODO: DBFlow inserts literal value into the ModelAdapter for the table so Strings must be escaped before setting
        }
        return mWords;
    }
}
