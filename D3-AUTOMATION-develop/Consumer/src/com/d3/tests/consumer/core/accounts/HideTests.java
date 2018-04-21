package com.d3.tests.consumer.core.accounts;

import static com.d3.helpers.AccountHelper.haveTotalsChanged;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.alerts.AccountBalanceAlert;
import com.d3.datawrappers.alerts.CreditDepositAlert;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.alerts.TransactionAmountExceedsAlert;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.l10n.settings.SettingsAccountsL10N;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import com.d3.pages.consumer.dashboard.PayTransfer;
import com.d3.pages.consumer.moneymovement.PayMultiple;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.ScheduleTransactionForm;
import com.d3.pages.consumer.planning.Budget;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import com.d3.pages.consumer.settings.accounts.Accounts;
import com.d3.pages.consumer.settings.alerts.Alerts;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.tests.annotations.RunForSpecificAlerts;
import com.d3.tests.annotations.RunForUserTypes;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

@Epic("Accounts")
@Feature("Hide Account")
public class HideTests extends AccountsTestBase {

    private static final String HIDE_ACCOUNT_BUTTON_ERROR = "Hide Account button still available for account with %s alert set up";

    private MyAccountsSection hideAccountsCommon(D3User user, D3Account account) {
        MyAccountsSection myAccountsSection =
            login(user).getHeader().clickAccountsButton().getMyAccountsSection()
                .clickAccountByAccountName(account.getName())
                .clickHideAccountButton();
        Assert.assertTrue(myAccountsSection
            .isTextDisplayed(AccountsL10N.Localization.HIDE_CONFIRM.getValue()));
        myAccountsSection.clickContinueButton();
        return myAccountsSection;
    }

    @TmsLink("287988")
    @Story("Hide Account")
    @Test(dataProvider = "Basic User with existing accounts")
    public void verifyHideAccount(D3User user, D3Account account) {
        MyAccountsSection myAccountsSection = hideAccountsCommon(user, account);
        logger.info("Checking to see if {} is still on the screen", account.getName());
        Assert.expectThrows(NoSuchElementException.class, () -> myAccountsSection.clickAccountByAccountName(account.getName()));
    }

    @TmsLink("522287")
    @Story("Show Hidden Account")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void verifyUnhideShowAccount(D3User user, D3Account account) {
        Dashboard dashboard = login(user);
        Accounts hiddenAccount = dashboard
            .getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickAccountsLink();
        Assert.assertTrue(hiddenAccount.isTextDisplayed(account.getName()), String.format("Account name %s is not displayed", account.getName()));

        hiddenAccount.clickShowAccountButton(account.getName());
        Assert.assertTrue(hiddenAccount.isTextDisplayed(SettingsAccountsL10N.Localization.NO_HIDDEN_ACCOUNTS.getValue()),
            "An account is still hidden to the user");

        MyAccountsSection myAccountsSection = dashboard.getHeader()
            .clickAccountsButton()
            .getMyAccountsSection();
        Assert.assertTrue(myAccountsSection.isTextPresent(account.getName()));
    }

    @TmsLink("522302")
    @TmsLink("522303")
    @Story("Hide Account Not Available For Account With Account Balance or Transaction Amount Exceeds Alert")
    @RunForSpecificAlerts(d3AlertType = {AccountBalanceAlert.class, TransactionAmountExceedsAlert.class})
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void validateHideAccountButtonIsNotDisplayedWhenAccountHasASpecificAlert(D3Alert alert) {
        MyAccountsSection myAccountsSection = login(alert.getUser()).getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(alert.getAccountUsed().getName());
        Assert.assertFalse(myAccountsSection.isHideAccountButtonDisplayed(),
            String.format(HIDE_ACCOUNT_BUTTON_ERROR, alert.getName()));
    }

    @TmsLink("522306")
    @Story("Hidden Account not available for Consumer Alerts")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void validateHiddenAccountsDoNotAppearInAlertsDropdown(D3User user, D3Account account) {
        Dashboard dashboard = login(user);
        AccountBalanceAlert alert = new AccountBalanceAlert(user);
        alert.createRandomData();

        AlertForm alertForm = dashboard.getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickAlertsLink()
            .addAlert(alert);
        Assert.assertFalse(alertForm.isAccountAvailable(account.getName()), "The account is still visible in alerts dropdown");
    }

    @TmsLink("522298")
    @Story("Hide Account Not available for account with recurring payment")
    @Test(dataProvider = "Basic User with Recurring Transfers By Accounts")
    public void validateHideAccountButtonIsNotDisplayedWhenAccountUsedForRecurringPaymentToOrFromAccount(D3User user,
        RecurringTransfer transfer) {
        D3Account fromAccount = transfer.getFromAccount();
        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(fromAccount.getName());
        Assert.assertFalse(myAccountsSection.isHideAccountButtonDisplayed(), "The hide account button is displayed");
    }

    @TmsLink("522305")
    @Story("Hide Account Available for excluded accounts")
    @Test(dataProvider = "Basic User with excluded existing accounts")
    public void validateHideAccountButtonDisplaysForExcludedAccounts(D3User user, D3Account account) {
        MyAccountsSection myAccountsSectionPage = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());
        Assert.assertTrue(myAccountsSectionPage.isHideAccountButtonDisplayed(),
            "The hidden account button is not displayed in excluded accounts");
    }

    @TmsLink("522296")
    @Story("Hide Account Not available for account with pending transfers")
    @Test(dataProvider = "Basic User with one time internal transfer")
    public void verifyOneTimeTransferRemovesHideButtonFromAccounts(D3User user, D3Transfer transfer) {
        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection();

        // Check to account
        myAccountsSection.clickAccountByAccountName(transfer.getToAccount().getName());
        Assert.assertFalse(myAccountsSection.isHideAccountButtonDisplayed(),
            "Hide button was visible for an account that has a pending transfer");

        // Check from account
        myAccountsSection.clickAccountByAccountName(transfer.getFromAccount().getName());
        Assert.assertFalse(myAccountsSection.isHideAccountButtonDisplayed(),
            "Hide button was visible for an account that has a pending transfer");
    }


    @Flaky
    @TmsLink("522308")
    @Story("Hidden accounts not included in budget")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void validateHiddenAccountsValuesAreNotIncludedInBudgetCalculations(D3User user, D3Account account) {

        String month = DatabaseUtils.getPostDateDebitTransactionForBudget(account);
        String sumDebitTransaction = DatabaseUtils.getSumDebitTransactionForBudget(account);

        Assert.assertNotNull(sumDebitTransaction, "Error getting sum of debit transactions for the budget");
        double sumTransactionsHideAccount = Double.parseDouble(sumDebitTransaction);

        String sumOfUsersDebitTransactions = DatabaseUtils.getSumOfUsersDebitTransactionsInSpecifyMonth(user, month);
        Assert.assertNotNull(sumOfUsersDebitTransactions, "Error getting sum of user's debit transactions for a specific month");
        double sumTotalTransactions = Double.parseDouble(sumOfUsersDebitTransactions);

        Budget budget = login(user)
            .getHeader()
            .clickPlanningButton()
            .clickCreateBudget()
            .selectStartDateBudget(month);
        Assert.assertTrue(budget.valuesAreNotIncluded(sumTotalTransactions, sumTransactionsHideAccount),
            "Hidden account transactions are included in the budget");
    }


    @TmsLink("522293")
    @Story("Hidden Accounts not available for Pay Multiple")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyHiddenAccountsAreNotDisplayedOnPayMultipleDropdown(D3User user) {
        // Get base dashboard page
        Dashboard dashboard = login(user);

        // Go to Accounts page 
        MyAccountsSection myAccountsSectionPage = dashboard
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection();

        // Account to Hide
        String accountToHide1 = user.getAccounts().get(0).getName();
        String accountToHide2 = user.getAccounts().get(2).getName();

        // Hide the accounts
        myAccountsSectionPage.clickAccountByAccountName(accountToHide1)
            .clickHideAccountButton()
            .clickContinueButton()
            .clickAccountByAccountName(accountToHide2)
            .clickHideAccountButton()
            .clickContinueButton();

        // Go to Pay Multiple page and select the first recipient
        PayMultiple payMultiple = myAccountsSectionPage.getHeader()
            .clickMoneyMovementButton()
            .getTabs()
            .clickPayMultipleLink()
            .clickFirstRecipient();

        // Verify hidden accounts are not listed
        Assert.assertFalse(payMultiple.isAccountDisplayed(accountToHide1));
        Assert.assertFalse(payMultiple.isAccountDisplayed(accountToHide2));
    }

    @TmsLink("522300")
    @Story("Hide Account Not available for Bill Pay funding accounts")
    @Test(dataProvider = "Basic User With Bill Pay")
    public void verifyHideAccountButtonIsNotDisplayedForBillPayAccounts(D3User user) {
        D3Account userAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        Assert.assertNotNull(userAccount, "error getting account");

        String account = userAccount.getName();
        MyAccountsSection myAccountsSectionPage = login(user)
            .getHeader().clickAccountsButton().getMyAccountsSection()
            .clickAccountByAccountName(account);
        Assert.assertFalse(myAccountsSectionPage.isHideAccountButtonDisplayed(), "Hide Account button is still displayed");
    }

    @TmsLink("522304")
    @Story("Hide Account Available when consumer alerts removed from account")
    @RunForSpecificAlerts(d3AlertType = {CreditDepositAlert.class})
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void validateHideAccountButtonDisplaysWhenAlertsAreRemoved(D3Alert alert) {
        Dashboard dashboard = login(alert.getUser());
        Alerts alerts = dashboard.getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickAlertsLink()
            .deleteAlert(alert)
            .confirmDeletingAlert();

        MyAccountsSection accountsSection = alerts.getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(alert.getAccountUsed().getName());
        Assert.assertTrue(accountsSection.isHideAccountButtonDisplayed(), "The hide account button is not present");
    }

    @TmsLink("522295")
    @Story("Current Month and NetWorth totals are 0 when all accounts hidden")
    @Test(dataProvider = "Basic User with all internal accounts hidden")
    public void verifyDashboardManageAndPlanTotalsAreUpdatedWhenAllAccountsAreHidden(D3User user) {
        //Get current month totals
        Dashboard dashboard = login(user);
        List<BigDecimal> newCurrentMonthInfo = dashboard.getCurrentMonthInfo();

        //Get Net Worth
        List<BigDecimal> newNetWorthInfo = dashboard.clickPlanButton().getNetWorthInfo(); // Verify Current month and Net Worth  totals

        for (BigDecimal actual : newCurrentMonthInfo) {
            Assert.assertEquals(actual.compareTo(BigDecimal.ZERO), 0, "Value on the dashboard was not zero");
        }

        for (BigDecimal actual : newNetWorthInfo) {
            Assert.assertEquals(actual.compareTo(BigDecimal.ZERO), 0, "Networth on the dashboard was not zero");
        }
    }

    @TmsLink("522294")
    @Story("Message displayed when all accounts are hidden")
    @Test(dataProvider = "Basic User with all internal accounts hidden")
    public void verifyMessageIsDisplayedWhenAllAccountsAreHidden(D3User user) {
        //Get all accounts for this user
        Dashboard dashboard = login(user);

        List<String> accountsAfterHide = DatabaseUtils.getMaskedUserAccounts(user);

        PayTransfer payTransfer = dashboard.clickPayTransferTab();
        Assert.assertTrue(payTransfer.arePayTransferDestinationAccountsCorrect(accountsAfterHide));

        ScheduleTransactionForm schedulePage = payTransfer.clickCancelButton()
            .getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();
        Assert.assertTrue(schedulePage.areDestinationAccountsCorrect(accountsAfterHide));

        schedulePage.clickCancelButton();
        Assert.assertTrue(schedulePage.isTextDisplayed(ScheduleL10N.Localization.SUBHEADING.getValue()));
    }

    @TmsLink("522307")
    @Story("Hidden Accounts not available as Goal Funding Accounts")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void verifyHiddenAccountsAreNotDisplayedOnPlanning(D3User user, D3Account account) {
        D3Goal goal = RetirementGoal.createRandomGoal(user);
        //Verify hide account is not displayed on add new goals
        GoalForm goals = login(user).getHeader().clickPlanningButton().getTabs().clickGoalsLink()
            .addGoal(goal);
        Assert.assertFalse(goals.isTextPresent(account.getName()));
    }

    @TmsLink("522290")
    @Story("Transactions not displayed for hidden accounts")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void verifyHiddenAccountsAreNotDisplayedOnTransactions(D3User user, D3Account account) {
        TransactionsPage transactions = login(user).getHeader().clickTransactionsButton();
        Assert.assertTrue(transactions.areCorrectTransactionsDisplayed(DatabaseUtils.getMaskedUserAccounts(user)));
    }

    @TmsLink("522299")
    @Story("Hide Account Not displayed for account used as goal funding account")
    @Test(dataProvider = "Basic User with Goals")
    public void verifyHiddenAccountButtonIsNotDisplayedForGoalFundingAccount(D3User user, D3Account account, D3Goal goal) {
        String accountToCheck = account.getName();
        MyAccountsSection myAccountsSectionPage = login(user)
            .getHeader().clickAccountsButton().getMyAccountsSection()
            .clickAccountByAccountName(accountToCheck);
        Assert.assertFalse(myAccountsSectionPage.isHideAccountButtonDisplayed(),
            "The hidden account button is not displayed for Goal Funding Accounts");
    }

    @TmsLink("522291")
    @Story("Hidden Accounts not available on Money Movement Schedule")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void verifyHiddenAccountsAreNotDisplayedOnMoneyMovement(D3User user, D3Account account) {
        // Go to  Money Movement page and click on Schedule
        ScheduleTransactionForm singleTransfer = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton()
            .selectARandomToAccount();

        // Check that hidden account is not available in destination and source dropdowns
        Assert.assertTrue(singleTransfer.areDestinationAccountsCorrect(DatabaseUtils.getMaskedUserAccounts(user)));
        Assert.assertTrue(singleTransfer.areSourceAccountsCorrect(DatabaseUtils.getMaskedUserAccounts(user)));
        Assert.assertFalse(singleTransfer.isAccountDisplayedOnDestinationDropDown(account.getName()));
        Assert.assertFalse(singleTransfer.isAccountDisplayedOnSourceDropDown(account.getName()));

        // Click on Cancel button
        singleTransfer.clickCancelButton();

        // Validate is on expected page
        Assert.assertTrue(singleTransfer.isTextPresent(ScheduleL10N.Localization.SUBHEADING.getValue()));
    }

    @TmsLink("522301")
    @Story("Hide Account Not available for account with Credit / Deposit Alert configured")
    @RunForSpecificAlerts(d3AlertType = {CreditDepositAlert.class})
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void verifyHideAccountNotAvailableForAccountWithCreditDepositAlert(D3Alert alert) {

        MyAccountsSection myAccountsSection = login(alert.getUser())
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(alert.getAccountUsed().getName());

        Assert.assertFalse(myAccountsSection.isHideAccountButtonDisplayed(),
            String.format(HIDE_ACCOUNT_BUTTON_ERROR, alert.getName()));
    }

    @TmsLink("522289")
    @Story("Hidden Accounts not displayed in Net Worth calculations")
    @Test(dataProvider = "Basic User with all internal accounts hidden")
    public void verifyHiddenAccountsAreNotDisplayedInNetWorthCalculationsAndTotalsAreUpdated(D3User user) {
        Dashboard dashboard = login(user);

        //Get the net worth info after hiding the account
        DashboardPlan dashboardPlanPage = dashboard.clickPlanButton();
        List<BigDecimal> currentNetWorthInfo = dashboardPlanPage.getNetWorthInfo();
        //Unhide Account
        dashboard.getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickAccountsLink()
            .clickAnyShowAccountButton();

        //Get the net worth  info after hiding the account
        List<BigDecimal> newCurrentNetWorthInfo = dashboard.getHeader().clickDashboardButton().clickPlanButton().getNetWorthInfo();
        Assert.assertTrue(dashboardPlanPage.isNetWorthCalculationCorrect(user));
        Assert.assertTrue(haveTotalsChanged(currentNetWorthInfo, newCurrentNetWorthInfo));
    }

    @TmsLink("522288")
    @Story("Hidden Accounts not displayed on Dashboard Manage")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void verifyHiddenAccountsAreNotDisplayedOnDashboardManageAndTotalsAreUpdated(D3User user, D3Account account) {
        String accountName = account.getName();
        Dashboard dashboard = login(user);

        //Get the list of accounts that are not hidden.
        List<String> accountsAfterHide = DatabaseUtils.getMaskedUserAccounts(user);

        //Validate hidden account is not displayed on the destination options drop down
        PayTransfer payTransfer = dashboard.getHeader().clickDashboardButton().clickPayTransferTab();
        Assert.assertTrue(payTransfer.arePayTransferDestinationAccountsCorrect(accountsAfterHide));
        payTransfer.clickCancelButton();

        //validate hidden account is not displayed on recent transactions
        Assert.assertTrue(dashboard.areRecentTransactionAccountsCorrect(accountsAfterHide));

        //List of current month status after hiding the account

        //validate hided account is not displayed on dashboard plan page
        Assert.assertFalse(dashboard.isTextPresent(accountName));

        Assert.assertTrue(dashboard.isCurrentMonthStatusCalculationCorrect(user));
    }


    @TmsLink("522292")
    @Story("Hidden Accounts not available on Money Movement Schedule")
    @Test(dataProvider = "Basic User with hidden existing accounts")
    public void verifyHiddenAccountsAreNotDisplayedOnMoneyMovementSchedule(D3User user, D3Account hiddenAccount) {

        //Get the list of the non-hidden accounts for the user
        List<String> accountsAfterHide = DatabaseUtils.getMaskedUserAccounts(user);
        ScheduleTransactionForm schedulePage = login(user).getHeader().clickMoneyMovementButton().clickScheduleButton();
        //Verify the Destination accounts
        Assert.assertTrue(schedulePage.areDestinationAccountsCorrect(accountsAfterHide));
        Assert.assertFalse(schedulePage.isAccountDisplayedOnDestinationDropDown(hiddenAccount.getName()));

        //Select a random account to enable Destination select.
        schedulePage.selectARandomToAccount();
        //Verify the Source accounts
        Assert.assertTrue(schedulePage.areSourceAccountsCorrect(accountsAfterHide));
        Assert.assertFalse(schedulePage.isAccountDisplayedOnSourceDropDown(hiddenAccount.getName()));
    }
}
