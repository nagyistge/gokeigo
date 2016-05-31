package com.id11236662.gokeigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.id11236662.gokeigo.util.AppDatabase;
import com.id11236662.gokeigo.util.StringUtility;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Parcelable version of the Entry object. This makes it easily transportable via intents.
 */
@Table(database = AppDatabase.class, allFields = true)
public class ParcelableEntry extends BaseModel implements Parcelable {

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

    public ParcelableEntry() {
        // Required empty public constructor to create table
    }

    public static final Creator<ParcelableEntry> CREATOR = new Creator<ParcelableEntry>() {
        @Override
        public ParcelableEntry createFromParcel(Parcel in) {
            return new ParcelableEntry(in);
        }

        @Override
        public ParcelableEntry[] newArray(int size) {
            return new ParcelableEntry[size];
        }
    };

    protected ParcelableEntry(Parcel source) {
        // There is a long-term bug where a single boolean cannot be written nor read in a Parcel...
        // Source: https://code.google.com/p/android/issues/detail?id=5973
        isCommonStatus = source.readInt() == 1;
        word = source.readString();
        reading = source.readString();
        blurb = source.readString();
        otherForms = source.readString();
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // There is a long-term bug where a single boolean cannot be written nor read in a Parcel...
        // Source: https://code.google.com/p/android/issues/detail?id=5973
        dest.writeInt(isCommonStatus ? 1 : 0);
        dest.writeString(word);
        dest.writeString(reading);
        dest.writeString(blurb);
        dest.writeString(otherForms);
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public static ParcelableEntry parse(Entry entry) {
        ParcelableEntry parcelableEntry = new ParcelableEntry();
        parcelableEntry.setIsCommonStatus(entry.getIsCommon());
        parcelableEntry.setWord(entry.getWord());
        parcelableEntry.setReading(entry.getReading());

        // Create blurb using the following of every sense element: parts of speech,
        // english definitions, info, see also, tags and links.
        StringBuilder stringBuilder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");
        List<Sense> senseList = entry.getSenses();
        for (int i = 0; i < senseList.size(); i++) {
            Sense sense = senseList.get(i);

            String partsOfSpeech = StringUtility.join(sense.getPartsOfSpeech());
            if (!partsOfSpeech.isEmpty()) {
                stringBuilder.append(partsOfSpeech);
                stringBuilder.append(lineSeparator);
            }

            int number = i + 1;
            stringBuilder.append(number);
            stringBuilder.append(". ");
            String englishDefinition = StringUtility.join(sense.getEnglishDefinitions());
            stringBuilder.append(englishDefinition);
            stringBuilder.append(lineSeparator);

            String info = StringUtility.join(sense.getInfo());
            if (!info.isEmpty()) {
                stringBuilder.append(info);
                stringBuilder.append(lineSeparator);
            }

            String seeAlso = StringUtility.join(sense.getSeeAlso());
            if (!seeAlso.isEmpty()) {
                stringBuilder.append("See also: ");
                stringBuilder.append(seeAlso);
                stringBuilder.append(lineSeparator);
            }

            String tags = StringUtility.join(sense.getTags());
            if (!tags.isEmpty()) {
                stringBuilder.append(tags);
                stringBuilder.append(lineSeparator);
            }
            // TODO figure out how to get the links as hyperlinks in the textview with Android-TextView-LinkBuilder
//            String link = StringUtility.join(sense.getLinks()); // Link is an object...
//            links.add(link);

            // Add comma to the list if it's not the last element.
            if (i < senseList.size() - 1) {
                stringBuilder.append(lineSeparator);
            }
        }
        parcelableEntry.setBlurb(stringBuilder.toString());
        parcelableEntry.setOtherForms(entry.getOtherForms());
        return parcelableEntry;
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
