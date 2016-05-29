package com.id11236662.gokeigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.id11236662.gokeigo.util.StringUtility;
import com.raizlabs.android.dbflow.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Entry {

    // Serialised fields have to match the json attibute or the mapping will not work.
    @SerializedName("is_common")
    @Expose
    private boolean isCommon;
    @SerializedName("japanese")
    @Expose
    private List<Japanese> japanese = new ArrayList<Japanese>();
    @SerializedName("senses")
    @Expose
    private List<Sense> senses = new ArrayList<Sense>();

    private String mWord = null;
    private String mReading = null;
    private String mOtherForms = null;

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

    /**
     * Get the first japanese element as that's the main result.
     * The other results are other forms of the main results.
     *
     * @return an element in the Japanese list if it's not empty
     */
    private Japanese getMainJapanese() {
        return japanese.size() > 0 ? japanese.get(0) : null;
    }

    // TODO: unit test this

    /**
     * Lazy load the word.
     *
     * @return word
     */
    public String getWord() {
        if (mWord == null) {
            loadWordAndReading();
        }
        return mWord;
    }

    /**
     * Lazy load the reading.
     *
     * @return reading
     */
    public String getReading() {
        if (mReading == null) {
            loadWordAndReading();
        }
        return mReading;
    }

    private void loadWordAndReading() {
        Japanese japanese = getMainJapanese();
        if (japanese != null) {
            // jisho.org's JSON data does not provide a word if it's wholly in katakana but it
            // always provides a reading, so use that as the word instead. In that case, blank out
            // the reading to avoid redundancy.
            String word = japanese.getWord();
            if (StringUtils.isNotNullOrEmpty(word)) {
                mWord = word;
                mReading = japanese.getReading();
            } else {
                mWord = japanese.getReading();
                mReading = "";
            }
        }
    }

    /**
     * Lazy load the other forms of Japanese.
     *
     * @return other forms of the main Japanese word
     */
    public String getOtherForms() {
        if (mOtherForms == null) {
            mOtherForms = "";
            // First japanese element in JSON is the main word, the few elements after are other forms.
            // Make a string that goes in this format: word 【reading】, word
            for (int i = 1; i < japanese.size(); i++) {
                Japanese japanese = this.japanese.get(i);
                mOtherForms += japanese.getWord();

                // Reading may be blank due to loadWordAndReading(). If blank, don't try to show it.
                if (!japanese.getReading().isEmpty()) {
                    mOtherForms += String.format("【%s】", japanese.getReading());
                }

                // Add comma to the list if it's not the last element.
                if (i < this.japanese.size() - 1) {
                    mOtherForms += "、";
                }
            }
        }
        return mOtherForms;
    }

    /**
     * Get the first sense element as it contains the most common defintion.
     *
     * @return an element in the Sense list if it's not empty
     */
    private Sense getMainSense() {
        return senses.size() > 0 ? senses.get(0) : null;
    }

    public String getDefinition() {
        Sense sense = getMainSense();
        return sense != null ? StringUtility.join(sense.getEnglishDefinitions()) : "";
    }

    public String getCommonStatus() {
        return isCommon ? "Common" : ""; //TODO: turn Common into resource string
    }
}
