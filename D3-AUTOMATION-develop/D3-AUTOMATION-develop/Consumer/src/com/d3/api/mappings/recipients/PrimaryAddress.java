package com.d3.api.mappings.recipients;

import com.d3.datawrappers.recipient.CompanyBillPayManualRecipient;
import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.datawrappers.recipient.base.WireRecipient;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimaryAddress {
    private String line1;
    private String line2;
    private String city;
    private String state;
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
}
