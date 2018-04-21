package com.d3.database;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.OverdraftStatus;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.D3UserProfile;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Accounts
 */
@Slf4j
public class AccountDatabaseHelper {
    private static final String ACCOUNT_NAME_COLUMN = "account_name";

    private AccountDatabaseHelper() {
    }

    /**
     * Exclude an account via the database
     *
     * @param accountName Name of the account to exclude
     * @param login Login id of the user to update
     */
    @Step("Exclude account via the DB for {login}'s account: {accountName}")
    public static void excludeAccount(String accountName, String login) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "UPDATE user_account SET excluded = 1 WHERE account_id IN "
                + "(SELECT ua.account_id FROM user_account AS ua "
                + "JOIN d3_account AS d3 ON (d3.id = ua.account_id) WHERE d3.account_name = ? "
                + "AND ua.user_id IN (SELECT id FROM d3_user WHERE login_id LIKE ?))";
            utils.executeUpdateQuery(query, accountName, login);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error excludingAccount: {} for {}", accountName, login);
            log.error("Exception:", e);
        }
    }

    /**
     * Select the value from database
     *
     * @param account To get the name of the user
     * @return String value of e-statements preferences
     */
    @CheckForNull
    @Step("Get the preference for Go Paperless for {account.name} from the db")
    public static String getPreferenceForGoPaperless(D3Account account) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT statement_preference_type FROM d3_account WHERE account_name = ?";
            return utils.getDataFromSelectQuery("statement_preference_type", query, account.getName());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting system preference", e);
            return null;
        }
    }

    /**
     * Returns the sum of all Asset accounts that are not hidden
     *
     * @param user User to get the asset accounts of
     * @return String value of the sum, null if error
     */
    @CheckForNull
    @Step("Get the amount sum of {user.login}'s non hidden asset accounts")
    public static String getSumOfUsersAssetAccounts(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT sum(available_balance) as sum FROM d3_account WHERE id IN "
                + "(SELECT d3_account.id FROM d3_account, d3_user, account_product, user_account WHERE user_account.hidden=0 "
                + "AND d3_account.id = user_account.account_id "
                + "AND user_account.user_id = d3_user.id " //NOSONAR
                + "AND d3_user.login_id =? "
                + "AND account_product.accounting_class = 'ASSET' " //NOSONAR
                + "AND d3_account.account_product_id = account_product.id " //NOSONAR
                + "AND d3_account.deleted = 0)";

            if (!user.getToggleMode().equals(ToggleMode.NONE)) {
                query += " AND d3_account.profile_type = ?"; //NOSONAR
                return utils.getDataFromSelectQuery("sum", query, user.getLogin(), user.getToggleMode());
            }

            return utils.getDataFromSelectQuery("sum", query, user.getLogin());

        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue get sum of user Asset accounts", e);
            return null;
        }
    }

    /**
     * Returns the sum of all Liabilities accounts that are not hidden
     *
     * @param user User to get all the liability accounts of
     * @return String value of the sum, null if error
     */
    @CheckForNull
    @Step("Get the amount sum of {user.login}'s non hidden liability accounts")
    public static String getSumOfUsersLiabilityAccounts(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT sum(balance) as sum FROM d3_account WHERE id IN "
                + "(SELECT d3_account.id FROM d3_account, d3_user, account_product, user_account "
                + "WHERE user_account.hidden=0 "
                + "AND d3_account.id = user_account.account_id "
                + "AND user_account.user_id = d3_user.id "
                + "AND d3_user.login_id = ? " //NOSONAR
                + "AND account_product.accounting_class = 'LIABILITY' "
                + "AND d3_account.account_product_id = account_product.id)";

            if (!user.getToggleMode().equals(ToggleMode.NONE)) {
                query += " and d3_account.profile_type = ?";
                return utils.getDataFromSelectQuery("sum", query, user.getLogin(), user.getToggleMode());
            }

            return utils.getDataFromSelectQuery("sum", query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting the sum of user Liability accounts", e);
            return null;
        }
    }

    /**
     * Get the masked user accounts for a given user
     *
     * @param user Which user to get the accounts for
     * @return A list of strings with account names
     */
    @CheckForNull
    @Step("Get the masked user accounts from the database")
    public static List<String> getMaskedUserAccounts(D3User user) {
        return getListOfUserAccounts(user, true);
    }

    /**
     * Get a random account of a user that has a posted transaction
     *
     * @param user User to get the account from
     * @param profileType Type of the profile to use
     * @return String value of the account name, null if error
     */
    @CheckForNull
    @Step("Get a random account that has a posted transaction for {user.login}")
    public static String getRandomAccountWithPostedTransaction(D3User user, D3UserProfile.ProfileType profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 account_name FROM d3_account d3a "
                + "JOIN d3_transaction txn ON d3a.id = txn.account_id"
                + " WHERE account_id IN (SELECT account_id FROM user_account WHERE user_id IN"
                + " (SELECT id FROM d3_user WHERE login_id = ?)) AND txn.pending = 0 AND profile_type = ? ORDER BY NEWID()";
            return utils.getDataFromSelectQuery(ACCOUNT_NAME_COLUMN, query, user.getLogin(), profileType.toString());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue finding an account with posted transactions for specific profile type", e);
            return null;
        }
    }

    /**
     * Gets the d3_account id from the database using the account name
     *
     * @param account D3Account to query for
     * @return The Account ID for the account, null if error
     */
    @CheckForNull
    @Step("Get the account id for {account.name}")
    static Integer getAccountIdFromName(D3Account account) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String id = utils.getDataFromSelectQuery("id", "SELECT id FROM d3_account WHERE account_name = ?", account.getName());
            if (id == null) {
                log.error("Error getting account id, came back null, {}", account);
                return null;
            }
            return Integer.valueOf(id);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting account id for account: {}", account, e);
            return null;
        }
    }

    /**
     * Get the value_string (See OverdraftStatus) from the database for the given accountId
     *
     * @param accountId Id of the d3_account
     * @param productType type of product to retrive the value of
     * @return The value_string of the account attribute, null if error
     */
    @CheckForNull
    @Step("Get the overdraft value for account: {accountId} and type: {productType}")
    private static String getOverdraftValue(int accountId, OverdraftProduct productType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT value_string FROM account_attribute WHERE name = ? AND account_id = ?";
            return utils.getDataFromSelectQuery("value_string", query, productType.getDbCode(), accountId);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting overdraft value for account with id: {} and productType: {}", accountId, productType.getDbCode(), e);
            return null;
        }
    }

    /**
     * Checks if the overdraft options are the expected values
     *
     * @param account account to check against
     * @param atmExpected expected overdraft status for the ATM/Debit card type
     * @param eTransferExpected expected overdraft status e-transfer/ACH status
     * @return True only if both values are the same
     * @throws D3DatabaseException if the accountId comes back null or other error
     */
    @Step("Check if the overdraft options ar correct for {account.name}")
    public static boolean areOverdraftOptionsCorrect(D3Account account, OverdraftStatus atmExpected, OverdraftStatus eTransferExpected)
        throws D3DatabaseException {

        Integer accountId = getAccountIdFromName(account);
        if (accountId == null) {
            throw new D3DatabaseException("Error getting the accountId from the database");
        }

        String atmOverDraftActual = getOverdraftValue(accountId, OverdraftProduct.ATM);
        if (atmOverDraftActual == null || atmOverDraftActual.isEmpty() || !atmOverDraftActual.equals(atmExpected.getDbCode())) {
            log.warn("ATM value was not as expected\natmOverDraftActual: {}\nExpected: {}", atmOverDraftActual, atmExpected.getDbCode());
            return false;
        }

        String eTransferActual = getOverdraftValue(accountId, OverdraftProduct.ACH_ETRANSFERS);
        if (eTransferActual == null || eTransferActual.isEmpty() || !eTransferActual.equals(eTransferExpected.getDbCode())) {
            log.warn("ATM value was not as expected\neTransferActual: {}\nExpected: {}", eTransferActual, eTransferExpected.getDbCode());
            return false;
        }

        return true;
    }

    @CheckForNull
    @Step("Get the list of {user.login}'s accounts from the db")
    private static List<String> getListOfUserAccounts(D3User user, boolean getMaskedValues) {
        List<String> accounts = new ArrayList<>();
        List<String> maskedAccounts = new ArrayList<>();

        String query = "SELECT d3a.account_name, mv.trimmed_value FROM d3_account d3a JOIN masked_value mv"
            + " ON d3a.masked_value_id=mv.id WHERE d3a.id IN (SELECT ua.account_id FROM user_account ua WHERE ua.hidden=0 and ua.user_id IN"
            + " (SELECT d3u.id FROM d3_user d3u WHERE d3u.login_id = ?))";

        if (user.getToggleMode().equals(ToggleMode.NONE)) {
            query += " and d3a.profile_type != ?";
        } else {
            query += " and d3a.profile_type = ?";
        }
        try (DatabaseUtils utils = new DatabaseUtils()) {
            try (PreparedStatement stmt = utils.getConn().prepareStatement(query)) {
                stmt.setObject(1, user.getLogin());
                stmt.setObject(2, user.getToggleMode().toString()); //NOSONAR dynamic query
                try (ResultSet set = stmt.executeQuery()) {
                    setAccountInfoListsFromDBSet(set, accounts, maskedAccounts);
                    return getMaskedValues ? maskedAccounts : accounts;
                }
            }
        } catch (D3DatabaseException | SQLException e) {
            log.error("Issue getting user accounts", e);
            return null;
        }
    }

    private static void setAccountInfoListsFromDBSet(ResultSet set, List<String> accountList, List<String> maskedAccountList) throws SQLException {
        while (set.next()) {
            accountList.add(set.getString(ACCOUNT_NAME_COLUMN));
            maskedAccountList.add(String.format("%s (*%s)", set.getString(ACCOUNT_NAME_COLUMN), set.getString("trimmed_value")));
        }
    }

    /**
     * Get the account product id via the source product id value
     *
     * @param productId source_product_id from the database
     * @return Integer value of the product id, null if error
     */
    @CheckForNull
    @Step("Get an account product id for product: {productId}")
    public static Integer getAccountProductId(String productId) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM account_product WHERE source_product_id = ?";
            String results = utils.getDataFromSelectQuery("id", query, productId);
            if (results == null) {
                log.error("Error getting account product id, came back null, {}", productId);
                return null;
            }
            return Integer.valueOf(results);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting account product id for {}", productId, e);
            return null;
        }
    }
}
