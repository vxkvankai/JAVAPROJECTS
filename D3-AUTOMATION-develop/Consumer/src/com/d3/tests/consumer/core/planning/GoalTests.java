package com.d3.tests.consumer.core.planning;

import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.datawrappers.goals.SavingsGoal;
import com.d3.datawrappers.user.D3User;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import com.d3.pages.consumer.planning.GoalsPage;
import com.d3.pages.consumer.planning.goals.RetirementGoalForm;
import com.d3.pages.consumer.planning.goals.SavingsGoalForm;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Planning")
@Feature("Goals")
public class GoalTests extends PlanningTestBase {

    @TmsLink("288103")
    @Story("Add Retirement Goal")
    @Test(dataProvider = "Basic User")
    public void verifyAddingRetirementGoal(D3User user) {
        D3Goal goal = RetirementGoal.createRandomGoal(user);
        Dashboard dashboard = login(user);
        GoalsPage goals = dashboard.getHeader().clickPlanningButton().getTabs().clickGoalsLink()
                .addGoal(goal)
                .fillOutForm(goal)
                .clickSaveGoalButton()
                .expandGoal(goal);

        Assert.assertTrue(goals.isGoalInformationCorrect(goal));
    }

    @TmsLink("523104")
    @Story("Add Retirement Goal")
    @Test(dataProvider = "Basic User")
    public void verifyAddingGoalsFromDashboardPlan(D3User user) {
        D3Goal goal = RetirementGoal.createRandomGoal(user);
        Dashboard dashboard = login(user);
        GoalsPage goals = dashboard.clickPlanButton().clickCreateGoalLink().getTabs().clickGoalsLink()
                .addGoal(goal)
                .fillOutForm(goal)
                .clickSaveGoalButton()
                .expandGoal(goal);

        Assert.assertTrue(goals.isGoalInformationCorrect(goal));
        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, Audits.PLANNING_GOAL_ADD));

    }

    @TmsLink("523116")
    @Story("Excluded Accounts not available as Goal Funding Accounts")
    @Test(dataProvider = "Basic User with excluded existing accounts")
    public void verifyExcludedAccountsAreNotDisplayedOnPlanning(D3User user, D3Account account) {
        D3Goal goal = RetirementGoal.createRandomGoal(user);
        GoalForm goals = login(user).getHeader().clickPlanningButton().getTabs().clickGoalsLink()
                .addGoal(goal);
        Assert.assertFalse(goals.isTextPresent(account.getName()));
    }

    @TmsLink("523102")
    @Story("Add Retirement Goal")
    @Test(dataProvider = "Basic User")
    public void addRetirementGoalWithFieldValidation(D3User user) {
        RetirementGoal goal = RetirementGoal.createRandomGoal(user);
        RetirementGoal goalWithInvalidData = RetirementGoal.createRandomGoalWithInvalidData(user);

        Dashboard dashboard = login(user);
        RetirementGoalForm goalsForm = (RetirementGoalForm) dashboard.getHeader().clickPlanningButton().getTabs()
                .clickGoalsLink()
                .addGoal(goal)
                .enterGoalAmount(getRandomString(10));
        goalsForm.clickSaveGoalButton();
        Assert.assertTrue(goalsForm.requiredFieldMessagesDisplay());
        goalsForm.fillOutForm(goalWithInvalidData).clickSaveGoalButton();
        Assert.assertTrue(goalsForm.fieldValidationMessagesDisplay());

        goalsForm.fillOutForm(goal).clickSaveGoalButton().expandGoal(goal);
        Assert.assertTrue(goalsForm.isGoalInformationCorrect(goal));

        DashboardPlan planPage = dashboard.getHeader().clickDashboardButton().clickPlanButton();
        Assert.assertTrue(planPage.isTextPresent(goal.getName()));

        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, Audits.PLANNING_GOAL_ADD));
    }

    @TmsLink("523103")
    @Story("Add Savings Goal")
    @Test(dataProvider = "Basic User")
    public void addSavingsGoalWithFieldValidation(D3User user) {
        SavingsGoal goal = SavingsGoal.createRandomGoal(user);
        SavingsGoal goalWithInvalidData = SavingsGoal.createRandomGoalWithInvalidData(user);

        Dashboard dashboard = login(user);
        SavingsGoalForm goalsForm = (SavingsGoalForm) dashboard.getHeader().clickPlanningButton().getTabs()
                .clickGoalsLink()
                .addGoal(goal)
                .enterGoalAmount(getRandomString(10));
        goalsForm.clickSaveGoalButton();
        Assert.assertTrue(goalsForm.requiredFieldMessagesDisplay());
        goalsForm.fillOutForm(goalWithInvalidData).clickSaveGoalButton();
        Assert.assertTrue(goalsForm.fieldValidationMessagesDisplay());

        goalsForm.fillOutForm(goal)
                .clickSaveGoalButton()
                .expandGoal(goal);
        Assert.assertTrue(goalsForm.isGoalInformationCorrect(goal));

        DashboardPlan planPage = dashboard.getHeader().clickDashboardButton().clickPlanButton();
        Assert.assertTrue(planPage.isTextPresent(goal.getName()));

        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, Audits.PLANNING_GOAL_ADD));
    }

    @TmsLink("523205")
    @TmsLink("523209")
    @Story("Delete goal")
    @Test(dataProvider = "Basic User with Goals")
    public void verifyDeleteGoal(D3User user, @SuppressWarnings("unused") D3Account account, D3Goal goal) {
        GoalsPage goalsPage = login(user).getHeader().clickPlanningButton().getTabs()
                .clickGoalsLink()
                .expandGoal(goal)
                .clickDeleteGoalButton()
                .deleteGoalCancel();
        Assert.assertTrue(goalsPage.isTextPresent(goal.getName()), "Goal was deleted with cancel");

        goalsPage.clickDeleteGoalButton()
                .deleteGoalConfirm();
        Assert.assertFalse(goalsPage.isTextPresent(goal.getName()), "Goal was not deleted");
        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, Audits.PLANNING_GOAL_DELETE));
    }
}
