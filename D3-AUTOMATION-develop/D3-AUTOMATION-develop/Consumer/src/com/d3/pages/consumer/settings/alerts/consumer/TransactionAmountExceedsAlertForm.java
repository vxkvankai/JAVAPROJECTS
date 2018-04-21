package com.d3.pages.consumer.settings.alerts.consumer;


import com.d3.datawrappers.alerts.TransactionAmountExceedsAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class TransactionAmountExceedsAlertForm extends AlertForm<TransactionAmountExceedsAlertForm, TransactionAmountExceedsAlert>
        implements AlertDetails<TransactionAmountExceedsAlert> {

    @FindBy(id = "thresholdProperty")
    private Element amountThreshold;

    @FindBy(id = "tranType")
    private Select transactionType;


    public TransactionAmountExceedsAlertForm(WebDriver driver) {
        super(driver);
    }

    public TransactionAmountExceedsAlertForm enterAmount(String amount) {
        amountThreshold.sendKeys(amount);
        return this;
    }

    public TransactionAmountExceedsAlertForm selectTransactionType(String type) {
        transactionType.selectByValue(type.toUpperCase());
        return this;
    }

    @Override
    protected TransactionAmountExceedsAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(TransactionAmountExceedsAlert alert) {
        selectAccount(alert.getAccountUsed().getName());
        enterAmount(alert.getAmountStr());
        selectTransactionType(alert.getTransactionType());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(TransactionAmountExceedsAlert alert) {
        String errorMsg = "%s: %s for Transaction Amount Exceeds Alert was not found on the DOM.";

        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getAmountStr(), errorMsg, "Amount for Transaction to exceed");
            checkIfTextDisplayed(alert.getAmountStr(), errorMsg, "Transaction Type");
        } catch (TextNotDisplayedException e) {
            log.warn("Transaction Amount Exceeds Alert was not validated", e);
            return false;
        }

        return true;
    }

}
