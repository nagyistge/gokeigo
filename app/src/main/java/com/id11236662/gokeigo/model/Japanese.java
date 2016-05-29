package com.id11236662.gokeigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

}
