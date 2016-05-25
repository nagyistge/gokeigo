package com.id11236662.gokeigo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Attribution {

    @SerializedName("jmdict")
    @Expose
    private Boolean jmdict;
    @SerializedName("jmnedict")
    @Expose
    private Boolean jmnedict;
    @SerializedName("dbpedia")
    @Expose
    private String dbpedia;

    public Boolean getJmdict() {
        return jmdict;
    }

    public void setJmdict(Boolean jmdict) {
        this.jmdict = jmdict;
    }

    public Boolean getJmnedict() {
        return jmnedict;
    }

    public void setJmnedict(Boolean jmnedict) {
        this.jmnedict = jmnedict;
    }

    public String getDbpedia() {
        return dbpedia;
    }

    public void setDbpedia(String dbpedia) {
        this.dbpedia = dbpedia;
    }

}
