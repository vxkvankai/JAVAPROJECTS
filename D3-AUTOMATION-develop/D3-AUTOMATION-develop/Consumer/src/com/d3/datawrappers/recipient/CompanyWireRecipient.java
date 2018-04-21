package com.d3.datawrappers.recipient;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.recipient.base.WireRecipient;
import com.d3.datawrappers.user.D3Address;
import com.d3.exceptions.D3ApiException;

public class CompanyWireRecipient extends WireRecipient {

    public CompanyWireRecipient(String name, String routingNumber, String accountNumber, String financialInstitution, D3Address address) {
        super(name, routingNumber, accountNumber, financialInstitution, address);
        who = RecipientWho.COMPANY;
    }

    @Override
    public Recipient getNewRandomVersion() {
        return WireRecipient.createRandomCompanyRecipient();
    }

    @Override
    public void addApiRecipient(MoneyMovementApiHelper api) throws D3ApiException {
        api.addRecipient(new ApiRecipient(this));
    }

    @Override
    public boolean eligibleForExpedite() {
        return false;
    }
}
    
  
