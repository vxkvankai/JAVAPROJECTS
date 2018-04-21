package com.d3.datawrappers.transfers;

import com.d3.database.DatabaseUtils;

public enum ProviderOption {
    REGULAR_PAYMENT("RegularPayment"),
    EXPEDITED_PAYMENT("ExpeditedPayment"),
    OVERNIGHT_CHECK("OvernightCheck"),
    INTERNAL_TRANSFER("InternalTransfer"),
    CORE_ON_US_TRANSFER("CoreOnUsTransfer"),
    ACH_TRANSFER("ACHTransfer"),
    FEDWIRE_TRANSFER("FedwireTransfer");

    private String externalId;
    ProviderOption(String externalId) {
        this.externalId = externalId;
    }

    public Integer getId() {
        return DatabaseUtils.getProviderOptionId(externalId);
    }

    public String getExternalId() {
        return externalId;
    }
}
