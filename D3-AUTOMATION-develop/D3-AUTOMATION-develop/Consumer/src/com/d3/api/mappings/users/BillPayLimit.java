package com.d3.api.mappings.users;


import com.d3.datawrappers.user.D3AccountLimits;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPayLimit {
    private double amount;
    private Boolean invalidCount;
    private Integer count;
    private String period;

    public BillPayLimit(D3AccountLimits limit) {
        this.amount = limit.getAmountLimit();
        this.count = limit.getCountLimit();
        this.period = limit.getPeriodLimit().name();
        this.invalidCount = false;
    }
}
