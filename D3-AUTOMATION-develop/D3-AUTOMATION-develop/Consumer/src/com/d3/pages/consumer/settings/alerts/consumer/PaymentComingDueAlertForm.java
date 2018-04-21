package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.PaymentComingDueAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

@Slf4j
public class PaymentComingDueAlertForm extends AlertForm<PaymentComingDueAlertForm, PaymentComingDueAlert>
        implements AlertDetails<PaymentComingDueAlert> {

    public PaymentComingDueAlertForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PaymentComingDueAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(PaymentComingDueAlert alert) {
        selectAccount(alert.getAccountUsed().getName());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(PaymentComingDueAlert alert) {

        // edge has a problem seeing the text right at the bottom of the screen
        if (System.getProperty("browse").equalsIgnoreCase("edge")) {
            ((JavascriptExecutor) driver).executeScript("scroll(0, 100)");
        }

        String errorMsg = "%s: %s for Payment Coming Due Alert was not found on the DOM.";
        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getAccountUsed().getName(), errorMsg, "User Account");
        } catch (TextNotDisplayedException e) {
            log.warn("Payment Coming Due Alert was not validated", e);
            return false;
        }
        return true;
    }
}
