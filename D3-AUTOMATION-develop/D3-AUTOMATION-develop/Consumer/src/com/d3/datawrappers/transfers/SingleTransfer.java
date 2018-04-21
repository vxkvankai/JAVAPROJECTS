package com.d3.datawrappers.transfers;

import static com.d3.helpers.RandomHelper.getRandomFutureDate;
import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.transfer.onetime.ApiOneTimeTransfer;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.monitoring.audits.Audits;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.text.NumberFormat;
import java.text.ParseException;

@Slf4j
public class SingleTransfer extends D3Transfer {

    public SingleTransfer(TransferableAccount toAccount, Double amount, D3Account fromAccount, DateTime scheduledDate, String note) {
        super(toAccount, amount, fromAccount, scheduledDate, note, false);
    }

    /**
     * Create a single transfer with random data from one account to another with a random note. Amount will be between 1 and 500
     *
     * @param toAccount Account to send the money to
     * @param fromAccount Account to send the money from
     */
    public static SingleTransfer createRandomTransfer(TransferableAccount toAccount, D3Account fromAccount) {
        return createRandomTransfer(toAccount, fromAccount, true);
    }

    /**
     * Create a single transfer with random data from one account to another. Amount will be between 1 and 500
     *
     * @param toAccount Account to send the money to
     * @param fromAccount Account to send the money from
     * @param createRandomNote Set to true to create a note in the transfer with random text, false if unwanted
     */
    public static SingleTransfer createRandomTransfer(TransferableAccount toAccount, D3Account fromAccount, boolean createRandomNote) {
        NumberFormat bla = NumberFormat.getCurrencyInstance();
        Double amount;
        try {
            amount = bla.parse(bla.format(getRandomNumber(1, 500))).doubleValue();
        } catch (ParseException e) {
            amount = 0d;
            log.error("Error creating a currency number. Setting amount to 0");
        }
        DateTime scheduledDate;
        do {
            scheduledDate = getRandomFutureDate();
        } while (scheduledDate.dayOfWeek().get() == DateTimeConstants.SUNDAY || scheduledDate.dayOfWeek().get() == DateTimeConstants.SATURDAY);
        String note = "";
        if (createRandomNote) {
            note = getRandomString(10);
        }

        SingleTransfer transfer = new SingleTransfer(toAccount, amount, fromAccount, scheduledDate, note);
        transfer.setProviderOption(toAccount.getProviderOption());

        transfer.setNoteFieldExists(toAccount.hasNoteField());

        return transfer;
    }

    public void addToApi(D3User user, String apiUrl) throws D3ApiException {
        MoneyMovementApiHelper api = new MoneyMovementApiHelper(apiUrl);
        api.login(user);
        log.info("Adding transfer: {}", this);
        api.addOneTimeTransfer(new ApiOneTimeTransfer(this, user));
    }

    public String toString() {
        return String.format("Single Transfer: %s to: %s, from: %s, amount: %s",
            getToAccount().getProviderOption(),
            getToAccount().getTransferableName(),
            getFromAccount().getTransferableName(),
            getAmountStr());
    }

    public Audits getNewTransferAuditType() {
        switch (getToAccount().getProviderOption()) {
            case ACH_TRANSFER:
                return Audits.ACH_TRANSFER_SCHEDULED;
            case FEDWIRE_TRANSFER:
                return Audits.FEDWIRE_TRANSFER_SCHEDULED;
            case INTERNAL_TRANSFER:
                return Audits.TRANSFER_SCHEDULED;
            default:
                return getToAccount().isBillPay() ? Audits.PAYMENT_SUBMITTED : Audits.PAYMENT_SCHEDULED;
        }
    }
}
