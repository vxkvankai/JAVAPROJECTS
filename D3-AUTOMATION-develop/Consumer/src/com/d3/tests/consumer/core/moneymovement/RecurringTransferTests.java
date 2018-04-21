package com.d3.tests.consumer.core.moneymovement;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferFrequency;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.RecurringTransferForm;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.SingleTransferForm;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.joda.time.DateTime;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Money Movement")
@Feature("Recurring Transfer")
public class RecurringTransferTests extends MoneyMovementTestBase {

    @TmsLink("288049")
    @Story("Form Validation")
    @Story("Balance Threshold Recurring Transfer Validation")
    @Test(dataProvider = "Basic User")
    public void verifyRecurringTransferBalanceThresholdFail(D3User user) {
        DatabaseUtils.updateCompanyAttributeValueString("moneyMovement.internal.transfer.cutoff", "11:59 PM");
        List<D3Account> accountList = user.getAccounts();

        StandardRecurringTransfer transfer = StandardRecurringTransfer.createRandomTransferDetails(accountList.get(0), accountList.get(1));
        transfer.setScheduledDate(new DateTime());
        transfer.setFrequency(RecurringTransferFrequency.EVERY_MONTH);
        Double threshold = transfer.getFromAccount().getAvailableBalance() + 100;
        transfer.setBalanceThreshold(threshold);

        SingleTransferForm form = login(user).getHeader().clickMoneyMovementButton()
                .clickScheduleButton();
        form.fillOutRecurringForm(transfer)
                .clickSubmitButton()
                .clickContinueButton();

        Assert.assertTrue(form.isTextDisplayed(
                String.format("%s has an available balance of $%,.2f which is below the threshold amount of $%,.2f",
                        transfer.getFromAccount().getName(),
                        transfer.getFromAccount().getAvailableBalance(), threshold)));
        form.clickOkButton();
    }

    @Issue("DPD-1907")
    @TmsLink("288048")
    @TmsLink("522835")
    @Story("Edit Recurring Transfer")
    @Test(dataProvider = "Basic User with Recurring Transfers")
    public void verifyEditRecurringModelOfRecurringTransfer(D3User user, RecurringTransfer transfer) {

        // Create new transfer object
        RecurringTransfer newTransfer = transfer.createNewEditDetails(user);

        Schedule schedulePage = login(user).getHeader().clickMoneyMovementButton();
        RecurringTransferForm form = schedulePage.expandTransferDetails(transfer.getAmountStr())
                .clickEditButton()
                .clickAllButton()
                .fillOutRecurringModelEditForm(newTransfer)
                .clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(
                String.format(ScheduleL10N.Localization.RECURRING_MODAL_TEXT.getValue(), newTransfer.getAmountStr(),
                        newTransfer.getToAccount().getTransferableName(), newTransfer.getFromAccount().getName(),
                        newTransfer.getFrequency().getDropdownValue())));

        Schedule schedule = form.clickContinueButton().clickOkButton();

        Assert.assertTrue(schedule.isRecurringTransferCorrect(newTransfer), "Transfer was not validated, see logs");
    }

    @TmsLink("522820")
    @Story("Edit Single Transfer")
    @Test(dataProvider = "Basic User with Recurring Transfers")
    public void verifyUserCanDeletePendingSingleInstanceOfRecurringTransfer(D3User user, RecurringTransfer transfer) {
        Schedule schedulePage = login(user).getHeader()
                .clickMoneyMovementButton()
                .expandTransferDetails(transfer.getAmountStr())
                .clickDeleteButton()
                .clickOneDeleteButton();
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_STATUS_TEXT.getValue()),
                "Cancelled text is not present on the screen");
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_TRANSFER_TEXT.getValue()),
                "Cancelled text is not present on the screen");
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.PENDING_TRANSFER_TEXT.getValue()));
        Assert.assertThrows(NoSuchElementException.class, schedulePage::clickDeleteButton);
        Assert.assertThrows(NoSuchElementException.class, schedulePage::clickEditButton);

        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, transfer.getCancelledTransferAuditType()),
                String.format("Did not find an audit record type: %s for %s", transfer.getCancelledTransferAuditType(), user));
    }

    //@Flaky // date is wrong sometimes on the assert
    @TmsLink("522824")
    @Test(dataProvider = "User with not submitted recurring transfers")
    public void verifyRecurringTransferFutureDate(D3User user, RecurringTransfer transfer) {
        Schedule schedulePage = login(user).getHeader()
                .clickMoneyMovementButton()
                .clickScheduleButton()
                .fillOutRecurringForm(transfer)
                .clickSubmitButton()
                .clickContinueButton()
                .clickOkButton();

        Assert.assertTrue(schedulePage.isScheduledTransactionDisplayedOnTimeLine());
        Assert.assertTrue(schedulePage.isTextPresent(transfer.getDisplayedDate()));
        Assert.assertTrue(schedulePage.isTextPresent(transfer.getAmountStr()));
    }

    @TmsLink("522834")
    @Test(dataProvider = "Basic User with Recurring Transfers")
    public void verifyEditingOneInstanceOfARecurringTransfer(D3User user, RecurringTransfer transfer) {
        RecurringTransfer newTransferDetails = transfer.createNewEditDetails(user);

        // Edit one instance of the recurring transfer
        Schedule scheduleForm = login(user).getHeader()
                .clickMoneyMovementButton()
                .expandTransferDetails(transfer.getAmountStr())
                .clickEditButton()
                .clickOneInstanceButton()
                .editFormValues(newTransferDetails)
                .clickSubmitButton()
                .clickContinueButton()
                .clickOkButton();

        // Verify the date and amount after the changes
        Assert.assertTrue(scheduleForm.isTextPresent(newTransferDetails.getDisplayedDate()));
        Assert.assertTrue(scheduleForm.isTextPresent(newTransferDetails.getAmountStr()));
    }

    @TmsLink("522821")
    @Test(dataProvider = "Basic User with Recurring Transfers")
    public void verifyUserCanDeletePendingAllInstancesOfRecurringTransfer(D3User user, RecurringTransfer transfer) {
        Schedule schedulePage = login(user).getHeader()
                .clickMoneyMovementButton()
                .expandTransferDetails(transfer.getAmountStr())
                .clickDeleteButton()
                .clickAllDeleteButton();

        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_STATUS_TEXT.getValue()),
                "Cancelled text is not present on the screen");
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_TRANSFER_TEXT.getValue()),
                "Cancelled text is not present on the screen");
        Assert.assertFalse(schedulePage.isTextDisplayed(ScheduleL10N.Localization.PENDING_TRANSFER_TEXT.getValue()),
                "Pending text is present on the screen");
        Assert.assertFalse(schedulePage.isTextDisplayed(ScheduleL10N.Localization.PROCESSED_TRANSFER_TEXT.getValue()),
                "Processed text is present on the screen");

        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, transfer.getCancelledTransferAuditType()),
                String.format("Did not find an audit record type: %s for %s", transfer.getCancelledTransferAuditType(), user));
    }
}
