package com.d3.pages.consumer.accounts.stoppayment;

import com.d3.datawrappers.account.stoppayment.StopPaymentSingle;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class StopPaymentSingleForm extends StopPaymentForm<StopPaymentSingleForm, StopPaymentSingle> {

    @FindBy(id = "checkNumber")
    private Element checkNumber;

    public StopPaymentSingleForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected StopPaymentSingleForm me() {
        return this;
    }

    public StopPaymentSingleForm enterCheckNumber(String check) {
        checkNumber.sendKeys(check);
        return this;
    }

    @Override
    public StopPaymentForm fillOutForm(StopPaymentSingle stopPayment) {
        fillOutFormCommon(stopPayment);
        enterAmount(stopPayment.getStopPaymentAmountStr());
        enterCheckNumber(stopPayment.getCheckNumberStr());
        return this;
    }


    /**
     * Checks that required fields 'Amount', 'Check #', and 'Payee' display the correct error message
     * when trying to save the Single Stop Payment form without enter values for these fields
     */
    @Step("Check if the required field messages are displayed")
    public boolean areRequiredFieldMessagesDisplayed() {
        String errorMsg = "The following message for the Single Stop Payment Required field: %s was not found on the DOM: %s";

        try {
            checkIfTextDisplayed("Enter an amount.", errorMsg, "Amount");
            checkIfTextDisplayed("Enter a check #.", errorMsg, "Check #");
            checkIfTextDisplayed("Enter a payee.", errorMsg, "Payee");
        } catch (TextNotDisplayedException e) {
            log.warn("Required field messages not displayed correctly: ", e);
            return false;
        }
        return true;
    }
}
