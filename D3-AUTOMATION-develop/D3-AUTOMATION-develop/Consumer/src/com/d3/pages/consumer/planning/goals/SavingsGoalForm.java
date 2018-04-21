package com.d3.pages.consumer.planning.goals;

import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.SavingsGoal;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.L10nCommon;
import com.d3.l10n.planning.GoalsL10N;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


/**
 * Page object for the Savings Goal Form see {@link GoalForm} for additional details
 */
@Slf4j
public class SavingsGoalForm extends GoalForm<SavingsGoalForm, SavingsGoal> {

    private static final String GOAL_ERROR = "Goal Information not correct:";

    @FindBy(id = "targetDate")
    private Element endDateField;

    /**
     * Constructor for Retirement Goal Form, don't use directly use
     * {@link org.openqa.selenium.support.PageFactory#initElements(WebDriver, Object)}
     *
     * @param driver Webdriver object
     */
    public SavingsGoalForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected SavingsGoalForm me() {
        return this;
    }

    /**
     * Enter the End date into the form
     *
     * @param date Text to input
     * @return this
     */
    public SavingsGoalForm enterEndDate(String date) {
        endDateField.sendKeys(date);
        return this;
    }

    public SavingsGoalForm fillOutForm(D3Goal goal) {
        SavingsGoal savGoal = (SavingsGoal) goal;
        fillOutFormCommon(savGoal);
        enterEndDate(savGoal.getTargetDateFormatted());
        return this;
    }


    @Override
    public boolean isGoalInformationCorrect(SavingsGoal goal) {
        String errorMsg = "%s: %s for Savings Goal was not found on the DOM";
        try {
            checkIfTextDisplayed(goal.getName(), errorMsg, "Goal Name");
            checkIfTextDisplayed(String.format(GoalsL10N.Localization.RATE_OF_RETURN_DETAIL.getValue(), Integer.toString(goal.getRateOfReturn())),
                errorMsg,
                "Rate Of Return");
            checkIfTextDisplayed(goal.getTargetDateFormatted(), errorMsg, "Goal End Date");
        } catch (TextNotDisplayedException e) {
            log.warn(GOAL_ERROR, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean requiredFieldMessagesDisplay() {
        String errorMsg = "Validation message is not displayed";
        try {
            checkIfTextDisplayed(GoalsL10N.Localization.GOAL_NAME_REQUIRED.getValue(), errorMsg);
            checkIfTextDisplayed(GoalsL10N.Localization.END_DATE_REQUIRED.getValue(), errorMsg);
            checkIfTextDisplayed(GoalsL10N.Localization.GOAL_AMOUNT_REQUIRED.getValue(), errorMsg);
        } catch (TextNotDisplayedException e) {
            log.warn(GOAL_ERROR, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean fieldValidationMessagesDisplay() {
        String errorMsg = "Validation message is not displayed";
        try {
            checkIfTextDisplayed(GoalsL10N.Localization.GOAL_AMOUNT_RANGE.getValue(), errorMsg);
            checkIfTextDisplayed(L10nCommon.Localization.CHARACTERS_NOT_ALLOWED.getValue(), errorMsg);
            checkIfTextDisplayed("Enter a value between 0 and 99.99",
                errorMsg); //NOTE (jmarshall) No L10N for Rate of Return field validation currently (12/20)
        } catch (TextNotDisplayedException e) {
            log.warn(GOAL_ERROR, e);
            return false;
        }
        return true;
    }
}
