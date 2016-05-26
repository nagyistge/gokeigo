package com.id11236662.gokeigo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class EntriesResponse {

    @SerializedName("data")
    @Expose
    private List<Entry> data = new ArrayList<Entry>();

    public List<Entry> getData() {
        return data;
    }

    public void setData(List<Entry> data) {
        this.data = data;
    }

}