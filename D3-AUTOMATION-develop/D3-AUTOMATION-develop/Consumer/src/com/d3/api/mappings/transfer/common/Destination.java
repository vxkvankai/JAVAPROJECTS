package com.d3.api.mappings.transfer.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Destination {
    private String id;

    public Destination(String id) {
        this.id = id;
    }

    public Destination() {
    }

    public static Destination externalDestination(Integer enpointId) {
        Destination destination = new Destination();
        destination.setId(String.format("EXTERNAL:%s", String.valueOf(enpointId)));
        return destination;
    }
}
