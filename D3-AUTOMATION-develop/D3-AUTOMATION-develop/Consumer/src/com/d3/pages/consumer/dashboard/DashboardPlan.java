package com.d3.pages.consumer.dashboard;

import static com.d3.helpers.DateAndCurrencyHelper.parseCurrency;

import com.d3.pages.consumer.planning.Budget;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DashboardPlan extends Dashboard {

    @FindBy(css = ".create-budget")
    private Element createBudgetButton;

    @FindBy(css = "div[class$='income'] div[class='amount']")
    private Element netWorthAssets;

    @FindBy(css = "div[class$='expenses'] div[class='amount']")
    private Element netWorthLiabilities;

    @FindBy(css = "div[class$='net'] div[class='amount']")
    private Element netWorthTotal;

    public DashboardPlan(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "a.create-goal-btn")
    private Element createGoalLink;


    @FindBy(css = "div.column-2")
    private Element goalsWidget;


    @FindBy(css = "article.budget-forecast")
    private Element budgetSummary;

    @FindBy(css = "article.budget-summary-charts")
    private Element cashSummary;


    @Override
    protected DashboardPlan me() {
        return this;
    }


    @Step("Click the create budget button")
    public Budget clickCreateBudgetButton() {
        createBudgetButton.click();
        return Budget.initialize(driver, Budget.class);
    }

    @Step("Click the create goal link")
    public Budget clickCreateGoalLink() {
        createGoalLink.click();
        return Budget.initialize(driver, Budget.class);
    }

    public boolean isGoalsWidgetDisplayed() {
        try {
            return goalsWidget.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error Finding Goals Widget", e);
            return false;
        }
    }

    @Step("Get the net worth assets")
    public String getNetWorthAssets() {
        return netWorthAssets.getText();
    }

    @Step("Get the net worth liabilities")
    public String getNetWorthLiabilities() {
        return netWorthLiabilities.getText();
    }

    @Step("Get the net worth total")
    public String getNetWorthTotal() {
        return netWorthTotal.getText();
    }

    /**
     * Returns all the values in the dashboard net worth widget
     *
     * @return a List with the order: {NetWorthAssets, NetWorthLiabilities, NetWorthTotal}
     */
    @Step("Get the net worth information")
    public List<BigDecimal> getNetWorthInfo() {
        List<BigDecimal> values = new ArrayList<>();
        values.add(parseCurrency(getNetWorthAssets()));
        values.add(parseCurrency(getNetWorthLiabilities()));
        values.add(parseCurrency(getNetWorthTotal()));
        return values;
    }

    public boolean isBudgetSummaryDisplayed() {
        try {
            return budgetSummary.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Error Finding Budget Summary Widget ");
            return false;
        }
    }

    public boolean isCashSummaryDisplayed() {
        try {
            return cashSummary.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Error Finding Cash Summary Widget");
            return false;
        }
    }

}






