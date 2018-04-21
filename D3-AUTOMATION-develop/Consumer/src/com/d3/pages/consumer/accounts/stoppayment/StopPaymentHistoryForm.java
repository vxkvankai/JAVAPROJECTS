package com.d3.pages.consumer.accounts.stoppayment;

import com.d3.datawrappers.account.stoppayment.StopPaymentHistory;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class StopPaymentHistoryForm extends StopPaymentForm<StopPaymentHistoryForm, StopPaymentHistory> {

    public StopPaymentHistoryForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected StopPaymentHistoryForm me() {
        return this;
    }

    @Override
    public StopPaymentForm fillOutForm(StopPaymentHistory stopPayment) {
        throw new UnsupportedOperationException("Not implemented yet (Stop Payment History Form is read only.)");
    }

    /**
     * Verifies the labels in stop history payment form are correct (values are hardcoded due to using a simulator)
     */
    @Step("Check if the correct information is displayed for the stop payment history form")
    public boolean isCorrectInformationDisplayed() {
        String errorMsg = "Stop Payment History field: %s was not found on the DOM.";

        try {
            checkIfTextDisplayed("Payee:", errorMsg);
            checkIfTextDisplayed("Expiration Date:", errorMsg);
            checkIfTextDisplayed("Status:", errorMsg);
            checkIfTextDisplayed("Low Check Number:", errorMsg);
            checkIfTextDisplayed("High Check Number:", errorMsg);
            checkIfTextDisplayed("Amount High:", errorMsg);
            checkIfTextDisplayed("Amount Low:", errorMsg);
            checkIfTextDisplayed("Check:", errorMsg);
            checkIfTextDisplayed("Amount:", errorMsg);
        } catch (TextNotDisplayedException e) {
            logger.warn("Stop Payment history field check failed:", e);
            return false;
        }

        return true;
    }
}
