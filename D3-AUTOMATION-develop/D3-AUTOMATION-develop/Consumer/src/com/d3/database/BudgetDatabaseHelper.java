package com.d3.database;

import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Budgets
 */
@Slf4j
public class BudgetDatabaseHelper {

    private BudgetDatabaseHelper() {
    }

    /**
     * Get Budget Id from the budget table for a specific user
     *
     * @param user D3User to get budget id for
     * @return Integer value of the id, null if error
     */
    @CheckForNull
    @Step("Get the budget id for {user}")
    public static Integer getBudgetId(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM budget WHERE user_id = (SELECT id FROM d3_user WHERE login_id = ?)";
            String result = utils.getDataFromSelectQuery("id", query, user.getLogin());
            if (result == null) {
                log.error("id came back null, {}", query);
                return null;
            }
            return Integer.valueOf(result);

        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a budget id", e);
            return null;
        }
    }

    /**
     * Get Budget Period Id from the budget table for a specific user and budgetId
     *
     * @param user D3User to get budget id for
     * @return Integer value of the id, null if error
     */
    @CheckForNull
    @Step("Get the budget period id for {user}'s budget}")
    public static Integer getBudgetPeriodId(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM budget_period WHERE budget_id = ?;";
            String result = utils.getDataFromSelectQuery("id", query, getBudgetId(user));
            if (result == null) {
                log.error("id came back null, {}", query);
                return null;
            }
            return Integer.valueOf(result);

        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a budget id", e);
            return null;
        }
    }

    /**
     * Get random category id from the specified user's budget where the actual_amount is greater than 0
     *
     * @param user D3User to get category id from
     * @return Integer value of the id, null if error
     */
    @CheckForNull
    @Step("Get the category id for {user}'s budget that has amount above zero")
    public static Integer getCategoryIdFromBudgetWhereAmountAboveZero(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 category_id FROM budget_category WHERE budget_period_id = ? AND actual_amount > 0 ORDER BY NEWID()";
            String result = utils.getDataFromSelectQuery("category_id", query, getBudgetPeriodId(user));
            if (result == null) {
                log.error("id came back null, {}", query);
                return null;
            }
            return Integer.valueOf(result);

        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a category id from budget period", e);
            return null;
        }
    }
}
