package com.d3.tests.consumer.core.dashboard;

import static com.d3.pages.consumer.AssertionsBase.assertThat;

import com.d3.database.AccountDatabaseHelper;
import com.d3.database.RecipientMMDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.SingleTransferForm;
import com.d3.tests.annotations.RunForUserTypes;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Dashboard")
@Slf4j
public class DashboardToggleTests extends DashboardTestBase {

    @Flaky
    @TmsLink("307885")
    @TmsLink("307894")
    @TmsLink("307867")
    @TmsLink("307876")
    @TmsLink("307903")
    @Feature("Current Month Status Widget")
    @Story("Current Month Status Calculations")
    @Test(dataProvider = "Get All User Types")
    public void verifyCurrentMonthStatusCalculations(D3User user) {
        Dashboard dashboard = login(user);
        assertThat(dashboard).hasCorrectCurrentMonthStatusCalculation(user);
    }

    @Flaky
    @TmsLink("307892")
    @TmsLink("307901")
    @TmsLink("307873")
    @TmsLink("307883")
    @TmsLink("307910")
    @Feature("Net Worth Widget")
    @Story("Net Worth Calculations")
    @Test(dataProvider = "Get All User Types")
    public void verifyNetWorthCalculations(D3User user) {
        DashboardPlan dashboardPlan = login(user).clickPlanButton();
        assertThat(dashboardPlan).hasCorrectNetWorthCalculations(user);
    }

    @TmsLink("307890")
    @TmsLink("307899")
    @TmsLink("307872")
    @TmsLink("307881")
    @TmsLink("307908")
    @Feature("Recent Transaction Widget")
    @Story("Accounts used on Recent Transactions")
    @Test(dataProvider = "Get All User Types")
    public void verifyAccountUsedOnRecentTransactions(D3User user) {
        Dashboard dashboard = login(user);
        assertThat(dashboard).hasCorrectRecentTransactionAccounts(AccountDatabaseHelper.getMaskedUserAccounts(user));
    }

    @Feature("Pay/Transfer")
    @Story("Available Accounts and Recipients")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyCorrectQuickPayAccountsAndRecipientsAvailable(D3User user) {
        List<String> userRecipients = RecipientMMDatabaseHelper.getDefaultUserRecipients(user);
        user.getRecipients().forEach(recipient -> userRecipients.add(recipient.getTransferableName()));

        SingleTransferForm payment = login(user).getHeader()
            .clickDashboardButton()
            .clickPayTransferButton();
        Assert.assertTrue(payment.areDestinationCompanyRecipientsCorrect(userRecipients),
            "Destination company recipients weren't the expected");
        Assert.assertTrue(payment.areDestinationPersonRecipientsCorrect(userRecipients),
            "Destination person recipients weren't the expected");

        Recipient companyRecipient = user.getFirstRecipientByType(RecipientWho.COMPANY);
        if (companyRecipient == null) {
            Assert.fail("Couldn't find company recipient on user");
        }

        payment.selectToAccount(companyRecipient);
        Assert.assertTrue(payment.areSourceAccountsCorrect(AccountDatabaseHelper.getMaskedUserAccounts(user)),
            "Source accounts were not what was expected");
    }

    @Feature("Pay/Transfer")
    @Story("Transfer - Available Accounts and Recipients")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyCorrectTransferNowAccountsAvailable(D3User user) {
        SingleTransferForm payTransfer = login(user).getHeader()
            .clickDashboardButton()
            .clickPayTransferButton();

        Assert.assertTrue(payTransfer.areDestinationAccountsCorrect(AccountDatabaseHelper.getMaskedUserAccounts(user)));

        payTransfer.selectToAccount(
            user.getUserType().isBusinessType() ? user.getFirstAccountByType(ProductType.BUSINESS_DEPOSIT_CHECKING)
                : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING));

        Assert.assertTrue(payTransfer.areSourceAccountsCorrect(AccountDatabaseHelper.getMaskedUserAccounts(user)));

    }

    @Flaky
    @Feature("Pay/Transfer")
    @Story("Quick Pay to Company Recipient")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
        UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyQuickPayToCompanyRecipient(D3User user) {

        Recipient toAccount = user.getFirstRecipientByType(RecipientWho.COMPANY);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        SingleTransfer newTransfer = SingleTransfer.createRandomTransfer(toAccount, fromAccount, false);

        SingleTransferForm payment = login(user)
            .getHeader()
            .clickDashboardButton()
            .clickPayTransferButton()
            .fillOutForm(newTransfer)
            .clickSubmitButton();
        try {
            payment.clickContinueButton();
        } catch (NoSuchElementException e) {
            log.warn("Can't click Accept Disclosure button, trying to click submit again", e);
            payment.clickSubmitButton()
                .clickContinueButton();
        }

        Assert.assertTrue(
            payment.isTextDisplayed(String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), newTransfer.getAmountStr(),
                newTransfer.getToAccount().getTransferableName(), newTransfer.getFromAccount().getTransferableName(),
                newTransfer.getScheduledDateString())));
        SingleTransferForm transferPage = payment.clickContinueButton();
        Schedule schedulePage;
        try {
            schedulePage = transferPage.clickOkButtonDashboard()
                .getHeader()
                .clickMoneyMovementButton();
        } catch (NoSuchElementException e) {
            log.warn("Something went wrong confirming the payment, trying OK button again", e);
            schedulePage = transferPage.clickOkButtonDashboard()
                .getHeader()
                .clickMoneyMovementButton();
        }

        schedulePage.expandTransferDetails(newTransfer.getAmountStr());

        Assert.assertTrue(schedulePage.isSingleTransferCorrect(newTransfer));
    }

    @Flaky
    @Feature("Pay/Transfer")
    @Story("Pay Person Recipient")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
        UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyQuickPayToPersonRecipient(D3User user) {

        Recipient toAccount = user.getFirstRecipientByType(RecipientWho.PERSON);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        SingleTransfer newTransfer = SingleTransfer.createRandomTransfer(toAccount, fromAccount, false);

        SingleTransferForm payment = login(user).getHeader().clickDashboardButton()
            .clickPayTransferButton()
            .fillOutForm(newTransfer)
            .clickSubmitButton();
        try {
            payment.clickContinueButton();
        } catch (NoSuchElementException e) {
            log.warn("Can't click Accept Disclosure button, trying to click submit again", e);
            payment.clickSubmitButton()
                .clickContinueButton();
        }
        Assert.assertTrue(
            payment.isTextDisplayed(String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), newTransfer.getAmountStr(),
                newTransfer.getToAccount().getTransferableName(), newTransfer.getFromAccount().getTransferableName(),
                newTransfer.getScheduledDateString())));
        Dashboard dashboard = payment.clickContinueButton()
            .clickOkButtonDashboard();

        Schedule schedulePage = dashboard.getHeader().clickMoneyMovementButton()
            .expandTransferDetails(newTransfer.getAmountStr());

        Assert.assertTrue(schedulePage.isSingleTransferCorrect(newTransfer));
    }

    @Feature("Pay/Transfer")
    @Story("Internal Account Transfer")
    @Test(dataProvider = "Get All User Types")
    public void verifyTransferBetweenInternalAccounts(D3User user) {

        D3Account toAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_CHECKING) : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        SingleTransfer newTransfer = SingleTransfer.createRandomTransfer(toAccount, fromAccount, false);
        // set date to today for transfer now

        SingleTransferForm transfer = login(user)
            .getHeader().clickDashboardButton()
            .clickPayTransferButton()
            .fillOutForm(newTransfer)
            .clickSubmitButton();

        // TODO Add check to the Date (need to figure out cutoff time)
        Assert.assertTrue(
            transfer.isTextDisplayed(String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), newTransfer.getAmountStr(),
                newTransfer.getToAccount().getTransferableName(), newTransfer.getFromAccount().getTransferableName(),
                newTransfer.getScheduledDateString())));
        Dashboard dashboard = transfer.clickContinueButton()
            .clickOkButtonDashboard();

        Schedule schedulePage = dashboard.getHeader().clickMoneyMovementButton()
            .expandTransferDetails(newTransfer.getAmountStr());

        Assert.assertTrue(schedulePage.isSingleTransferCorrect(newTransfer));
    }
}
