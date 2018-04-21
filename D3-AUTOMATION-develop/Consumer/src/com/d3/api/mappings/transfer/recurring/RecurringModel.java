package com.d3.api.mappings.transfer.recurring;

import com.d3.datawrappers.transfers.BillPayRecurringTransfer;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferEndType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RecurringModel {

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
    private Object recurringModelField;
    @SerializedName("recurring")
    @Expose
    private Boolean recurring;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("endOption")
    @Expose
    private String endOption;
    @SerializedName("thresholdAmount")
    @Expose
    private Double thresholdAmount;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("memo")
    @Expose
    private String memo;

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

    public Object getRecurringModelField() {
        return recurringModelField;
    }

    public void setRecurringModelField(Object recurringModelField) {
        this.recurringModelField = recurringModelField;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getEndOption() {
        return endOption;
    }

    public void setEndOption(String endOption) {
        this.endOption = endOption;
    }

    public Double getThresholdAmount() {
        return thresholdAmount;
    }

    public void setThresholdAmount(Double thresholdAmount) {
        this.thresholdAmount = thresholdAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public RecurringModel(RecurringTransfer transfer, D3User user) {
        source = new Source(transfer.getFromAccount().getInternalExternalId(user));
        destination = new Destination(transfer.getToAccount().getInternalExternalId(user));
        providerOption = new ProviderOption(transfer.getProviderOption());
        recurringModelField = null;
        recurring = false;
        amount = Double.valueOf(transfer.getAmountStr());
        frequency = transfer.getFrequency().toString();
        endOption = transfer.getEndType().toString();
        if (!transfer.getToAccount().isBillPay()) {
            thresholdAmount = ((StandardRecurringTransfer) transfer).getBalanceThreshold();
        } else {
            memo = ((BillPayRecurringTransfer) transfer).getMemo();
        }
        note = transfer.getNote().isEmpty() ? null : transfer.getNote();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        startDate = formatter.print(transfer.getScheduledDate());
        if (transfer.getEndType() == RecurringTransferEndType.END_DATE) {
            endDate = formatter.print(transfer.getEndDate());
        }
    }
}
