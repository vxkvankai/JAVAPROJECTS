package com.d3.pages.consumer.dashboard;

import static com.d3.helpers.AccountHelper.verifyAccounts;
import static com.d3.helpers.DateAndCurrencyHelper.compareCurrency;
import static com.d3.helpers.DateAndCurrencyHelper.parseCurrency;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.dashboard.DashboardL10N;
import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;

public class Dashboard extends BaseConsumerPage {

    private static final String NON_DIGITS = "[^0-9]";

    @FindBy(xpath = "//a[@href='#dashboard/plan']")
    private Element planButton;

    @FindBy(xpath = "//button[@data-type='pay-transfer']")
    private Element payTransferTab;

    @FindBy(css = "div.account-name > a")
    private List<Element> accountLinks;

    @FindBy(css = "div[class$='income'] div[class='amount']")
    private Element currentMonthStatusIncome;

    @FindBy(css = "div[class$='expenses'] div[class='amount']")
    private Element currentMonthStatusExpenses;

    @FindBy(css = "div[class$='net'] div[class='amount']")
    private Element currentMonthStatusNet;

    @FindBy(css = "div.account-name.text-nowrap")
    private List<Element> recentTransactionAccount;

    public Dashboard(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Dashboard me() {
        return this;
    }

    @Step("Click the plan button")
    public DashboardPlan clickPlanButton() {
        planButton.click();
        waitForSpinner();
        return DashboardPlan.initialize(driver, DashboardPlan.class);
    }

    @Step("Click the Pay Transfer Tab")
    public PayTransfer clickPayTransferTab() {
        payTransferTab.click();
        waitForSpinner();
        return PayTransfer.initialize(driver, PayTransfer.class);
    }

    @Step("Click the accounts link for {account}")
    public TransactionsPage clickAccountLink(D3Account account) {
        getElementInListByTextContains(accountLinks, account.getName()).click();
        waitForSpinner();
        return TransactionsPage.initialize(driver, TransactionsPage.class);
    }

    @Step("Get the Current monthly income")
    public String getCurrentMonthlyIncome() {
        return currentMonthStatusIncome.getText();
    }

    @Step("Get the current monthly income in Decimal format")
    public BigDecimal getCurrentMonthlyIncomeDec() {
        return new BigDecimal(getCurrentMonthlyIncome().replace("$", "").replace(",", ""));
    }

    @Step("Get the current month status expenses")
    public String getCurrentMonthStatusExpenses() {
        return currentMonthStatusExpenses.getText();
    }

    @Step("Get the current Month Status expenses in Decimal format")
    public BigDecimal getCurrentMonthStatusExpensesDec() {
        return new BigDecimal(getCurrentMonthStatusExpenses().replace("$", "").replace(",", ""));
    }

    @Step("Get the current month status net")
    public String getCurrentMonthStatusNet() {
        return currentMonthStatusNet.getText();
    }

    @Step("Get the current month status net in decimal format")
    public BigDecimal getCurrentMonthStatusNetDec() {
        return new BigDecimal(getCurrentMonthStatusNet().replace("$", "").replace(",", ""));
    }

    @Step("Check the current month status calculations")
    public boolean isCurrentMonthStatusCalculationCorrect(D3User user) {
        String incomeAmountStr = DatabaseUtils.getSumOfUsersCreditTransactions(user);
        String expenseAmountStr = DatabaseUtils.getSumOfUsersDebitTransactions(user);

        if (incomeAmountStr == null || incomeAmountStr.isEmpty() || expenseAmountStr == null || expenseAmountStr.isEmpty()) {
            logger.error("Error getting the sum amounts: income amount: {}, expense Amount: {}", incomeAmountStr, expenseAmountStr);
            return false;
        }

        BigDecimal incomeAmount = new BigDecimal(incomeAmountStr);
        BigDecimal expenseAmount = new BigDecimal(expenseAmountStr);
        BigDecimal availableCash = incomeAmount.subtract(expenseAmount);

        BigDecimal incomeDisplayedInWidget = getCurrentMonthlyIncomeDec();
        BigDecimal expensesDisplayedInWidget = getCurrentMonthStatusExpensesDec();
        BigDecimal availableCashDisplayedInWidget = getCurrentMonthStatusNetDec();

        return compareCurrency(incomeDisplayedInWidget, incomeAmount) &&
            compareCurrency(expensesDisplayedInWidget, expenseAmount) &&
            compareCurrency(availableCashDisplayedInWidget, availableCash);

    }

    @Step("Check if the recent transaction accounts are correct")
    public boolean areRecentTransactionAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, recentTransactionAccount, Element::getText);
    }

    /**
     * Returns all the values in the dashboard current month widget
     *
     * @return a List with the order: {CurrentMonthlyIncome, CurrentMonthStatusExpenses, CurrentMonthStatusNet}
     */
    @Step("Get the current month information")
    public List<BigDecimal> getCurrentMonthInfo() {
        List<BigDecimal> values = new ArrayList<>();
        values.add(parseCurrency(getCurrentMonthlyIncome()));
        values.add(parseCurrency(getCurrentMonthStatusExpenses()));
        values.add(parseCurrency(getCurrentMonthStatusNet()));
        return values;
    }

    /**
     * Returns a string with the balance displayed in the dashboard for the set account
     *
     * @param a string with the name account
     * @return a String with the account balance
     */
    @CheckForNull
    @Step("Get the account balance for {accountName}")
    public String getAccountBalance(String accountName) {
        //TODO find a better locator for this
        By by = By.xpath("//div[div[contains(.,'" + accountName + "')]]/div[contains(@class,'balance')]//span");
        try {
            return new D3Element(driver.findElement(by)).getText();
        } catch (NoSuchElementException e) {
            logger.warn("The account was not found");
            return null;
        }

    }

    @Step("Check if the dashboard page content is correct")
    public boolean isDashboardPageContentIsCorrect() {
        //TODO Include DashboardL10N.Localization.ZELLE_WIDGET to text check once Zelle is available
        String errorMsg = "Dashboard content %s was not found on the DOM";
        try {
            checkIfTextDisplayed(DashboardL10N.Localization.CASHFLOW_STATUS.getValue(), errorMsg);
            checkIfTextDisplayed(DashboardL10N.Localization.DEPOSIT_ACCOUNTS.getValue(), errorMsg);
            checkIfTextDisplayed(DashboardL10N.Localization.PAY_TRANSFER_WIDGET.getValue(), errorMsg);
        } catch (TextNotDisplayedException e) {
            logger.warn("Dashboard page content was not correct: ", e);
            return false;
        }
        return true;
    }
}
