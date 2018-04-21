package com.d3.datawrappers.transfers;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.monitoring.audits.Audits;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public abstract class D3Transfer implements Serializable {

    private TransferableAccount toAccount;
    private Double amount;
    private D3Account fromAccount;
    private DateTime scheduledDate;
    private String note;
    private boolean recurring;
    private ProviderOption providerOption;
    private boolean noteFieldExists = true;


    D3Transfer(TransferableAccount toAccount, Double amount, D3Account fromAccount, DateTime scheduledDate, String note, boolean recurring) {
        this.toAccount = toAccount;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.scheduledDate = scheduledDate;
        this.note = note;
        this.recurring = recurring;
    }

    public boolean hasNote() {
        return noteFieldExists;
    }

    public void setNoteFieldExists(boolean exists) {
        noteFieldExists = exists;
    }

    public TransferableAccount getToAccount() {
        return toAccount;
    }

    public void setToAccount(Recipient toAccount) {
        this.toAccount = toAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public String getAmountStr() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(amount);
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public D3Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(D3Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public DateTime getScheduledDate() {
        return scheduledDate;
    }

    public String getScheduledDateString() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        return formatter.print(getScheduledDate());
    }

    public void setScheduledDate(DateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public ProviderOption getProviderOption() {
        return providerOption;
    }

    public void setProviderOption(ProviderOption providerOption) {
        this.providerOption = providerOption;
    }

    public Audits getCancelledTransferAuditType() {
        switch (getToAccount().getProviderOption()) {
            case ACH_TRANSFER:
                return Audits.ACH_TRANSFER_CANCELLED;
            case FEDWIRE_TRANSFER:
                return Audits.FEDWIRE_TRANSFER_CANCELLED;
            case INTERNAL_TRANSFER:
                return Audits.TRANSFER_CANCELLED;
            default:
                return Audits.PAYMENT_CANCEL_SUBMITTED;
        }
    }

    public String getDisplayedDate() {
        return getScheduledDate().getYear() == DateTime.now().getYear() ?
                String.format("%s %s", Month.of(getScheduledDate().getMonthOfYear()).getDisplayName(
                        TextStyle.SHORT, Locale.ENGLISH), String.valueOf(getScheduledDate().getDayOfMonth())) : getScheduledDateString();
    }

    public Audits getProcessedTransferAuditType() {
        switch (this.providerOption) {
            case CORE_ON_US_TRANSFER:
                return Audits.ON_US_TRANSFER_PROCESSED;
            case FEDWIRE_TRANSFER:
                return Audits.FEDWIRE_TRANSFER_PROCESSED;
            case ACH_TRANSFER:
                return Audits.ACH_TRANSFER_PROCESSED;
            case INTERNAL_TRANSFER:
                return Audits.TRANSFER_PROCESSED;
            default:
                throw new NotImplementedException();
        }
    }
}
