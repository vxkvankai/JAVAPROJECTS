package com.d3.tests.consumer.core.planning;

import static com.d3.helpers.DateAndCurrencyHelper.convertMoneyAndSubtract;

import com.d3.datawrappers.user.D3User;
import com.d3.helpers.DateAndCurrencyHelper;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.planning.BudgetL10N;
import com.d3.pages.consumer.planning.Budget;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Epic("Planning")
@Feature("Budget")
@Slf4j
public class BudgetTests extends PlanningTestBase {

    @TmsLink("288102")
    @Story("Deleting Budget")
    @Test(dataProvider = "Basic User With Budget")
    public void verifyDeletingBudget(D3User user) {
        Budget budget = login(user).getHeader()
            .clickPlanningButton()
            .clickDeleteBudget();
        try {
            Assert.assertTrue(budget.isTextDisplayed(BudgetL10N.Localization.DELETE_LABEL.getValue()));
        } catch (AssertionError e) {
            log.warn("Error clicking delete budget button", e);
            budget.clickDeleteBudget();
            Assert.assertTrue(budget.isTextDisplayed(BudgetL10N.Localization.DELETE_LABEL.getValue()));
        }

        budget.clickConfirmButton();
        Assert.assertTrue(budget.isTextDisplayed("Reach your financial goals faster, save money or reduce your debt by creating a budget"));
    }

    @TmsLink("288101")
    @Story("Creating Budget")
    @Test(dataProvider = "Basic User")
    public void verifyCreatingBudgetPlanning(D3User user) throws ParseException {
        Budget budget = login(user).getHeader()
            .clickPlanningButton()
            .clickCreateBudget();
        Assert.assertTrue(convertMoneyAndSubtract(budget.getBudgetedAmount(), budget.getProgressBarAmount(), budget.getBudgetDifference()),
            String.format("%s - %s != %s", budget.getBudgetedAmount(), budget.getProgressBarAmount(), budget.getBudgetDifference()));
    }


    @TmsLink("523097")
    @Story("Budget E2E")
    @Test(dataProvider = "Basic User")
    public void verifyBudgetUpdates(D3User user) {
        String newBudgetValue = RandomHelper.getRandomCurrencyValue(0, 50);

        Budget budget = login(user).getHeader()
            .clickPlanningButton()
            .clickCreateBudget();

        List<BigDecimal> originalBudgetInfo = budget.getBudgetInfo();
        String previousAmount = budget.enterExpenseIntoRandomCategory(newBudgetValue);
        BigDecimal totalNewValue = new BigDecimal(newBudgetValue).add(new BigDecimal(previousAmount));

        List<BigDecimal> newBudgetInfo = budget.getBudgetInfo();

        Assert.assertTrue(DateAndCurrencyHelper.compareCurrency(newBudgetInfo.get(0), originalBudgetInfo.get(0).add(totalNewValue)),
            String.format("Old budget info (plus new value) %s did not equal new budget info: %s",
                originalBudgetInfo.get(0).add(totalNewValue), newBudgetInfo.get(0)));
        Assert.assertTrue(DateAndCurrencyHelper.compareCurrency(newBudgetInfo.get(1), originalBudgetInfo.get(1).add(totalNewValue)),
            String.format("Old budget info (plus new value) %s did not equal new budget info: %s",
                originalBudgetInfo.get(1).add(totalNewValue), newBudgetInfo.get(1)));

        Budget newBudgetPage = budget.clickResetBudget()
            .clickConfirmButton();
        newBudgetPage.reloadPage();

        List<BigDecimal> resetValues = newBudgetPage.getBudgetInfo();

        Assert.assertTrue(DateAndCurrencyHelper.compareCurrency(resetValues.get(0), originalBudgetInfo.get(0)),
            String.format("Old budget info %s did not equal new budget info: %s", originalBudgetInfo.get(0), resetValues.get(0)));
        Assert.assertTrue(DateAndCurrencyHelper.compareCurrency(resetValues.get(1), originalBudgetInfo.get(1)),
            String.format("Old budget info %s did not equal new budget info: %s", originalBudgetInfo.get(1), resetValues.get(1)));

        budget.clickDeleteBudget()
            .clickCancelButton();

        List<BigDecimal> cancelValues = newBudgetPage.getBudgetInfo();
        Assert.assertEquals(resetValues.get(0), cancelValues.get(0));
        Assert.assertEquals(resetValues.get(1), cancelValues.get(1));

        Assert.assertTrue(DateAndCurrencyHelper.compareCurrency(resetValues.get(0), cancelValues.get(0)),
            String.format("Cancel info %s did not equal new budget info: %s", cancelValues.get(0), resetValues.get(0)));
        Assert.assertTrue(DateAndCurrencyHelper.compareCurrency(resetValues.get(1), cancelValues.get(1)),
            String.format("Cancel info %s did not equal new budget info: %s", cancelValues.get(1), resetValues.get(1)));
    }
}
