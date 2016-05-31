package com.id11236662.gokeigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.id11236662.gokeigo.util.Constants;
import com.id11236662.gokeigo.util.StringUtility;
import com.raizlabs.android.dbflow.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

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
    private Boolean mIsRespectful = null;
    private Boolean mIsHumble = null;

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

    /**
     * Calculate the word once and store it.
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
     * Calculate the reading once and store it.
     *
     * @return reading
     */
    public String getReading() {
        if (mReading == null) {
            loadWordAndReading();
        }
        return mReading;
    }

    /**
     * Calculate the load and reading.
     */
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
     * Calculate the other forms of Japanese once and store them.
     *
     * @return other forms of the main Japanese word
     */
    public String getOtherForms() {
        if (mOtherForms == null) {
            mOtherForms = "";
            for (int i = 1; i < japanese.size(); i++) {
                mOtherForms += japanese.get(i).getWordAndReading();

                // Add comma to the list if it's not the last element.
                if (i < this.japanese.size() - 1) {
                    mOtherForms += "、";
                }
            }
        }
        return mOtherForms;
    }

    /**
     * Get the first sense element as it contains the main details.
     *
     * @return an element in the Sense list if it's not empty
     */
    private Sense getMainSense() {
        return senses.size() > 0 ? senses.get(0) : null;
    }

    /**
     * Get the first definition as it's the most common one.
     *
     * @return the first definition, appropriate to display on the item results screen.
     */
    public String getDefinition() {
        Sense sense = getMainSense();
        return sense != null ? StringUtility.join(sense.getEnglishDefinitions()) : "";
    }

    /**
     * Calculate the keigo related booleans once and store them.
     *
     * @return nullable boolean
     */
    public Boolean getIsRespectful() {
        if (mIsRespectful == null) {
            loadKeigoBooleans();
        }
        return mIsRespectful;
    }

    /**
     * Calculate the keigo related booleans once and store them.
     *
     * @return nullable boolean
     */
    public Boolean getIsHumble() {
        if (mIsHumble == null) {
            loadKeigoBooleans();
        }
        return mIsHumble;
    }

    /**
     * Calculate the keigo related booleans
     */
    private void loadKeigoBooleans() {
        mIsRespectful = false;
        mIsHumble = false;

        // Type of keigo is stored in the tags within the senses nodes.
        for (Sense sense : senses) {
            for (String tag : sense.getTags()) {
                if (tag != null && tag.equals(Constants.ENTRY_TAG_RESPECTFUL)) {
                    mIsRespectful = true;
                } else if (tag != null && tag.equals(Constants.ENTRY_TAG_HUMBLE)) {
                    mIsHumble = true;
                }

                // Stop looping if they're both true already.
                if (mIsRespectful && mIsHumble) {
                    break;
                }
            }
        }
    }
}
