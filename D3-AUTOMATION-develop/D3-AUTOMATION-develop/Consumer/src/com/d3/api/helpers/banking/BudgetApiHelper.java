package com.d3.api.helpers.banking;

import com.d3.api.mappings.planning.Budget;
import com.d3.api.mappings.planning.BudgetCategory;
import com.d3.api.mappings.planning.Goals;
import com.d3.api.services.BankingService;
import com.d3.datawrappers.CategoryType;
import com.d3.datawrappers.alerts.BudgetCategoryThresholdAlert;
import com.d3.datawrappers.alerts.TotalBudgetThresholdAlert;
import com.d3.datawrappers.alerts.enums.BudgetThreshold;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BudgetApiHelper extends D3BankingApi {
    private static final String ERROR_MSG = "Error";
    private static final String DEFAULT_BUDGET_NAME = "Default Budget";

    public BudgetApiHelper(String baseUrl) {
        super(baseUrl);
    }

    public BudgetApiHelper(D3BankingApi existingApi) {
        super(existingApi);
    }

    public D3BankingApi addGoal(D3User user, D3Goal goal) throws D3ApiException {
        log.info("Attempting to add {} goal for user {}", goal, user.getLogin());
        BankingService service = retrofit.create(BankingService.class);
        Goals newGoal = new Goals(user, goal);
        Call<Void> addGoal = service.addGoal(newGoal);
        try {
            Response<Void> response = addGoal.execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException(String.format("Creating %s goal for %s failed", goal, user.getLogin()));
        }

        log.info("Creating {} goal for {} was successful", goal, user.getLogin());
        return this;
    }

    /**
     * Creates budget for logged in user
     *
     * @return D3BankingApi
     * @throws D3ApiException Thrown when the issue creating budget
     */
    public D3BankingApi createBudget() throws D3ApiException {
        log.info("Attempting to create budget for user"); //NOSONAR
        BankingService service = retrofit.create(BankingService.class);
        Call<Void> createBudget = service.createBudget(new JsonObject());
        try {
            Response<Void> response = createBudget.execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Creating budget failed for user");
        }

        log.info("Creating budget for user was successful"); //NOSONAR
        return this;
    }

    /**
     * Gets list of budgets for logged in user
     *
     * @return List of Budgets
     * @throws D3ApiException Thrown if issue retrieving list of budgets
     */
    public List<Budget> getBudgets() throws D3ApiException {
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<List<Budget>> budgetResponse = service.getBudgets().execute();
            checkFor200(budgetResponse);
            return budgetResponse.body();
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error getting budgets");
        }
    }

    /**
     * Gets List of budgets and budget categories for current budget period of a logged in user
     *
     * @return List of Budgets
     * @throws D3ApiException Thrown if issue retrieving list of budgets for current period
     */
    public List<Budget> getCurrentBudgetPeriods() throws D3ApiException {
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<List<Budget>> budgetResponse = service.getCurrentBudgetPeriods().execute();
            checkFor200(budgetResponse);
            return budgetResponse.body();
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error getting current budget periods");
        }
    }

    /**
     * Method will update Budget Category budget amount to value that would trigger BudgetCategoryThresholdAlert
     *
     * @param alert BudgetCategoryThresholdAlert to get data from
     * @return D3BankingApi
     * @throws D3ApiException Thrown if issue updating budget category
     */
    public D3BankingApi updateBudgetCategoryToTriggerAlert(BudgetCategoryThresholdAlert alert) throws D3ApiException {
        log.info("Attempting to update budget named: {}", DEFAULT_BUDGET_NAME);
        List<Budget> budgets = getBudgets();
        Budget currentBudgetPeriod = getCurrentBudgetPeriods().get(0);
        if (budgets == null || budgets.isEmpty()) {
            throw new D3ApiException("No budgets created for user");
        }

        Budget budgetToEdit = null;
        for (Budget budget : budgets) {
            if (budget.getName().equalsIgnoreCase(DEFAULT_BUDGET_NAME)) {
                budgetToEdit = budget;
                break;
            }
        }

        if (budgetToEdit == null) {
            throw new D3ApiException(String.format("No Budget created for user that matches name: %s in list: %s", DEFAULT_BUDGET_NAME, budgets));
        }

        BudgetCategory budgetCategoryToEdit = null;
        for (BudgetCategory budgetCategory : currentBudgetPeriod.getBudgetCategories()) {
            if (budgetCategory.getCategory().getId() == alert.getCategoryId()) {
                budgetCategoryToEdit = budgetCategory;
                break;
            }
        }

        if (budgetCategoryToEdit == null) {
            throw new D3ApiException(String.format("No Category found in user's budget that matches id: %s", alert.getCategoryId()));
        }

        budgetCategoryToEdit.setBudgetAmount(amountThatWillTriggerAlert(budgetCategoryToEdit, alert));

        log.info("Attempting to edit category {}", budgetCategoryToEdit.getCategory().getName());
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<BudgetCategory> response = service.updateCurrentPeriodBudgetCategory(budgetToEdit.getId(), budgetCategoryToEdit.getId(), budgetCategoryToEdit).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error updating budget category");
        }

        log.info("Budget Category: {} updated successfully", budgetCategoryToEdit.getCategory().getName());
        return this;
    }

    /**
     * Method will update Total Budget budget expense amount to value that would trigger TotalBudgetThresholdAlert
     *
     * @param alert TotalBudgetThresholdAlert to get data from
     * @return D3BankingApi
     * @throws D3ApiException Thrown if issue updating total budget expenses
     */
    public D3BankingApi updateTotalBudgetToTriggerAlert(TotalBudgetThresholdAlert alert) throws D3ApiException {
        log.info("Attempting to update budget named: {}", DEFAULT_BUDGET_NAME);
        List<Budget> budgets = getBudgets();
        Budget currentBudgetPeriod = getCurrentBudgetPeriods().get(0);
        if (budgets == null || budgets.isEmpty()) {
            throw new D3ApiException("No budgets created for user");
        }

        Budget budgetToEdit = null;
        for (Budget budget : budgets) {
            if (budget.getName().equalsIgnoreCase(DEFAULT_BUDGET_NAME)) {
                budgetToEdit = budget;
                break;
            }
        }

        if (budgetToEdit == null) {
            throw new D3ApiException(String.format("No Budget created for user that matches name: %s in list: %s", DEFAULT_BUDGET_NAME, budgets));
        }

        BankingService service = retrofit.create(BankingService.class);
        List<BudgetCategory> expenseCategories = getBudgetCategoriesWithActualAmountGreaterThanZero(currentBudgetPeriod, CategoryType.getExpenseCategoryTypes());
        for (BudgetCategory budgetCategory : expenseCategories) {
            budgetCategory.setBudgetAmount(amountThatWillTriggerAlert(currentBudgetPeriod, alert));
            try {
                Response<BudgetCategory> response = service.updateCurrentPeriodBudgetCategory(budgetToEdit.getId(), budgetCategory.getId(), budgetCategory).execute();
                checkFor200(response);
            } catch (IOException | D3ApiException e) {
                log.error(ERROR_MSG, e);
                throw new D3ApiException("Error updating budget category");
            }
        }

        log.info("Total Budget Threshold for {} was updated successfully", currentBudgetPeriod.getName());
        return this;
    }


    /**
     * Method will get the amount that the given BudgetCategory budgetAmount needs to set at in order to trigger BudgetCategoryThresholdAlert alert
     *
     * @param budgetCategory BudgetCategory Alert
     * @param alert BudgetCategoryThresholdAlert to get data from
     * @return Double value to set category budgeted amount as
     */
    private Double amountThatWillTriggerAlert(BudgetCategory budgetCategory, BudgetCategoryThresholdAlert alert) {
        if (alert.getThresholdValue() == BudgetThreshold.Value.AMOUNT) {
            if (alert.getThresholdType() == BudgetThreshold.Type.APPROACH) {
                return budgetCategory.getActualAmount() + ((Double) alert.getThreshold() - 1);
            } else {
                return budgetCategory.getActualAmount() - ((Double) alert.getThreshold() + 1);
            }
        } else {
            if (alert.getThresholdType() == BudgetThreshold.Type.EXCEED) {
                return budgetCategory.getActualAmount() * (1 - ((Double.valueOf(alert.getThreshold().toString()) + 1) / 100));
            } else {
                return budgetCategory.getActualAmount() / ((Double.valueOf(alert.getThreshold().toString()) + 1) / 100);
            }

        }
    }

    /**
     * Method will get the amount that the given Budget budgetAmount needs to set at in order to trigger TotalBudgetThresholdAlert alert
     *
     * @param currentPeriodBudget Current Period Budget to get categories and amounts from
     * @param alert TotalBudgetThresholdAlert to get data from
     * @return Double value to set Total Budget amount as
     */
    private Double amountThatWillTriggerAlert(Budget currentPeriodBudget, TotalBudgetThresholdAlert alert) {
        if (alert.getThresholdType() == BudgetThreshold.Type.EXCEED) {
            return 0.01;
        } else {
            List<BudgetCategory> expenseCategories = getBudgetCategoriesWithActualAmountGreaterThanZero(currentPeriodBudget, CategoryType.getExpenseCategoryTypes());
            if (alert.getThresholdValue() == BudgetThreshold.Value.AMOUNT) {
                return (currentPeriodBudget.getActualExpense() + ((Double) alert.getThreshold() - 1)) / expenseCategories.size();
            } else {
                return (currentPeriodBudget.getActualExpense() / ((Double.valueOf(alert.getThreshold().toString()) + 1) / 100)) / expenseCategories.size();
            }
        }
    }


    /**
     * Will return List of Budget Categories with type matching given categoryType and actual amount above 0
     *
     * @param currentPeriodBudget Budget of current period to get budget categories from
     * @param categoryType Category Types to filter by (EXPENSE vs INCOME)
     * @return List of Budget Categories
     */
    private List<BudgetCategory> getBudgetCategoriesWithActualAmountGreaterThanZero(Budget currentPeriodBudget, List<CategoryType> categoryType) {
        return currentPeriodBudget.getBudgetCategories().stream()
            .filter(budgetCategory -> categoryType.toString().contains(budgetCategory.getCategory().getType()) && budgetCategory.getActualAmount() > 0)
            .collect(Collectors.toList());

    }
}
