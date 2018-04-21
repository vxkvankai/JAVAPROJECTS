package com.d3.api.mappings.ebills;

import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.ebills.D3EBill;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoPay {
    private String leadDays;
    private String trigger;
    private String amountType;
    private String sourceId;
    private double fixedAmount;
    private Boolean alertWhenScheduled;
    private Boolean alertWhenSent;

    public AutoPay(D3User user, D3EBill autoPay) {
        this.trigger = autoPay.getPayOn().name();
        this.amountType = autoPay.getAmountType().name();
        this.sourceId = String.format("INTERNAL:%s", String.valueOf(UserDatabaseHelper.waitForUserAccountId(user, autoPay.getAutoPayAccount())));
        this.fixedAmount = autoPay.getAmount();
        if (autoPay.getPayOn() == com.d3.datawrappers.ebills.enums.AutoPay.PayOn.LEAD_DAYS) {
            this.leadDays = autoPay.getDaysBefore().name();
        }
        this.alertWhenScheduled = autoPay.getAlertWhenScheduled();
        this.alertWhenSent = autoPay.getAlertWhenSent();
    }
}

