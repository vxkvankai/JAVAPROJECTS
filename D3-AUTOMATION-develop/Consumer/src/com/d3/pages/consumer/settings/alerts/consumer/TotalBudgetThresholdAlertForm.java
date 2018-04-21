package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.TotalBudgetThresholdAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class TotalBudgetThresholdAlertForm extends AlertForm<TotalBudgetThresholdAlertForm, TotalBudgetThresholdAlert>
        implements AlertDetails<TotalBudgetThresholdAlert> {

    @FindBy(id = "thresholdType")
    private Select thresholdValue;

    @FindBy(name = "properties.THRESHOLD_TYPE")
    private Select thresholdType;

    @FindBy(id = "thresholdProperty")
    private Element thresholdAmount;


    public TotalBudgetThresholdAlertForm(WebDriver driver) {
        super(driver);
    }

    public TotalBudgetThresholdAlertForm selectThresholdValue(String actual) {
        thresholdValue.selectByText(actual);
        return this;
    }

    public TotalBudgetThresholdAlertForm selectThresholdType(String type) {
        thresholdType.selectByText(type);
        return this;
    }

    public TotalBudgetThresholdAlertForm enterThreshold(String threshold) {
        thresholdAmount.sendKeys(threshold);
        return this;
    }

    @Override
    protected TotalBudgetThresholdAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(TotalBudgetThresholdAlert alert) {
        selectThresholdValue(alert.getThresholdValue());
        selectThresholdType(alert.getThresholdType());
        enterThreshold(alert.getThreshold().toString());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(TotalBudgetThresholdAlert alert) {
        String errorMsg = "%s: %s for Total Budget Threshold Alert was not found on the DOM.";
        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getThresholdType(), errorMsg, "Amount or Percent Value");
            checkIfTextDisplayed(alert.getThresholdType(), errorMsg, "Threshold Exceed or Approach Budget");
        } catch (TextNotDisplayedException e) {
            logger.warn("Total Budget Threshold Alert was not validated", e);
            return false;
        }

        return true;
    }
}
