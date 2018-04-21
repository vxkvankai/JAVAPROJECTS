package com.d3.api.mappings.worksheets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderOption {
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProviderOption(com.d3.datawrappers.transfers.ProviderOption option) {
        id = option.getId();
    }

    public ProviderOption() { }

}
