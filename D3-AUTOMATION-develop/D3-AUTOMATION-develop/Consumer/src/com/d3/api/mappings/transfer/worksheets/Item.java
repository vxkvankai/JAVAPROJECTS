package com.d3.api.mappings.transfer.worksheets;

import com.d3.api.mappings.transfer.common.Destination;
import com.d3.api.mappings.transfer.common.ProviderOption;
import com.d3.api.mappings.transfer.common.Source;
import com.d3.database.RecipientMMDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private Source source;
    private Destination destination;
    private ProviderOption providerOption;
    private Object recurringModel;
    private Integer leadDays;
    private String earliestScheduleDate;
    private Boolean paperPaymentRequired;
    private String method;
    private List<Object> details = null;
    private Boolean recurring;
    private Double amount;
    private String memo;
    private String scheduledDate;

    public static Item paymentProviderItems(D3User user, PayMultipleTransfer payment) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        Integer endpointId = RecipientMMDatabaseHelper.getExternalEndpointId(user, payment.getToAccount().getName());
        Map<String, String> endpointProviderAttributes = RecipientMMDatabaseHelper.getEndpointProviderAttributes(endpointId);
        Item item = new Item();
        item.setDestination(Destination.externalDestination(endpointId));
        item.setSource(Source.internalSourceAccount(UserDatabaseHelper.waitForUserAccountId(user, payment.getFromAccount())));
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
