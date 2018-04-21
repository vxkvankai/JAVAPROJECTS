package com.d3.api.mappings.users;

import com.d3.datawrappers.user.D3Address;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhysicalAddress {
    private String countryCode;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postalCode;

    public PhysicalAddress(D3Address address) {
        this.city = address.getCity();
        this.countryCode = address.getCountryCode();
        this.line1 = address.getAddress1();
        if (address.getAddress2() != null && !address.getAddress2().isEmpty()) {
            this.line2 = address.getAddress2();
        }
        this.postalCode = address.getPostalCode();
        this.state = address.getState();
    }

}
