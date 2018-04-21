package com.d3.pages.asserts.dashboard;

import static com.d3.helpers.AccountHelper.verifyAccounts;
import static com.d3.helpers.DateAndCurrencyHelper.compareCurrency;

import com.d3.database.TransactionDatabaseHelper;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.dashboard.DashboardL10N;
import com.d3.pages.consumer.AssertionsBase;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;

import java.math.BigDecimal;
import java.util.List;

public class DashboardAssert extends AssertionsBase<DashboardAssert, Dashboard> {

    protected static final String URL = "#dashboard/manage";

    public DashboardAssert(Dashboard actual) {
        super(actual, DashboardAssert.class);
    }

    @Step("Check if the recent transaction accounts are correct")
    public DashboardAssert hasCorrectRecentTransactionAccounts(List<String> userAccounts) {
        if (!verifyAccounts(userAccounts, getListElement("recentTransactionAccount"), Element::getText)) {
            failWithMessage("Recent Transactions included an account that was not in the following accounts: <%s>", userAccounts);
        }
        return this;
    }

    @Step("Check the current month status calculations")
    public DashboardAssert hasCorrectCurrentMonthStatusCalculation(D3User user) {
        String incomeAmountStr = TransactionDatabaseHelper.getSumOfUsersCreditTransactions(user);
        String expenseAmountStr = TransactionDatabaseHelper.getSumOfUsersDebitTransactions(user);

        if (incomeAmountStr == null || incomeAmountStr.isEmpty() || expenseAmountStr == null || expenseAmountStr.isEmpty()) {
            failWithMessage("Error getting the sum amounts: income amount: <%s>, expense amount: <%s>", incomeAmountStr, expenseAmountStr);
        }

        BigDecimal incomeAmount = new BigDecimal(incomeAmountStr);
        BigDecimal expenseAmount = new BigDecimal(expenseAmountStr);
        BigDecimal availableCash = incomeAmount.subtract(expenseAmount);

        BigDecimal incomeDisplayedInWidget = actual.getCurrentMonthlyIncomeDec();
        BigDecimal expensesDisplayedInWidget = actual.getCurrentMonthStatusExpensesDec();
        BigDecimal availableCashDisplayedInWidget = actual.getCurrentMonthStatusNetDec();

        if (!compareCurrency(incomeDisplayedInWidget, incomeAmount)) {
            failWithMessage("Comparing currency failed. Income displayed in current month status widget <%s> did not match the expected income amount <%s>", incomeDisplayedInWidget, incomeAmount);

        }
        if (!compareCurrency(expensesDisplayedInWidget, expenseAmount)) {
            failWithMessage("Comparing currency failed. Expenses displayed in current month status widget <%s> did not match the expected expense amount <%s>", expensesDisplayedInWidget, expenseAmount);

        }
        if (!compareCurrency(availableCashDisplayedInWidget, availableCash)) {
            failWithMessage("Comparing currency failed. Available Cash displayed in current month status widget <%s> did not match the expected available cash amount <%s>", availableCashDisplayedInWidget, availableCash);
        }

        return this;

    }

    @Step("Check if the dashboard page content is correct")
    public DashboardAssert displaysCorrectL10nValues() {
        return displaysText(DashboardL10N.Localization.CASHFLOW_STATUS.getValue())
            .displaysText(DashboardL10N.Localization.PAY_TRANSFER_WIDGET.getValue());
    }

    @Step("Check NetWorth Widget is Displayed")
    public DashboardAssert netWorthWidgetDisplayed() {
        return elementDisplays("netWorthWidget", "Net Worth Widget was not displayed, but should have been");
    }

    @Step("Check NetWorth Widget is not Displayed")
    public DashboardAssert netWorthWidgetNotDisplayed() {
        return elementNotDisplayed("netWorthWidget", "Net Worth Widget was displayed for user, but should not be displayed");
    }

    @Step("Check Current Month Status Widget is Displayed")
    public DashboardAssert currentMonthStatusWidgetDisplayed() {
        return elementDisplays("currentMonthStatusWidget", "Current Month Status Widget was not displayed, but should have been");
    }

    @Step("Check Current Month Status Widget is not Displayed")
    public DashboardAssert currentMonthStatusWidgetNotDisplayed() {
        return elementNotDisplayed("currentMonthStatusWidget", "Current Month Status Widget was displayed, but should not have been");
    }

    @Step("Check Dashboard Plan Button is Displayed")
    public DashboardAssert dashboardPlanButtonDisplayed() {
        return elementDisplays("planButton", "Dashboard Plan button was not displayed, but should not have been");
    }

    @Override
    public DashboardAssert atPage() {
        return checkPageUrl(URL);
    }
}
