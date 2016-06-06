package com.id11236662.gokeigo.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Sense {

    // Serialised fields have to match the json attibute or the mapping will not work.

    @SerializedName("english_definitions")
    @Expose
    private List<String> englishDefinitions = new ArrayList<String>();

    @SerializedName("parts_of_speech")
    @Expose
    private List<String> partsOfSpeech = new ArrayList<String>();

    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();

    @SerializedName("see_also")
    @Expose
    private List<String> seeAlso = new ArrayList<String>();

    @SerializedName("info")
    @Expose
    private List<String> info = new ArrayList<String>();

    public List<String> getEnglishDefinitions() {
        return englishDefinitions;
    }

    public void setEnglishDefinitions(List<String> englishDefinitions) {
        this.englishDefinitions = englishDefinitions;
    }

    public List<String> getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public void setPartsOfSpeech(List<String> partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(List<String> seeAlso) {
        this.seeAlso = seeAlso;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

}
