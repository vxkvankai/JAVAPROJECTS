package com.d3.pages.consumer.planning.goals;

import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.L10nCommon;
import com.d3.l10n.planning.GoalsL10N;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the Retirement Goal Form see {@link GoalForm} for additional details
 */
@Slf4j
public class RetirementGoalForm extends GoalForm<RetirementGoalForm, RetirementGoal> {

    @FindBy(id = "retirementAge")
    private Element retirementAge;

    @FindBy(id = "dateOfBirth")
    private Element dateOfBirth;

    /**
     * Constructor for Retirement Goal Form, don't use directly use
     * {@link org.openqa.selenium.support.PageFactory#initElements(WebDriver, Object)}
     *
     * @param driver Webdriver object
     */
    public RetirementGoalForm(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter the Retirement age into the form
     *
     * @param age Text to input
     * @return this
     */
    public RetirementGoalForm enterRetirementAge(String age) {
        retirementAge.sendKeys(age);
        return this;
    }

    /**
     * Enter the birthdate into the form
     *
     * @param birthDate Text to input
     * @return this
     */
    public RetirementGoalForm enterBirthDate(String birthDate) {
        dateOfBirth.sendKeys(birthDate);
        return this;
    }

    @Override
    protected RetirementGoalForm me() {
        return this;
    }

    @Override
    public RetirementGoalForm fillOutForm(D3Goal goal) {
        RetirementGoal retGoal = (RetirementGoal) goal;
        fillOutFormCommon(retGoal);
        enterRetirementAge(Integer.toString(retGoal.getRetirementAge()));
        enterBirthDate(retGoal.getBirthDateFormatted());
        return this;
    }

    /**
     * Get the estimated retirement date from a goal
     *
     * @param goal Goal object to check
     * @return Date in format MM/dd/yyyy
     */
    // TODO: move this to D3Goal
    public String getEstimatedRetirementDate(D3Goal goal) {
        RetirementGoal retGoal = (RetirementGoal) goal;
        return DateTimeFormat.forPattern("MM/dd/yyyy")
            .print(retGoal.getBirthDate().withYear(retGoal.getRetirementAge() + retGoal.getBirthDate().getYear()));
    }

    @Override
    public boolean isGoalInformationCorrect(RetirementGoal goal) {
        String errorMsg = "%s: %s for Retirement Goal was not found on the DOM";

        try {
            checkIfTextDisplayed(goal.getName(), errorMsg, "Goal Name");
            checkIfTextDisplayed(String.format(GoalsL10N.Localization.RATE_OF_RETURN_DETAIL.getValue(), Integer.toString(goal.getRateOfReturn())),
                errorMsg, "Rate of Return");
            checkIfTextDisplayed(getEstimatedRetirementDate(goal), errorMsg, "Estimated Retirement Date");
        } catch (TextNotDisplayedException e) {
            log.warn("Goal Information not correct: ", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean requiredFieldMessagesDisplay() {
        String errorMsg = "Required field messaging (%s) for %s field was not found on the DOM";
        try {
            checkIfTextDisplayed(GoalsL10N.Localization.GOAL_NAME_REQUIRED.getValue(), errorMsg, "Goal Name");
            checkIfTextDisplayed(GoalsL10N.Localization.BIRTH_DATE_REQUIRED.getValue(), errorMsg, "Birth Date");
            checkIfTextDisplayed(GoalsL10N.Localization.GOAL_AMOUNT_REQUIRED.getValue(), errorMsg, "Goal Amount");
        } catch (TextNotDisplayedException e) {
            log.warn("Required messaging was not displayed correctly: ", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean fieldValidationMessagesDisplay() {
        String errorMsg = "FieldValidation message is not displayed";
        try {
            checkIfTextDisplayed(GoalsL10N.Localization.RETIREMENT_AGE_RANGE.getValue(), errorMsg);
            checkIfTextDisplayed(GoalsL10N.Localization.GOAL_AMOUNT_RANGE.getValue(), errorMsg);
            checkIfTextDisplayed(L10nCommon.Localization.CHARACTERS_NOT_ALLOWED.getValue(), errorMsg);
            checkIfTextDisplayed("Enter a value between 0 and 99.99",
                errorMsg); //NOTE (jmarshall) No L10N for Rate of Return field validation currently (12/20)
        } catch (TextNotDisplayedException e) {
            log.warn("Goal Information not correct: ", e);
            return false;
        }
        return true;
    }
}
