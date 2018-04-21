package com.d3.api.mappings.recipients;

import com.d3.datawrappers.recipient.CompanyBillPayManualRecipient;
import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.datawrappers.recipient.base.WireRecipient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrimaryAddress {

    @SerializedName("line1")
    @Expose
    private String line1;
    @SerializedName("line2")
    @Expose
    private String line2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;

    public PrimaryAddress(PersonBillAddressRecipient recipient) {
        this.line1 = recipient.getAdd1();
        this.line2 = recipient.getAdd2();
        this.city = recipient.getCity();
        this.postalCode = recipient.getZipCode();
        this.state = recipient.getState();
    }

    public PrimaryAddress(WireRecipient recipient) {
        this.line1 = recipient.getAddress1();
        this.line2 = recipient.getAddress2();
        this.city = recipient.getCity();
        this.postalCode = recipient.getZipCode();
        this.state = recipient.getState();
    }

    public PrimaryAddress(CompanyBillPayManualRecipient recipient) {
        this.line1 = recipient.getAdd1();
        this.line2 = recipient.getAdd2();
        this.city = recipient.getCity();
        this.postalCode = recipient.getZipCode();
        this.state = recipient.getState();
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}
