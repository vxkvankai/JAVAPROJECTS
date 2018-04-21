package com.d3.tests.consumer.core.moneymovement;

import static com.d3.helpers.AccountHelper.getAuditFutureTransactionStatus;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.PayTransfer;
import com.d3.pages.consumer.headers.MessagesTabs;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.ScheduleTransactionForm;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.SingleTransferForm;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Money Movement")
@Feature("Single Transfer")
public class SingleTransferTests extends MoneyMovementTestBase {

    @Story("Internal Account Single Transfer")
    @Test(dataProvider = "Basic User")
    public void verifySingleTransferInternalAccounts(D3User user) {
        List<D3Account> accounts = user.getAccounts();
        SingleTransfer transfer = SingleTransfer.createRandomTransfer(accounts.get(0), accounts.get(1));
        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();
        form.fillOutForm(transfer)
            .clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), transfer.getAmountStr(),
            transfer.getToAccount().getName(), transfer.getFromAccount().getName(), transfer.getScheduledDateString())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton();
        Assert.assertTrue(schedulePage.isSingleTransferCorrect(transfer), "Transfer was not validated, see logs");
    }

    @TmsLink("522818")
    @TmsLink("522827")
    @Story("Single Transfer Form Validation")
    @Test(dataProvider = "User with not submitted transfers")
    public void verifySingleTransferMinAmount(D3User user, SingleTransfer transfer) {
        transfer.setAmount(.99);
        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton()
            .fillOutForm(transfer)
            .clickSubmitButton();
        Assert.assertTrue(form.isTextDisplayed(String.format(ScheduleL10N.Localization.RANGE_VALIDATION_ERROR.getValue(), "1.00", "999,999,999.99")),
            "Min range error not shown");
        transfer.setAmount(10000000000.0);
        form.fillOutForm(transfer)
            .clickSubmitButton();
        Assert.assertTrue(form.isTextDisplayed(String.format(ScheduleL10N.Localization.RANGE_VALIDATION_ERROR.getValue(), "1.00", "999,999,999.99")),
            "Max range error not shown");
    }

    @TmsLink("522819")
    @Story("Edit Single Transfer")
    @Test(dataProvider = "User with pending single transfers")
    public void verifyUserCanDeletePendingSingleTransfer(D3User user, SingleTransfer transfer) {
        Schedule schedulePage = login(user).getHeader()
            .clickMoneyMovementButton()
            .expandTransferDetails(transfer.getAmountStr())
            .clickDeleteButton();
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CONFIRM_DELETE_MSG.getValue()),
            "Confirm delete message is not shown");
        schedulePage.clickConfirmDeleteButton();
        Assert.assertThrows(NoSuchElementException.class, schedulePage::clickDeleteButton);
        Assert.assertThrows(NoSuchElementException.class, schedulePage::clickEditButton);
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_STATUS_TEXT.getValue()),
            "Cancelled text is not present on the screen");
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_TRANSFER_TEXT.getValue()),
            "Cancelled text is not present on the screen");

        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, transfer.getCancelledTransferAuditType()),
            String.format("Did not find an audit record type: %s for %s", transfer.getCancelledTransferAuditType(), user));

    }

    @TmsLink("522809")
    @Story("Single Transfer for each recipient")
    @Test(dataProvider = "User with not submitted transfers")
    public void verifySuccessfulOneTimeTransferWithAuditRecord(D3User user, SingleTransfer transfer) {
        SingleTransferForm transferForm = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton()
            .fillOutForm(transfer)
            .clickSubmitButton()
            .clickContinueButton();

        Assert.assertTrue(transferForm.isTextDisplayed(ScheduleL10N.Localization.SUCCESS_TEXT_SCHEDULED_TRANSACTION.getValue()));

        Schedule scheduleForm = transferForm.clickOkButton();
        Assert.assertTrue(scheduleForm.isScheduledTransactionDisplayedOnTimeLine());

        MessagesTabs messages = scheduleForm.getHeader()
            .clickMessagesButton()
            .getTabs()
            .clickFirstAlert();

        Assert.assertTrue(messages.isTextDisplayed(messages.getScheduleMessage(transfer)), "Transaction message was not seen");

        String auditTransactionStatus = getAuditFutureTransactionStatus(user, transfer.getToAccount().getProviderOption());
        Assert.assertFalse(auditTransactionStatus.isEmpty());
        Assert.assertEquals(auditTransactionStatus, D3Transaction.TransactionStatus.PENDING.toString(),
            "Scheduled transaction status was not correct");
    }

    @TmsLink("522830")
    @Test(dataProvider = "User with pending single transfers")
    public void verifyEditSingleTransfer(D3User user, SingleTransfer transfer) {
        SingleTransfer newTransferDetails = SingleTransfer.createRandomTransfer(transfer.getToAccount(), transfer.getFromAccount());

        // Edit the amount, note and date of the transfer
        Schedule scheduleForm = login(user).getHeader()
            .clickMoneyMovementButton()
            .expandTransferDetails(transfer.getAmountStr())
            .clickEditTransferButton()
            .editFormValues(newTransferDetails)
            .clickSubmitButton()
            .clickContinueButton()
            .clickOkButton();

        // Verify the date, amount and note after the changes
        Assert.assertTrue(scheduleForm.isTextDisplayed(newTransferDetails.getDisplayedDate()));
        Assert.assertTrue(scheduleForm.isTextDisplayed(newTransferDetails.getAmountStr()));
        if (transfer.hasNote()) {
            Assert.assertTrue(scheduleForm.isTextDisplayed(newTransferDetails.getNote()));
        }
    }

    @TmsLink("522811")
    @Story("Validate the error messages on the UI when the fields are blank")
    @Test(dataProvider = "User with not submitted transfers")
    public void verifyErrorMessageWhenFieldAreBlankTransferAllRecipients(D3User user, SingleTransfer transfer) {
        ScheduleTransactionForm transferForm = login(user)
            .getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton()
            .selectToAccount(transfer.getToAccount())
            .clickSubmitButton();
        Assert.assertTrue(transferForm.isTextDisplayed(ScheduleL10N.Localization.ENTER_AMOUNT_ERROR.getValue()),
            "Validation message was not displayed");
    }

    @TmsLink("305915")
    @Story("Validate failed status internal transfer")
    @Test(dataProvider = "Basic User")
    public void validateFailedStatusForInternalTransfers(D3User user) {
        SingleTransfer transfer = SingleTransfer.createRandomTransfer(user.getAccounts().get(0), user.getAccounts().get(1));
        transfer.setScheduledDate(new DateTime());
        // NOTE: entering 500 will cause the simulator to fail the transaction. This test will be different with a live connection
        transfer.setAmount(500.00);

        Dashboard singleTransfer = login(user).getHeader().clickDashboardButton()
            .clickPayTransferTab()
            .enterPayTransferDetails(transfer)
            .clickPayTransferSubmitButton()
            .clickPayTransferContinueButton()
            .clickPayTransferOkButton();

        Schedule moneyMovementPage = singleTransfer.getHeader().clickMoneyMovementButton().expandTransferDetails(transfer.getAmountStr());
        Assert.assertTrue(moneyMovementPage.isFailedTransactionDisplayedOnTimeline());

        String auditTransactionStatus = DatabaseUtils.getAuditRecordAttributes(user, Audits.TRANSFER_PROCESSED)
            .getOrDefault(AuditAttribute.STATUS_CODE.getAttributeName(), "");
        Assert.assertNotNull(auditTransactionStatus);
        Assert.assertEquals(auditTransactionStatus, D3Transaction.TransactionStatus.FAILED.toString());
    }

    @TmsLink("522817")
    @TmsLink("522808")
    @Story("Transfer scheduled for current date for each recipient with Audit record")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_EXTERNAL_TRANSFER_CUTOFF, value = "11:59 PM", enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_FEDWIRE_TRANSFER_CUTOFF, value = "11:59 PM", enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF, value = "11:59 PM", enabled = false)
    @Test(dataProvider = "User with not submitted transfers current date")
    public void verifySuccessfulCurrentDateTransferWithAuditRecord(D3User user, SingleTransfer transfer) {
        Schedule scheduleForm = login(user).getHeader().clickMoneyMovementButton();
        SingleTransferForm transferForm = scheduleForm.clickScheduleButton()
            .fillOutForm(transfer)
            .clickSubmitButton()
            .clickContinueButton();

        Assert.assertTrue(transferForm.isTextDisplayed(
            transfer.getProviderOption() == ProviderOption.INTERNAL_TRANSFER ? (ScheduleL10N.Localization.SUCCESS_TITLE_SCHEDULED_TRANSACTION
                .getValue()) : ScheduleL10N.Localization.SUCCESS_TEXT_SCHEDULED_TRANSACTION.getValue()));

        transferForm.clickOkButton();
        Assert.assertTrue(scheduleForm.isProcessedTransactionDisplayedOnTimeline());

        String auditTransactionStatus = DatabaseUtils.getAuditRecordAttributes(user, transfer.getProcessedTransferAuditType())
            .getOrDefault(AuditAttribute.STATUS_CODE.getAttributeName(), "");
        Assert.assertFalse(auditTransactionStatus.isEmpty());
        Assert.assertEquals(auditTransactionStatus, D3Transaction.TransactionStatus.PROCESSED.toString(),
            "Scheduled transaction status was not correct");
    }

    @TmsLink("348608")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_EXTERNAL_TRANSFER_CUTOFF, value = "11:59 PM", enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF, value = "11:59 PM", enabled = false)
    @Story("Expedited and Overnight Payments")
    @Test(dataProvider = "User with not submitted expedited single transfers")
    public void verifySuccessfullySchedulingExpeditedPayments(D3User user, SingleTransfer transfer) {
        //The fees for simulator are constant $25 for Expedited and $50 for overnight payments
        String fee = transfer.getProviderOption() == ProviderOption.EXPEDITED_PAYMENT ? "+$25.00" : "+$50.00";
        SingleTransferForm transferForm = login(user).getHeader().clickMoneyMovementButton()
            .clickScheduleButton()
            .fillOutForm(transfer)
            .selectExpeditedOption();
        Assert.assertTrue(transferForm.isTextPresent(String.format("%s %s", ScheduleL10N.Localization.EXPEDITED_TITLE.getValue(), fee)), "Get it there Faster option is not being displayed");

        transferForm.clickSubmitButton()
            .clickContinueButton();
        Assert.assertTrue(transferForm.isTextDisplayed(ScheduleL10N.Localization.SUCCESS_TEXT_SCHEDULED_TRANSACTION.getValue()));

        transferForm.clickOkButton();
        String paymentMethod = DatabaseUtils.getAuditRecordAttributes(user, Audits.PAYMENT_SUBMITTED)
            .getOrDefault(AuditAttribute.PAYMENT_DELIVERY_METHOD.getAttributeName(), "");

        //Verify PAYMENT_DELIVERY_METHOD is ExpeditedPayment or OvernightCheck
        Assert.assertTrue(StringUtils.deleteWhitespace(paymentMethod).startsWith(transfer.getProviderOption().getExternalId()));
    }

    @TmsLink("522799")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF, value = "01:00 AM")
    @Story("Single Transfer cutoff time")
    @Test(dataProvider = "Basic User")
    public void verifyDashboardTransferIsScheduledNextDayIfCutoffTimeIsPassed(D3User user) {
        SingleTransfer transfer = SingleTransfer.createRandomTransfer(user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING), user
            .getFirstAccountByType(ProductType.DEPOSIT_SAVINGS));
        transfer.setScheduledDate(new DateTime());

        DateTime tomorrow = new DateTime().plusDays(1);

        PayTransfer payTransferPage = login(user)
            .clickPayTransferTab()
            .selectPayTransferDestination(transfer.getToAccount())
            .selectPayTransferFromAccount(transfer.getFromAccount());

        Assert.assertEquals(DateTimeComparator.getDateOnlyInstance().compare(payTransferPage.getScheduledDate(), tomorrow), 0,
            "Date was not set to tomorrow");

        payTransferPage.enterPayTransferDetails(transfer);

        Assert.assertTrue(payTransferPage.isTextDisplayed(ScheduleL10N.Localization.ENTER_SCHEDULED_DATE_ERROR.getValue()),
            "Today's date should be invalid");

        transfer.setScheduledDate(tomorrow);

        payTransferPage.enterPayTransferDate(transfer.getScheduledDate());

        Assert.assertTrue(payTransferPage.isTextNotPresent(ScheduleL10N.Localization.ENTER_SCHEDULED_DATE_ERROR.getValue()),
            "Tomorrows's date should be valid");
    }

    @AfterClass(alwaysRun = true)
    public void resetCutoffTime() {
        DatabaseUtils.updateCompanyAttributeValueString(CompanyAttribute.MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF, "11:59 PM");
    }
}
