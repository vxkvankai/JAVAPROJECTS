package com.d3.datawrappers.user;

import com.d3.datawrappers.State;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Address;

import java.io.Serializable;

public class D3Address implements Serializable {

    private String address1; // Required
    private String address2; // Optional
    private String address3; // Optional
    private String address4; // Optional
    private String city; // Required
    private String state; // Required
    private String countryCode; // Optional
    private String postalCode; // Required
    private Double latitude; // Optional
    private Double longitude; // Optional

    public D3Address(String address1, String address2, String city, String state, String postalCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public D3Address(Address address) {
        this(address.getAddressLine1(), address.getAddressLine2(), address.getCity(), State.getRandom(),  address.getPostalCode());
    }

    public static D3Address getRandomAddress() {
        return new D3Address(Fairy.create().person().getAddress());
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
