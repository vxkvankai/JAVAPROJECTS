package com.d3.tests.consumer.core.settings;

import static com.d3.pages.consumer.AssertionsBase.assertThat;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.AccountPermissions;
import com.d3.datawrappers.user.enums.UserServices;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import com.d3.pages.consumer.headers.Header;
import com.d3.pages.consumer.headers.MoneyMovementTabs;
import com.d3.pages.consumer.headers.PlanningTabs;
import com.d3.pages.consumer.headers.SettingsTabs;
import com.d3.pages.consumer.planning.Budget;
import com.d3.pages.consumer.settings.users.UsersPage;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import com.d3.tests.annotations.WithSpecificServiceAndPermissions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Settings")
@Feature("Secondary Users")
@Slf4j
public class SecondaryUserTests extends SettingsTestBase {

    @TmsLink("307640")
    @TmsLink("75520")
    @TmsLink("307637")
    @TmsLink("307644")
    @TmsLink("307648")
    @TmsLink("307649")
    @TmsLink("307651")
    @TmsLink("307646")
    @TmsLink("307639")
    @TmsLink("307645")
    @TmsLink("307638")
    @Story("Add Secondary User")
    @Test(dataProvider = "Basic User With Bill Pay")
    public void verifyAddSecondaryUser(D3User user) {
        D3SecondaryUser secondaryUser = D3SecondaryUser.createUserWithRandomServicesAndPermissions(user, user.getAssetAccounts());

        UsersPage createSecondaryUser = login(user).getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickUsersLink()
            .clickAddUserButton()
            .enterSecondaryUserInformation(secondaryUser)
            .continueToAccountServicesForm()
            .setAccountPermissionAndLimits(secondaryUser)
            .continueToBankingServicesForm()
            .setBankingServicePermissions(secondaryUser)
            .clickSubmitButton()
            .expandUserDetails(secondaryUser.getLogin());

        Assert.assertTrue(createSecondaryUser.areUserDetailsCorrect(secondaryUser), "Secondary User that was created is displaying the wrong information or permission and access levels");
    }

    @TmsLink("306567")
    @Story("Dashboard Manage NOT displayed for Secondary User with no account access")
    @WithSpecificServiceAndPermissions(withoutAccountPermissions = AccountPermissions.READ, withUserServices = UserServices.BUDGET)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyDashboardManageNotDisplayedWithNoAccessToAccounts(D3SecondaryUser secondaryUser) {
        Dashboard dashboard = login(secondaryUser)
            .getHeader()
            .clickDashboardButton();
        log.info("Checking to see if Dashboard Manage is available to secondary user {}", secondaryUser.getLogin());
        Assert.expectThrows(NoSuchElementException.class, dashboard::clickManageButton);
    }

    @TmsLink("306820")
    @Story("No Self-Service Access")
    @Test(dataProvider = "Secondary User with Random Account and Service Access")
    public void verifySecondaryUserDoesNotHaveAccessToSelfServiceTab(D3SecondaryUser secondaryUser) {
        Header navigation = login(secondaryUser).getHeader();
        log.info("Checking to see if Self-Service navigation tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.expectThrows(TimeoutException.class, navigation::clickSelfServiceButton);
    }

    @TmsLink("306786")
    @Story("No Goals Access")
    @WithSpecificServiceAndPermissions(withoutUserServices = UserServices.GOALS, withUserServices = UserServices.BUDGET)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifySecondaryUserDoesNotHaveAccessToGoals(D3SecondaryUser secondaryUser) {
        PlanningTabs planningPage = login(secondaryUser).getHeader().clickPlanningButton().getTabs();
        log.info("Checking to see if Goals sub-menu tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.expectThrows(TimeoutException.class, planningPage::clickGoalsLink);
    }

    @TmsLink("306759")
    @Story("No Budget Access")
    @WithSpecificServiceAndPermissions(withoutUserServices = UserServices.BUDGET, withUserServices = UserServices.GOALS)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifySecondaryUserDoesNotHaveAccessToBudgets(D3SecondaryUser secondaryUser) {
        PlanningTabs planningPage = login(secondaryUser).getHeader().clickPlanningButton().getTabs();
        log.info("Checking to see if Budget sub-menu tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.expectThrows(TimeoutException.class, planningPage::clickBudgetLink);
    }

    @TmsLink("306708")
    @Story("Statement & Transactions Disabled For All Accounts")
    @WithSpecificServiceAndPermissions(withoutAccountPermissions = {AccountPermissions.STATEMENTS, AccountPermissions.TRANSACTIONS})
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyTransactionsPageNotAvailableWhenStatementAndTransactionsNotEnabledForAnyAccounts(D3SecondaryUser secondaryUser) {
        Header navigation = login(secondaryUser).getHeader();
        log.info("Checking to see if Transactions navigation tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.expectThrows(TimeoutException.class, navigation::clickTransactionsButton);
    }

    @TmsLink("306716")
    @Story("Statement & Transactions Enabled For At Least One Account")
    @WithSpecificServiceAndPermissions(withoutAccountPermissions = {AccountPermissions.STATEMENTS, AccountPermissions.TRANSACTIONS})
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set for single account")
    public void verifyTransactionsPageNotAvailableWhenStatementAndTransactionsEnabledForOneAccount(D3SecondaryUser secondaryUser, D3Account accountWithoutPermissions) {
        TransactionsPage transactionsPage = login(secondaryUser).getHeader().clickTransactionsButton();
        Assert.assertFalse(transactionsPage.isAccountAvailableInDropdown(accountWithoutPermissions));
    }

    @TmsLink("306676")
    @Story("Dashboard NOT displayed for Secondary User with no access to Transactions, Budgets, or Goals")
    @WithSpecificServiceAndPermissions(withoutAccountPermissions = {AccountPermissions.STATEMENTS, AccountPermissions.TRANSACTIONS}
        , withoutUserServices = {UserServices.BUDGET, UserServices.GOALS})
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyDashboardNotDisplayedWithNoAccessToTransactionBudgetGoals(D3SecondaryUser secondaryUser) {
        Header navigation = login(secondaryUser).getHeader();
        log.info("Checking to see if Dashboard is available to secondary user {}", secondaryUser.getLogin());
        Assert.expectThrows(TimeoutException.class, navigation::clickDashboardButton);
    }


    @TmsLink("306682")
    @Story("Dashboard Plan displayed for Secondary User with access to one or more accounts")
    @WithSpecificServiceAndPermissions(withAccountPermissions = {AccountPermissions.STATEMENTS, AccountPermissions.TRANSACTIONS}
        , withoutUserServices = {UserServices.BUDGET, UserServices.GOALS})
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyDashBoardPlanDisplayedWithAccessToOneOrMoreAccounts(D3SecondaryUser secondaryUser) {
        Dashboard dashboard = login(secondaryUser).getHeader().clickDashboardButton();
        assertThat(dashboard).atPage()
            .dashboardPlanButtonDisplayed();

    }

    @TmsLink("306606")
    @Story("Validate Current Month Status and Net Worth widget is NOT displayed for Secondary Users")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.CURRENT_MONTH_STATUS)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.NET_WORTH)
    @WithSpecificServiceAndPermissions(withUserServices = UserServices.BUDGET)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyCurrentMonthStatusAndNetWorthNotDisplayed(D3SecondaryUser secondaryUser) {
        Dashboard dashboard = login(secondaryUser).getHeader().clickDashboardButton();
        assertThat(dashboard).netWorthWidgetNotDisplayed()
            .currentMonthStatusWidgetNotDisplayed();
        dashboard.clickPlanButton();
        assertThat(dashboard).netWorthWidgetNotDisplayed()
            .currentMonthStatusWidgetNotDisplayed();

    }

    @Issue("DPD-3928")
    @TmsLink("306718")
    @Story(" Money Movement Schedule NOT available for Secondary User with no access to Payments, Transfers, or Wires")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ, withUserServices = UserServices.RECIPIENTS)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyScheduleNotDisplayedWithNoAccessToPaymentsTransfersWires(D3SecondaryUser secondaryUser) {
        MoneyMovementTabs moneyMovementTabs = login(secondaryUser).getHeader().clickMoneyMovementButton().getTabs();
        log.info("Checking to see if Recipeints tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertThrows(TimeoutException.class, moneyMovementTabs::clickRecipientsLink);
        log.info("Checking to see if schedule tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertThrows(TimeoutException.class, moneyMovementTabs::clickScheduleLink);

    }


    @TmsLink("306739")
    @Story(" Recipients NOT available for Secondary User With No Access to Recipients")
    @WithSpecificServiceAndPermissions(withoutUserServices = UserServices.RECIPIENTS, withAccountPermissions = AccountPermissions.ACH)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyRecipientsNotAvailableWithNoAccesstoRecipients(D3SecondaryUser secondaryUser) {
        MoneyMovementTabs moneyMovementTabs = login(secondaryUser).getHeader().clickMoneyMovementButton().getTabs();
        log.info("Checking to see if Recipients tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertThrows(TimeoutException.class, moneyMovementTabs::clickRecipientsLink);

    }

    @TmsLink("306698")
    @Story("Accounts is Not Displayed in Navigation menu for secondary user with no accounts access")
    @WithSpecificServiceAndPermissions(withoutAccountPermissions = AccountPermissions.READ)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyAccountsNotDisplayedInNavigationMenu(D3SecondaryUser secondaryUser) {
        Header navigation = login(secondaryUser).getHeader();
        log.info("Checking to see if Accounts navigation tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertFalse(navigation.isFooterLinkAccountsDisplayed(), "Footer Link 'Accounts' Displayed");
        Assert.assertThrows(TimeoutException.class, navigation::clickAccountsButton);

    }

    @TmsLink("306832")
    @Story("Categories not available to secondary user ")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ, withoutUserServices = UserServices.CATEGORIES)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyCatagoriesNotDisplayed(D3SecondaryUser secondaryUser) {
        SettingsTabs settingsTabs = login(secondaryUser).getHeader().clickSettingsButton().getTabs();
        Assert.assertThrows(TimeoutException.class, settingsTabs::clickCategoriesLink);
    }

    @TmsLink("306822")
    @Story("Rules tab not available to secondary user ")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ, withoutUserServices = UserServices.RULES)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyRulesNotDisplayed(D3SecondaryUser secondaryUser) {
        SettingsTabs settingsTabs = login(secondaryUser).getHeader().clickSettingsButton().getTabs();
        Assert.assertThrows(TimeoutException.class, settingsTabs::clickRulesLink);


    }

    @Issue("DPD-3928")
    @TmsLink("306733")
    @Story("E-Bills NOT available for Secondary User with no access to Payments, Transfers, or Wires")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ, withUserServices = UserServices.RECIPIENTS)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyEbillsNotAvailableWithNoAccessToPaymentsTransferWires(D3SecondaryUser secondaryUser) {
        MoneyMovementTabs moneyMovementTabs = login(secondaryUser)
            .getHeader()
            .clickMoneyMovementButton()
            .getTabs();
        log.info("Checking to see if Recipient tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertTrue(moneyMovementTabs.isRecipientTabDisplayed(), "Recipient tab not displayed for secondary user");
        log.info("Checking to see if Ebills tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertFalse(moneyMovementTabs.isEbillsButtonDisplayed(), "Ebills tab Displayed for secondary user");

    }

    @Issue("DPD-3928")
    @TmsLink("306774")
    @Story("Pay Multiple NOT available for Secondary User without access to Payments for any account")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ, withUserServices = UserServices.RECIPIENTS)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyPayMultipleNotAvaiableWithNoAccessToPayments(D3SecondaryUser secondaryUser) {
        MoneyMovementTabs moneyMovementTabs = login(secondaryUser)
            .getHeader()
            .clickMoneyMovementButton()
            .getTabs();
        log.info("Checking to see if Recipient tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertTrue(moneyMovementTabs.isRecipientTabDisplayed(), "Recipient tab not displayed for secondary user");
        log.info("Checking to see if Pay Multiple tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertFalse(moneyMovementTabs.isPayMultipleButtonDisplayed(), "Pay Multiple tab Displayed for secondary user");

    }


    @TmsLink("306764")
    @Story("Budgets for Secondary User when Planning > Budgets enabled in Banking Services")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ, withUserServices = {UserServices.BUDGET, UserServices.GOALS})
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyBudgetsAvailableToSecondaryUser(D3SecondaryUser secondaryUser) {

        Budget budget = login(secondaryUser)
            .getHeader()
            .clickPlanningButton()
            .getTabs()
            .clickBudgetLink();

        Assert.assertThrows(NoSuchElementException.class, budget::clickCreateBudget);
        Assert.assertThrows(NoSuchElementException.class, budget::clickDeleteBudget);
        Assert.assertThrows(NoSuchElementException.class, budget::clickResetBudget);

        budget.clickExportBudgetButton();

        Assert.assertTrue(budget.isDownloadButtonDisplayed(), "Download button not displayed for secondary user");


    }


    @TmsLink("306696")
    @Story("Dashboard Plan displayed for Secondary User view only access to Goals")
    @WithSpecificServiceAndPermissions(withAccountPermissions = {AccountPermissions.STATEMENTS, AccountPermissions.TRANSACTIONS}, withUserServices = UserServices.GOALS)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyDashboardPlanDisplayedWithViewOnlyAccessToGoals(D3SecondaryUser secondaryUser) {
        DashboardPlan dashboardPlan = login(secondaryUser).getHeader().clickDashboardButton().clickPlanButton();
        Assert.assertThrows(NoSuchElementException.class, dashboardPlan::clickCreateBudgetButton);
        Assert.assertTrue(dashboardPlan.isGoalsWidgetDisplayed(), "Goals Widget not displayed for secondary user");
    }


    @TmsLink("306691")
    @Story("Validate Dashboard Plan displayed for Secondary User with view only access to Budgets")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.WIRE, withUserServices = UserServices.BUDGET)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyDashBoardPlanAvailableWithViewOnlyAccessToBudgets(D3SecondaryUser secondaryUser) {
        DashboardPlan dashboardPlan = login(secondaryUser)
            .getHeader()
            .clickDashboardButton()
            .clickPlanButton();
        Assert.assertThrows(NoSuchElementException.class, dashboardPlan::clickCreateBudgetButton);
        Assert.assertTrue(dashboardPlan.isBudgetSummaryDisplayed(), "Budget Summary Widget is not Displayed for Secondary User");
        Assert.assertTrue(dashboardPlan.isCashSummaryDisplayed(), "Cash Summary Widget is not available to Secondary User");
    }


    @TmsLink("306806")
    @Story("Validate Settings - Accounts, Alerts, Dashboard, and Users NOT available for any Secondary User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.USER_MANAGEMENT)
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyAccountsAlertsDashboardUsersNotAvailableToSecondaryUser(D3SecondaryUser secondaryUser) {
        SettingsTabs settingsTabs = login(secondaryUser)
            .getHeader()
            .clickSettingsButton()
            .getTabs();

        Assert.assertFalse(settingsTabs.isAlertTabDisplayed(), "Setting > Alert tab is displayed to Secondary user");
        Assert.assertFalse(settingsTabs.isAccountsTabDisplayed(), "Setting > Accounts tab is displayed to Secondary user");
        Assert.assertFalse(settingsTabs.isDashboardTabDisplayed(), "Setting > Dashboard tab is displayed to Secondary user");
        Assert.assertFalse(settingsTabs.isUsersTabDisplayed(), "Setting > Users tab is displayed to Secondary user");
    }

    @Issue("DPD-3928")
    @Issue("DPD-4163")
    @TmsLink("306749")
    @Story("PopMoney NOT available for Secondary User without access to Payments for any account")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.READ, withoutUserServices = UserServices.RECIPIENTS)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public void verifyPopMoneyNotAvailableToSecondaryUser(D3SecondaryUser secondaryUser) {
        MoneyMovementTabs moneyMovementTabs = login(secondaryUser)
            .getHeader()
            .clickMoneyMovementButton()
            .getTabs();
        log.info("Checking to see if Recipient tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertTrue(moneyMovementTabs.isRecipientTabDisplayed(), "Recipient tab not displayed for secondary user");
        log.info("Checking to see if Pop Money tab is available to secondary user {}", secondaryUser.getLogin());
        Assert.assertThrows(NoSuchElementException.class, moneyMovementTabs::clickPopMoneyLink);
    }

    @TmsLink("306704")
    @Story("Validate Accounts Page displayed for Secondary User with access to one or more accounts")
    @WithSpecificServiceAndPermissions(withAccountPermissions = AccountPermissions.ACH)
    @Test(dataProvider = "Secondary User with Specific Service Access and Account Permissions set for single account")
    public void verifyAccountsPageDisplayed(D3SecondaryUser secondaryUser, D3Account accountWithPermissions) {
        MyAccountsSection myAccountsSection = login(secondaryUser)
            .getHeader()
            .clickAccountsButton()
            .clickAccountByAccountName(accountWithPermissions.getName());

        Assert.assertThrows(NoSuchElementException.class, myAccountsSection::clickEditAccountButton); //to check Read only access
        Assert.assertThrows(NoSuchElementException.class, myAccountsSection::clickExcludeAccountButton);
        Assert.assertThrows(NoSuchElementException.class, myAccountsSection::clickHideAccountButton);
        Assert.assertThrows(NoSuchElementException.class, myAccountsSection::clickOrderChecksButton);
        Assert.assertThrows(NoSuchElementException.class, myAccountsSection::clickEstatementEnrollButton);
        Assert.assertThrows(NoSuchElementException.class, myAccountsSection::clickStopPaymentButton);
        Assert.assertThrows(NoSuchElementException.class, myAccountsSection::clickOverdraftButton);


    }

}
