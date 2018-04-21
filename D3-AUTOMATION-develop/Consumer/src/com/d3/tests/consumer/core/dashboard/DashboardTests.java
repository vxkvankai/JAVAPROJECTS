package com.d3.tests.consumer.core.dashboard;

import static com.d3.helpers.AccountHelper.getAuditFutureTransactionStatus;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.common.CommonL10N;
import com.d3.l10n.dashboard.DashboardL10N;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.l10n.planning.BudgetL10N;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import com.d3.pages.consumer.dashboard.PayTransfer;
import com.d3.pages.consumer.headers.MessagesTabs;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.planning.Budget;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Dashboard")
public class DashboardTests extends DashboardTestBase {

    private static final String ERROR_MSG = "Error text not shown";

    @TmsLink("523518")
    @Feature("Accounts Widget")
    @Story("Account links")
    @Test(dataProvider = "Basic User")
    public void verifyClickingAccountLink(D3User user) {
        // get an account with transactions in it
        D3Account accountToTest = user.getRandomAccount();
        while (accountToTest.getTransactionList().isEmpty()) {
            accountToTest = user.getRandomAccount();
        }

        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard, "Login failed");
        Assert.assertTrue(dashboard.isDashboardPageContentIsCorrect(), "Dashboard Page content is incorrect");
        Assert.assertTrue(dashboard.isCurrentMonthStatusCalculationCorrect(user), "Dashboard calculation is incorrect");

        TransactionsPage transactionsPage = dashboard.clickAccountLink(accountToTest);

        D3Transaction randomTransaction = accountToTest.getRandomTransaction();
        logger.info("Checking if {} is shown", randomTransaction);
        Assert.assertTrue(transactionsPage.isTransactionShown(randomTransaction), String.format("%s transaction wasn't shown", randomTransaction));
    }


    @TmsLink("288006")
    @Feature("Budget")
    @Story("Create Budget")
    @Test(dataProvider = "Basic User With Bill Pay")
    public void verifyCreatingBudget(D3User user) {
        Dashboard dashboard = login(user).getHeader().clickDashboardButton();

        DashboardPlan plan = dashboard.clickPlanButton();
        Assert.assertTrue(plan.isTextDisplayed(DashboardL10N.Localization.NO_BUDGET.getValue()));

        Budget budget = plan.clickCreateBudgetButton();
        Assert.assertTrue(budget.isTextDisplayed(BudgetL10N.Localization.DELETE.getValue()));
    }

    @Flaky // due to amount not getting onChange event
    @TmsLink("305912")
    @TmsLink("522810")
    @Feature("Pay/Transfer")
    @Story("Transfer Now")
    @Test(dataProvider = "User with not submitted transfers")
    public void verifyTransferNow(D3User user, SingleTransfer transfer) {
        PayTransfer payTransfer = login(user)
                .getHeader().clickDashboardButton()
                .clickPayTransferTab()
                .enterPayTransferDetails(transfer)
                .clickPayTransferSubmitButton();

        Assert.assertTrue(payTransfer.isTextDisplayed(
                String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), transfer.getAmountStr(),
                        transfer.getToAccount().getTransferableName(), transfer.getFromAccount().getTransferableName(),
                        transfer.getScheduledDateString())));
        Dashboard dashboard = payTransfer.clickPayTransferContinueButton().clickPayTransferOkButton();
        Schedule schedulePage = dashboard.getHeader()
                .clickMoneyMovementButton()
                .expandTransferDetails(transfer.getAmountStr());
        Assert.assertTrue(schedulePage.isSingleTransferCorrect(transfer));
        Assert.assertTrue(schedulePage.isScheduledTransactionDisplayedOnTimeLine());

        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, transfer.getNewTransferAuditType()),
                String.format("Did not find an audit record type: %s for %s", transfer.getNewTransferAuditType(), user));
        String auditTransactionStatus = getAuditFutureTransactionStatus(user, transfer.getProviderOption());
        Assert.assertEquals(auditTransactionStatus, D3Transaction.TransactionStatus.PENDING.toString(),
                "Scheduled transaction status was not correct");

        MessagesTabs secureMessages = login(user).getHeader().clickMessagesButton().getTabs();
        Assert.assertTrue(secureMessages.isTextDisplayed(secureMessages.getScheduleMessage(transfer)), "Transaction message was not seen");
    }

    @Flaky
    @TmsLink("305913")
    @Story("Transfer Now")
    @Test(dataProvider = "User with not submitted transfers")
    public void verifyTransferNowFieldValidation(D3User user, SingleTransfer transfer) {
        //set initial bad values
        String badChars = "~`<>^|/";
        Double originalAmount = transfer.getAmount();

        transfer.setAmount(0.99);

        PayTransfer payTransferPage = login(user)
                .getHeader().clickDashboardButton()
                .clickPayTransferTab()
                .clickPayTransferSubmitButton();

        Assert.assertTrue(payTransferPage.isTextDisplayed("Enter a Destination"), ERROR_MSG);
        Assert.assertTrue(payTransferPage.isTextDisplayed(ScheduleL10N.Localization.ENTER_AMOUNT_ERROR.getValue()), ERROR_MSG);
        Assert.assertTrue(payTransferPage.isTextDisplayed("Enter a Source"), ERROR_MSG);
        Assert.assertTrue(payTransferPage.isTextDisplayed(ScheduleL10N.Localization.ENTER_SCHEDULED_DATE_ERROR.getValue()), ERROR_MSG);

        payTransferPage.enterPayTransferDetails(transfer)
                .clickPayTransferSubmitButton();

        String validationErrorStr = String.format(ScheduleL10N.Localization.RANGE_VALIDATION_ERROR.getValue(), "1.00", "999,999,999.99");

        Assert.assertTrue(payTransferPage.isTextDisplayed(validationErrorStr), ERROR_MSG);

        transfer.setAmount(999999999999.99);
        payTransferPage.enterPayTransferAmount(transfer.getAmountStr())
                .clickPayTransferSubmitButton();

        Assert.assertTrue(payTransferPage.isTextDisplayed(validationErrorStr), ERROR_MSG);

        transfer.setAmount(originalAmount);
        payTransferPage.enterPayTransferAmount(transfer.getAmountStr());

        if (transfer.hasNote()) {
            for (char badChar : badChars.toCharArray()) {
                payTransferPage.enterNote(String.valueOf(badChar))
                        .clickPayTransferSubmitButton();
                Assert.assertTrue(payTransferPage.isTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue()), ERROR_MSG);
            }
        } else {
            Assert.assertThrows(TimeoutException.class, () -> payTransferPage.enterNote("test"));
        }
    }

    @TmsLink("348369")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_EXTERNAL_TRANSFER_CUTOFF, value = "11:59 PM", enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF, value = "11:59 PM", enabled = false)
    @Story("Expedited and Overnight Payments")
    @Test(dataProvider = "User with not submitted expedited single transfers")
    public void verifySubmittingExpeditedPaymentsFromPayTransfer(D3User user, SingleTransfer transfer) {
        //The fees for simulator are constant $25 for Expedited and $50 for overnight payments
        String fee = transfer.getProviderOption() == ProviderOption.EXPEDITED_PAYMENT ? "+$25.00" : "+$50.00";

        PayTransfer payTransfer = login(user).clickPayTransferTab()
            .enterPayTransferDetails(transfer)
            .selectExpeditedOption();
        Assert.assertTrue(payTransfer.isTextPresent(String.format("%s %s", ScheduleL10N.Localization.EXPEDITED_TITLE.getValue(), fee)), "Get it there Faster option is not being displayed");

        payTransfer.clickPayTransferSubmitButton()
            .clickPayTransferContinueButton();
        Assert.assertTrue(payTransfer.isTextDisplayed(ScheduleL10N.Localization.SUCCESS_TEXT_SCHEDULED_TRANSACTION.getValue()));

        payTransfer.clickPayTransferOkButton();
        String paymentMethod = DatabaseUtils.getAuditRecordAttributes(user, Audits.PAYMENT_SUBMITTED)
            .getOrDefault(AuditAttribute.PAYMENT_DELIVERY_METHOD.getAttributeName(), "");

        //Verify PAYMENT_DELIVERY_METHOD is ExpeditedPayment or OvernightCheck
        Assert.assertTrue(StringUtils.deleteWhitespace(paymentMethod).startsWith(transfer.getProviderOption().getExternalId()));

    }
}

