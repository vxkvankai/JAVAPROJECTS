package com.d3.database;

import com.d3.datawrappers.CategoryType;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Transactions
 */
@Slf4j
public class TransactionDatabaseHelper {

    private TransactionDatabaseHelper() {
    }

    /**
     * Get the renaming_rule_id of a transaction
     *
     * @param transactionName Name of the transaction to find
     * @return the renaming rule id
     */
    @CheckForNull
    @Step("Get the renamed transaction id of {transactionName}")
    public static String getRenamedTransactionId(String transactionName) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT renaming_rule_id FROM user_transaction WHERE user_description = ?";
            return utils.getDataFromSelectQuery("renaming_rule_id", query, transactionName);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a renamed transaction id for transaction name: {}", transactionName, e);
            return null;
        }
    }

    /**
     * Get a random category name from the category table
     *
     * @param profileType What profile type to use (profile_type column)
     * @return String of the category_name if found, null if there was an error
     */
    @CheckForNull
    @Step("Get a random category name for the {profileType} type")
    public static String getRandomCategoryName(String profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 category_name FROM category WHERE profile_type = ? AND category_name = category_group "
                + "AND user_id IS NULL ORDER BY NEWID()";
            return utils.getDataFromSelectQuery("category_name", query, profileType);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a random category name", e);
            return null;
        }
    }

    /**
     * Get the Category ID from the category table for a specific category
     *
     * @param categoryName name of the category to get the id of
     * @param profileType Type of profile
     * @return Integer value of the id, null if error
     */
    @CheckForNull
    @Step("Get the category id for {categoryName} and {profileType} type")
    public static Integer getCategoryId(String categoryName, String profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM category WHERE category_name = ? AND profile_type = ?";
            String result = utils.getDataFromSelectQuery("id", query, categoryName, profileType);
            if (result == null) {
                log.error("id came back null, {}", query);
                return null;
            }
            return Integer.valueOf(result);

        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a categoryID", e);
            return null;
        }
    }

    /**
     * Get the Category Name from the category table for a specific category id
     *
     * @param categoryId id of the category to get the name of
     * @return Integer value of the id, null if error
     */
    @CheckForNull
    @Step("Get the category name for {categoryId}")
    public static String getCategoryName(int categoryId) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT category_name FROM category WHERE id = ?";
            String result = utils.getDataFromSelectQuery("category_name", query, categoryId);
            if (result == null) {
                log.error("category_name came back null, {}", query);
                return null;
            }
            return result;

        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a category name for given id", e);
            return null;
        }
    }

    /**
     * Get a random transaction id from the database for a specific user
     *
     * @param user Which user the transaction will be long to
     * @return The Integer value of the id, null if error
     */
    @CheckForNull
    @Step("Get a random transaction id for {user.login}")
    public static Integer getRandomTransactionId(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 id FROM d3_transaction WHERE account_id IN "
                + "(SELECT account_id FROM user_account WHERE user_id IN "
                + "(SELECT id FROM d3_user WHERE login_id = ?)) ORDER BY NEWID()";
            String result = utils.getDataFromSelectQuery("id", query, user.getLogin());
            if (result == null) {
                log.error("Transaction id came back null, {}", query);
                return null;
            }

            return Integer.valueOf(result);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a random TransactionId", e);
            return null;
        }
    }

    /**
     * Get the sum of the user's debit transactions amount
     *
     * @param user User to query
     * @return String value of the sum, null if error
     */
    @CheckForNull
    @Step("get the amount sum of debit transactions for {user.login}")
    public static String getSumOfUsersDebitTransactions(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            boolean isToggle = user.getToggleMode() != ToggleMode.NONE;
            String login = user.getLogin();
            String dbCode = D3Transaction.TransactionType.DEBIT.getDbCode();

            @Language("SQL") String query = getTotalAmountQuery(isToggle);
            if (isToggle) {
                return utils.getDataFromSelectQuery("sum", query, login, dbCode, user.getToggleMode().toString(),
                    login, dbCode, user.getToggleMode().toString());
            }
            return utils.getDataFromSelectQuery("sum", query, login, dbCode, login, dbCode);

        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting the sum of user debit transactions", e);
            return null;
        }
    }

    /**
     * Get the sum of the user's credit transactions amount
     *
     * @param user User to query
     * @return String value of the sum, null if error
     */
    @CheckForNull
    @Step("get the amount sum of credit transactions for {user.login}")
    public static String getSumOfUsersCreditTransactions(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            boolean isToggle = user.getToggleMode() != ToggleMode.NONE;
            String login = user.getLogin();
            String dbCode = D3Transaction.TransactionType.CREDIT.getDbCode();

            @Language("SQL") String query = getTotalAmountQuery(isToggle);
            if (isToggle) {
                return utils.getDataFromSelectQuery("sum", query, login, dbCode, user.getToggleMode().toString(),
                    login, dbCode, user.getToggleMode().toString());
            }
            return utils.getDataFromSelectQuery("sum", query, login, dbCode, login, dbCode);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting the sum of user credit transactions", e);
            return null;
        }
    }

    /**
     * Returns the query for the total amount of the transactions that is used for the calculation on the dashboard current month
     * widget
     *
     * @param toggleUser Set to true to create the query for a toggle user, false otherwise
     * @return String query. For param injection, the following order is used, total of 4 or 6 params needed:
     *
     * login_id, txn_type [0,1], [profile_type] (only if toggleUser is set to true),
     * login_id, txn_type [0,1], [profile_type] (only if toggleUser is set to true)
     */
    private static @Language("SQL")
    String getTotalAmountQuery(boolean toggleUser) {

        @SuppressWarnings("SqlResolve") @Language("SQL") String firstUnion =
            "SELECT d3_account.id FROM d3_account, d3_user, account_product, user_account "
                + "WHERE d3_account.id = user_account.account_id "
                + "AND user_account.hidden = 0 "
                + "AND user_account.excluded = 0 "
                + "AND user_account.user_id = d3_user.id "
                + "AND d3_user.login_id = ? "
                + "AND account_product.accounting_class = 'ASSET' "
                + "AND d3_account.account_product_id = account_product.id "
                + "AND d3_account.deleted = 0 "
                + "AND t.post_date >= DATEADD(MM, DATEDIFF(MM, 0, GETDATE()), 0) "
                + "AND t.txn_type = ?";
        if (toggleUser) {
            firstUnion += " AND d3_account.profile_type = ?";
        }

        @SuppressWarnings("SqlResolve") @Language("SQL") String secondUnion =
            "SELECT d3_account.id FROM d3_account, d3_user, account_product, user_account "
                + "WHERE d3_account.id = user_account.account_id "
                + "AND user_account.hidden = 0 "
                + "AND user_account.excluded = 0 "
                + "AND user_account.user_id = d3_user.id "
                + "AND d3_user.login_id = ? "
                + "AND account_product.accounting_class = 'ASSET' "
                + "AND d3_account.account_product_id = account_product.id "
                + "AND t.post_date >= DATEADD(MM, DATEDIFF(MM, 0, GETDATE()), 0) "
                + "AND t.txn_type = ? "
                + "AND d3_account.deleted = 0";
        if (toggleUser) {
            secondUnion += " AND d3_account.profile_type = ?";
        }

        @Language("SQL") String query = "SELECT SUM(t2.amount) AS sum FROM "
            + "(SELECT amount FROM d3_transaction t WHERE t.account_id IN "
            + "(" + firstUnion + ") "
            + "UNION "
            + "SELECT amount FROM d3_transaction_memo t WHERE t.account_id IN "
            + "(" + secondUnion + ")) AS t2";
        return query;
    }

    /**
     * Get a random category that is not shared across the profile types
     *
     * @param profileType profile type to query the database with
     * @return String value of the category_group, null if error
     */
    @CheckForNull
    @Step("Get a random category that's not shared across {profileType}")
    public static String getRandomCategoryNotSharedAcrossProfileTypes(String profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query =
                "SELECT TOP 1 category_group FROM category WHERE category_group IN"
                    + " (SELECT category_group FROM category GROUP BY category_group HAVING COUNT(DISTINCT profile_type) = 1)"
                    + " AND profile_type = ? AND user_id IS NULL"
                    + " ORDER BY NEWID()"; //NOSONAR

            return utils.getDataFromSelectQuery("category_group", query, profileType);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting category for specific profile type", e);
            return null;
        }
    }

    /**
     * Get a random category of a certain profile type
     *
     * @param categoryTypes Types of category to get a random one of
     * @param profileType Profile type to query the db with
     * @return String value of the category_group, null if error
     */
    @CheckForNull
    @Step("Get a random category of type {profileType}")
    public static String getRandomCategoryOfType(List<CategoryType> categoryTypes, String profileType) {
        List<String> filter = new ArrayList<>();
        categoryTypes.forEach(categoryType -> filter.add(categoryType.name()));

        try (DatabaseUtils utils = new DatabaseUtils()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < filter.size(); ++i) {
                builder.append("?,");
            }

            filter.add(profileType);

            @Language("SQL") String query = "SELECT TOP 1 category_group FROM category WHERE category_group IN"
                + " (SELECT category_group FROM category WHERE category_type IN ("
                + builder.deleteCharAt(builder.length() - 1).toString()
                + ")) AND profile_type = ? AND user_id IS NULL"
                + " ORDER BY NEWID()";

            return utils.getDataFromSelectQuery("category_group", query, filter);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting category for specific category types {} and profile type of {}.", categoryTypes.toString(), profileType, e);
            return null;
        }
    }

    /**
     * Get the sum debit amount of transactions that are used in the budget
     *
     * @param account Account to get the transactions from
     * @return String value of the sum, null if error
     */
    @CheckForNull
    @Step("Get the sum debit transactions for {account.name} for the budget from the DB")
    public static String getSumDebitTransactionForBudget(D3Account account) {
        Integer id = AccountDatabaseHelper.getAccountIdFromName(account);

        if (id == null) {
            log.error("Error getting the account ID from the name, came back as null, {}", id);
            return null;
        }

        String startMonth = getPostDateDebitTransactionForBudget(account);
        if (startMonth == null) {
            log.error("Error getting the post date, came back null, {}", startMonth);
            return null;
        }

        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT sum(amount) AS sum"
                + " FROM d3_transaction"
                + " WHERE account_id= ?"
                + " AND category_id!=139 AND FORMAT(post_date,'MMM') = ?"
                + " AND YEAR(post_date)=YEAR(GETDATE()) AND txn_type=1";

            return utils.getDataFromSelectQuery("sum", query, id, startMonth);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue finding an account with posted transactions for specific profile type", e);
            return null;
        }
    }

    /**
     * Get the post date debit transactions from the database for the budget
     *
     * @param account Account to get the transactions from
     * @return String value of the date (min(post_date), 'MMM'), null if error
     */
    @CheckForNull
    @Step("Get the post date debit transaction for {account.name} for the budget from the DB")
    public static String getPostDateDebitTransactionForBudget(D3Account account) {
        Integer id = AccountDatabaseHelper.getAccountIdFromName(account);
        if (id == null) {
            log.error("Error getting the account ID from the name, came back as null, {}", id);
            return null;
        }

        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT FORMAT(min(post_date),'MMM') AS date"
                + " FROM d3_transaction"
                + " WHERE account_id= ?"
                + " AND category_id!=139 AND YEAR(post_date)=YEAR(GETDATE()) AND txn_type=1";
            return utils.getDataFromSelectQuery("date", query, id);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue finding a date for the query", e);
            return null;
        }
    }

    /**
     * Get the sum of users debit transactions in a specific month
     *
     * @param user User to get the transactions of
     * @param month which month to get the transactions of
     * @return Sum of the amount, null if error
     */
    @CheckForNull
    @Step("Get the sum of {user.login}'s debit transactions for the month: {month}")
    public static String getSumOfUsersDebitTransactionsInSpecifyMonth(D3User user, String month) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT sum(amount) AS sum FROM d3_transaction WHERE  account_id IN "
                + "(SELECT d3_account.id  FROM d3_account,user_account,d3_user WHERE d3_user.login_id = ? "
                + "AND d3_account.id =user_account.account_id "
                + "AND user_account.user_id=d3_user.id) "
                + "AND category_id!=139 "
                + "AND FORMAT(post_date,'MMM') = ? AND YEAR(post_date)=YEAR(GETDATE()) AND txn_type=1";
            return utils.getDataFromSelectQuery("sum", query, user.getLogin(), month);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue get sum of user debit transactions", e);
            return null;
        }
    }
}
