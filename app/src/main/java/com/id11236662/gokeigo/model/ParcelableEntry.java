package com.id11236662.gokeigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.id11236662.gokeigo.util.StringUtils;

import java.util.List;

/**
 * Parcelable version of the Entry object. This makes it passable via an intent.
 */
public class ParcelableEntry implements Parcelable {

    private String commonStatus;
    private String word;
    private String reading;
    private String blurb;

    private ParcelableEntry() {
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
        commonStatus = source.readString();
        word = source.readString();
        reading = source.readString();
        blurb = source.readString();
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
        dest.writeString(blurb);
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

    // TODO: Figure out the rest from links onwards later.
    public static ParcelableEntry parse(Entry entry) {
        ParcelableEntry parcelableEntry = new ParcelableEntry();
        parcelableEntry.setCommonStatus(entry.getCommonStatus());
        parcelableEntry.setWord(entry.getWord());
        parcelableEntry.setReading(entry.getReading());

        StringBuilder stringBuilder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");
        List<Sense> senseList = entry.getSenses();
        for (int i = 0; i < senseList.size(); i++) {
            Sense sense = senseList.get(i);

            int number = i + 1;
            stringBuilder.append(number).append(". ");
            String englishDefinition = StringUtils.join(sense.getEnglishDefinitions());
            stringBuilder.append(englishDefinition);
            stringBuilder.append(lineSeparator);
            String partsOfSpeech = StringUtils.join(sense.getPartsOfSpeech());
            stringBuilder.append(partsOfSpeech);
            stringBuilder.append(lineSeparator);
        }
        parcelableEntry.setBlurb(stringBuilder.toString());

//            String link = StringUtils.join(sense.getLinks()); // Link is an object...
//            links.add(link);


        //    private ArrayList<String> tags = new ArrayList<>();
//    private ArrayList<String> seeAlso = new ArrayList<>();
//    private ArrayList<String> source = new ArrayList<>();
//    private ArrayList<String> info = new ArrayList<>();
//    private boolean isJmdict;
//    private boolean isJmnedict;
//    private boolean isDbpedia;

        return parcelableEntry;
    }

    public String getCommonStatus() {
        return commonStatus;
    }

    public void setCommonStatus(String commonStatus) {
        this.commonStatus = commonStatus;
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
}
