package com.d3.api.mappings.billpay;

import com.d3.datawrappers.user.D3Address;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String countryCode;
    private String postalCode;

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
