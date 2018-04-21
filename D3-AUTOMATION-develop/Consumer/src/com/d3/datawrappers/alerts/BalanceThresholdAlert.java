package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.alerts.enums.BalanceThreshold;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.BalanceThresholdAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;

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
        this.amountDbl = (randomThreshold == BalanceThreshold.LT) ? getRandomNumber(1000, 2000) : getRandomNumber(5000, 6000);
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
    public void triggerAlert() throws D3ApiException, ConduitException {
        throw new D3ApiException(String.format("Triggering %s alert not yet implemented", this.getName()));

    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        throw new NotImplementedException(String.format("getTriggeredAlertMessageDetails for %s alert not yet implemented", this.alert.name()));
    }
}
