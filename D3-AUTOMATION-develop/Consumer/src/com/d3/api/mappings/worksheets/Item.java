package com.d3.api.mappings.worksheets;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;

public class Item {

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
    @SerializedName("leadDays")
    @Expose
    private Integer leadDays;
    @SerializedName("earliestScheduleDate")
    @Expose
    private String earliestScheduleDate;
    @SerializedName("paperPaymentRequired")
    @Expose
    private Boolean paperPaymentRequired;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("details")
    @Expose
    private List<Object> details = null;
    @SerializedName("recurring")
    @Expose
    private Boolean recurring;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("memo")
    @Expose
    private String memo;
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

    public Integer getLeadDays() {
        return leadDays;
    }

    public void setLeadDays(Integer leadDays) {
        this.leadDays = leadDays;
    }

    public String getEarliestScheduleDate() {
        return earliestScheduleDate;
    }

    public void setEarliestScheduleDate(String earliestScheduleDate) {
        this.earliestScheduleDate = earliestScheduleDate;
    }

    public Boolean getPaperPaymentRequired() {
        return paperPaymentRequired;
    }

    public void setPaperPaymentRequired(Boolean paperPaymentRequired) {
        this.paperPaymentRequired = paperPaymentRequired;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getDetails() {
        return details;
    }

    public void setDetails(List<Object> details) {
        this.details = details;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }


    public static Item paymentProviderItems(D3User user, PayMultipleTransfer payment) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        Integer endpointId = DatabaseUtils.getExternalEndpointId(user, payment.getToAccount().getName());
        Map<String, String> endpointProviderAttributes = DatabaseUtils.getEndpointProviderAttributes(endpointId);
        Item item = new Item();
        item.setDestination(Destination.externalDestination(endpointId));
        item.setSource(Source.internalSourceAccount(DatabaseUtils.waitForUserAccountId(user, payment.getFromAccount())));
        item.details = null;
        item.setEarliestScheduleDate(endpointProviderAttributes.get("earliestPaymentDate"));
        item.setLeadDays(Integer.valueOf(endpointProviderAttributes.get("leadDays")));
        item.setMemo(payment.getNote());
        item.setMethod("BILL_PAY");
        item.setPaperPaymentRequired(Boolean.valueOf(endpointProviderAttributes.get("paperPaymentEnabled")));
        item.providerOption = new ProviderOption();
        item.recurring = false;
        item.setScheduledDate(formatter.print(payment.getScheduledDate()));
        item.amount = payment.getAmount();
        return item;
    }

}
