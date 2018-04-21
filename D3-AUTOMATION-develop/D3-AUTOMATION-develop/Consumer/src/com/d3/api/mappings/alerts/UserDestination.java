package com.d3.api.mappings.alerts;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDestination {

    private Boolean checked;
    private Integer id;
    private String type;
    private String label;
    private String value;
    private Boolean primary;
    private Boolean outOfBand;
    private Boolean defaultDest;
    private Boolean readOnly;

    public static UserDestination sendToInbox(int destinationId) {
        UserDestination destination = new UserDestination();
        destination.checked = true;
        destination.defaultDest = false;
        destination.id = destinationId;
        destination.label = "INBOX";
        destination.outOfBand = false;
        destination.primary = false;
        destination.readOnly = false;
        destination.type = "INBOX";
        destination.value = "Inbox";
        return destination;
    }

}

