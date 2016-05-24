package com.id11236662.gokeigo.data;

import android.provider.SyncStateContract;
import android.support.annotation.NonNull;

import com.id11236662.gokeigo.util.Constants;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This singleton class manages CRUD operations in relation to Entries
 */
public class EntryManager {
    private static EntryManager mInstance = null;
    private List<Entry> mEntries;

    private EntryManager() {
        // Hide constructor to ensure singleton pattern
    }

    public static EntryManager getInstance() {
        if (mInstance == null) {
            mInstance = new EntryManager();
        }
        return mInstance;
    }

    /**
     * Create data if there's no data in the DB.
     */
    public void createDataIfNeeded() {
        if (getEntries().size() == 0) {
            addEntry("行く", "iku", "to go");
            addEntry("書く", "kaku", "to write");
            addEntry("来る", "kuru", "to come");
            addEntry("見る", "miru", "to see, to watch");
        }
    }

    public void addEntry(String wordInKanjiKana, String wordInRomaji, String definition) {
        Entry entry = new Entry(wordInKanjiKana, wordInRomaji, Constants.Level.POLITE, definition);
        getEntries().add(entry);
        entry.insert();
    }

    /**
     * @return a list of Entries from the DB
     */
    public List<Entry> getEntries() {
        if (mEntries == null) {
            // TODO: Make the transaction be Async
            mEntries = SQLite.select().from(Entry.class).queryList();
        }
        return mEntries;
    }
}
