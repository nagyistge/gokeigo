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

    public void saveEntry(Entry entry) {
        getEntries().add(entry);
        entry.insert();
        entry.save();
    }

    public void saveNotes(Entry entry, String notes) {

        // TODO: DBFlow inserts literal value into the ModelAdapter for the table so Strings must be escaped before setting

        entry.setNotes(notes);
        entry.save();
    }

    /**
     * @return a list of Words from the DB via lazy loading
     */

    public List<Entry> getEntries() {
        if (mEntries == null) {

            // TODO: Make the transaction be Async

            mEntries = SQLite.select().from(Entry.class).queryList();
        }
        return mEntries;
    }

    public Entry getEntry(Entry entry) {
        for (Entry savedEntry : getEntries()) {
            if (savedEntry.getWord().equals(entry.getWord()) && savedEntry.getReading().equals(entry.getReading())) {
                return savedEntry;
            }
        }
        return null;
    }
}
