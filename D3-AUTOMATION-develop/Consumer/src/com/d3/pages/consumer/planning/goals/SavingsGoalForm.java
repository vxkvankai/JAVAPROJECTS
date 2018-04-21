package com.d3.pages.consumer.planning.goals;

import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.SavingsGoal;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.common.CommonL10N;
import com.d3.l10n.planning.GoalsL10N;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import com.d3.support.internal.Element;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class SavingsGoalForm extends GoalForm<SavingsGoalForm, SavingsGoal> {

    @FindBy(id = "targetDate")
    private Element endDateField;

    public SavingsGoalForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected SavingsGoalForm me() {
        return this;
    }

    public SavingsGoalForm enterEndDate(String date) {
        endDateField.sendKeys(date);
        return this;
    }

    public String getRandomEndDate(String currentDate) {
        String currentYear = StringUtils.substringAfterLast(currentDate, "/");
        return StringUtils.replace(currentDate, currentYear,
                Integer.toString(getRandomNumberInt(Integer.parseInt(currentYear), Integer.parseInt(currentYear) + 10)));
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
            checkIfTextDisplayed(String.format(GoalsL10N.Localization.RATE_OF_RETURN_DETAIL.getValue(), Integer.toString(goal.getRateOfReturn())), errorMsg,
                    "Rate Of Return");
            checkIfTextDisplayed(goal.getTargetDateFormatted(), errorMsg, "Goal End Date");
        } catch (TextNotDisplayedException e) {
            logger.warn("Goal Information not correct: ", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean requiredFieldMessagesDisplay(){
        String errorMsg = "Validation message is not displayed";
        try{
        checkIfTextDisplayed(GoalsL10N.Localization.GOAL_NAME_REQUIRED.getValue(),errorMsg);
        checkIfTextDisplayed(GoalsL10N.Localization.END_DATE_REQUIRED.getValue(),errorMsg);
        checkIfTextDisplayed(GoalsL10N.Localization.GOAL_AMOUNT_REQUIRED.getValue(),errorMsg);
        } catch (TextNotDisplayedException e) {
            logger.warn("Goal Information not correct: ", e);
            return false;
        }
        return true;
    }
    
    @Override
    public boolean fieldValidationMessagesDisplay() {
        String errorMsg = "Validation message is not displayed";
        try {
            checkIfTextDisplayed(GoalsL10N.Localization.GOAL_AMOUNT_RANGE.getValue(), errorMsg);
            checkIfTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue(), errorMsg);
            checkIfTextDisplayed("Enter a value between 0 and 99.99",
                    errorMsg); //NOTE (jmarshall) No L10N for Rate of Return field validation currently (12/20)
        } catch (TextNotDisplayedException e) {
            logger.warn("Goal Information not correct: ", e);
            return false;
        }
        return true;
    }
}
