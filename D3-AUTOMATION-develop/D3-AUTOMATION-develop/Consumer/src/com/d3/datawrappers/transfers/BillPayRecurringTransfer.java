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

public class BillPayRecurringTransfer extends RecurringTransfer {

    private Double firstPayment;
    private String memo;

    public BillPayRecurringTransfer(TransferableAccount toAccount, Double amount, D3Account fromAccount,
            DateTime scheduledDate, String memo) {
        super(toAccount, amount, fromAccount, scheduledDate, "");
        this.memo = memo;
    }

    public Double getFirstPayment() {
        return firstPayment;
    }

    public String getFirstPaymentStr() {
        return new DecimalFormat("#.00").format(firstPayment);
    }

    public void setFirstPayment(Double firstPayment) {
        this.firstPayment = firstPayment;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public static BillPayRecurringTransfer createRandomTransferDetails(TransferableAccount toAccount, D3Account fromAccount) {
        Double amount = getRandomNumber(50, 500);
        DateTime scheduledDate = getRandomFutureDate();
        while (scheduledDate.dayOfWeek().get() == DateTimeConstants.SUNDAY || scheduledDate.dayOfWeek().get() == DateTimeConstants.SATURDAY) {
            scheduledDate = getRandomFutureDate();
        }
        String memo = getRandomString(10);

        // TODO randomize the end type or make new tests
        BillPayRecurringTransfer transfer = new BillPayRecurringTransfer(toAccount, amount, fromAccount, scheduledDate, memo);
        transfer.setProviderOption(toAccount.getProviderOption());
        transfer.setFrequency(RecurringTransferFrequency.getRandomBillPay());
        transfer.setEndType(RecurringTransferEndType.INDEFINITE);
        transfer.setFirstPayment(getRandomNumber(10, 50));
        return transfer;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public RecurringTransfer createNewEditDetails(D3User user) {
        D3Account fromAccount = null;
        for (D3Account account : user.getAccounts()) {
            if (!account.getName().equals(getFromAccount().getName()) && account.isAsset()) {
                fromAccount = account;
            }
        }

        if (fromAccount == null) {
            fromAccount = getFromAccount();
        }

        // get new transfer and set non-editable ones as same
        RecurringTransfer newTransfer = createRandomTransferDetails(getToAccount(), fromAccount);
        newTransfer.setScheduledDate(getScheduledDate());
        newTransfer.setEndType(getEndType());
        newTransfer.setFrequency(getFrequency());

        return newTransfer;
    }
}
