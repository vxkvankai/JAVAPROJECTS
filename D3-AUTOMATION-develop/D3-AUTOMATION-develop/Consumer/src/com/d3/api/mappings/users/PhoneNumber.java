package com.d3.api.mappings.users;

import com.d3.datawrappers.user.D3Contact;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumber {
    private String type;
    private String label;
    private String value;
    private Boolean primary;
    private Boolean outOfBand;
    private Boolean defaultDest;

    public PhoneNumber(D3Contact contact) {
        this.defaultDest = false;
        this.label = contact.getLabel().name();
        this.outOfBand = false;
        this.primary = contact.getLabel() == D3Contact.ContactLabel.HOME;
        this.type = contact.getType().name();
        this.value = contact.getValue();
    }
}