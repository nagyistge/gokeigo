package com.id11236662.gokeigo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Word {

    @SerializedName("is_common")
    @Expose
    private Boolean isCommon;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = new ArrayList<Object>();
    @SerializedName("japanese")
    @Expose
    private List<Japanese> japanese = new ArrayList<Japanese>();
    @SerializedName("senses")
    @Expose
    private List<Sense> senses = new ArrayList<Sense>();
    @SerializedName("attribution")
    @Expose
    private Attribution attribution;

    public Boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
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
     * Get the main japanese element
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

}
