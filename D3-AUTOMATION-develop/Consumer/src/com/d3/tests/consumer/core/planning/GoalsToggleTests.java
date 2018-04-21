package com.d3.tests.consumer.core.planning;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.datawrappers.goals.SavingsGoal;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.l10n.planning.GoalsL10N;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.planning.GoalsPage;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import com.d3.tests.annotations.RunForUserTypes;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;


@Epic("Planning")
@Feature("Goals")
public class GoalsToggleTests extends PlanningTestBase {

    @Story("Available Funding Accounts for Goals")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyGoalFundingAccounts(D3User user) {
        D3Goal goal = user.getUserType().isBusinessType() ? SavingsGoal.createRandomGoal(user) : RetirementGoal.createRandomGoal(user);
        Dashboard dashboard = login(user);
        GoalForm goals = dashboard.getHeader().clickPlanningButton().getTabs().clickGoalsLink()
                .addGoal(goal);
        Assert.assertTrue(goals.areFundingAccountsCorrect(DatabaseUtils.getMaskedUserAccounts(user)));
    }

    @Story("Add Goal")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
            UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyAddingGoals(D3User user) {
        D3Goal goal = user.getUserType().isBusinessType() ? SavingsGoal.createRandomGoal(user) : RetirementGoal.createRandomGoal(user);
        Dashboard dashboard = login(user);
        GoalsPage goals = dashboard.getHeader().clickPlanningButton().getTabs().clickGoalsLink()
                .addGoal(goal)
                .fillOutForm(goal)
                .clickSaveGoalButton()
                .expandGoal(goal);
        Assert.assertTrue(goals.isGoalInformationCorrect(goal));
    }

    @Story("Edit Goal")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
            UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Goal")
    public void verifyEditingGoals(D3User user, D3Goal existingGoal) {
        D3Goal goal = user.getUserType().isBusinessType() ? SavingsGoal.createRandomGoal(user) : RetirementGoal.createRandomGoal(user);
        Dashboard dashboard = login(user);
        GoalsPage goals = dashboard.getHeader().clickPlanningButton().getTabs().clickGoalsLink().expandGoal(existingGoal).clickEditGoalButton(goal)
                .fillOutForm(goal)
                .clickSaveGoalButton();

        Assert.assertTrue(goals.isGoalInformationCorrect(goal));
    }

    @Flaky
    @Story("Delete Goal")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
            UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Goal")
    public void verifyDeletingGoals(D3User user, D3Goal goal) {
        Dashboard dashboard = login(user);
        GoalsPage goals = dashboard.getHeader()
                .clickPlanningButton()
                .getTabs()
                .clickGoalsLink()
                .expandGoal(goal)
                .clickDeleteGoalButton()
                .deleteGoalConfirm();

        Assert.assertTrue(goals.isTextPresent(GoalsL10N.Localization.NO_GOALS.getValue()));
    }
}

