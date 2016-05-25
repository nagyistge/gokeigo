package com.id11236662.gokeigo.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Sense {

    @SerializedName("english_definitions")
    @Expose
    private List<String> englishDefinitions = new ArrayList<String>();
    @SerializedName("parts_of_speech")
    @Expose
    private List<String> partsOfSpeech = new ArrayList<String>();
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("tags")
    @Expose
    private List<Object> tags = new ArrayList<Object>();
    @SerializedName("restrictions")
    @Expose
    private List<Object> restrictions = new ArrayList<Object>();
    @SerializedName("see_also")
    @Expose
    private List<Object> seeAlso = new ArrayList<Object>();
    @SerializedName("antonyms")
    @Expose
    private List<Object> antonyms = new ArrayList<Object>();
    @SerializedName("source")
    @Expose
    private List<Object> source = new ArrayList<Object>();
    @SerializedName("info")
    @Expose
    private List<Object> info = new ArrayList<Object>();
    @SerializedName("sentences")
    @Expose
    private List<Object> sentences = new ArrayList<Object>();

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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public List<Object> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Object> restrictions) {
        this.restrictions = restrictions;
    }

    public List<Object> getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(List<Object> seeAlso) {
        this.seeAlso = seeAlso;
    }

    public List<Object> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<Object> antonyms) {
        this.antonyms = antonyms;
    }

    public List<Object> getSource() {
        return source;
    }

    public void setSource(List<Object> source) {
        this.source = source;
    }

    public List<Object> getInfo() {
        return info;
    }

    public void setInfo(List<Object> info) {
        this.info = info;
    }

    public List<Object> getSentences() {
        return sentences;
    }

    public void setSentences(List<Object> sentences) {
        this.sentences = sentences;
    }

}
