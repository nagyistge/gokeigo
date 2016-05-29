package com.id11236662.gokeigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.id11236662.gokeigo.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Entry {

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

    private String mWord = null;
    private String mReading = null;

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
     * Get the first japanese element as that's the actual result.
     * Unknown reason as to why the API returns other results. Ignore them.
     * @return an element in the Japanese list if it's not empty
     */
    private Japanese getActualJapanese() {
        return japanese.size() > 0 ? japanese.get(0) : null;
    }

    // TODO: unit test this

    /**
     * Lazy load the word.
     * @return
     */
    public String getWord() {
        if (mWord == null) {
            loadWordAndReading();
        }
        return mWord;
    }

    /**
     * Lazy load the reading.
     * @return
     */
    public String getReading() {
        if (mReading == null) {
            loadWordAndReading();
        }
        return mReading;
    }

    private void loadWordAndReading() {
        Japanese japanese = getActualJapanese();
        if (japanese != null) {
            // jisho.org's JSON data does not provide a word if it's wholly in katakana but it
            // always provides a reading, so use that if there is no word provided.
            String word = japanese.getWord();
            if (com.raizlabs.android.dbflow.StringUtils.isNotNullOrEmpty(word)) {
                mWord = word;
                mReading = japanese.getReading();
            } else {
                mWord = japanese.getReading();
                mReading = "";
            }
        }
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
}
