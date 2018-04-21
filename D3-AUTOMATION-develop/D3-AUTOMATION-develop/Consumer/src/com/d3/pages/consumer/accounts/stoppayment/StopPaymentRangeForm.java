package com.d3.pages.consumer.accounts.stoppayment;

import com.d3.datawrappers.account.stoppayment.StopPaymentRange;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class StopPaymentRangeForm extends StopPaymentForm<StopPaymentRangeForm, StopPaymentRange> {

    @FindBy(id = "checkNumberFrom")
    private Element startCheck;

    @FindBy(id = "checkNumberTo")
    private Element endCheck;

    @FindBy(css = "div[class$='help-block error-message']")
    private List<Element> requiredFieldMessage;

    public StopPaymentRangeForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected StopPaymentRangeForm me() {
        return this;
    }

    public StopPaymentRangeForm enterStartCheck(String check) {
        startCheck.sendKeys(check);
        return this;
    }

    public StopPaymentRangeForm enterEndCheck(String check) {
        endCheck.sendKeys(check);
        return this;
    }

    @Override
    public StopPaymentForm fillOutForm(StopPaymentRange stopPaymentType) {
        fillOutFormCommon(stopPaymentType);
        enterStartCheck(stopPaymentType.getStartCheckStr());
        enterEndCheck(stopPaymentType.getEndCheckStr());
        return this;
    }

    /**
     * Checks that required fields 'Start Check' and 'End Check' display the correct error message
     * when trying to save the Stop Payment Range form without enter values for these fields
     */
    @Step("Check if the required field messages are displayed")
    public boolean areRequiredFieldMessagesDisplayed() {
        String numberOfReqFields = "There should be 2 required field messages, but found {}";

        log.info("Verifying 2 error messages are displayed");
        if (requiredFieldMessage.size() != 2) {
            log.warn(numberOfReqFields, requiredFieldMessage.size());
            return false;
        }

        String checkNumFieldMessage = "Enter a check #.";
        for (Element errorMessage : requiredFieldMessage) {
            try {
                checkIfTextEquals(errorMessage.getText(), checkNumFieldMessage);
            } catch (TextNotDisplayedException e) {
                log.warn("Error matching error messages on the form: ", e);
                return false;
            }
        }
        log.info("Error messages all appeared correctly");

        return true;
    }
}
