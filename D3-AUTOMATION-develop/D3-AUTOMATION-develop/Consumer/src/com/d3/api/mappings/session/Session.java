
package com.d3.api.mappings.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {
    private List<Profile> profiles = null;
    private String csrfToken;
    private List<Object> tos = null;
    private Boolean rdcAllowed;
    private Integer startupKey;
    private String activeProfileType;
    private Integer selectedProfileIndex;
    private List<String> validProfileTypes = null;
    private String serverTime;
    private List<Object> syncOps = null;
    private Integer profileIndex;

    public Session() {
        this.profileIndex = 1;
    }
}
