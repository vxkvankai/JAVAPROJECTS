package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.CreditDepositAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class CreditDepositAlertForm extends AlertForm<CreditDepositAlertForm, CreditDepositAlert> implements AlertDetails<CreditDepositAlert> {

    public CreditDepositAlertForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected CreditDepositAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(CreditDepositAlert alert) {
        selectAccount(alert.getAccountUsed().getName());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(CreditDepositAlert alert) {
        String errorMsg = "%s: %s for Credit Deposit Alert was not found on the DOM.";
        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
        } catch (TextNotDisplayedException e) {
            log.warn("Credit Deposit Alert was not validated", e);
            return false;
        }

        return true;
    }

}
