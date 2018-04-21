package com.d3.pages.consumer.dashboard;

import static com.d3.helpers.AccountHelper.verifyAccounts;
import static com.d3.helpers.AccountHelper.verifyRecipients;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class PayTransfer extends Dashboard {

    @FindBy(id = "destination")
    private Select payTransferTo;

    @FindBy(css = "#destination optgroup[label='Companies'] option")
    private List<Element> payTransferToCompanyRecipients;

    @FindBy(css = "#destination optgroup[label='People'] option")
    private List<Element> payTransferToPersonRecipients;

    @FindBy(css = "#destination optgroup[label='Accounts'] option")
    private List<Element> payTransferToAccounts;

    @FindBy(name = "amount")
    private Element payTransferAmount;

    @FindBy(id = "source")
    private Select payTransferFrom;

    @FindBy(css = "#source optgroup[label='Accounts'] option")
    private List<Element> payTransferFromInternalAccounts;

    @FindBy(name = "date")
    private Element payTransferDate;

    // different buttons but have the same selector :/
    @FindBy(css = "button.wizard-submit")
    private Element payTransferSubmitButton;

    @FindBy(id = "viewTransferDisclosure")
    private Element payTransferViewDisclosuresLink;

    @FindBy(id = "sendTransfer")
    private Element payTransferContinueButton;

    @FindBy(css = "button.wizard-submit")
    private Element payTransferOkButton;

    @FindBy(xpath = "//span[@aria-label='Cancel']")
    private Element cancelButton;

    @FindBy(id = "note")
    private Element noteInput;

    @FindBy(name = "expedited")
    private CheckBox expedite;
    
    public PayTransfer(WebDriver driver) {
        super(driver);
    }

    @Step("Enter {note} as note")
    public PayTransfer enterNote(String note) {
        noteInput.sendKeys(note);
        return this;
    }

    @Step("Click the cancel button")
    public Dashboard clickCancelButton() {
        cancelButton.click();
        waitForSpinner();
        return Dashboard.initialize(driver, Dashboard.class);
    }

    @Step("Select {recipientName} as the destination account")
    public PayTransfer selectPayTransferDestination(String recipientName) {
        payTransferTo.selectByTextContains(recipientName);
        waitForSpinner();
        return this;
    }

    @Step("Select {recipient} as the destination account")
    public PayTransfer selectPayTransferDestination(TransferableAccount recipient) {
        return selectPayTransferDestination(recipient.getTransferableName());
    }

    @Step("Enter {amount} as the amount")
    public PayTransfer enterPayTransferAmount(String amount) {
        payTransferAmount.sendKeys(amount);
        return this;
    }

    @Step("Select {name} as the source account")
    public PayTransfer selectPayTransferFromAccount(String name) {
        payTransferFrom.selectByTextContains(name);
        waitForSpinner();
        return this;
    }

    @Step("Select {account} as the destination account")
    public PayTransfer selectPayTransferFromAccount(D3Account account) {
        return selectPayTransferFromAccount(account.getName());
    }

    @Step("Enter {date} as the scheduled date")
    public PayTransfer enterPayTransferDate(String date) {
        payTransferDate.sendKeys(date);
        return this;
    }

    @Step("Enter {date} as the scheduled date")
    public PayTransfer enterPayTransferDate(DateTime date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        return enterPayTransferDate(formatter.print(date));
    }

    @Step("Click the submit button")
    public PayTransfer clickPayTransferSubmitButton() {
        payTransferSubmitButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the continue button")
    public PayTransfer clickPayTransferContinueButton() {
        payTransferContinueButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the OK button")
    public Dashboard clickPayTransferOkButton() {
        try {
            payTransferOkButton.click();
            waitForSpinner();
        } catch (TimeoutException e) {
            // stupid edge driver
            payTransferOkButton.click();
            waitForSpinner();
        }
        return Dashboard.initialize(driver, Dashboard.class);
    }

    @Step("Select the expedited option")
    public PayTransfer selectExpeditedOption() {
        expedite.check();
        return this;
    }

    @Step("Get the scheduled date")
    public DateTime getScheduledDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        return formatter.parseDateTime(payTransferDate.getValueAttribute());
    }

    /**
     * Enters the Pay/Transfer details given a D3Transfer object. The method assumes the Pay Transfer tab is open. This method does not submit the
     * payment
     *
     * @param transfer Transfer details to enter
     * @return PayTransfer object
     */
    @Step("Enter the transfer details into the form")
    public PayTransfer enterPayTransferDetails(D3Transfer transfer) {
        selectPayTransferDestination(transfer.getToAccount())
                .enterPayTransferAmount(transfer.getAmountStr())
                .selectPayTransferFromAccount(transfer.getFromAccount())
            .enterPayTransferDate(transfer.getScheduledDate());
        if (transfer.hasNote()) {
            return enterNote(transfer.getNote());
        }
        Assert.assertThrows(TimeoutException.class, () -> enterNote(""));
        return this;
    }

    @Step("Check if the available destination accounts are correct")
    public boolean arePayTransferDestinationAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, payTransferToAccounts, Element::getText, true);
    }

    @Step("Check if the company recipients are correct")
    public boolean arePayTransferCompanyRecipientsCorrect(List<String> recipients) {
        return verifyRecipients(recipients, payTransferToCompanyRecipients);
    }

    @Step("Check if the person recipients are correct")
    public boolean arePayTransferPersonRecipientsCorrect(List<String> recipients) {
        return verifyRecipients(recipients, payTransferToPersonRecipients);
    }

    @Step("Check if the source accounts are correct")
    public boolean arePayTransferSourceAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, payTransferFromInternalAccounts, Element::getText);
    }

}
