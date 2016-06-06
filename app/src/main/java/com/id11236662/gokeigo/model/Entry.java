package com.id11236662.gokeigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.id11236662.gokeigo.util.GoKeigoDatabase;
import com.id11236662.gokeigo.util.TypeUtility;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * Parcelable, persistent and concise version of the Data object (which contains elements of Japanese and Sense).
 * The implementation of parcelable makes the object be easily transportable through intents.
 */

@Table(database = GoKeigoDatabase.class, allFields = true)
public class Entry extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    private long id;
    private boolean isCommon;
    private String word;
    private String reading;
    private String wordAndReading;
    private String blurb;
    private String otherForms;
    private String notes;
    private boolean isStarred;
    private Date lastAccessedDate;
    private boolean isHumble;
    private boolean isRespectful;
    private String definition;

    public Entry() {

        // Required empty public constructor to create table

    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    protected Entry(Parcel source) {

        // There is a long-term bug where a single boolean cannot be written nor read in a Parcel...
        // Source: https://code.google.com/p/android/issues/detail?id=5973

        isCommon = TypeUtility.getBooleanFromInt(source.readInt());
        word = source.readString();
        reading = source.readString();
        wordAndReading = source.readString();
        blurb = source.readString();
        otherForms = source.readString();
        isRespectful = TypeUtility.getBooleanFromInt(source.readInt());
        isHumble = TypeUtility.getBooleanFromInt(source.readInt());
        definition = source.readString();
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

        dest.writeInt(TypeUtility.getIntFromBoolean(isCommon));
        dest.writeString(word);
        dest.writeString(reading);
        dest.writeString(wordAndReading);
        dest.writeString(blurb);
        dest.writeString(otherForms);
        dest.writeInt(TypeUtility.getIntFromBoolean(isRespectful));
        dest.writeInt(TypeUtility.getIntFromBoolean(isHumble));
        dest.writeString(definition);
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

    public static Entry parse(Data data) {
        Entry entry = new Entry();
        entry.setIsCommon(data.getIsCommon());
        entry.setWord(data.getWord());
        entry.setReading(data.getReading());
        entry.setWordAndReading((data.getWordAndReading()));

        // Create blurb using the following of every sense element: parts of speech,
        // english definitions, info, see also, tags and links.

        StringBuilder stringBuilder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");
        List<Sense> senseList = data.getSenses();
        for (int i = 0; i < senseList.size(); i++) {
            Sense sense = senseList.get(i);

            String partsOfSpeech = TypeUtility.join(sense.getPartsOfSpeech());
            if (!partsOfSpeech.isEmpty()) {
                stringBuilder.append(partsOfSpeech);
                stringBuilder.append(lineSeparator);
            }

            int number = i + 1;
            stringBuilder.append(number);
            stringBuilder.append(". ");
            String englishDefinition = TypeUtility.join(sense.getEnglishDefinitions());
            stringBuilder.append(englishDefinition);
            stringBuilder.append(lineSeparator);

            String info = TypeUtility.join(sense.getInfo());
            if (!info.isEmpty()) {
                stringBuilder.append(info);
                stringBuilder.append(lineSeparator);
            }

            String seeAlso = TypeUtility.join(sense.getSeeAlso());
            if (!seeAlso.isEmpty()) {
                stringBuilder.append("See also: ");
                stringBuilder.append(seeAlso);
                stringBuilder.append(lineSeparator);
            }

            String tags = TypeUtility.join(sense.getTags());
            if (!tags.isEmpty()) {
                stringBuilder.append(tags);
                stringBuilder.append(lineSeparator);
            }

            // Add comma to the list if it's not the last element.

            if (i < senseList.size() - 1) {
                stringBuilder.append(lineSeparator);
            }
        }
        entry.setBlurb(stringBuilder.toString());
        entry.setOtherForms(data.getOtherForms());
        entry.setIsRespectful(data.getIsRespectful());
        entry.setIsHumble(data.getIsHumble());
        entry.setDefinition(data.getDefinition());
        return entry;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(boolean isCommonStatus) {
        this.isCommon = isCommonStatus;
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

    public String getWordAndReading() {
        return wordAndReading;
    }

    public void setWordAndReading(String wordAndReading) {
        this.wordAndReading = wordAndReading;
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

    public void switchIsStarredState() {
        isStarred = !isStarred;
    }

    public Date getLastAccessedDate() {
        return lastAccessedDate;
    }

    public boolean getIsRespectful() {
        return isRespectful;
    }

    public void setIsRespectful(boolean respectful) {
        isRespectful = respectful;
    }

    public void setLastAccessedDate(Date lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    public boolean getIsHumble() {
        return isHumble;
    }

    public void setIsHumble(boolean humble) {
        isHumble = humble;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
