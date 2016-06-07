package com.id11236662.gokeigo.model;

import android.util.Log;

import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.JishoClient;
import com.id11236662.gokeigo.util.JishoService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

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
     * @return a list of saved Entries from the DB via lazy loading
     */

    private List<Entry> getSavedEntries() {
        if (mEntries == null) {

            // TODO: Make the transaction be Async

            mEntries = SQLite.select().from(Entry.class).queryList();
        }
        return mEntries;
    }

    /**
     * Call the API for a list of entries by supplying a keyword for the URI query and keigo level filters.
     * Execute search process.
     *
     * @param includeRespectful if true, include respectful-related entries. If false, exclude respectful-related entries.
     * @param includeHumble if true, include humble-related entries. If false, exclude humble-related entries.
     * @param query the search keyword.
     * @return list of data retrieved from the Jisho web service. If null, there's been an exception.
     */

    public List<Data> searchData(boolean includeRespectful, boolean includeHumble, String query) {
        // If none of the options is checked, don't bother searching and return 0 results.

        List<Data> results = new ArrayList<>();

        // Create client to access jisho's web service.

        JishoService jishoService = JishoClient.getClient().create(JishoService.class);

        // If "Include Respectful" option has been checked, grab respectful-related entries.

        if (includeRespectful) {
            Call<DataResponse> call = jishoService.getData(Constants.KEYWORD_PREFIX_RESPECTFUL + query);
            try {
                results.addAll(call.execute().body().getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // If "Include Humble" option has been checked, grab humble-related entries.

        if (includeHumble) {
            Call<DataResponse> call = jishoService.getData(Constants.KEYWORD_PREFIX_HUMBLE + query);
            try {
                results.addAll(call.execute().body().getData());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        Log.d(Constants.TAG, "EntryManager.searchData.size(): " + results.size());
        return results;
    }

    public void clearHistory() {
        // TODO: clear history
    }

    /**
     * @param entry that could be in the list and/or DB already.
     * @return saved entry.
     */

    public Entry getPreviouslySavedEntryIfAvailableElseReturnPassedEntry(Entry entry) {

        Entry previouslySavedEntry = null;

        // Entries are unique with their word and reading combination. That is their ID. JSON data provides no unique key.

        for (Entry savedEntry : getSavedEntries()) {
            if (savedEntry.getWord().equals(entry.getWord()) && savedEntry.getReading().equals(entry.getReading())) {
                previouslySavedEntry = savedEntry;
                break;
            }
        }

        // Set current date to be the last accessed date.

        Date currentDate = new Date();
        entry.setLastAccessedDate(currentDate);

        if (previouslySavedEntry != null) {

            // Update entry's notes and starred state with information from previously saved entry.
            // Reasons:
            // - If the jisho.org API has updated the blurb-related attributes of the entry
            //   since it's been last saved; we want to save the latest information.
            // - If we add another column to Entry, this is our chance populate the new column
            //   on previously saved entries.

            entry.setIsStarred(previouslySavedEntry.getIsStarred());
            entry.setNotes(previouslySavedEntry.getNotes());

            // Replace saved entry with new entry.

            replaceAndSaveEntry(previouslySavedEntry, entry);
        } else {

            // Insert the new entry for the first time.
            insertAndSaveEntry(entry);
        }

        return entry;
    }

    private void replaceAndSaveEntry(Entry oldEntry, Entry newEntry) {

        // Remove entry from the list and the DB.
        getSavedEntries().remove(oldEntry);
        oldEntry.delete();

        insertAndSaveEntry(newEntry);
    }

    private void insertAndSaveEntry(Entry entry) {

        // Add entry to the list, and then the DB.

        getSavedEntries().add(entry);
        entry.insert();
        entry.save();
    }

    public void saveEntry(Entry entry) {
        entry.save();
    }

    public List<Entry> getPreviouslyAccessedEntries() {
        // TODO: display them in order.
        for (Entry entry : getSavedEntries()) {
            // TODO: Check if datetime can be null
            Date date = entry.getLastAccessedDate();
        }

        return getSavedEntries();
    }
}
