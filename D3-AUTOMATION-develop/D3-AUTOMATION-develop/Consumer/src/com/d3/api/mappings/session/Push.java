
package com.d3.api.mappings.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Push {
    private Integer id;
    private String type;
    private String label;
    private String value;
    private Boolean primary;
    private Boolean outOfBand;
    private Boolean defaultDest;
    private Boolean readOnly;
}
