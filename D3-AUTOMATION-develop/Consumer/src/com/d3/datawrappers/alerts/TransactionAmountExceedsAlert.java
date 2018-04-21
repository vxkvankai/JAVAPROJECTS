package com.d3.datawrappers.alerts;


import static com.d3.helpers.RandomHelper.getRandomNumber;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.settings.alerts.consumer.TransactionAmountExceedsAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ThreadLocalRandom;


public class TransactionAmountExceedsAlert extends D3Alert {

    private static final ImmutableList<String> TRANSACTION_TYPES = ImmutableList.of("All", "Credit", "Debit");

    public String getTransactionType() {
        return transactionType;
    }

    private String transactionType;

    public TransactionAmountExceedsAlert(D3Account account, double amount, String transactionType) {
        super(ConsumerAlerts.TRANSACTION_AMOUNT);
        this.account = account;
        this.amountDbl = amount;
        this.transactionType = transactionType;
    }

    public TransactionAmountExceedsAlert(D3User user) {
        super(user);
    }

    @Override
    public TransactionAmountExceedsAlert copy() {
        TransactionAmountExceedsAlert alert = new TransactionAmountExceedsAlert(this.account, this.amountDbl, this.transactionType);
        alert.user = user;
        return alert;
    }

    @Override
    public void createRandomData() {
        this.alert = ConsumerAlerts.TRANSACTION_AMOUNT;
        this.account = user.getRandomAccount();
        this.amountDbl = getRandomNumber(100, 999);
        this.transactionType = TRANSACTION_TYPES.get(ThreadLocalRandom.current().nextInt(TRANSACTION_TYPES.size()));
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return TransactionAmountExceedsAlertForm.initialize(driver, TransactionAmountExceedsAlertForm.class);
    }

    @Override
    public void triggerAlert() throws D3ApiException, ConduitException {
        throw new D3ApiException(String.format("Triggering %s alert not yet implemented", this.getName()));
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        throw new NotImplementedException(String.format("getTriggeredAlertMessageDetails for %s alert not yet implemented", this.alert.name()));
    }


}
