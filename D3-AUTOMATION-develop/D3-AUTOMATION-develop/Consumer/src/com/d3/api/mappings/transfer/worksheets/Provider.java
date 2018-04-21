package com.d3.api.mappings.transfer.worksheets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Provider {
    private String transferMethod;
    private String name;
    private String description;

    public static Provider checkFreeBillPayProvider() {
        Provider provider = new Provider();
        provider.setDescription("CheckFree");
        provider.setName("CheckFree");
        provider.setTransferMethod("BILL_PAY");
        return provider;
    }
}