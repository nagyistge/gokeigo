package com.id11236662.gokeigo.model;

import android.util.Log;

import com.id11236662.gokeigo.util.Constants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * This singleton ... // TODO
 */
public class KeigoManager {
    private static KeigoManager mInstance = null;
    private Hashtable<String, Entry> mRespectfulEntries = new Hashtable<>();
    private Hashtable<String, Entry> mHumbleEntries = new Hashtable<>();

    private KeigoManager() {
        // Hide constructor to ensure singleton pattern
    }

    public static KeigoManager getInstance() {
        if (mInstance == null) {
            mInstance = new KeigoManager();
        }
        return mInstance;
    }

    public void addRespectfulEntries(List<Entry> entries) {
        Log.d(Constants.TAG_DEBUGGING, "addRespectfulEntries");
        for (Entry entry : entries) {
            Log.d(Constants.TAG_DEBUGGING, entry.getWord());
            mRespectfulEntries.put(entry.getWord(), entry);
        }
    }

    public void addHumbleEntries(List<Entry> entries) {
        Log.d(Constants.TAG_DEBUGGING, "addHumbleEntries");
        for (Entry entry : entries) {
            Log.d(Constants.TAG_DEBUGGING, entry.getWord());
            mHumbleEntries.put(entry.getWord(), entry);
        }
    }

    public List<Entry> getRespectfulEntries(Entry dictionaryEntry) {
        List<Entry> result = new ArrayList<>();
        for (Entry entry : mRespectfulEntries.values()) {
        }
        return result;
    }
}
