package com.d3.pages.consumer.planning;

import static com.d3.helpers.DateAndCurrencyHelper.parseCurrency;

import com.d3.helpers.RandomHelper;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Budget extends PlanningBasePage {
    @FindBy(id = "createBudgetButton")
    private Element createBudget;

    @FindBy(css = "button.deleteBudget")
    private Element deleteBudget;

    @FindBy(css = "button.resetBudget")
    private Element resetBudget;

    @FindBy(className = "progress-bar")
    private Element progressBar;

    @FindBy(css = "div.amount.budgeted-amount")
    private Element budgetedAmount;

    @FindBy(css = "div.amount.difference-amount")
    private Element budgetDifferenceAmount;

    @FindBy(xpath = "//button[contains(text(), 'Continue')]")
    private Element confirmButton;

    @FindBy(id = "budgetEndDate")
    private Element budgetEndDate;

    @FindBy(id = "budgetBeginDate")
    private Element budgetStartDate;

    @FindBy(css = ".input-group-addon")
    private Element budgetCalendar;

    @FindBy(css = "div.datepicker-months")
    private Element widgetCalendar;

    @FindBy(css = ".month")
    private List<Element> months;

    @FindBy(css = "div.expense-categories input.form-control")
    private List<Element> budgetExpenseInputs;

    @FindBy(css = "button.cancel")
    private Element cancelButton;


    public Budget(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Budget me() {
        return this;
    }

    @Step("Click Create budget button")
    public Budget clickCreateBudget() {
        createBudget.click();
        return this;
    }

    @Step("Click the delete budget button")
    public Budget clickDeleteBudget() {
        deleteBudget.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the confirm button")
    public Budget clickConfirmButton() {
        confirmButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the reset budget button")
    public Budget clickResetBudget() {
        resetBudget.click();
        waitForSpinner();
        return this;
    }

    @Step("Get the budgeted amount")
    public String getBudgetedAmount() {
        return budgetedAmount.getText();
    }

    @Step("Get the progress bar amount")
    public String getProgressBarAmount() {
        return progressBar.getText();
    }

    @Step("Get the budget difference")
    public String getBudgetDifference() {
        return budgetDifferenceAmount.getText();
    }

    @Step("Enter the expense {text} into a random category")
    public String enterExpenseIntoRandomCategory(String text) {
        Element input = RandomHelper.getRandomElementFromList(budgetExpenseInputs);
        String returnValue = input.getValueAttribute();
        input.sendKeys(text);
        waitForSpinner();
        return returnValue.replace("$", "");
    }

    /**
     * Selecting the month on budget calendar
     */
    @Step("Select the start month ({month}) on the budget calendar")
    public Budget selectStartDateBudget(String month) {
        budgetCalendar.click();
        for (WebElement cell : months) {
            if (cell.getText().equals(month)) {
                cell.click();
                waitForSpinner();
                break;
            }
        }
        return this;
    }


    private double getAmountBudget() {
        return Double.valueOf(getProgressBarAmount().replace("$", "").replace(",", ""));

    }

    /**
     * Comparing budget amount after to hide the account with initial amount
     *
     * @return boolean, If the amount does not include hidden account values return True, otherwise False
     */
    @Step("Check if vlaues are not included")
    public boolean valuesAreNotIncluded(double totalTransaction, double transactionHideAccount) {
        // Note(JMoravec): I have no idea what this is doing/for, probably should remove/update this
        // TODO: ^^
        DecimalFormat df = new DecimalFormat("#.##");
        double difference = Double.parseDouble(df.format((totalTransaction - transactionHideAccount)));
        return getAmountBudget() <= difference;
    }

    /**
     * Returns the values in the budget widget
     *
     * @return a List with the order: {budgetAmount, budgetDifference}
     */
    @Step("Get the budget info")
    public List<BigDecimal> getBudgetInfo() {
        List<BigDecimal> values = new ArrayList<>();
        values.add(parseCurrency(getBudgetedAmount()));
        values.add(parseCurrency(getBudgetDifference()));
        return values;
    }

    @Step("Click the cancel button")
    public Budget clickCancelButton() {
        cancelButton.click();
        return this;
    }

}
