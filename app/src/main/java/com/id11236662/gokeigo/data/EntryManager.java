package com.id11236662.gokeigo.data;

import android.support.annotation.NonNull;

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
    }

    public static EntryManager getInstance() {
        if (mInstance == null) {
            mInstance = new EntryManager();
        }
        return mInstance;
    }

    // TODO: TEMP
    public void addDummyData() {
        addEntry("見る", "miru");
    }

    public void addEntry(String wordInKanjiKana, String wordInRomaji) {
        Entry entry = new Entry(wordInKanjiKana, wordInRomaji);
        getEntries().add(entry);
        //TODO: implement Sugar ORM later. entry.save();
    }

    public List<Entry> getEntries() {
        if (mEntries == null) {
            //TODO: implement Sugar ORM later. mEntries = Entry.listAll(Entry.class);
            mEntries = new ArrayList<>(); // TODO: temp
        }
        return mEntries;
    }

}
