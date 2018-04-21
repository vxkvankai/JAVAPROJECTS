package com.d3.datawrappers.account;

import com.d3.database.AccountDatabaseHelper;
import com.d3.datawrappers.account.D3Account.AccountingClass;

public enum ProductType {
    ASSET(AccountingClass.ASSET, "Asset Account"),
    //For Offline Asset Accounts. Can't be added through conduit
    BUSINESS_CREDIT_CARD(AccountingClass.LIABILITY, "Business Credit Card"),
    BUSINESS_DEPOSIT_CHECKING(AccountingClass.ASSET, "Deposit - Business Checking"),
    BUSINESS_DEPOSIT_SAVINGS(AccountingClass.ASSET, "Deposit - Business Savings"),
    CREDIT_CARD(AccountingClass.LIABILITY, "Credit Card"),
    DEPOSIT_CHECKING(AccountingClass.ASSET, "Deposit - Checking"),
    DEPOSIT_SAVINGS(AccountingClass.ASSET, "Deposit - Savings"),
    LIABILITY(AccountingClass.LIABILITY, "Liability Account");  //For Offline Liability Account. Can't be added through conduit


    AccountingClass accountingClass;
    String formatting;

    ProductType(AccountingClass accountingClass, String formatting) {
        this.accountingClass = accountingClass;
        this.formatting = formatting;
    }

    public String getName() {
        return formatting;
    }

    public AccountingClass getAccountingClass() {
        return accountingClass;
    }

    public Integer getAccountProductId() {
        return AccountDatabaseHelper.getAccountProductId(this.toString());
    }
}
