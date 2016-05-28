package com.id11236662.gokeigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.id11236662.gokeigo.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Entry implements Parcelable {

    @SerializedName("is_common")
    @Expose
    private boolean isCommon;
    @SerializedName("japanese")
    @Expose
    private List<Japanese> japanese = new ArrayList<Japanese>();
    @SerializedName("senses")
    @Expose
    private List<Sense> senses = new ArrayList<Sense>();
    @SerializedName("attribution")
    @Expose
    private Attribution attribution;

    public Entry(Parcel source) {
        // TODO:
    }

    public boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(boolean isCommon) {
        this.isCommon = isCommon;
    }

    public List<Japanese> getJapanese() {
        return japanese;
    }

    public void setJapanese(List<Japanese> japanese) {
        this.japanese = japanese;
    }

    public List<Sense> getSenses() {
        return senses;
    }

    public void setSenses(List<Sense> senses) {
        this.senses = senses;
    }

    public Attribution getAttribution() {
        return attribution;
    }

    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
    }

    /**
     * Get the first japanese element
     * @return an element in the Japanese list if it's not empty
     */
    private Japanese getJapaneseMain() {
        return japanese.size() > 0 ? japanese.get(0) : null;
    }

    public String getWord() {
        Japanese japanese = getJapaneseMain();
        return japanese != null ? japanese.getWord() : "";
    }

    public String getReading() {
        Japanese japanese = getJapaneseMain();
        return japanese != null ? japanese.getReading() : "";
    }

    /**
     * Get the first sense element
     * @return an element in the Sense list if it's not empty
     */
    private Sense getSenseMain() {
        return senses.size() > 0 ? senses.get(0) : null;
    }

    public String getDefinition() {
        Sense sense = getSenseMain();
        return sense != null ? StringUtils.join(sense.getEnglishDefinitions()) : "";
    }

    public String getCommonStatus() {
        return isCommon ? "Common" : ""; //TODO: turn Common into resource string
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0; // TODO:
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
