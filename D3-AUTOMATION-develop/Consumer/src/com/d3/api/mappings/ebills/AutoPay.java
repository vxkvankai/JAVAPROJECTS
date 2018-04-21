package com.d3.api.mappings.ebills;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.ebills.D3EBill;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutoPay {

    @SerializedName("leadDays")
    @Expose
    private String leadDays;
    @SerializedName("trigger")
    @Expose
    private String trigger;
    @SerializedName("amountType")
    @Expose
    private String amountType;
    @SerializedName("sourceId")
    @Expose
    private String sourceId;
    @SerializedName("fixedAmount")
    @Expose
    private double fixedAmount;
    @SerializedName("alertWhenScheduled")
    @Expose
    private Boolean alertWhenScheduled;
    @SerializedName("alertWhenSent")
    @Expose
    private Boolean alertWhenSent;

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getLeadDays() {
        return leadDays;
    }

    public void setLeadDays(String leadDays) {
        this.leadDays = leadDays;
    }

    public Boolean getAlertWhenScheduled() {
        return alertWhenScheduled;
    }

    public void setAlertWhenScheduled(Boolean alertWhenScheduled) {
        this.alertWhenScheduled = alertWhenScheduled;
    }

    public Boolean getAlertWhenSent() {
        return alertWhenSent;
    }

    public void setAlertWhenSent(Boolean alertWhenSent) {
        this.alertWhenSent = alertWhenSent;
    }

    public double getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public AutoPay(D3User user, D3EBill autoPay) {
        this.trigger = autoPay.getPayOn().name();
        this.amountType = autoPay.getAmountType().name();
        this.sourceId = String.format("INTERNAL:%s", String.valueOf(DatabaseUtils.waitForUserAccountId(user, autoPay.getAutoPayAccount())));
        this.fixedAmount = autoPay.getAmount();
        if (autoPay.getPayOn() == com.d3.datawrappers.ebills.enums.AutoPay.PayOn.LEAD_DAYS) {
            this.leadDays = autoPay.getDaysBefore().name();
        }
        this.alertWhenScheduled = autoPay.getAlertWhenScheduled();
        this.alertWhenSent = autoPay.getAlertWhenSent();
    }

}

