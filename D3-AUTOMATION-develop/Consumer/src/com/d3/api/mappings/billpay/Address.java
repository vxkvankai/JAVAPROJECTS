package com.d3.api.mappings.billpay;

import com.d3.datawrappers.user.D3Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {
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
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public static Address addressFromD3Address(D3Address address) {
        Address newAddress = new Address();
        newAddress.setLine1(address.getAddress1());
        newAddress.setLine2(address.getAddress2());
        newAddress.setCity(address.getCity());
        newAddress.setState(address.getState());
        newAddress.setCountryCode(address.getCountryCode());
        newAddress.setPostalCode(address.getPostalCode());
        return newAddress;
    }

}
