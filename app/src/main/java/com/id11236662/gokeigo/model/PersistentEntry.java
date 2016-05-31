package com.id11236662.gokeigo.model;

import com.id11236662.gokeigo.util.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * This class represents a container that holds an entry using its word and reading as composite key;
 * persists notes and starred status.
 */
@Table(database = AppDatabase.class, allFields = true)
public class PersistentEntry extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;
    private boolean isCommonStatus;
    private String word;
    private String reading;
    private String blurb;
    private String otherForms;
    private String notes;
    private boolean isStarred;
    // TODO: DateTime for history log

    public PersistentEntry() {
        // Required empty public constructor so the table can be created
    }

    public PersistentEntry(ParcelableEntry parcelableEntry) {
        isCommonStatus = parcelableEntry.getIsCommonStatus();
        word = parcelableEntry.getWord();
        reading = parcelableEntry.getReading();
        blurb = parcelableEntry.getBlurb();
        otherForms = parcelableEntry.getOtherForms();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsCommonStatus() {
        return isCommonStatus;
    }

    public void setIsCommonStatus(boolean isCommonStatus) {
        this.isCommonStatus = isCommonStatus;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getOtherForms() {
        return otherForms;
    }

    public void setOtherForms(String otherForms) {
        this.otherForms = otherForms;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean starred) {
        isStarred = starred;
    }
}
