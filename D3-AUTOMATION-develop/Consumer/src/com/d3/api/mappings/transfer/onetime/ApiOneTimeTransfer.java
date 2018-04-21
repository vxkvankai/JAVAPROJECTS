package com.d3.api.mappings.transfer.onetime;

import com.d3.api.mappings.transfer.common.Destination;
import com.d3.api.mappings.transfer.common.ProviderOption;
import com.d3.api.mappings.transfer.common.Source;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.format.DateTimeFormat;

public class ApiOneTimeTransfer {

    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("destination")
    @Expose
    private Destination destination;
    @SerializedName("providerOption")
    @Expose
    private ProviderOption providerOption;
    @SerializedName("recurringModel")
    @Expose
    private Object recurringModel;
    @SerializedName("recurring")
    @Expose
    private Boolean recurring;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("scheduledDate")
    @Expose
    private String scheduledDate;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public ProviderOption getProviderOption() {
        return providerOption;
    }

    public void setProviderOption(ProviderOption providerOption) {
        this.providerOption = providerOption;
    }

    public Object getRecurringModel() {
        return recurringModel;
    }

    public void setRecurringModel(Object recurringModel) {
        this.recurringModel = recurringModel;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public ApiOneTimeTransfer(SingleTransfer transfer, D3User user) {
        amount = transfer.getAmount();
        destination = new Destination(transfer.getToAccount().getInternalExternalId(user));
        note = transfer.getNote();
        providerOption = new ProviderOption(transfer.getProviderOption());
        recurring = false;
        scheduledDate = DateTimeFormat.forPattern("yyyy-MM-dd").print(transfer.getScheduledDate());
        source = new Source(transfer.getFromAccount().getInternalExternalId(user));
    }

}