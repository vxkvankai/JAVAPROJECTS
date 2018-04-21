package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.BalanceThresholdAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class BalanceThresholdAlertForm extends AlertForm<BalanceThresholdAlertForm, BalanceThresholdAlert>
        implements AlertDetails<BalanceThresholdAlert> {

    @FindBy(id = "operatorProperty")
    private Select balanceThreshold;

    @FindBy(id = "thresholdProperty")
    private Element balanceThresholdAmount;

    public BalanceThresholdAlertForm(WebDriver driver) {
        super(driver);
    }

    public BalanceThresholdAlertForm selectThreshold(String threshold) {
        balanceThreshold.selectByText(threshold);
        return this;
    }

    public BalanceThresholdAlertForm enterThresholdAmount(String amount) {
        balanceThresholdAmount.sendKeys(amount);
        return this;
    }

    @Override
    protected BalanceThresholdAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(BalanceThresholdAlert alert) {
        selectAccount(alert.getAccountUsed().getName());
        selectThreshold(alert.getThreshold());
        enterThresholdAmount(alert.getAmountStr());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(BalanceThresholdAlert alert) {
        String errorMsg = "%s: %s for Balance Threshold Alert was not found on the DOM.";
        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getThreshold(), errorMsg, "Threshold");
            checkIfTextDisplayed(alert.getAmountStr(), errorMsg, "Amount");
        } catch (TextNotDisplayedException e) {
            log.warn("Balance Threshold Alert was not validated", e);
            return false;
        }

        return true;
    }

}
