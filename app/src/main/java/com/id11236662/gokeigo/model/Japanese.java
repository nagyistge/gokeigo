package com.id11236662.gokeigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.StringUtils;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Japanese {

    // Serialised fields have to match the json attibute or the mapping will not work.
    @SerializedName("word")
    @Expose
    private String word;
    @SerializedName("reading")
    @Expose
    private String reading;

    private String mWordAndReading = null;

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

    /**
     * Lazy load the word and reading.
     * @return the word and reading in following format: word 【reading】, word
     */
    public String getWordAndReading() {
        if (mWordAndReading == null) {
            if (StringUtils.isNotNullOrEmpty(word) && StringUtils.isNotNullOrEmpty(reading)) {
                mWordAndReading = String.format("%s【%s】", word, reading);
            } else if (StringUtils.isNotNullOrEmpty(word)) {
                mWordAndReading = word;
            } else if (StringUtils.isNotNullOrEmpty(reading)) {
                mWordAndReading = reading;
            }
        }
        return mWordAndReading;
    }

}
