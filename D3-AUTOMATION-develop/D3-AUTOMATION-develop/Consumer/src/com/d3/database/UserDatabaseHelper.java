package com.d3.database;

import static java.lang.Thread.sleep;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.Loginable;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Users
 */
@Slf4j
public class UserDatabaseHelper {

    private static final String ERROR_CONNECTING_MSG = "Error connecting to the db";
    private static final String ACCOUNT_NAME_COLUMN = "account_name";

    private UserDatabaseHelper() {
    }

    /**
     * Wait for the user account id to exist for the user
     *
     * @param user User to query
     * @param account Which account to wait for
     * @return The Account id from the database
     */
    @CheckForNull
    @Step("Wait for {user.login}'s {account.name} account to be present in the db")
    public static Integer waitForUserAccountId(D3User user, D3Account account) {
        log.info("Getting User accountId for user: {} and account: {}", user, account);
        Integer accountId = null;
        // trying multiple times if needed in case test code gets here before account is actually created via conduit
        for (int i = 0; i <= 100; ++i) {
            try {
                accountId = user.getUserType().isBusinessType()
                    ? getUserAccountIdFromDb(user.getFirstCompany(), account)
                    : getUserAccountIdFromDb(user, account);
                break;
            } catch (SQLException e) {
                log.warn("Error getting a user accountId", e);
            }
        }

        if (accountId == null) {
            log.error("No AccountId was found for account: {}", account);
        }
        log.info("AccountId: {}", accountId);

        return accountId;
    }

    private static Integer getUserAccountIdFromDb(Loginable user, D3Account account) throws SQLException {
        int id = getUserIdFromUserName(user.getLogin());
        String query = "SELECT * FROM user_account WHERE user_id = ?";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            try (PreparedStatement statement = utils.getConn().prepareStatement(query)) {
                statement.setObject(1, id);
                try (ResultSet set = statement.executeQuery()) {
                    log.info("Running query: {}", query);
                    return getUserAccountIdFromSet(utils, set, account.getName());
                }
            }

        } catch (D3DatabaseException e) {
            log.error(ERROR_CONNECTING_MSG, e);
            throw new SQLException(ERROR_CONNECTING_MSG);
        }
    }

    private static Integer getUserAccountIdFromSet(DatabaseUtils utils, ResultSet set, String accountName) throws SQLException {
        while (set.next()) {
            int accountId = set.getInt("account_id");

            @Language("SQL") String accountQuery = "SELECT * FROM d3_account WHERE id = ?";

            String accountNameResult = utils.getDataFromSelectQuery(ACCOUNT_NAME_COLUMN, accountQuery, accountId);
            if (accountNameResult != null && accountNameResult.equals(accountName)) {
                return set.getInt("id");
            }
        }
        throw new SQLException("Error getting the account Id");
    }

    /**
     * Get a random user name from the database for a given fi
     *
     * @param fi fi to find the user in
     * @return String value of the user login id
     */
    public static String getRandomUserName(String fi) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 login_id FROM d3_user d3u "
                + "JOIN user_profile up ON d3u.user_profile_id = up.id "
                + "WHERE up.profile_type = 'CONSUMER' "
                + "AND company_id = (SELECT id FROM company WHERE source_company_id = ?) "
                + "AND host_id LIKE 'host%' "
                + "ORDER BY NEWID();";
            return utils.getDataFromSelectQuery("login_id", query, fi);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting random user name", e);
            return null;
        }
    }

    /**
     * Gets the UserId value from the database given a login username
     *
     * @param username Login username
     * @return id value from the d3_user table
     * @throws SQLException on connection/SQL error
     */
    static int getUserIdFromUserName(String username) throws SQLException {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM d3_user WHERE login_id = ?";
            String data = utils.getDataFromSelectQuery("id", query, username);
            if (data == null) {
                throw new SQLException("Error getting user id, return value was null");
            }
            return Integer.valueOf(data);
        } catch (D3DatabaseException e) {
            log.error("Error getting user id", e);
            throw new SQLException("Error getting the user id from the username");
        }
    }

    /**
     * Get the destination id for a given dest_value and user from the database
     *
     * @param user User to query for
     * @param destinationValue dest_value to get the id of
     * @return Integer value of the id, null otherwise
     */
    @CheckForNull
    @Step("Get the destination id of {destinationValue} for {user.login}")
    public static Integer getDestinationId(D3User user, String destinationValue) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int id = getUserIdFromUserName(user.getLogin());
            @Language("SQL") String query = "SELECT dest.id FROM user_dest AS dest WHERE dest.user_profile_id = "
                + "(SELECT d3u.user_profile_id FROM d3_user AS d3u WHERE d3u.id = ?) AND dest.dest_value = ?";
            String returnValue = utils.getDataFromSelectQuery("id", query, id, destinationValue);
            if (returnValue == null) {
                log.error("dest.id value came back null, {}", query);
                return null;
            }
            return Integer.valueOf(returnValue);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting the destinationId", e);
            return null;
        }
    }

    /**
     * Get the most recent OOB Verification code for a user from the DB
     *
     * @param user User to query
     * @return String value of the OOB code
     */
    @Step("Get the OOB code for {user.login}")
    public static String getOOBVerificationCode(D3User user) {
        String verificationCode = null;
        log.info("Getting OOB code for {}", user);
        for (int i = 0; i < 10; ++i) {
            try {
                verificationCode = getOOBVerificationCodeFromLogin(user.getLogin());
                if (verificationCode != null) {
                    break;
                }
            } catch (SQLException e) {
                log.error("Error getting an oob verification code", e);
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error("Issue sleeping: Interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        if (verificationCode == null) {
            log.error("Verification code was not found, {}", verificationCode);
        }
        return verificationCode;
    }

    /**
     * Waits for the user to be in the database
     *
     * @param user user to wait for
     * @return The Id of the user, -1 if not found
     */
    public static int waitForUserToInDB(D3User user) {
        //noinspection ConstantConditions
        for (int i = 0; i < 250; ++i) {
            try {
                int userId = getUserIdFromUserName(user.getLogin());
                log.info("User has been created with userid: {}", userId);
                return userId;
            } catch (SQLException ignored) {
                log.debug("Sql error getting user", ignored);
                sleepALittle();
            }
        }
        return -1;
    }

    private static void sleepALittle() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Delete the user from the user_sync_operation table
     *
     * @param user to delete
     */
    @Step("Delete {user.login} from the user sync operation")
    public static void deleteFromUserSyncOperation(D3User user) {
        log.info("Deleting from user_sync_operation table for user: {}", user);
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @SuppressWarnings("SqlResolve") @Language("SQL") String query = "DELETE FROM user_sync_operation WHERE user_id = "
                + "(SELECT id FROM d3_user WHERE login_id = ?)";
            utils.executeUpdateQuery(query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue deleting from user_sync_operation table", e);
        }
    }

    @CheckForNull
    private static String getOOBVerificationCodeFromLogin(String login) throws SQLException {
        @Language("SQL") String query = "SELECT verification_code FROM user_verification_code WHERE user_id = ?";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getDataFromSelectQuery("verification_code", query, getUserIdFromUserName(login));
        } catch (D3DatabaseException e) {
            log.error(ERROR_CONNECTING_MSG, e);
            throw new SQLException(ERROR_CONNECTING_MSG);
        }
    }
}
