package com.d3.tests.consumer.core.moneymovement;

import com.d3.database.AuditDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.account.enums.AccountProductAttributes;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.AccountHelper;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.schedule.enums.RecurringTransferFrequency;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.RecurringTransferForm;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.SingleTransferForm;
import com.d3.tests.annotations.RunWithAccountProductAttribute;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.joda.time.DateTime;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

@Epic("Money Movement")
@Feature("Recurring Transfer")
public class RecurringTransferTests extends MoneyMovementTestBase {
    private static final String CANCELLED_TEXT_ERROR_MSG = "Cancelled text is not present on the screen";

    @TmsLink("288049")
    @Story("Form Validation")
    @Story("Balance Threshold Recurring Transfer Validation")
    @Test(dataProvider = "Basic User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF, value = "11:59 PM")
    public void verifyRecurringTransferBalanceThresholdFail(D3User user) {
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
    @TmsLink("305930")
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
    @TmsLink("305942")
    @Story("Edit Single Transfer")
    @Test(dataProvider = "Basic User with Recurring Transfers")
    public void verifyUserCanDeletePendingSingleInstanceOfRecurringTransfer(D3User user, RecurringTransfer transfer) {
        Schedule schedulePage = login(user).getHeader()
            .clickMoneyMovementButton()
            .expandTransferDetails(transfer.getAmountStr())
            .clickDeleteButton()
            .clickOneDeleteButton();
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_STATUS_TEXT.getValue()),
            CANCELLED_TEXT_ERROR_MSG);
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_TRANSFER_TEXT.getValue()),
            CANCELLED_TEXT_ERROR_MSG);
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.PENDING_TRANSFER_TEXT.getValue()));
        Assert.assertThrows(NoSuchElementException.class, schedulePage::clickDeleteButton);
        Assert.assertThrows(NoSuchElementException.class, schedulePage::clickEditButton);

        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, transfer.getCancelledTransferAuditType()),
            String.format("Did not find an audit record type: %s for %s", transfer.getCancelledTransferAuditType(), user));
    }

    @TmsLink("522824")
    @TmsLink("523037")
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
    @TmsLink("305934")
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
    @TmsLink("305943")
    @Test(dataProvider = "Basic User with Recurring Transfers")
    public void verifyUserCanDeletePendingAllInstancesOfRecurringTransfer(D3User user, RecurringTransfer transfer) {
        Schedule schedulePage = login(user).getHeader()
            .clickMoneyMovementButton()
            .expandTransferDetails(transfer.getAmountStr())
            .clickDeleteButton()
            .clickAllDeleteButton();

        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_STATUS_TEXT.getValue()),
            CANCELLED_TEXT_ERROR_MSG);
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.CANCELED_TRANSFER_TEXT.getValue()),
            CANCELLED_TEXT_ERROR_MSG);
        Assert.assertTrue(schedulePage.isTextNotPresent(ScheduleL10N.Localization.PENDING_TRANSFER_TEXT.getValue()),
            "Pending text is not present on the screen");
        Assert.assertTrue(schedulePage.isTextNotPresent(ScheduleL10N.Localization.PROCESSED_TRANSFER_TEXT.getValue()),
            "Processed text is not present on the screen");

        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, transfer.getCancelledTransferAuditType()),
            String.format("Did not find an audit record type: %s for %s", transfer.getCancelledTransferAuditType(), user));
    }

    @Flaky
    @TmsLink("305920")
    @Story("Dashboard recurring transfer")
    @Test(dataProvider = "User with not submitted recurring transfers")
    public void verifyUserCanSubmitRecurringTransfersViaTheDashboard(D3User user, RecurringTransfer transfer) {
        Schedule schedulePage = login(user)
            .clickPayTransferButton()
            .fillOutRecurringForm(transfer)
            .clickSubmitButton()
            .clickContinueButton()
            .clickOkButtonDashboard()
            .getHeader()
            .clickMoneyMovementButton()
            .expandTransferDetails(transfer.getAmountStr());

        Assert.assertTrue(schedulePage.isScheduledTransactionDisplayedOnTimeLine(),
            "Transfer was not on the timeline");
        Assert.assertTrue(schedulePage.isTextPresent(transfer.getDisplayedDate()),
            "Date was not correct on the timeline");
        Assert.assertTrue(schedulePage.isTextPresent(transfer.getAmountStr()),
            "Amount was not correct on the timeline");

        String auditTransactionStatus = AccountHelper.getAuditFutureTransactionStatus(user, transfer.getToAccount().getProviderOption());
        Assert.assertFalse(auditTransactionStatus.isEmpty());
        Assert.assertEquals(auditTransactionStatus, D3Transaction.TransactionStatus.PENDING.toString(),
            "Scheduled transaction status was not correct");
    }

    @TmsLink("307410")
    @Story("Recurring Principal Only Payment")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_PRINCIPLE_ONLY)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF, value = "11:59 PM")
    @RunWithAccountProductAttribute(accountProduct = ProductType.CREDIT_CARD, attribute = AccountProductAttributes.PRINCIPAL_ONLY_PAYMENT)
    @Test(dataProvider = "Basic User")
    public void verifyRecurringPrincipalOnlyPaymentCurrentDate(D3User user) {
        StandardRecurringTransfer transfer = StandardRecurringTransfer.createRandomTransferDetails(user.getFirstAccountByType(ProductType.CREDIT_CARD), RandomHelper.getRandomElementFromList(user.getAssetAccounts()));
        transfer.setScheduledDate(DateTime.now());
        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();
        form.fillOutRecurringForm(transfer)
            .selectPrincipalOnlyCheckbox()
            .clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(
            String.format(ScheduleL10N.Localization.RECURRING_MODAL_TEXT.getValue(), transfer.getAmountStr(),
                transfer.getToAccount().getTransferableName(), transfer.getFromAccount().getName(),
                transfer.getFrequency().getDropdownValue())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton().expandNextScheduledTransfer();
        Assert.assertTrue(schedulePage.isRecurringTransferCorrect(transfer), "Transfer was not validated, see logs");
        Assert.assertTrue(schedulePage.isTextDisplayed("Applied to Principal"), "Applied to Principal was not displayed for transaction");

        Map<String, String> auditAttributes = AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.RECURRING_TRANSFER_SCHEDULED);
        Assert.assertNotNull(auditAttributes);
        Assert.assertEquals(auditAttributes.getOrDefault(AuditAttribute.PRINCIPAL_ONLY.getAttributeName(), ""), String.valueOf(true));
    }

    @TmsLink("307411")
    @Story("Recurring Principal Only Payment")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_PRINCIPLE_ONLY)
    @RunWithAccountProductAttribute(accountProduct = ProductType.CREDIT_CARD, attribute = AccountProductAttributes.PRINCIPAL_ONLY_PAYMENT)
    @Test(dataProvider = "Basic User")
    public void verifyRecurringPrincipalOnlyPaymentFutureDate(D3User user) {
        StandardRecurringTransfer transfer = StandardRecurringTransfer.createRandomTransferDetails(user.getFirstAccountByType(ProductType.CREDIT_CARD), RandomHelper.getRandomElementFromList(user.getAssetAccounts()));
        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();
        form.fillOutRecurringForm(transfer)
            .selectPrincipalOnlyCheckbox()
            .clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(
            String.format(ScheduleL10N.Localization.RECURRING_MODAL_TEXT.getValue(), transfer.getAmountStr(),
                transfer.getToAccount().getTransferableName(), transfer.getFromAccount().getName(),
                transfer.getFrequency().getDropdownValue())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton();
        Assert.assertTrue(schedulePage.isRecurringTransferCorrect(transfer), "Transfer was not validated, see logs");
        Assert.assertTrue(schedulePage.isTextDisplayed("Applied to Principal"), "Applied to Principal was not displayed for transaction");

        Map<String, String> auditAttributes = AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.RECURRING_TRANSFER_SCHEDULED);
        Assert.assertNotNull(auditAttributes);
        Assert.assertEquals(auditAttributes.getOrDefault(AuditAttribute.PRINCIPAL_ONLY.getAttributeName(), ""), String.valueOf(true));
    }
}
