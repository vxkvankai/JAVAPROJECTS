package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.BudgetCategoryThresholdAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class BudgetCategoryThresholdAlertForm extends AlertForm<BudgetCategoryThresholdAlertForm, BudgetCategoryThresholdAlert>
        implements AlertDetails<BudgetCategoryThresholdAlert> {

    @FindBy(id = "categoryId")
    private Select category;

    @FindBy(id = "thresholdProperty")
    private Select thresholdValue;

    @FindBy(name = "properties.THRESHOLD_TYPE")
    private Select thresholdType;

    @FindBy(id = "thresholdCategory")
    private Element thresholdAmount;


    public BudgetCategoryThresholdAlertForm(WebDriver driver) {
        super(driver);
    }

    public BudgetCategoryThresholdAlertForm selectCategory(String categoryId) {
        category.selectByValue(categoryId);
        return this;
    }

    public BudgetCategoryThresholdAlertForm selectThresholdValue(String actual) {
        thresholdValue.selectByText(actual);
        return this;
    }

    public BudgetCategoryThresholdAlertForm selectThresholdType(String type) {
        thresholdType.selectByText(type);
        return this;
    }

    public BudgetCategoryThresholdAlertForm enterCategoryThreshold(String threshold) {
        thresholdAmount.sendKeys(threshold);
        return this;
    }

    @Override
    protected BudgetCategoryThresholdAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(BudgetCategoryThresholdAlert alert) {
        logger.info("Selecting Category: {} with Id: {}", alert.getCategory(), alert.getCategoryId());
        selectCategory(String.valueOf(alert.getCategoryId()));
        selectThresholdValue(alert.getThresholdValue());
        selectThresholdType(alert.getThresholdType());
        enterCategoryThreshold(alert.getThreshold().toString());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(BudgetCategoryThresholdAlert alert) {
        String errorMsg = "%s: %s for Budget Category Threshold Alert was not found on the DOM.";

        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getThresholdValue(), errorMsg, "Amount or Percent Value");
            checkIfTextDisplayed(alert.getThreshold().toString(), errorMsg, "Threshold Amount or Percent Value");
            checkIfTextDisplayed(alert.getCategory(), errorMsg, "Category Name");
            checkIfTextDisplayed(alert.getThresholdType(), errorMsg, "Threshold Exceed or Approach Budget");
        } catch (TextNotDisplayedException e) {
            logger.warn("Budget Category Threshold Alert was not validated", e);
            return false;
        }

        return true;
    }
}
