package com.d3.api.mappings.worksheets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provider {

    @SerializedName("transferMethod")
    @Expose
    private String transferMethod;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;

    public String getTransferMethod() {
        return transferMethod;
    }

    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Provider checkFreeBillPayProvider() {
        Provider provider = new Provider();
        provider.setDescription("CheckFree");
        provider.setName("CheckFree");
        provider.setTransferMethod("BILL_PAY");
        return provider;
    }

}