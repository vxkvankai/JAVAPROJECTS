package com.d3.api.mappings.transfer.common;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {
    private String id;

    public Source(String id) {
        this.id = id;
    }

    public Source() {
    }

    public static Source internalSourceAccount(Integer accountId) {
        Source source = new Source();
        source.setId(String.format("INTERNAL:%s", String.valueOf(accountId)));
        return source;
    }
}
