package com.d3.pages.consumer.moneymovement;

import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.transfers.BillPayRecurringTransfer;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.RecurringTransferForm;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.SingleTransferForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import javax.annotation.CheckForNull;

@Slf4j
public class Schedule extends MoneyMovementBasePage {

    @FindBy(css = "button.add-transfer-button")
    private Element scheduleButton;

    @FindBy(name = "searchTerm")
    private Element searchInput;

    @FindBy(className = "search-button")
    private Element searchButton;

    @FindBy(css = "ul.transfer-list li[class^='entity']")
    private List<Element> summaryElement;

    @FindBy(css = "button.edit-button")
    private Element editButton;

    @FindBy(css = "button.submit-one")
    private Element allButton;

    @FindBy(css = "button.print-btn")
    private Element downloadButton;

    @FindBy(css = "button.submit-two")
    private Element oneButton;

    @FindBy(css = "button.delete-button")
    private Element deleteButton;

    @FindBy(xpath = "//button[text()='Yes']")
    private Element yesButton;

    @FindBy(css = "li.active div[class='subtext']")
    private Element transactionStatus;

    @FindBy(css = "button.submit-two")
    private Element oneInstanceButton;

    public Schedule(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Schedule me() {
        return this;
    }

    public SingleTransferForm clickScheduleButton() {
        scheduleButton.click();
        return SingleTransferForm.initialize(driver, SingleTransferForm.class);
    }

    public Schedule enterSearchTerm(String searchTerm) {
        searchInput.sendKeys(searchTerm);
        return this;
    }

    public Schedule clickSearchButton() {
        searchButton.click();
        return this;
    }

    private Schedule expandFirstShownSummary() {
        getDisplayedElement(summaryElement).click();
        return this;
    }

    public Schedule expandShownSummaryThatHasText(String text) {
        getElementInListByTextContains(summaryElement, text).click();
        return this;
    }

    public Schedule expandTransferDetails(String uniqueSearchTerm) {
        return enterSearchTerm(uniqueSearchTerm).clickSearchButton().expandFirstShownSummary();
    }

    public Schedule expandNextScheduledTransfer() {
        return enterSearchTerm("Pending").clickSearchButton().expandFirstShownSummary();
    }

    public Schedule clickEditButton() {
        editButton.click();
        return this;
    }

    public SingleTransferForm clickEditTransferButton() {
        editButton.click();
        return SingleTransferForm.initialize(driver, SingleTransferForm.class);
    }

    public RecurringTransferForm clickAllButton() {
        allButton.click();
        return RecurringTransferForm.initialize(driver, RecurringTransferForm.class);
    }

    public Schedule clickOneDeleteButton() {
        oneButton.click();
        return this;
    }

    public Schedule clickAllDeleteButton() {
        allButton.click();
        return this;
    }

    private boolean isTransferCorrect(D3Transfer transfer) {
        String formattedAmount = transfer.getAmountStr();

        if (!isTextDisplayed(transfer.getToAccount().getTransferableName())) {
            log.error("Name: {} was not validated", transfer.getToAccount().getTransferableName());
            return false;
        }

        if (!isTextDisplayed(formattedAmount)) {
            log.error("Amount: {} was not Validated", transfer.getAmountStr());
            return false;
        }

        return true;
    }

    public boolean isSingleTransferCorrect(SingleTransfer transfer) {
        return isTransferCorrect(transfer);
    }

    public boolean isRecurringTransferCorrect(RecurringTransfer transfer) {
        String textToCheck = getRecurringTransferModalText(transfer);

        if (!isTransferCorrect(transfer)) {
            return false;
        }

        if (!isTextDisplayed(textToCheck)) {
            log.error("Transfer detail text: {} was not validated", textToCheck);
            return false;
        }

        if (!transfer.getToAccount().isBillPay() && !isTextDisplayed(String.format(ScheduleL10N.Localization.THRESHOLD_DETAIL.getValue(),
                ((StandardRecurringTransfer) transfer).getBalanceThresholdStr()))) {
            log.error("Balance threshold was not validated");
            return false;
        }

        // TODO JMoravec, probably need to rethink this, I don't like using an `instanceof` here
        if (transfer instanceof BillPayRecurringTransfer) {
            if (transfer.getToAccount().isBillPay() && !isTextDisplayed(((BillPayRecurringTransfer) transfer).getMemo())) {
                log.error("Memo: {} was not validated", ((BillPayRecurringTransfer) transfer).getMemo());
                return false;
            } else {
                if (!isTextDisplayed(transfer.getNote())) {
                    log.error("Note was not validated");
                    return false;
                }
            }
        }
        // TODO test against all 3 types of end type will probably need to add some validation logic here
        return true;
    }

    @CheckForNull
    private String getRecurringTransferModalText(RecurringTransfer transfer) {
        switch (transfer.getEndType()) {
            case END_DATE:
                return String.format(ScheduleL10N.Localization.RECURRING_END_DATE.getValue(), transfer.getAmountStr(),
                    transfer.getFrequency().getDropdownValue(), transfer.getScheduledDateString(), transfer.getEndDate());
            case INDEFINITE:
                return String.format(ScheduleL10N.Localization.RECURRING_INDEFINITE.getValue(), transfer.getAmountStr(),
                    transfer.getFrequency().getDropdownValue(), transfer.getScheduledDateString());
            case NUMBER_OF_TRANSACTIONS:
                return String.format(ScheduleL10N.Localization.RECURRING_NUMBER_OF_TXNS.getValue(), transfer.getAmountStr(),
                    transfer.getFrequency().getDropdownValue(), transfer.getScheduledDateString(), transfer.getNumberOfTransactions());
            default:
                return null;
        }
    }

    public boolean isFailedTransactionDisplayedOnTimeline() {
        return (transactionStatus.getText().equals(D3Transaction.TransactionStatus.FAILED.toString()));
    }

    /**
     * This method validates that the proper options are displayed for the transactions
     *
     * @return true if download button and "Processed" status are displayed for transaction
     */
    public boolean isProcessedTransactionDisplayedOnTimeline() {
        return (downloadButton.isDisplayed() && transactionStatus.getText().equals(D3Transaction.TransactionStatus.PROCESSED.toString()));
    }

    public Schedule clickDeleteButton() {
        deleteButton.click();
        return this;
    }

    public Schedule clickConfirmDeleteButton() {
        yesButton.click();
        return this;
    }

    public boolean isScheduledTransactionDisplayedOnTimeLine() {
        return (deleteButton.isDisplayed() && editButton.isDisplayed() && transactionStatus.getText()
            .equals(D3Transaction.TransactionStatus.PENDING.toString()));
    }

    public RecurringTransferForm clickOneInstanceButton() {
        oneInstanceButton.click();
        return RecurringTransferForm.initialize(driver, RecurringTransferForm.class);
    }
}
