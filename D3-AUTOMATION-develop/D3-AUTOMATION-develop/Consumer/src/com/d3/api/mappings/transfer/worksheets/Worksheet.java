package com.d3.api.mappings.transfer.worksheets;

import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Worksheet {
    private Provider provider;
    private String destinationType;
    private String status;
    private List<Item> items;
    private String name;
    private Boolean template;

    public Worksheet(D3User user, List<PayMultipleTransfer> payments) {
        this.destinationType = "ALL";
        this.status = "NEW";
        this.template = true;
        this.name = String.format("Template-%s", RandomHelper.getRandomString(7));
        this.items = new ArrayList<>();
        payments.forEach(payment -> this.items.add(Item.paymentProviderItems(user, payment)));
        this.provider = Provider.checkFreeBillPayProvider();
    }
}
