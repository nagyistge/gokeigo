package com.id11236662.gokeigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.id11236662.gokeigo.util.StringUtils;

import java.util.ArrayList;

/**
 * Parcelable version of the Entry object. This makes it passable via an intent.
 */
public class ParcelableEntry implements Parcelable {

    private String commonStatus;
    private String word;
    private String reading;
    private ArrayList<String> englishDefinitions = new ArrayList<>();
    private ArrayList<String> partsOfSpeeches = new ArrayList<>();
//    private ArrayList<String> links = new ArrayList<>();
//    private ArrayList<String> tags = new ArrayList<>();
//    private ArrayList<String> seeAlso = new ArrayList<>();
//    private ArrayList<String> source = new ArrayList<>();
//    private ArrayList<String> info = new ArrayList<>();
//    private boolean isJmdict;
//    private boolean isJmnedict;
//    private boolean isDbpedia;

    private ParcelableEntry() {
    }

    protected ParcelableEntry(Parcel source) {
        commonStatus = source.readString();
        word = source.readString();
        reading = source.readString();
        englishDefinitions = source.createStringArrayList();
        partsOfSpeeches = source.createStringArrayList();
    }

    // TODO: Figure out the rest from links onwards later.
    public static ParcelableEntry parse(Entry entry) {
        ParcelableEntry parcelableEntry = new ParcelableEntry();
        parcelableEntry.setCommonStatus(entry.getCommonStatus());
        parcelableEntry.setWord(entry.getWord());
        parcelableEntry.setReading(entry.getReading());

        ArrayList<String> englishDefinitions = new ArrayList<>();
        ArrayList<String> partsOfSpeeches = new ArrayList<>();
        for (Sense sense : entry.getSenses()) {
            String englishDefinition = StringUtils.join(sense.getEnglishDefinitions());
            englishDefinitions.add(englishDefinition);

            String partsOfSpeech = StringUtils.join(sense.getPartsOfSpeech());
            partsOfSpeeches.add(partsOfSpeech);

//            String link = StringUtils.join(sense.getLinks()); // Link is an object...
//            links.add(link);
        }
        parcelableEntry.setEnglishDefinitions(englishDefinitions);
        parcelableEntry.setPartsOfSpeeches(partsOfSpeeches);

        return parcelableEntry;
    }

    public void setCommonStatus(String commonStatus) {
        this.commonStatus = commonStatus;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public void setEnglishDefinitions(ArrayList<String> englishDefinitions) {
        this.englishDefinitions = englishDefinitions;
    }

    public void setPartsOfSpeeches(ArrayList<String> partsOfSpeeches) {
        this.partsOfSpeeches = partsOfSpeeches;
    }

    public String getCommonStatus() {
        return commonStatus;
    }

    public String getWord() {
        return word;
    }

    public String getReading() {
        return reading;
    }

    public ArrayList<String> getEnglishDefinitions() {
        return englishDefinitions;
    }

    public ArrayList<String> getPartsOfSpeeches() {
        return partsOfSpeeches;
    }

//    public ArrayList<String> getLinks() {
//        return links;
//    }
//
//    public ArrayList<String> getTags() {
//        return tags;
//    }
//
//    public ArrayList<String> getSeeAlso() {
//        return seeAlso;
//    }
//
//    public ArrayList<String> getSource() {
//        return source;
//    }
//
//    public ArrayList<String> getInfo() {
//        return info;
//    }
//
//    public boolean isJmdict() {
//        return isJmdict;
//    }
//
//    public boolean isJmnedict() {
//        return isJmnedict;
//    }
//
//    public boolean isDbpedia() {
//        return isDbpedia;
//    }

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

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commonStatus);
        dest.writeString(word);
        dest.writeString(reading);
        dest.writeStringList(englishDefinitions);
        dest.writeStringList(partsOfSpeeches);
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
}
