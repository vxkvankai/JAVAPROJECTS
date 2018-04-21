package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberInt;
import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;

import com.d3.api.helpers.banking.BudgetApiHelper;
import com.d3.database.DatabaseHelper;
import com.d3.datawrappers.alerts.enums.BudgetThreshold;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.TotalBudgetThresholdAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.text.DecimalFormat;

/**
 * Alert object for the Total Budget Threshold Alert, see {@link D3Alert} for more details
 */
public class TotalBudgetThresholdAlert extends D3Alert {

    private BudgetThreshold.Value thresholdValue;
    private BudgetThreshold.Type thresholdType;
    private Number threshold;

    /**
     * Creates an Alert for when the total budget amount is above or below the specified threshold
     *
     * @param thresholdValue Threshold value (AMOUNT or PERCENT)
     * @param thresholdType Threshold Type (APPROACH or EXCEED budget by)
     * @param threshold Threshold percentage or dollar amount depending on what the ThresholdValue is
     */
    public TotalBudgetThresholdAlert(BudgetThreshold.Value thresholdValue, BudgetThreshold.Type thresholdType, Number threshold) {
        super(ConsumerAlerts.BUDGET_TOTAL_THRESHOLD);
        this.thresholdValue = thresholdValue;
        this.thresholdType = thresholdType;
        this.threshold = threshold;
    }

    public BudgetThreshold.Value getThresholdValue() {
        return thresholdValue;
    }

    public BudgetThreshold.Type getThresholdType() {
        return thresholdType;
    }

    public Number getThreshold() {
        return threshold;
    }

    public String getThresholdStr() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(threshold);
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
        this.thresholdValue = RandomHelper.getRandomElementFromArray(BudgetThreshold.Value.values());
        this.thresholdType = RandomHelper.getRandomElementFromArray(BudgetThreshold.Type.values());
        //noinspection RedundantCast
        this.threshold = (thresholdValue == BudgetThreshold.Value.AMOUNT) ? Math.ceil(getRandomNumber(100, 999)) : (Number) getRandomNumberInt(1, 99);
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
        BudgetApiHelper api = new BudgetApiHelper(getConsumerApiURLFromProperties(getUser().getCuID()));
        api.login(getUser());
        api.updateTotalBudgetToTriggerAlert(this);
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
        // Return message in format : Your total spending {{APPROACH_EXCEED}} {{BUDGET_AMOUNT}} {{PERCENT_OR_AMOUNT}}.
        String approachExceed = this.getThresholdType() == BudgetThreshold.Type.APPROACH ? "has approached" : "exceeds";
        String percentAmount = this.getThresholdValue() == BudgetThreshold.Value.AMOUNT ? "" : "percent";
        String ofExpenseBudget = this.getThresholdType() == BudgetThreshold.Type.APPROACH ? "of your expense budget" : "over your expense budget";
        String budgetAmount = this.getThresholdValue() == BudgetThreshold.Value.AMOUNT ? String.format("$%s", this.getThresholdStr()) : String.format("%s", this.getThreshold());
        return String.format(alert.getMessageDetails(), approachExceed, budgetAmount, String.format("%s %s", percentAmount, ofExpenseBudget));
    }
}
