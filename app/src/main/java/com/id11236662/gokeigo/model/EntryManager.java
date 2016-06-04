package com.id11236662.gokeigo.model;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * This singleton class manages CRUD operations in relation to Entries
 */

public class EntryManager {
    private static EntryManager instance = null;
    private List<Entry> mEntries;

    private EntryManager() {

        // Hide constructor to ensure singleton pattern

    }

    public static EntryManager getInstance() {
        if (instance == null) {
            instance = new EntryManager();
        }
        return instance;
    }

    /**
     * @return a list of Words from the DB via lazy loading
     */

    private List<Entry> getEntries() {
        if (mEntries == null) {

            // TODO: Make the transaction be Async

            mEntries = SQLite.select().from(Entry.class).queryList();
        }
        return mEntries;
    }

    public Entry getEntry(Entry entry) {

        // Entries are unique with their word and reading combination. That is their ID. JSON data provides no unique key.

        for (Entry savedEntry : getEntries()) {
            if (savedEntry.getWord().equals(entry.getWord()) && savedEntry.getReading().equals(entry.getReading())) {
                return savedEntry;
            }
        }
        return null;
    }

    public void insertEntry(Entry entry) {

        // Add entry to the list, and then the DB.

        getEntries().add(entry);
        entry.insert();
        entry.save();
    }

    public void updateEntry(Entry entry) {
        entry.save();
    }
}
