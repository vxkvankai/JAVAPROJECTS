package com.d3.pages.consumer.planning.goals.base;

import static com.d3.helpers.AccountHelper.verifyAccounts;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.pages.consumer.planning.GoalsPage;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public abstract class GoalForm<T extends GoalForm, R extends D3Goal> extends PageObjectBase {

    @Override
    protected abstract T me();

    @FindBy(id = "name")
    private Element goalName;

    @FindBy(id = "startDate")
    private Element startDate;

    @FindBy(id = "targetAmount")
    private Element goalAmount;

    @FindBy(css = "button.cancel-financial-goal")
    private Element cancelGoal;

    @FindBy(css = "button.save-financial-goal")
    private Element saveGoal;

    @FindBy(id = "rateOfReturn")
    private Element rateOfReturn;

    @FindBy(css = "input.includeInGoal")
    private List<CheckBox> fundingAccountCheckbox;

    @FindBy(css = "div.goal-account-name")
    private List<Element> fundingAccounts;
    
    public GoalForm(WebDriver driver) {
        super(driver);
    }

    public T enterGoalName(String name) {
        goalName.sendKeys(name);
        return me();
    }

    public T enterGoalStartDate(String date) {
        startDate.sendKeys(date);
        return me();
    }

    public GoalsPage clickSaveGoalButton() {
        saveGoal.click();
        return GoalsPage.initialize(driver, GoalsPage.class);
    }

    public T clickCancelGoalButton() {
        cancelGoal.click();
        return me();
    }

    public T enterGoalAmount(String amount) {
        goalAmount.sendKeys(amount);
        return me();
    }

    public T selectFundingAccount(D3Account account) {
        log.info("Selecting funding account {} for user goal", account.getName());
        for (int x = 0; x < fundingAccounts.size(); x++) {
            if (fundingAccounts.get(x).getText().contains(account.getName())) {
                fundingAccountCheckbox.get(x).check();
                break;
            }
        }
        return me();
    }

    /**
     * Returns true if the given account is funding the goal, false if not funding or not found
     *
     * @param account to check against the goal
     */
    public boolean isFundingAccountSelected(D3Account account) {
        for (Element checkbox : fundingAccounts) {
            if (checkbox.getText().contains(account.getName())) {
                return fundingAccountCheckbox.get(fundingAccounts.indexOf(checkbox)).isChecked();
            }
        }
        log.warn("Never found a checkbox for account: {}", account);
        return false;
    }

    public T enterRateOfReturn(String expectedRate) {
        rateOfReturn.sendKeys(expectedRate);
        return me();
    }

    public abstract GoalForm fillOutForm(D3Goal goal);

    protected T fillOutFormCommon(D3Goal goal) {
        goal.getFundingAccounts().keySet().forEach(this::selectFundingAccount);
        //NOTE (jmarshall) keep these steps in order
        enterGoalName(goal.getName());
        enterGoalStartDate(goal.getStartDateFormatted());
        enterGoalAmount(goal.getTargetAmountStr());
        enterRateOfReturn(Integer.toString(goal.getRateOfReturn()));
        return me();
    }
    
    public abstract boolean isGoalInformationCorrect(R goal);

    public boolean areFundingAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, fundingAccounts, Element::getText);
    }

    public abstract boolean fieldValidationMessagesDisplay();
    
    public abstract boolean requiredFieldMessagesDisplay();
}
