package com.id11236662.gokeigo.data;

import com.id11236662.gokeigo.util.Constants;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * This singleton class manages CRUD operations in relation to Entries
 */
//public class EntryManager {
//    private static EntryManager mInstance = null;
//    private List<Entry> mEntries;
//
//    private EntryManager() {
//        // Hide constructor to ensure singleton pattern
//    }
//
//    public static EntryManager getInstance() {
//        if (mInstance == null) {
//            mInstance = new EntryManager();
//        }
//        return mInstance;
//    }
//
//    public void addEntry(String wordInKanjiKana, String wordInRomaji, String definition) {
//        Entry word = new Entry(wordInKanjiKana, wordInRomaji, Constants.Level.POLITE, definition);
//        getEntries().add(word);
//        word.insert();
//    }
//
//    /**
//     * @return a list of Entries from the DB via lazy loading
//     */
//    public List<Entry> getEntries() {
//        if (mEntries == null) {
//            // TODO: Make the transaction be Async
//            mEntries = SQLite.select().from(Entry.class).queryList();
// TODO: DBFlow inserts literal value into the ModelAdapter for the table so Strings must be escaped before setting
//        }
//        return mEntries;
//    }
//}
