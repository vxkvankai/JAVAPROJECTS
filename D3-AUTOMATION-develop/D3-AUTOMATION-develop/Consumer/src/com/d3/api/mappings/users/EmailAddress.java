package com.d3.api.mappings.users;

import com.d3.datawrappers.user.D3SecondaryUser;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAddress {
    private Integer id;
    private String type;
    private String label;
    private String value;
    private Boolean primary;
    private Boolean outOfBand;
    private Boolean defaultDest;
    private Boolean readOnly;
    private Boolean verified;
    private Boolean alternate;

    public EmailAddress(D3SecondaryUser secondaryUser) {
        this.defaultDest = false;
        this.label = "PRIMARY";
        this.outOfBand = false;
        this.primary = true;
        this.type = "EMAIL";
        this.value = secondaryUser.getEmail();
    }
}
