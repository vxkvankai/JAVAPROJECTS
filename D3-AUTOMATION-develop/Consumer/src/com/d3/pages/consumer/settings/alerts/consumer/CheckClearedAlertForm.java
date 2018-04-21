package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class CheckClearedAlertForm extends AlertForm<CheckClearedAlertForm, CheckClearedAlert> implements AlertDetails<CheckClearedAlert> {

    @FindBy(name = "properties.CHECK_NUMBER")
    private Element checkNumber;

    public CheckClearedAlertForm(WebDriver driver) {
        super(driver);
    }

    public CheckClearedAlertForm enterCheckNumber(String check) {
        checkNumber.sendKeys(check);
        return this;
    }

    @Override
    protected CheckClearedAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(CheckClearedAlert alert) {
        selectAccount(alert.getAccountUsed().getName());
        enterCheckNumber(alert.getCheckNumber());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(CheckClearedAlert alert) {
        String errorMsg = "%s: %s for Check Cleared Alert was not found on the DOM.";
        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");

            String shortDescription = "Alert when check has cleared";
            checkIfTextDisplayed(shortDescription, errorMsg, "Short Alert Description");
            checkIfTextDisplayed(alert.getCheckNumber(), errorMsg, "Check Number");
        } catch (TextNotDisplayedException e) {
            logger.warn("Check Cleared Alert was not validated", e);
            return false;
        }

        return true;
    }

}
