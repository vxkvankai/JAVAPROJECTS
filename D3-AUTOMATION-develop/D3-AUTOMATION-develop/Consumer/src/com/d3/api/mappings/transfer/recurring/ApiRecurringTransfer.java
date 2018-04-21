package com.d3.api.mappings.transfer.recurring;

import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiRecurringTransfer {
    private Object source;
    private Object destination;
    private Object providerOption;
    private RecurringModel recurringModel;
    private Boolean recurring;

    public ApiRecurringTransfer(RecurringTransfer transfer, D3User user) {
        source = null;
        destination = null;
        recurring = true;
        recurringModel = new RecurringModel(transfer, user);
    }

    public String toString() {
        return String.format("To %s From %s for %s",
            recurringModel.getDestination(),
            recurringModel.getSource(),
            recurringModel.getAmount());
    }
}
