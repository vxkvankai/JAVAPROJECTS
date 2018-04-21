package com.d3.datawrappers.account;

import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.user.D3User;

import java.io.Serializable;

public interface TransferableAccount extends Serializable {
    String getName();
    String getInternalExternalId(D3User user);
    ProviderOption getProviderOption();
    String getTransferableName();
    boolean isBillPay();
    boolean hasNoteField();
    boolean eligibleForExpedite();
}
