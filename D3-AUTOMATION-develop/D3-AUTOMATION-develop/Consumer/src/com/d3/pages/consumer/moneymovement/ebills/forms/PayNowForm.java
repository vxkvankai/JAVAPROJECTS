package com.d3.pages.consumer.moneymovement.ebills.forms;

import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.ScheduleTransactionForm;
import org.joda.time.DateTimeComparator;
import org.openqa.selenium.WebDriver;

public class PayNowForm extends ScheduleTransactionForm<PayNowForm> {

    public PayNowForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PayNowForm me() {
        return this;
    }

    @Override
    public PayNowForm fillOutForm(D3Transfer transfer) {
        //NOTE: Pay Now form should be pre-populated with amount due and due date
        selectFromAccount(transfer.getFromAccount());
        return transfer.hasNote() ? enterNote(transfer.getNote()) : me();
    }


    /**
     * This method verifies the Pay Now form amount field is pre-populated with Amount Due and Scheduled Date is pre-populated with Due Date
     *
     * @param transfer EBill Pay Now transfer details to verify
     * @return true if correct amount and scheduled date are populated, false otherwise
     */
    public boolean isFormPopulatedWithCorrectValues(D3Transfer transfer) {
        return (getAmount().equals(transfer.getAmountStr()) &&
            DateTimeComparator.getDateOnlyInstance().compare(getScheduledDate(), transfer.getScheduledDate()) == 0);
    }

}
