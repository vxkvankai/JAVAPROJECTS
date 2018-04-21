package com.d3.pages.consumer.moneymovement.schedule.forms.add;

import static com.d3.helpers.AccountHelper.verifyAccounts;
import static com.d3.helpers.AccountHelper.verifyRecipients;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class ScheduleTransactionForm<T extends ScheduleTransactionForm> extends MoneyMovementBasePage {

    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");

    @FindBy(id = "destination")
    private Select toAccountDropdown;

    @FindBy(css = "#destination optgroup[label='Accounts'] option")
    private List<Element> destinationInternalAccounts;

    @FindBy(css = "#destination optgroup[label='Companies'] option")
    private List<Element> destinationCompanyRecipients;

    @FindBy(css = "#destination optgroup[label='People'] option")
    private List<Element> destinationPersonRecipients;

    @FindBy(name = "amount")
    private Element amountInput;

    @FindBy(id = "source")
    private Select fromAccountDropdown;

    @FindBy(css = "#source optgroup[label='Accounts'] option")
    private List<Element> sourceInternalAccounts;

    @FindBy(name = "date")
    private Element scheduledDateInput;

    // input tag has id but chrome can't click it
    @FindBy(css = "label.paymentRecurring")
    private Element recurringPaymentButton;

    @FindBy(name = "note")
    private Element noteInput;

    @FindBy(id = "memo")
    private Element memoInput;

    @FindBy(css = "button.wizard-submit")
    private Element submitButton;

    @FindBy(id = "sendTransfer")
    private Element continueButton;

    @FindBy(xpath = "//button[.='OK']")
    private Element okButton;

    @FindBy(xpath = "//span[@aria-label='Cancel']")
    private Element cancelTransactionButton;

    @FindBy(name = "principalOnly")
    private CheckBox principalOnly;

    public ScheduleTransactionForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected abstract T me();

    @Step("Select {account} for the destination")
    public T selectToAccount(TransferableAccount account) {
        toAccountDropdown.selectByTextContains(account.getTransferableName());
        waitForSpinner();
        return me();
    }

    @Step("Enter {amount} as the amount")
    public T enterAmount(String amount) {
        amountInput.sendKeys(amount);
        return me();
    }

    @Step("Select {account} as the source account")
    public T selectFromAccount(D3Account account) {
        fromAccountDropdown.selectByTextContains(account.getName());
        return me();
    }

    @Step("Enter {date} as the scheduled date")
    public T enterScheduledDate(DateTime date) {
        scheduledDateInput.sendKeys(dateFormatter.print(date));
        return me();
    }

    @Step("Turn on the recurring option")
    public RecurringTransferForm turnOnRecurringOption() {
        recurringPaymentButton.click();
        return RecurringTransferForm.initialize(driver, RecurringTransferForm.class);
    }

    @Step("Enter {note} as the note")
    public T enterNote(String note) {
        noteInput.sendKeys(note);
        return me();
    }

    @Step("Click the submit button")
    public T clickSubmitButton() {
        submitButton.click();
        return me();
    }

    @Step("Enter {memo} as the memo")
    public T enterMemo(String memo) {
        memoInput.sendKeys(memo);
        return me();
    }

    @Step("Click the continue button")
    public T clickContinueButton() {
        try {
            continueButton.click();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            continueButton.click();
        }
        return me();
    }

    @Step("Check if the source accounts are correct")
    public boolean areSourceAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, sourceInternalAccounts, Element::getText);
    }

    @Step("Check if the destination accounts are correct")
    public boolean areDestinationAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, destinationInternalAccounts, Element::getText);
    }

    @Step("Check if the destination company recipients are correct")
    public boolean areDestinationCompanyRecipientsCorrect(List<String> recipients) {
        return verifyRecipients(recipients, destinationCompanyRecipients);
    }

    @Step("Check if the person recipients are correct")
    public boolean areDestinationPersonRecipientsCorrect(List<String> recipients) {
        return verifyRecipients(recipients, destinationPersonRecipients);
    }

    @Step("Click the OK button (and return the dashboard page)")
    public Dashboard clickOkButtonDashboard() {
        clickOkButton();
        return Dashboard.initialize(driver, Dashboard.class);
    }

    @Step("Click the OK button")
    public Schedule clickOkButton() {
        try {
            okButton.doubleClick();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            // for edge browser
            okButton.click();
        }

        waitForScheduleModalToClose();
        return Schedule.initialize(driver, Schedule.class);
    }

    @Step("Fill out the form")
    public abstract T fillOutForm(D3Transfer transfer);

    @Step("Click the cancel button")
    public ScheduleTransactionForm clickCancelButton() {
        cancelTransactionButton.click();
        waitForScheduleModalToClose();
        return this;
    }

    @Step("Select a random destination account")
    public T selectARandomToAccount() {
        String randomAcct = destinationInternalAccounts.get(ThreadLocalRandom.current().nextInt(destinationInternalAccounts.size())).getText();
        toAccountDropdown.selectByTextContains(randomAcct);
        waitForSpinner();
        return me();
    }

    @Step("Check if {accountName} is displayed in the destination dropdown")
    public boolean isAccountDisplayedOnDestinationDropDown(String accountName) {
        return verifyAccounts(accountName, destinationInternalAccounts, WebElement::getText);
    }

    @Step("Check if {accountName} is displayed in the source dropdown")
    public boolean isAccountDisplayedOnSourceDropDown(String accountName) {
        return verifyAccounts(accountName, sourceInternalAccounts, WebElement::getText);
    }

    @Step("Edit the form values")
    public T editFormValues(D3Transfer transfer) {
        enterAmount(transfer.getAmountStr())
            .enterScheduledDate(transfer.getScheduledDate());
        return transfer.hasNote() ? enterNote(transfer.getNote()) : me();
    }

    @Step("Get the amount from the form")
    public String getAmount() {
        return amountInput.getValueAttribute().replaceAll("[$]", "");
    }

    @Step("Get the scheduled date from the form")
    public DateTime getScheduledDate() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        return formatter.parseDateTime(scheduledDateInput.getValueAttribute());
    }
    @Step("Check if {accountName} is displayed in the To dropdown for new manual Company Recipient ")
    public boolean verifyNewRecipientToDropDown(String accountName){
        return toAccountDropdown.optionContainsText(accountName);

    }

    @Step("Select the Apply To Principal checkbox")
    public T selectPrincipalOnlyCheckbox() {
        principalOnly.check();
        return me();
    }
}
