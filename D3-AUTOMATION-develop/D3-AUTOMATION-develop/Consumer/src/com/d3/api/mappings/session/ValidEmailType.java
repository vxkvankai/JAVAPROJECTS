package com.d3.api.mappings.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidEmailType {
    private String interfaceName;
    private String internalName;
    private Integer order;
    private Boolean repeatable;
    private String destinationType;
}
