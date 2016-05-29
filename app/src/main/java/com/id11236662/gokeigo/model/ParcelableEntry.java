package com.id11236662.gokeigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.id11236662.gokeigo.util.StringUtility;

import java.util.List;

/**
 * Parcelable version of the Entry object. This makes it passable via an intent.
 */
public class ParcelableEntry implements Parcelable {

    private String commonStatus;
    private String word;
    private String reading;
    private String blurb;
    private String otherForms;

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
        dest.writeString(commonStatus);
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
        parcelableEntry.setCommonStatus(entry.getCommonStatus());
        parcelableEntry.setWord(entry.getWord());
        parcelableEntry.setReading(entry.getReading());

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

    public String getOtherForms() {
        return otherForms;
    }

    public void setOtherForms(String otherForms) {
        this.otherForms = otherForms;
    }
}
