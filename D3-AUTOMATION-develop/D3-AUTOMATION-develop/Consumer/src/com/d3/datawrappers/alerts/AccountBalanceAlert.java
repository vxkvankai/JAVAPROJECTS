package com.d3.datawrappers.alerts;

import com.d3.database.DatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.settings.alerts.consumer.AccountBalanceAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

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
    public void triggerAlert() {
        DatabaseHelper.runScheduledJob(this.jobTrigger());
        Assert.assertTrue(DatabaseHelper.waitForJobToRun(this.jobTrigger()), String.format("%s alert was not triggered", this.getAlert().name()));
        DatabaseHelper.stopScheduledJob(this.jobTrigger());
        Assert.assertTrue(hasAlertBeenCreated(getUser(), this), String.format("%s alert was not created", this.getAlert().name()));
    }

    @Override
    public D3ScheduledJobs jobTrigger() {
        return D3ScheduledJobs.SCHEDULED_ALERTS;
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        return String.format(alert.getMessageDetails(), getAccountUsed().getName(), String.format("$%s", getAccountUsed().getAvailableBalanceStr()));
    }
}
