package com.id11236662.gokeigo.model;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;
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

    /**
     * @param entry that could be in the list and/or DB already.
     * @return previously saved entry or the same entry that was passed.
     */

    public Entry getPreviouslySavedEntryIfAvailableElseReturnPassedEntry(Entry entry) {

        Entry previouslySavedEntry = null;

        // Entries are unique with their word and reading combination. That is their ID. JSON data provides no unique key.

        for (Entry savedEntry : getEntries()) {
            if (savedEntry.getWord().equals(entry.getWord()) && savedEntry.getReading().equals(entry.getReading())) {
                previouslySavedEntry = savedEntry;
                break;
            }
        }

        // Set current date to be the last accessed date.

        Date currentDate = new Date();
        entry.setLastAccessedDate(currentDate);

        if (previouslySavedEntry == null) {

            // Save entry to history for the first time and return it.

            insertEntry(entry);
            return entry;
        } else {

            // Update previously saved entry and return it.

            saveEntry(previouslySavedEntry);
            return previouslySavedEntry;
        }
    }

    private void insertEntry(Entry entry) {

        // Add entry to the list, and then the DB.

        getEntries().add(entry);
        entry.insert();
        entry.save();
    }

    public void saveEntry(Entry entry) {
        entry.save();
    }

    public List<Entry> getPreviouslyAccessedEntries() {
        // TODO: display them in order.
        for (Entry entry : getEntries()) {
            // TODO: Check if datetime can be null
            Date date = entry.getLastAccessedDate();
        }

        return getEntries();
    }
}
