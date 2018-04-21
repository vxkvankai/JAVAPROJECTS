package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;

import com.d3.database.DatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.alerts.enums.BalanceThreshold;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.BalanceThresholdAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class BalanceThresholdAlert extends D3Alert {

    public String getThreshold() {
        return threshold;
    }

    private String threshold;

    public BalanceThresholdAlert(D3Account account, String threshold, double amount) {
        super(ConsumerAlerts.BALANCE_THRESHOLD);
        this.account = account;
        this.threshold = threshold;
        this.amountDbl = amount;
    }

    public BalanceThresholdAlert(D3User user) {
        super(user);
    }

    @Override
    public BalanceThresholdAlert copy() {
        BalanceThresholdAlert alert = new BalanceThresholdAlert(this.account, this.threshold, this.amountDbl);
        alert.user = this.user;
        return alert;
    }

    @Override
    public void createRandomData() {
        BalanceThreshold randomThreshold = RandomHelper.getRandomElementFromArray(BalanceThreshold.values());
        alert = ConsumerAlerts.BALANCE_THRESHOLD;
        this.account = user.getRandomAccount();
        this.threshold = randomThreshold.toString();
        this.amountDbl = (randomThreshold == BalanceThreshold.LT) ? getRandomNumber(10001, 11000) : getRandomNumber(500, 999);
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return BalanceThresholdAlertForm.initialize(driver, BalanceThresholdAlertForm.class);
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
        return D3ScheduledJobs.DAILY_ALERTS;
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        String overUnder = getThreshold().equals("Less Than") ? "under" : "over";
        return String.format(alert.getMessageDetails(), getAccountUsed().getName(), overUnder, String.format("$%s", getAmountStr()));
    }
}
