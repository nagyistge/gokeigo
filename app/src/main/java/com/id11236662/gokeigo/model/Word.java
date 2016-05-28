package com.id11236662.gokeigo.model;

import com.id11236662.gokeigo.util.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * This class represents a container that holds a word in dictionary form;
 * its respectful and humble versions if they've been calculated; its notes and starred status.
 */
@Table(database = AppDatabase.class, allFields = true)
public class Word extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;
    private String dictionaryVersion;
    private String respectfulVersion;
    private String humbleVersion;
    private String note;
    private boolean isStarred;

    public Word() {
        // Required empty public constructor so the table can be created
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDictionaryVersion() {
        return dictionaryVersion;
    }

    public void setDictionaryVersion(String dictionaryVersion) {
        this.dictionaryVersion = dictionaryVersion;
    }

    public String getRespectfulVersion() {
        return respectfulVersion;
    }

    public void setRespectfulVersion(String respectfulVersion) {
        this.respectfulVersion = respectfulVersion;
    }

    public String getHumbleVersion() {
        return humbleVersion;
    }

    public void setHumbleVersion(String humbleVersion) {
        this.humbleVersion = humbleVersion;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean starred) {
        isStarred = starred;
    }
}
