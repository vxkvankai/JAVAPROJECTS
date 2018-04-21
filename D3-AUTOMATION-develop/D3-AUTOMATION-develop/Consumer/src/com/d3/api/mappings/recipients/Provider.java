package com.d3.api.mappings.recipients;

import com.d3.datawrappers.recipient.CompanyBillPayManualRecipient;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.datawrappers.recipient.base.AccountIOwnRecipient;
import com.d3.datawrappers.recipient.base.OnUsAccountIOwnRecipient;
import com.d3.datawrappers.recipient.base.TransferMethod;
import com.d3.datawrappers.recipient.base.WireRecipient;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Provider {

    private String transferMethod;
    private PrimaryAddress primaryAddress;
    private String phoneNumber;
    private String email;
    private String accountNumber;
    private String fiName;
    private String routingNumber;
    private String accountType;
    private boolean isMicroDeposit;
    private boolean preNote;
    private SearchResults searchResults;
    private String merchantZip;
    private boolean merchantZipRequired;

    public Provider(PersonBillAddressRecipient recipient) {
        this.phoneNumber = recipient.getPhoneNumber();
        this.email = recipient.getEmail();
        this.transferMethod = TransferMethod.BILL_PAY.toString();
        this.primaryAddress = new PrimaryAddress(recipient);
    }

    public Provider(WireRecipient recipient) {
        this.accountNumber = recipient.getAccountNumber();
        this.fiName = recipient.getFinancialInstitution();
        this.routingNumber = recipient.getRoutingNumber();
        this.transferMethod = TransferMethod.FEDWIRE.toString();
        this.primaryAddress = new PrimaryAddress(recipient);
    }

    public Provider(CompanyBillPaySeedRecipient recipient, SearchResults searchResults) {
        this.accountNumber = recipient.getBillerAccountNumber();
        this.merchantZip = recipient.getMerchantZip();
        this.merchantZipRequired = true;
        this.searchResults = searchResults;
        this.transferMethod = TransferMethod.BILL_PAY.toString();
    }

    public Provider(AccountIOwnRecipient recipient) {
        this.accountNumber = recipient.getAccountNumber();
        this.accountType = recipient.getAccountType().toString();
        this.fiName = recipient.getFinancialInstitution();
        this.isMicroDeposit = true;
        this.preNote = true;
        this.routingNumber = recipient.getRoutingNumber();
        this.transferMethod = TransferMethod.ACH.toString();
    }


    public Provider(CompanyBillPayManualRecipient recipient) {
        this.accountNumber = recipient.getBillerAccountNumber();
        this.primaryAddress = new PrimaryAddress(recipient);
        this.transferMethod = TransferMethod.BILL_PAY.toString();
    }

    public Provider(OnUsAccountIOwnRecipient recipient) {
        this.accountNumber = recipient.getAccountNumber();
        this.accountType=recipient.getAccountType().toString();
        this.fiName = recipient.getFinancialInstitution();
        this.routingNumber = recipient.getRoutingNumber();
        this.transferMethod = TransferMethod.ON_US.toString();
    }

}

