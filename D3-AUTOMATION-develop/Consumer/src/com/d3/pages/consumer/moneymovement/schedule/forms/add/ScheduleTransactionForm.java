package com.d3.pages.consumer.moneymovement.schedule.forms.add;

import static com.d3.helpers.AccountHelper.verifyAccounts;
import static com.d3.helpers.AccountHelper.verifyRecipients;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
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

    @Override
    protected abstract T me();

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

    @FindBy(css = "label.paymentOneTime")
    private Element singleTransferButton;

    @FindBy(name = "note")
    private Element noteInput;

    @FindBy(id = "memo")
    private Element memoInput;

    @FindBy(css = "button.wizard-submit")
    private Element submitButton;

    @FindBy(id = "sendTransfer")
    private Element continueButton;

    @FindBy(css = "button.wizard-submit")
    private Element okButton;

    @FindBy(xpath = "//span[@aria-label='Cancel']")
    private Element cancelTransactionButton;

    @FindBy(name = "expedited")
    private Element expeditedOvernightCheck;
   
    protected DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");

    public ScheduleTransactionForm(WebDriver driver) {
        super(driver);
    }

    public T selectToAccount(TransferableAccount account) {
        toAccountDropdown.selectByTextContains(account.getTransferableName());
        waitForSpinner();
        return me();
    }
    
    public T enterAmount(String amount) {
        amountInput.sendKeys(amount);
        return me();
    }

    public T selectFromAccount(D3Account account) {
        fromAccountDropdown.selectByTextContains(account.getName());
        return me();
    }

    public T enterScheduledDate(DateTime date) {
        scheduledDateInput.sendKeys(dateFormatter.print(date));
        return me();
    }

    public RecurringTransferForm turnOnRecurringOption() {
        recurringPaymentButton.click();
        return RecurringTransferForm.initialize(driver, RecurringTransferForm.class);
    }

    public SingleTransferForm turnOffRecurringOption() {
        singleTransferButton.click();
        return SingleTransferForm.initialize(driver, SingleTransferForm.class);
    }

    public T enterNote(String note) {
        noteInput.sendKeys(note);
        return me();
    }

    public T clickSubmitButton() {
        submitButton.click();
        waitForSpinner();
        return me();
    }

    public T enterMemo(String memo) {
        memoInput.sendKeys(memo);
        return me();
    }

    public T clickContinueButton() {
        continueButton.click();
        waitForSpinner();
        return me();
    }

    public boolean areSourceAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, sourceInternalAccounts, Element::getText);
    }

    public boolean areDestinationAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, destinationInternalAccounts, Element::getText);
    }

    public boolean areDestinationCompanyRecipientsCorrect(List<String> recipients) {
        return verifyRecipients(recipients, destinationCompanyRecipients);
    }

    public boolean areDestinationPersonRecipientsCorrect(List<String> recipients) {
        return verifyRecipients(recipients, destinationPersonRecipients);
    }

    public Schedule clickOkButton() {
        try {
            okButton.click();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            // for edge browser
            okButton.click();
        }
        waitForSpinner();
        return Schedule.initialize(driver, Schedule.class);
    }

    public abstract T fillOutForm(D3Transfer transfer);

    public ScheduleTransactionForm clickCancelButton() {
        cancelTransactionButton.click();
        return this;
    }

    public T selectARandomToAccount() {
        String randomAcct = destinationInternalAccounts.get(ThreadLocalRandom.current().nextInt(destinationInternalAccounts.size())).getText();
        toAccountDropdown.selectByTextContains(randomAcct);
        waitForSpinner();
        return me();
    }

    public boolean isAccountDisplayedOnDestinationDropDown(String accountName) {
        return verifyAccounts(accountName, destinationInternalAccounts, WebElement::getText);
    }

    public boolean isAccountDisplayedOnSourceDropDown(String accountName) {
        return verifyAccounts(accountName, sourceInternalAccounts, WebElement::getText);
    }

    public T editFormValues(D3Transfer transfer) {
        enterAmount(transfer.getAmountStr())
                .enterScheduledDate(transfer.getScheduledDate());
        return transfer.hasNote() ? enterNote(transfer.getNote()) : me();
    }
    
    public T checkexpEditedOvernightOption() {
        expeditedOvernightCheck.click();
        return me();
    }

    public String getAmount() {
        return amountInput.getValueAttribute().replaceAll("[$]", "");
    }

    public String getScheduledDate() {
        return scheduledDateInput.getValueAttribute();
    }
}
