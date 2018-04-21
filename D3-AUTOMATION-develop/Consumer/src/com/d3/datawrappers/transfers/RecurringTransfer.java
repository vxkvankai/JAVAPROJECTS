package com.d3.datawrappers.transfers;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.transfer.recurring.ApiRecurringTransfer;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferEndType;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferFrequency;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public abstract class RecurringTransfer extends D3Transfer implements Serializable {

    private RecurringTransferFrequency frequency;
    private RecurringTransferEndType endType;
    private Integer numberOfTransactions;
    private DateTime endDate;

    private Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    public RecurringTransfer(TransferableAccount toAccount, Double amount, D3Account fromAccount, DateTime scheduledDate, String note) {
        super(toAccount, amount, fromAccount, scheduledDate, note, true);
    }

    public RecurringTransferFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(RecurringTransferFrequency frequency) {
        this.frequency = frequency;
    }

    public RecurringTransferEndType getEndType() {
        return endType;
    }

    public void setEndType(RecurringTransferEndType endType) {
        this.endType = endType;
    }

    public Integer getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(Integer numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public DateTime getScheduledDate() {
        // scheduled date is the last day of the month if the frequency is set that way
        return getFrequency() == RecurringTransferFrequency.EVERY_MONTH_ON_LAST_DAY
                ? super.getScheduledDate().dayOfMonth().withMaximumValue()
                : super.getScheduledDate();
    }

    /**
     * Add the recurring transfer to the app via the api
     *
     * @param user User to associate the transfer with
     * @param api api object to use to add the transfer to (needs to be logged in/authenticated)
     */
    public RecurringTransfer addToApi(D3User user, MoneyMovementApiHelper api) throws D3ApiException {
        getLogger().info("Adding transfer: {}", this);
        api.addRecurringTransfer(new ApiRecurringTransfer(this, user));
        return this;
    }

    /**
     * Creates a new transfer that contains the same to and from accounts
     */
    public abstract RecurringTransfer createNewEditDetails(D3User user);

    public String toString() {
        return String.format("Recurring Transfer, Type: %s to recipient type: %s From %s for %s on %s. Frequency: %s, Endtype: %s", getClass(),
                getToAccount().getClass(),
                getFromAccount().getName(),
                getAmountStr(), getScheduledDateString(), getFrequency(), getEndType());
    }


}
