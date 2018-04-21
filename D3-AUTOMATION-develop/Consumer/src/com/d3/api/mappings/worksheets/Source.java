package com.d3.api.mappings.worksheets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {
    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Source internalSourceAccount(Integer accountId) {
        Source source = new Source();
        source.setId(String.format("INTERNAL:%s", String.valueOf(accountId)));
        return source;
    }

}
