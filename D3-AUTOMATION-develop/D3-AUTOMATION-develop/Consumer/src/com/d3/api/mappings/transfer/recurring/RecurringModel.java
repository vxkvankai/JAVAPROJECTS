package com.d3.api.mappings.transfer.recurring;

import com.d3.api.mappings.transfer.common.Destination;
import com.d3.api.mappings.transfer.common.ProviderOption;
import com.d3.api.mappings.transfer.common.Source;
import com.d3.datawrappers.transfers.BillPayRecurringTransfer;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferEndType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecurringModel {
    private Source source;
    private Destination destination;
    private ProviderOption providerOption;
    private Object recurringModelField;
    private Boolean recurring;
    private Double amount;
    private String frequency;
    private String endOption;
    private Double thresholdAmount;
    private String note;
    private String endDate;
    private String startDate;
    private String memo;

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
