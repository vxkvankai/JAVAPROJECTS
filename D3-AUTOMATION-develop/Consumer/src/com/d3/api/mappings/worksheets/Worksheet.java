package com.d3.api.mappings.worksheets;

import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Worksheet {
    @SerializedName("provider")
    @Expose
    private Provider provider;
    @SerializedName("destinationType")
    @Expose
    private String destinationType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("template")
    @Expose
    private Boolean template;

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTemplate() {
        return template;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

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
