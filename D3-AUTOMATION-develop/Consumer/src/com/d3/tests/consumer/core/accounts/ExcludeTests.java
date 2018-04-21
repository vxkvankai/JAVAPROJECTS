package com.d3.tests.consumer.core.accounts;

import static com.d3.helpers.AccountHelper.compareDashboardLists;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.pages.consumer.accounts.Accounts;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import com.d3.pages.consumer.planning.Budget;
import com.d3.pages.consumer.planning.GoalsPage;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Epic("Accounts")
@Feature("Exclude Account")
public class ExcludeTests extends AccountsTestBase {

    /**
     * Logs in, goes to the account page, clicks on the account, clicks exclude, then asserts the correct text is present
     *
     * @param user User to login as
     * @param account Account to click
     */
    @Step("Run common exclude test steps")
    private MyAccountsSection excludeCommonSteps(D3User user, D3Account account) {
        MyAccountsSection myAccountsSectionPage = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickExcludeAccountButton();

        Assert.assertTrue(myAccountsSectionPage.isTextDisplayed(account.getExcludeAccountConfirmationText()),
            "Exclude confirmation text was not displayed");
        return myAccountsSectionPage;
    }

    @TmsLink("287920")
    @Story("Exclude Account")
    @Test(dataProvider = "Basic User")
    public void verifyExcludeAccount(D3User user) {
        String accountName = user.getFirstAccountByAccountingClass(D3Account.AccountingClass.ASSET).getName();

        Dashboard dashboard = login(user);
        MyAccountsSection myAccountsSection = dashboard.getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(accountName)
            .clickExcludeAccountButton();

        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.EXCLUDE_ASSET_CONFIRM.getValue()),
            "Exclude asset confirmation message was not displayed");

        myAccountsSection.clickContinueButton();
        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.EXCLUDED.getValue()),
            "Excluded text was not displayed");
    }

    @TmsLink("522314")
    @Story("Exclude Account - Cancel")
    @Test(dataProvider = "Basic User with existing accounts")
    public void verifyCancelExcludeAccount(D3User user, D3Account account) {
        MyAccountsSection myAccountsSectionPage = excludeCommonSteps(user, account);
        myAccountsSectionPage.clickCancelButton();

        Assert.assertTrue(myAccountsSectionPage.isTextNotPresent(account.getExcludeAccountConfirmationText()),
            "Exclude account confirmation text was displayed when it shouldn't be");
        driver.navigate().refresh();

        Assert.assertTrue(myAccountsSectionPage.isTextNotPresent(AccountsL10N.Localization.EXCLUDED.getValue()),
            "Excluded text was displayed when it shouldn't be");

        myAccountsSectionPage.clickAccountByAccountName(account.getName());
        Assert.assertTrue(myAccountsSectionPage.isTextDisplayed("Exclude Account"),
            "Exclude Account text was not displayed");

        myAccountsSectionPage.clickExcludeAccountButton();
        Assert.assertTrue(myAccountsSectionPage.isTextDisplayed(account.getExcludeAccountConfirmationText()),
            "Exclude account confirmation text was not displayed");
    }

    @TmsLink("287987")
    @Story("Include Account")
    @Test(dataProvider = "Basic User with existing accounts")
    public void verifyIncludeAccount(D3User user, D3Account account) {
        String accountName = account.getName();

        // setup exclusion for the account
        DatabaseUtils.excludeAccount(accountName, user.getLogin());
        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(accountName)
            .clickIncludeAccountButton();

        Assert.assertTrue(myAccountsSection.isTextDisplayed(account.getIncludeAccountConfirmationText()),
            "Include account confirmation text was not displayed");

        myAccountsSection.clickContinueButton();
        Assert.assertTrue(myAccountsSection.isTextDisplayed(accountName),
            String.format("Account name: %s was not disaplyed", accountName));
        Assert.assertFalse(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.EXCLUDED.getValue()),
            String.format("%s should not be seen on the screen", AccountsL10N.Localization.EXCLUDED.getValue()));
    }

    @Flaky
    @TmsLink("523099")
    @TmsLink("522318")
    @Story("Include Account")
    @Test(dataProvider = "Basic User with excluded existing accounts")
    public void verifyUserCanIncludeExcludedAccounts(D3User user, D3Account account) {
        // get pre info for validation later
        Dashboard dashboardPage = login(user);
        Assert.assertNotNull(dashboardPage, "Issue logging in");

        List<BigDecimal> currentMonthInfo = dashboardPage.getCurrentMonthInfo();

        DashboardPlan dashboardPlanPage = dashboardPage.clickPlanButton();
        List<BigDecimal> currentNetWorthInfo = dashboardPlanPage.getNetWorthInfo();

        Budget budgetPage = dashboardPlanPage.getHeader().clickPlanningButton();
        List<BigDecimal> currentBudgetInfo = budgetPage.getBudgetInfo();

        // Go to accounts page and include the account
        Accounts accountsPage = budgetPage.getHeader().clickAccountsButton();
        accountsPage.getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickIncludeAccountButton()
            .clickContinueButton();

        // Go back to Dashboard to verify changes
        dashboardPage = accountsPage.getHeader().clickDashboardButton();
        List<BigDecimal> newCurrentMonthInfo = dashboardPage.getCurrentMonthInfo();

        logger.info("Validating the Current Month Dashboard");
        Assert.assertTrue(compareDashboardLists(currentMonthInfo, newCurrentMonthInfo, account.getCurrentMonthInfo(false)));

        dashboardPlanPage = dashboardPage.clickPlanButton();
        List<BigDecimal> newCurrentNetWorthInfo = dashboardPlanPage.getNetWorthInfo();
        logger.info("Validating the Net Worth Dashboard");
        Assert.assertTrue(compareDashboardLists(currentNetWorthInfo, newCurrentNetWorthInfo, account.getNetWorthInfo()));

        budgetPage = dashboardPlanPage.getHeader().clickPlanningButton();
        List<BigDecimal> newCurrentBudgetInfo = budgetPage.getBudgetInfo();
        List<BigDecimal> expectedBudgetInfo = new ArrayList<>();
        expectedBudgetInfo.add(BigDecimal.ZERO);
        expectedBudgetInfo.add(account.getCurrentMonthLiabilities(true).negate());
        Assert.assertTrue(compareDashboardLists(currentBudgetInfo, newCurrentBudgetInfo, expectedBudgetInfo), "Budget info was wrong, see logs");
    }

    @TmsLink("522315")
    @Story("Exclude Account for Goals")
    @Test(dataProvider = "Basic User with Goals")
    public void verifyUserCanExcludeGoalAccount(D3User user, D3Account account, D3Goal goal) {
        MyAccountsSection accountsPage = excludeCommonSteps(user, account).clickContinueButton();
        GoalsPage goalsPage = accountsPage.getHeader().clickPlanningButton().getTabs().clickGoalsLink();
        // Verify that the number 0 is shown on the page
        Assert.assertTrue(goalsPage.isTextDisplayed("$0.00"));
        GoalForm goalForm = goalsPage.expandGoal(goal).clickEditGoalButton(goal);
        Assert.assertFalse(goalForm.isFundingAccountSelected(account));
    }
}
