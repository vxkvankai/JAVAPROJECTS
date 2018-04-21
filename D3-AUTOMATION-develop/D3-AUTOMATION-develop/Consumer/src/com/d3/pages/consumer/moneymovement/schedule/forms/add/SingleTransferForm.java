package com.d3.pages.consumer.moneymovement.schedule.forms.add;

import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.support.wrappers.base.CheckBox;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class SingleTransferForm extends ScheduleTransactionForm<SingleTransferForm> {

    @FindBy(name = "expedited")
    private CheckBox expedite;

    public SingleTransferForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected SingleTransferForm me() {
        return this;
    }

    @Override
    @Step("Fill out the single transfer form")
    public SingleTransferForm fillOutForm(D3Transfer transfer) {
        selectToAccount(transfer.getToAccount())
                .enterAmount(transfer.getAmountStr())
                .selectFromAccount(transfer.getFromAccount())
                .enterScheduledDate(transfer.getScheduledDate());
        return transfer.hasNote() ? enterNote(transfer.getNote()) : me();
    }

    @Step("Fill out the recurring transfer form")
    public RecurringTransferForm fillOutRecurringForm(RecurringTransfer transfer) {
        return fillOutForm(transfer)
            .turnOnRecurringOption()
            .fillOutRecurringForm(transfer);
    }

    @Step("Select the expedited option")
    public SingleTransferForm selectExpeditedOption() {
        expedite.check();
        return this;
    }
}
