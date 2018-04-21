package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.alerts.enums.BudgetThreshold;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.BudgetCategoryThresholdAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.text.DecimalFormat;

@SuppressWarnings("ConstantConditions")
public class BudgetCategoryThresholdAlert extends D3Alert {

    private static DecimalFormat df = new DecimalFormat("#.00");
    private String category;
    private String thresholdValue;
    private String thresholdType;
    private Object threshold; // NOSONAR seems to work fine without transient keyword, TODO: look into not having this be of type Object

    public BudgetCategoryThresholdAlert(String category, String thresholdValue, String thresholdType, Object threshold) {
        super(ConsumerAlerts.BUDGET_CATEGORY_THRESHOLD);
        this.category = category;
        this.thresholdValue = thresholdValue;
        this.thresholdType = thresholdType;
        this.threshold = threshold;
    }

    public BudgetCategoryThresholdAlert(D3User user) {
        super(user);
    }

    @Override
    public BudgetCategoryThresholdAlert copy() {
        BudgetCategoryThresholdAlert alert = new BudgetCategoryThresholdAlert(this.category, this.thresholdValue, this.thresholdType, this.threshold);
        alert.user = this.user;
        return alert;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryId() {
        return DatabaseUtils.getCategoryId(category, "CONSUMER");
    }

    public String getThresholdValue() {
        return thresholdValue;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public Object getThreshold() {
        return threshold;
    }

    @Override
    public void createRandomData() {
        BudgetThreshold.Value randomValue = RandomHelper.getRandomElementFromArray(BudgetThreshold.Value.values());
        this.alert = ConsumerAlerts.BUDGET_CATEGORY_THRESHOLD;
        this.category = DatabaseUtils.getRandomCategoryName("CONSUMER");
        this.thresholdValue = randomValue.toString();
        this.thresholdType = RandomHelper.getRandomElementFromArray(BudgetThreshold.Type.values()).toString();
        this.threshold = (randomValue == BudgetThreshold.Value.AMOUNT) ? df.format(getRandomNumber(100, 500)) : getRandomNumberInt(1, 99);
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return BudgetCategoryThresholdAlertForm.initialize(driver, BudgetCategoryThresholdAlertForm.class);
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
