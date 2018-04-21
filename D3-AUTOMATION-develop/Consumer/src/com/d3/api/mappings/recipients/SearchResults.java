package com.d3.api.mappings.recipients;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResults {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("merchantId")
    @Expose
    private String merchantId;
    @SerializedName("addressOnFile")
    @Expose
    private Boolean addressOnFile;
    @SerializedName("merchantZipRequired")
    @Expose
    private Boolean merchantZipRequired;
    @SerializedName("billerId")
    @Expose
    private String billerId;
    @SerializedName("eBillCapable")
    @Expose
    private Boolean eBillCapable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Boolean getAddressOnFile() {
        return addressOnFile;
    }

    public void setAddressOnFile(Boolean addressOnFile) {
        this.addressOnFile = addressOnFile;
    }

    public Boolean getMerchantZipRequired() {
        return merchantZipRequired;
    }

    public void setMerchantZipRequired(Boolean merchantZipRequired) {
        this.merchantZipRequired = merchantZipRequired;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public Boolean getEBillCapable() {
        return eBillCapable;
    }

    public void setEBillCapable(Boolean eBillCapable) {
        this.eBillCapable = eBillCapable;
    }
}
