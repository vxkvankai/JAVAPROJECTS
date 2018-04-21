package com.d3.api.mappings.transfer.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderOption {
    private Integer id;

    public ProviderOption(com.d3.datawrappers.transfers.ProviderOption option) {
        id = option.getId();
    }

    public ProviderOption() {
    }
}
