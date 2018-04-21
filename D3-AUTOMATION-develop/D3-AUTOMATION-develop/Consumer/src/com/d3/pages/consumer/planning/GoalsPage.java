package com.d3.pages.consumer.planning;

import com.d3.datawrappers.goals.D3Goal;
import com.d3.l10n.planning.GoalsL10N;
import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class GoalsPage extends BaseConsumerPage {

    @FindBy(css = "div.add-financial-goal button")
    private Element addGoalButton;

    @FindBy(css = "li.financialGoalItem")
    private List<Element> userGoal;

    @FindBy(css = "button.edit-financial-goal")
    private Element editGoalButton;

    @FindBy(css = "button.delete-financial-goal")
    private Element deleteGoalButton;

    @FindBy(css = "button.submit-one")
    private Element deleteGoalConfirm;
    
    @FindBy(css = "button.cancel")
    private Element deleteGoalCancel;

    private D3Element newGoalDropdownSelect(String goalType) {
        return new D3Element(driver.findElement(By.linkText(goalType)));
    }

    public GoalsPage clickAddGoalButton() {
        addGoalButton.click();
        return this;
    }

    public GoalsPage expandGoal(D3Goal goal) {
        getElementInListByTextContains(userGoal, goal.getName()).click();
        return this;
    }
    
    /**
     * Click the Edit Goal button (goal needs to have been expanded first)
     *
     * @param goal Goal to edit (needed to return the correct form page)
     */
    public GoalForm clickEditGoalButton(D3Goal goal) {
        editGoalButton.click();
        return goal.getGoalForm(driver);
    }

    /**
     * Click the Delete Goal button (goal needs to have been expanded first)
     */
    public GoalsPage clickDeleteGoalButton() {
        deleteGoalButton.click();
        waitUntilTextPresent(GoalsL10N.Localization.DELETE_TITLE.getValue());
        waitUntilTextPresent(GoalsL10N.Localization.DELETE_TEXT.getValue());
        return this;
    }

    public GoalsPage deleteGoalConfirm() {
        deleteGoalConfirm.click();
        return this;
    }
    
    public GoalsPage deleteGoalCancel() {
        deleteGoalCancel.click();
        return this;
    }

    public GoalsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected GoalsPage me() {
        return this;
    }

    public GoalForm addGoal(D3Goal goal) {
        return clickAddGoalButton().selectGoalFromAddGoalDropdown(goal);
    }

    /**
     * Select the correct goal type from the add goal dropdown
     *
     * @param goal Will select the same type as the given goal
     */
    public GoalForm selectGoalFromAddGoalDropdown(D3Goal goal) {
        newGoalDropdownSelect(goal.getType().toString()).click();
        return goal.getGoalForm(driver);
    }

    public boolean isGoalInformationCorrect(D3Goal goal) {
        return goal.getGoalForm(driver).isGoalInformationCorrect(goal);
    }
}
