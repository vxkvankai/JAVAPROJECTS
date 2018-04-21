package com.d3.api.mappings.authentication;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authentication {
    private Challenge challenge;
    private List<Object> previousItems = null;
    private String token;
    private Boolean passwordResetEnabled;
    private Boolean authenticated;
    private Boolean isAuthenticated;
}
