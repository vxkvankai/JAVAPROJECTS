package com.d3.pages.consumer.dashboard;

import static com.d3.helpers.DateAndCurrencyHelper.parseCurrency;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.planning.Budget;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DashboardPlan extends BaseConsumerPage {

    private static final String NON_DIGITS = "[^0-9]";

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
        waitForSpinner();
        return Budget.initialize(driver, Budget.class);
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

    @Step("Check if the net worth calculations are correct")
    public boolean isNetWorthCalculationCorrect(D3User user) {
        DecimalFormat decimalFormat = new DecimalFormat("###.##");

        long totalAssets = Math.round(Double.parseDouble((
            Optional.ofNullable(DatabaseUtils.getSumOfUsersAssetAccounts(user)).filter(sum -> !sum.isEmpty())
                .orElse("0.00"))));

        long totalLiabilities = Math.round(Double.parseDouble(
            Optional.ofNullable(DatabaseUtils.getSumOfUsersLiabilityAccounts(user)).filter(sum -> !sum.isEmpty())
                .orElse("0.00")));

        String totalAssetsFromDatabase = StringUtils.substringBefore(String.valueOf(totalAssets), ".").trim();
        String totalLiabilitiesFromDatabase = StringUtils.substringBefore(String.valueOf(totalLiabilities), ".").trim();
        String netWorthFromDatabase = String.valueOf(decimalFormat.format(totalAssets - totalLiabilities)).replaceAll(NON_DIGITS, "").trim();

        String totalAssetsDisplayedInWidget = getNetWorthAssets().replaceAll(NON_DIGITS, "").trim();
        String totalLiabilitiesDisplayedInWidget = getNetWorthLiabilities().replaceAll(NON_DIGITS, "").trim();
        String netWorthDisplayedInWidget = getNetWorthTotal().replaceAll(NON_DIGITS, "").trim();

        try {
            logger.info("Checking if the Net Worth Assets Total displayed on the UI {} matches the database value", totalAssetsDisplayedInWidget);
            checkIfTextEquals(totalAssetsDisplayedInWidget, totalAssetsFromDatabase);

            logger.info("Checking if the Net Worth Liabilities Total displayed on the UI {} matches the database value",
                totalLiabilitiesDisplayedInWidget);
            checkIfTextEquals(totalLiabilitiesDisplayedInWidget, totalLiabilitiesFromDatabase);

            logger.info("Checking if the Net Worth total displayed on the UI {} matches the database value", netWorthDisplayedInWidget);
            checkIfTextEquals(netWorthDisplayedInWidget, netWorthFromDatabase);
        } catch (TextNotDisplayedException e) {
            logger.warn("Dashboard widget did not validate: ", e);
            return false;
        }

        return true;
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
    
   
    
}
