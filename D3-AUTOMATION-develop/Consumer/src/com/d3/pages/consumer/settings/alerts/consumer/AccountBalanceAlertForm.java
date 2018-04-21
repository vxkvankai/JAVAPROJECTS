package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.AccountBalanceAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import org.openqa.selenium.WebDriver;

public class AccountBalanceAlertForm extends AlertForm<AccountBalanceAlertForm, AccountBalanceAlert> implements AlertDetails<AccountBalanceAlert> {

    public AccountBalanceAlertForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AccountBalanceAlertForm me() {
        return this;
    }


    @Override
    public AlertForm fillOutForm(AccountBalanceAlert alert) {
        selectAccount(alert.getAccountUsed().getName());
        selectFrequency(alert.getFrequency());
        selectFrequencyAttributes(alert.getFrequency());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(AccountBalanceAlert alert) {
        String errorMsg = "%s: %s for Account Balance Alert was not found on the DOM.";

        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getFrequency().toString(), errorMsg, "Alert Frequency");
        } catch (TextNotDisplayedException e) {
            logger.warn("AccountBalanceAlert was not correct", e);
            return false;
        }

        return true;
    }

}
