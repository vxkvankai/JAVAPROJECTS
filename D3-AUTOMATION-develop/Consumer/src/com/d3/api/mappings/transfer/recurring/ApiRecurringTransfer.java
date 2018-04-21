package com.d3.api.mappings.transfer.recurring;

import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiRecurringTransfer {

    @SerializedName("source")
    @Expose
    private Object source;
    @SerializedName("destination")
    @Expose
    private Object destination;
    @SerializedName("providerOption")
    @Expose
    private Object providerOption;
    @SerializedName("recurringModel")
    @Expose
    private RecurringModel recurringModel;
    @SerializedName("recurring")
    @Expose
    private Boolean recurring;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getDestination() {
        return destination;
    }

    public void setDestination(Object destination) {
        this.destination = destination;
    }

    public Object getProviderOption() {
        return providerOption;
    }

    public void setProviderOption(Object providerOption) {
        this.providerOption = providerOption;
    }

    public RecurringModel getRecurringModel() {
        return recurringModel;
    }

    public void setRecurringModel(RecurringModel recurringModel) {
        this.recurringModel = recurringModel;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public ApiRecurringTransfer(RecurringTransfer transfer, D3User user) {
        source = null;
        destination = null;
        recurring = true;
        recurringModel = new RecurringModel(transfer, user);
    }

    public String toString() {
        return String.format("To %s From %s for %s", recurringModel.getDestination(), recurringModel.getSource(), recurringModel.getAmount());
    }
}
