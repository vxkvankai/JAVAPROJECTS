package com.d3.api.mappings.worksheets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Destination externalDestination(Integer enpointId) {
        Destination destination = new Destination();
        destination.setId(String.format("EXTERNAL:%s", String.valueOf(enpointId)));
        return destination;
    }

}
