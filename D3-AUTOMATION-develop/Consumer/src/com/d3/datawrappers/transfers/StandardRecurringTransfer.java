package com.d3.datawrappers.transfers;

import static com.d3.helpers.RandomHelper.getRandomFutureDate;
import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferEndType;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferFrequency;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.text.DecimalFormat;

public class StandardRecurringTransfer extends RecurringTransfer {

    private Double balanceThreshold;

    public StandardRecurringTransfer(TransferableAccount toAccount, Double amount, D3Account fromAccount,
            DateTime scheduledDate, String note) {
        super(toAccount, amount, fromAccount, scheduledDate, note);
    }

    public Double getBalanceThreshold() {
        return balanceThreshold;
    }

    public String getBalanceThresholdStr() {
        return new DecimalFormat("#0.00").format(balanceThreshold);
    }

    public void setBalanceThreshold(Double balanceThreshold) {
        this.balanceThreshold = balanceThreshold;
    }

    public static StandardRecurringTransfer createRandomTransferDetails(TransferableAccount toAccount, D3Account fromAccount) {
        Double amount = getRandomNumber(50, 500);
        DateTime scheduledDate = getRandomFutureDate();
        while (scheduledDate.dayOfWeek().get() == DateTimeConstants.SUNDAY || scheduledDate.dayOfWeek().get() == DateTimeConstants.SATURDAY) {
            scheduledDate = getRandomFutureDate();
        }
        String note = getRandomString(10);

        // TODO randomize the end type
        StandardRecurringTransfer transfer = new StandardRecurringTransfer(toAccount, amount, fromAccount, scheduledDate, note);
        transfer.setProviderOption(toAccount.getProviderOption());
        do {
            transfer.setFrequency(RecurringTransferFrequency.getRandom());
        } while (toAccount.getProviderOption() == ProviderOption.FEDWIRE_TRANSFER
                && transfer.getFrequency() == RecurringTransferFrequency.EVERY_MONTH_ON_LAST_DAY);
        transfer.setEndType(RecurringTransferEndType.INDEFINITE);
        transfer.setBalanceThreshold(getRandomNumber(0, 50));
        transfer.setNoteFieldExists(toAccount.hasNoteField());
        return transfer;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public RecurringTransfer createNewEditDetails(D3User user) {
        // get new transfer and set non-editable ones as same
        RecurringTransfer newTransfer = createRandomTransferDetails(getToAccount(), getFromAccount());
        newTransfer.setScheduledDate(getScheduledDate());
        newTransfer.setEndType(getEndType());
        newTransfer.setFrequency(getFrequency());

        return newTransfer;
    }
}
