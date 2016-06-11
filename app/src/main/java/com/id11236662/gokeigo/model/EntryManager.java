package com.id11236662.gokeigo.model;

import android.util.Log;

import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.JishoClient;
import com.id11236662.gokeigo.util.JishoService;
import com.raizlabs.android.dbflow.StringUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            mEntries = SQLite.select().from(Entry.class).queryList();
        }
        return mEntries;
    }

    private boolean hasInitialised() {
        return getSavedEntries().size() > 0;
    }

    /**
     * Call the API for a list of entries under the #hon tag and entries under the #hum tag, and save the list.
     */
    public List<Entry> fetchKeigoEntries() {

        if (!hasInitialised()) {

            // Create client to access jisho's web service.

            JishoService jishoService = JishoClient.getClient().create(JishoService.class);

            List<Data> dataList = new ArrayList<>();

            // Add data under the #hon tag to the data list.

            Call<DataResponse> call = jishoService.getData(Constants.KEYWORD_RESPECTFUL_TAG);
            try {
                dataList.addAll(call.execute().body().getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Add data under the #hum tag to the data list.

            call = jishoService.getData(Constants.KEYWORD_HUMBLE_TAG);
            try {
                dataList.addAll(call.execute().body().getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Parse each element in the data list into an Entry-type, save the Entry and put the Entry
            // in the class's entry list.

            for (Data data : dataList) {
                Entry entry = Entry.parse(data);

                // Only add to the list if it hasn't been added before to avoid any possible duplicates.

                if (!contains(entry)) {
                    addAndSaveEntry(entry);
                }
            }

            // TODO: Add entries from a dedicated class that contain keigo words if not in the results. Also make sure it's not in the list before adding.
            // These text files have words like ご覧になる.
        }

        Log.d(Constants.TAG, "EntryManager.fetchKeigoEntries() - getSavedEntries().size: " + getSavedEntries().size());
        return getSavedEntries();
    }

    /**
     * Returns if the passed entry has been saved.
     *
     * @param entry to check if it's been saved.
     * @return true if it has been saved already. Otherwise, false.
     */

    private boolean contains(Entry entry) {
        boolean result = false;
        for (Entry savedEntry : getSavedEntries()) {

            // Entries are unique with their word and reading combination. That is their ID. JSON data provides no unique key.

            if (savedEntry.getWord().equals(entry.getWord()) && savedEntry.getReading().equals(entry.getReading())) {
                Log.d(Constants.TAG, "EntryManager.contains() - duplicate: " + savedEntry.getWord());
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Generate a list of entries that match the keyword.
     *
     * @param keyword to match with the word, reading and definition of entries.
     * @return a list of entries that match the query
     */

    public List<Entry> filterEntries(String keyword) {
        keyword = keyword.toLowerCase();

        final List<Entry> filteredEntryList = new ArrayList<>();
        for (Entry entry : getSavedEntries()) {
            final String word = entry.getWord();
            final String reading = entry.getReading();
            final String definition = entry.getDefinition().toLowerCase();

            if (word.contains(keyword) || reading.contains(keyword) || definition.contains(keyword)) {
                filteredEntryList.add(entry);
            }
        }
        Log.d(Constants.TAG, "EntryManager.filterEntries() - filteredEntryList.size: " + filteredEntryList.size());
        return filteredEntryList;
    }

    /**
     * Add an entry to the list and DB and save it.
     *
     * @param entry to add to the list and DB
     */

    private void addAndSaveEntry(Entry entry) {

        // Add entry to the list, and then the DB.

        getSavedEntries().add(entry);
        entry.insert();
        entry.save();
    }

    /**
     * Clear history by erasing the last accessed date of every saved Entry.
     * Don't delete all saved entries as they may have notes or have been marked with a star!
     */

    public void clearHistory() {
        for (Entry savedEntry : getSavedEntries()) {
            savedEntry.setLastAccessedDate(null);
            savedEntry.save();
        }
    }

    /**
     * Update or set the last accessed date of the entry, and save it.
     *
     * @param entry to update the last accessed date on.
     */

    public void updateLastAccessedDate(Entry entry) {
        entry = getSavedEntry(entry);

        // Set current date to be the last accessed date.

        Date currentDate = new Date();
        entry.setLastAccessedDate(currentDate);
        entry.save();
    }

    /**
     * Set notes to an entry and save it.
     *
     * @param entry to be updated with the note
     * @param notes to set to the entry
     */

    public void savesNotesOnEntry(Entry entry, String notes) {
        entry = getSavedEntry(entry);
        entry.setNotes(notes);
        entry.save();
    }

    /**
     * Switch star state of an entry and save it.
     *
     * @param entry to have its star state switched.
     */

    public void switchStarredStateAndSaveEntry(Entry entry) {
        entry = getSavedEntry(entry);
        entry.switchIsStarredState();
        entry.save();
    }

    /**
     * Return the entry that's in this class's list. If the passed entry is from the search view,
     * where the entry address is different to the address of the matched entry in the list,
     * get the matched entry to correctly persist the entry.
     *
     * @param entry that could be on the search screen and using a different address than the entry in this class's list.
     * @return saved entry.
     */

    private Entry getSavedEntry(Entry entry) {

        Entry previouslySavedEntry = entry;

        // Entries are unique with their word and reading combination. That is their ID. JSON data provides no unique key.

        for (Entry savedEntry : getSavedEntries()) {
            if (savedEntry.getWord().equals(entry.getWord()) && savedEntry.getReading().equals(entry.getReading())) {
                previouslySavedEntry = savedEntry;
                break;
            }
        }

        return previouslySavedEntry;
    }

    /**
     * Filter through the saved entries and only return entries, with a last accessed date, in
     * DESC order. The last accessed date may have been cleared via the Clear History setting.
     *
     * @return list of previously accessed entries that have not been cleared.
     */

    public List<Entry> getPreviouslyAccessedEntries() {
        List<Entry> previouslyAccessedEntries = new ArrayList<>();

        for (Entry savedEntry : getSavedEntries()) {
            if (savedEntry.getLastAccessedDate() != null) {
                previouslyAccessedEntries.add(savedEntry);
            }
        }

        // Sort the entries by date in desc order, e.g. 2016, 2015, 2014.

        Collections.sort(previouslyAccessedEntries, new Comparator<Entry>() {
            @Override
            public int compare(Entry lhs, Entry rhs) {
                Date lhsDate = lhs.getLastAccessedDate();
                Date rhsDate = rhs.getLastAccessedDate();
                return lhsDate.before(rhsDate) ? 1 : -1;
            }
        });
        Log.d(Constants.TAG, "EntryManager.getPreviouslyAccessedEntries() - getSavedEntries().size: " + getSavedEntries().size());
        return previouslyAccessedEntries;
    }

    /**
     * Filter through the saved entries and only return entries with non-blank notes.
     *
     * @return list of entries that have notes.
     */

    public List<Entry> getNoteAddedEntries() {
        List<Entry> noteAddedEntries = new ArrayList<>();

        for (Entry savedEntry : getSavedEntries()) {
            if (StringUtils.isNotNullOrEmpty(savedEntry.getNotes())) {
                noteAddedEntries.add(savedEntry);
            }
        }
        Log.d(Constants.TAG, "EntryManager.getNoteAddedEntries() - getSavedEntries().size: " + getSavedEntries().size());
        return noteAddedEntries;
    }

    /**
     * Filter through the saved entries and only return entries that have been marked with a star.
     *
     * @return list of entries that have been stared.
     */

    public List<Entry> getStarredEntries() {
        List<Entry> starredEntry = new ArrayList<>();

        for (Entry savedEntry : getSavedEntries()) {
            if (savedEntry.getIsStarred()) {
                starredEntry.add(savedEntry);
            }
        }
        Log.d(Constants.TAG, "EntryManager.getStarredEntries() - getSavedEntries().size: " + getSavedEntries().size());
        return starredEntry;
    }
}
