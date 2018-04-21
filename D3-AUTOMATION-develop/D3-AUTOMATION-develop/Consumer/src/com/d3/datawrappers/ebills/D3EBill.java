package com.d3.datawrappers.ebills;


import com.d3.database.EBillDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.ebills.enums.AutoPay;
import com.d3.datawrappers.ebills.enums.FileReason;
import com.d3.datawrappers.ebills.enums.Status;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class D3EBill implements Serializable {

    private static final String AMOUNT_DUE_COLUMN = "amount_due";
    private static final String DUE_DATE_COLUMN = "due_date_text";

    private FileReason fileReason;
    private AutoPay.AmountType amountType;
    private AutoPay.PayOn payOn;
    private AutoPay.DaysBefore daysBefore;
    private D3Account d3Account;
    private double amount;
    private boolean alertWhenScheduled;
    private boolean alertWhenSent;
    private String note;
    private Status status;
    private SingleTransfer transfer;

    public String getNote() {
        return note;
    }

    public FileReason getFileReason() {
        return fileReason;
    }

    public Status getStatus() {
        return status;
    }

    public SingleTransfer getTransfer() {
        return transfer;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountStr() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(amount);
    }

    public AutoPay.AmountType getAmountType() {
        return amountType;
    }

    public void setAmountType(AutoPay.AmountType amountType) {
        this.amountType = amountType;
    }

    public AutoPay.PayOn getPayOn() {
        return payOn;
    }

    public void setPayOn(AutoPay.PayOn payOn) {
        this.payOn = payOn;
    }

    public AutoPay.DaysBefore getDaysBefore() {
        return daysBefore;
    }

    public D3Account getAutoPayAccount() {
        return d3Account;
    }

    public boolean getAlertWhenScheduled() {
        return alertWhenScheduled;
    }

    public boolean getAlertWhenSent() {
        return alertWhenSent;
    }


    private D3EBill(FileReason fileReason, String note) {
        this.fileReason = fileReason;
        this.note = note;
        this.status = Status.FILED;
    }

    private D3EBill(SingleTransfer transfer) {
        this.transfer = transfer;
        this.status = Status.PAID;
    }

    private D3EBill(D3Account d3Account, AutoPay.AmountType amountType, AutoPay.PayOn payOn, AutoPay.DaysBefore daysBefore, double amount, boolean alertWhenSent, boolean alertWhenScheduled) {
        this.d3Account = d3Account;
        this.amountType = amountType;
        this.payOn = payOn;
        this.daysBefore = daysBefore;
        this.amount = amount;
        this.alertWhenSent = alertWhenSent;
        this.alertWhenScheduled = alertWhenScheduled;
    }

    /**
     * Create random data to file an E-Bill
     */
    public static D3EBill createRandomFiling() {
        FileReason reason = FileReason.getRandom();
        String note = RandomHelper.getRandomString(10);
        return new D3EBill(reason, note);
    }


    /**
     * Creates random single transfer to Pay Ebill
     * Gets random ebill from the m2_ebill database table to set the transfer amount and scheduled date
     *
     * @return D3Ebill
     */
    public static D3EBill createRandomPayNowTransfer(D3User user, Recipient recipient) {
        int ebillId = EBillDatabaseHelper.getReceivedEBillId(user, recipient);
        SingleTransfer transfer = SingleTransfer.createRandomTransfer(recipient, user.getFirstAccountByAccountingClass(D3Account.AccountingClass.ASSET));
        transfer.setAmount(Double.valueOf(EBillDatabaseHelper.getEBillAttribute(ebillId, AMOUNT_DUE_COLUMN)));
        transfer.setScheduledDate(DateTime.parse(EBillDatabaseHelper.getEBillAttribute(ebillId, DUE_DATE_COLUMN)));
        return new D3EBill(transfer);
    }


    /**
     * Creates random Auto Pay information for an active E-Biller
     *
     * @return D3Ebill auto pay
     */
    public static D3EBill createRandomAutoPay(D3User user) {
        D3Account account = RandomHelper.getRandomElementFromList(user.getAssetAccounts());
        AutoPay.AmountType amountType = RandomHelper.getRandomElementFromArray(AutoPay.AmountType.values());
        AutoPay.PayOn payOn = RandomHelper.getRandomElementFromArray(AutoPay.PayOn.values());
        AutoPay.DaysBefore daysBefore = RandomHelper.getRandomElementFromArray(AutoPay.DaysBefore.values());
        double amount = RandomHelper.getRandomNumber(10, 50);
        boolean alertWhenSent = ThreadLocalRandom.current().nextBoolean();
        boolean alertWhenScheduled = ThreadLocalRandom.current().nextBoolean();
        return new D3EBill(account, amountType, payOn, daysBefore, amount, alertWhenSent, alertWhenScheduled);
    }
}
