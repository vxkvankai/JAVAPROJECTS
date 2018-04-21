package com.d3.api.mappings.recipients;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResults {
    private String name;
    private String merchantId;
    private Boolean addressOnFile;
    private Boolean merchantZipRequired;
    private String billerId;
    private Boolean eBillCapable;
}
