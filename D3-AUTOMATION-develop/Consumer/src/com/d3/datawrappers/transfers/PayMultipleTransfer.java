package com.d3.datawrappers.transfers;

import static com.d3.helpers.RandomHelper.getRandomFutureDate;
import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;

public class PayMultipleTransfer extends D3Transfer {

    private PayMultipleTransfer(TransferableAccount toAccount, Double amount, D3Account fromAccount, DateTime scheduledDate, String memo) {
        super(toAccount, amount, fromAccount, scheduledDate, memo, false);
    }

    /**
     * Create a single pay multiple transfer with random data from one account to another with a random note. Amount will be between 1 and 500
     *
     * @param toAccount Account to send the money to
     * @param fromAccount Account to send the money from
     */
    public static PayMultipleTransfer createRandomTransfer(TransferableAccount toAccount, D3Account fromAccount) {
        return createRandomTransfer(toAccount, fromAccount, true);
    }


    /**
     * Create a single transfer with random data from one account to another. Amount will be between 1 and 500
     *
     * @param toAccount Account to send the money to
     * @param fromAccount Account to send the money from
     * @param createRandomMemo Set to true to create a memo in the transfer with random text, false if unwanted
     */
    private static PayMultipleTransfer createRandomTransfer(TransferableAccount toAccount, D3Account fromAccount, boolean createRandomMemo) {
        NumberFormat bla = NumberFormat.getCurrencyInstance();
        Double amount;
        try {
            amount = bla.parse(bla.format(getRandomNumber(1, 500))).doubleValue();
        } catch (ParseException e) {
            amount = 0d;
            LoggerFactory.getLogger(PayMultipleTransfer.class).error("Error creating a currency number. Setting amount to 0");
        }
        DateTime scheduledDate = getRandomFutureDate();
        while (scheduledDate.dayOfWeek().get() == DateTimeConstants.SUNDAY || scheduledDate.dayOfWeek().get() == DateTimeConstants.SATURDAY) {
            scheduledDate = getRandomFutureDate();
        }
        String memo = "";
        if (createRandomMemo) {
            memo = getRandomString(10);
        }

        PayMultipleTransfer transfer = new PayMultipleTransfer(toAccount, amount, fromAccount, scheduledDate, memo);
        transfer.setProviderOption(toAccount.getProviderOption());

        return transfer;
    }

    public String toString() {
        return String.format("Pay Multiple Transfer to: %s, from: %s, amount: %s", getToAccount().getTransferableName(),
                getFromAccount().getTransferableName(), getAmountStr());
    }
}

