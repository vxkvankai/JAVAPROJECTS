package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.datawrappers.alerts.enums.BudgetThreshold;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.TotalBudgetThresholdAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;

public class TotalBudgetThresholdAlert extends D3Alert {

    public String getThresholdValue() {
        return thresholdValue;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public Number getThreshold() {
        return threshold;
    }

    private String thresholdValue;
    private String thresholdType;
    private Number threshold;

    public TotalBudgetThresholdAlert(String thresholdValue, String thresholdType, Number threshold) {
        super(ConsumerAlerts.BUDGET_TOTAL_THRESHOLD);
        this.thresholdValue = thresholdValue;
        this.thresholdType = thresholdType;
        this.threshold = threshold;
    }

    public TotalBudgetThresholdAlert(D3User user) {
        super(user);
    }

    @Override
    public TotalBudgetThresholdAlert copy() {
        TotalBudgetThresholdAlert alert = new TotalBudgetThresholdAlert(this.thresholdValue, this.thresholdType, this.threshold);
        alert.user = user;
        return alert;
    }

    @Override
    public void createRandomData() {
        this.alert = ConsumerAlerts.BUDGET_TOTAL_THRESHOLD;
        BudgetThreshold.Value randomValue = RandomHelper.getRandomElementFromArray(BudgetThreshold.Value.values());
        this.thresholdValue = randomValue.toString();
        this.thresholdType = RandomHelper.getRandomElementFromArray(BudgetThreshold.Type.values()).toString();
        this.threshold = (randomValue == BudgetThreshold.Value.AMOUNT) ? getRandomNumber(100, 999) : getRandomNumberInt(1, 99);
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return TotalBudgetThresholdAlertForm.initialize(driver, TotalBudgetThresholdAlertForm.class);
    }

    @Override
    public void triggerAlert() throws D3ApiException {
        throw new D3ApiException(String.format("Triggering %s alert not yet implemented", this.getName()));

    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        throw new NotImplementedException(String.format("getTriggeredAlertMessageDetails for %s alert not yet implemented", this.alert.name()));
    }
}
