package com.id11236662.gokeigo.model;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * This singleton class manages CRUD operations in relation to Entries
 */
public class EntryManager {
    private static EntryManager mInstance = null;
    private List<ParcelableEntry> mEntries;

    private EntryManager() {
        // Hide constructor to ensure singleton pattern
    }

    public static EntryManager getInstance() {
        if (mInstance == null) {
            mInstance = new EntryManager();
        }
        return mInstance;
    }

    public void saveEntry(ParcelableEntry parcelableEntry) {
        getEntries().add(parcelableEntry);
        parcelableEntry.insert();
        parcelableEntry.save();
    }

    public void saveNotes(ParcelableEntry parcelableEntry, String notes) {
        // TODO: DBFlow inserts literal value into the ModelAdapter for the table so Strings must be escaped before setting
        parcelableEntry.setNotes(notes);
        parcelableEntry.save();
    }

    /**
     * @return a list of Words from the DB via lazy loading
     */
    public List<ParcelableEntry> getEntries() {
        if (mEntries == null) {
            // TODO: Make the transaction be Async
            mEntries = SQLite.select().from(ParcelableEntry.class).queryList();
        }
        return mEntries;
    }

    public ParcelableEntry getEntry(ParcelableEntry entry) {
        for (ParcelableEntry savedEntry : getEntries()) {
            if (savedEntry.getWord().equals(entry.getWord()) && savedEntry.getReading().equals(entry.getReading())) {
                return savedEntry;
            }
        }
        return null;
    }
}
