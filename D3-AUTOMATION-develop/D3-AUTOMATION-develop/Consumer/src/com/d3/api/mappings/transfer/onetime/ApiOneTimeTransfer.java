package com.d3.api.mappings.transfer.onetime;

import com.d3.api.mappings.transfer.common.Destination;
import com.d3.api.mappings.transfer.common.ProviderOption;
import com.d3.api.mappings.transfer.common.Source;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.format.DateTimeFormat;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiOneTimeTransfer {
    private Source source;
    private Destination destination;
    private ProviderOption providerOption;
    private Object recurringModel;
    private Boolean recurring;
    private Double amount;
    private String note;
    private String scheduledDate;

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