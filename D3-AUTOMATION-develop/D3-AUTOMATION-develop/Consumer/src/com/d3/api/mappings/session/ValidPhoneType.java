package com.d3.api.mappings.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidPhoneType {
    private String interfaceName;
    private String internalName;
    private Boolean repeatable;
    private Boolean mobileType;
    private String destinationType;
}
