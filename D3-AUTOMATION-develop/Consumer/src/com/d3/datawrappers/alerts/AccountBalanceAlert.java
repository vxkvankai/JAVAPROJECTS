package com.d3.datawrappers.alerts;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.settings.alerts.consumer.AccountBalanceAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;

public class AccountBalanceAlert extends D3Alert {


    public AccountBalanceAlert(D3Account account, AlertFrequency frequency) {
        super(ConsumerAlerts.PERIODIC_BALANCE);
        this.account = account;
        this.frequency = frequency;
    }

    public AccountBalanceAlert(D3User user) {
        super(user);
    }

    @Override
    public AccountBalanceAlert copy() {
        AccountBalanceAlert alert = new AccountBalanceAlert(this.account, this.frequency);
        alert.user = user;
        return alert;
    }

    @Override
    public void createRandomData() {
        alert = ConsumerAlerts.PERIODIC_BALANCE;
        this.account = user.getRandomAccount();
        this.frequency = AlertFrequency.getRandom();
    }

    @Override
    public boolean hasFrequency() {
        return true;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return AccountBalanceAlertForm.initialize(driver, AccountBalanceAlertForm.class);
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
