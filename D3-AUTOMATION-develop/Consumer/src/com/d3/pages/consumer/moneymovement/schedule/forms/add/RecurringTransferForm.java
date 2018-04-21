package com.d3.pages.consumer.moneymovement.schedule.forms.add;

import com.d3.datawrappers.transfers.BillPayRecurringTransfer;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferEndType;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferFrequency;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class RecurringTransferForm extends ScheduleTransactionForm<RecurringTransferForm> {

    @FindBy(name = "frequency")
    private Select frequencyDropdown;

    @FindBy(name = "endOption")
    private Select endTypeDropdown;

    @FindBy(name = "endDate")
    private Element endDateInput;

    @FindBy(name = "maxOccurrences")
    private Element numberOfTransactionsInput;

    @FindBy(name = "thresholdAmount")
    private Element balanceThresholdInput;

    @FindBy(id = "firstAmount")
    private Element firstPaymentInput;

    public RecurringTransferForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected RecurringTransferForm me() {
        return this;
    }

    public RecurringTransferForm selectFrequency(RecurringTransferFrequency frequency) {
        frequencyDropdown.selectByValue(frequency.toString());
        return this;
    }

    public RecurringTransferForm selectEndType(RecurringTransferEndType endType) {
        endTypeDropdown.selectByText(endType.getDropdownValue());
        return this;
    }

    public RecurringTransferForm enterEndDate(DateTime date) {
        endDateInput.sendKeys(dateFormatter.print(date));
        return this;
    }

    public RecurringTransferForm enterNumberOfTransactions(String transactions) {
        numberOfTransactionsInput.sendKeys(transactions);
        return this;
    }

    public RecurringTransferForm enterBalanceThreshold(String balanceThreshold) {
        balanceThresholdInput.sendKeys(balanceThreshold);
        return this;
    }

    public RecurringTransferForm enterFirstPayment(String firstPayment) {
        firstPaymentInput.sendKeys(firstPayment);
        return this;
    }

    @Override
    public RecurringTransferForm fillOutForm(D3Transfer transfer) {
        selectToAccount(transfer.getToAccount())
                .enterAmount(transfer.getAmountStr())
                .selectFromAccount(transfer.getFromAccount())
                .enterScheduledDate(transfer.getScheduledDate())
                .enterNote(transfer.getNote())
                .turnOnRecurringOption();
        return fillOutRecurringForm((RecurringTransfer) transfer);
    }

    public RecurringTransferForm fillOutRecurringForm(RecurringTransfer transfer) {
        RecurringTransferEndType endType = transfer.getEndType();
        selectFrequency(transfer.getFrequency()).selectEndType(endType);

        if (endType == RecurringTransferEndType.END_DATE) {
            enterEndDate(transfer.getEndDate());
        } else if (endType == RecurringTransferEndType.NUMBER_OF_TRANSACTIONS) {
            enterNumberOfTransactions(transfer.getNumberOfTransactions().toString());
        }
        if (transfer.getToAccount().isBillPay()) {
            enterFirstPayment(((BillPayRecurringTransfer) transfer).getFirstPaymentStr())
                    .enterMemo(((BillPayRecurringTransfer) transfer).getMemo());
        } else {
            enterBalanceThreshold(((StandardRecurringTransfer) transfer).getBalanceThresholdStr());
        }
        
        return this;
    }

    public RecurringTransferForm fillOutRecurringModelEditForm(RecurringTransfer transfer) {
        enterAmount(transfer.getAmountStr());

        if (transfer.getToAccount().isBillPay()) {
            selectFromAccount(transfer.getFromAccount())
                    .enterFirstPayment(((BillPayRecurringTransfer) transfer).getFirstPaymentStr())
                    .enterMemo(((BillPayRecurringTransfer) transfer).getMemo());
        } else {
            enterBalanceThreshold(((StandardRecurringTransfer) transfer).getBalanceThresholdStr());
            if (transfer.getProviderOption() == ProviderOption.ACH_TRANSFER) {
                // because ACH decided to use the term Memo instead
                enterMemo(transfer.getNote());
            } else {
                enterNote(transfer.getNote());
            }
        }
        waitForSpinner();
        return this;
    }
}
