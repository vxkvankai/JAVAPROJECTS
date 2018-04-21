package com.d3.pages.consumer.dashboard;

import static com.d3.helpers.DateAndCurrencyHelper.parseCurrency;

import com.d3.datawrappers.account.D3Account;
import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.SingleTransferForm;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;

@Slf4j
public class Dashboard extends BaseConsumerPage {

    @FindBy(xpath = "//a[@href='#dashboard/plan']")
    private Element planButton;

    @FindBy(xpath = "//a[@data-type='pay-transfer']")
    private Element payTransferButton;

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

    @FindBy(xpath = "//a[@href='#dashboard/manage']")
    private Element manageButton;

    @FindBy(css = "article.networth")
    private Element netWorthWidget;

    @FindBy(css = "article.cashflow")
    private Element currentMonthStatusWidget;

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
        return DashboardPlan.initialize(driver, DashboardPlan.class);
    }

    @Step("Click the Manage Button")
    public Dashboard clickManageButton() {
        manageButton.click();
        return this;
    }

    @Step("Click the Pay Transfer Tab")
    public SingleTransferForm clickPayTransferButton() {
        payTransferButton.click();
        return SingleTransferForm.initialize(driver, SingleTransferForm.class);
    }

    @Step("Click the accounts link for {account}")
    public TransactionsPage clickAccountLink(D3Account account) {
        getElementInListByTextContains(accountLinks, account.getName()).click();
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
            log.warn("The account was not found", e);
            return null;
        }
    }
}
