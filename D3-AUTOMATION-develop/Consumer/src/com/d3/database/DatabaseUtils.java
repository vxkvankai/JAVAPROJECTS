package com.d3.database;

import static com.d3.database.DatabaseUtils.DatabaseType.ORACLE;
import static java.lang.Thread.sleep;

import com.d3.datawrappers.CategoryType;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.account.OverdraftStatus;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.alerts.AlertType;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.messages.SecureMessage;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.D3UserProfile;
import com.d3.datawrappers.user.Loginable;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3DatabaseException;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public class DatabaseUtils implements AutoCloseable {

    private static final String ACCOUNT_NAME_COLUMN = "account_name";
    private static final String PROP_VALUE_COLUMN = "prop_value";
    private static final String NAME_VALUE_COLUMN = "name";
    private static final String ATTR_NAME_COLUMN = "attr_name";
    private static Logger logger = LoggerFactory.getLogger("DatabaseUtils");
    private Connection conn;
    private DatabaseType dbType;
    private String host;
    private String nameOrService;
    private String user;
    private String password;
    private String port;
    private String schema;

    public DatabaseUtils() throws D3DatabaseException {
        dbType = DatabaseType.valueOf(System.getProperty("dbType").toUpperCase());
        host = System.getProperty("dbHost");
        nameOrService = System.getProperty("dbNameOrService");
        user = System.getProperty("dbUser");
        password = System.getProperty("dbPassword");
        port = System.getProperty("dbPort");
        schema = System.getProperty("dbSchema", "");
        connectToDatabase();
    }

    @CheckForNull
    public static Integer waitForUserAccountId(D3User user, D3Account account) {
        logger.info("Getting User accountId for user: {} and account: {}", user, account);
        Integer accountId = null;
        // trying multiple times if needed in case test code gets here before account is actually created via conduit
        for (int i = 0; i <= 100; ++i) {
            try (DatabaseUtils utils = new DatabaseUtils()) {
                accountId = user.getUserType().isBusinessType() ? utils.getUserAccountIdFromDb(user.getFirstCompany(), account)

                    : utils.getUserAccountIdFromDb(user, account);
                if (accountId != null) {
                    break;
                }
            } catch (SQLException | D3DatabaseException e) {
                logger.error("Error getting a user accountId", e);
            }
        }

        if (accountId == null) {
            logger.error("No AccountId was found for account: {}", account);
        }
        logger.info("AccountId: {}", accountId);

        return accountId;
    }

    @Nullable
    public static String getRenamedTransactionId(String transactionName) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = String.format("SELECT renaming_rule_id FROM user_transaction where user_description = ''%s''", transactionName);
            return utils.getDataFromSelectQuery("renaming_rule_id", query);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting a renamed transaction id for transaction name: {}", transactionName, e);
            return null;
        }
    }

    public static void deleteFromUserSyncOperation(D3User user) {
        logger.info("Deleting from user_sync_operation table for user: {}", user);
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "DELETE FROM user_sync_operation WHERE user_id = (SELECT id from d3_user where login_id = ''{0}'')";
            utils.executeUpdateQuery(query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue deleting from user_sync_operation table", e);
        }
    }

    @CheckForNull
    public static String getRandomCategoryName(String profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT TOP 1 category_name FROM category WHERE profile_type = ''{0}'' AND category_name = category_group AND user_id IS null ORDER BY NEWID()";
            return utils.getDataFromSelectQuery("category_name", query, profileType);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting a random category name", e);
            return null;
        }
    }

    @CheckForNull
    public static Integer getCategoryId(String categoryName, String profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT id FROM category WHERE category_name = ''{0}'' AND profile_type = ''{1}''";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, categoryName, profileType));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting a categoryID", e);
            return null;
        }
    }

    @CheckForNull
    public static Integer getRandomTransactionId(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT TOP 1 id FROM d3_transaction WHERE account_id IN (SELECT account_id FROM user_account WHERE user_id IN "
                    + "(SELECT id FROM d3_user WHERE login_id = ''{0}'')) ORDER BY NEWID()";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, user.getLogin()));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting a random TransactionId", e);
            return null;
        }
    }

    /**
     * Updates the value string for a company attribute at the system level based on the company attribute definition given and String value
     *
     * @param definition String value of the company attribute definition
     * @param value bool value to set as the value_string for a given company attribute, gets converted to a string "true", or "false"
     */
    public static void updateCompanyAttributeValueString(String definition, boolean value) {
        updateCompanyAttributeValueString(definition, String.valueOf(value));
    }

    /**
     * Updates the value string for a company attribute at the system level based on the company attribute definition given and String value
     *
     * @param definition String value of the company attribute definition
     * @param value String value to set as the value_string for a given company attribute
     */
    // TODO: change this to private
    @Step("Update the company attribue")
    public static void updateCompanyAttributeValueString(String definition, String value) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "UPDATE company_attribute SET value_string = ''{0}'' WHERE definition  = ''{1}'' AND "
                + "company_id = (SELECT id FROM company WHERE bank_structure = ''ROOT'')";
            utils.executeUpdateQuery(query, value, definition);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error updating company attribute {} to {}", definition, value);
        }
    }

    public static void updateCompanyAttributeValueString(CompanyAttribute attribute, String value) {
        updateCompanyAttributeValueString(attribute.getDefinition(), value);
    }

    @Step("Exclude account via the DB for account: {accountName}")
    public static void excludeAccount(String accountName, String login) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "UPDATE user_account SET excluded = 1 WHERE account_id in (SELECT ua.account_id FROM user_account AS "
                + "ua JOIN d3_account AS d3 ON (d3.id = ua.account_id) WHERE d3.account_name = ''{0}'' "
                + "AND ua.user_id IN (SELECT id FROM d3_user WHERE login_id like ''{1}''))";
            utils.executeUpdateQuery(query, accountName, login);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error excludingAccount: {} for {}", accountName, login);
            logger.error("Exception:", e);
        }
    }

    @CheckForNull
    public static Integer getDestinationId(D3User user, String destinationValue) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int id = utils.getUserIdFromUserName(user.getLogin());
            String query =
                "SELECT dest.id FROM user_dest as dest WHERE dest.user_profile_id = (SELECT d3u.user_profile_id FROM d3_user as d3u WHERE d3u.id "
                    + "= ''{0,number,#}'') AND dest.dest_value = ''{1}''";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, id, destinationValue));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting the destinationId", e);
            return null;
        }
    }

    /**
     * Select the value from database
     *
     * @param account To get the name of the user
     * @return String value of estatements preferences
     */
    @CheckForNull
    @Step("Get the preference for Go Paperless from the db")
    public static String getPreferenceForGoPaperless(D3Account account) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = String.format("Select statement_preference_type from d3_account WHERE account_name =''%s''", account.getName());
            return utils.getDataFromSelectQuery("statement_preference_type", query);

        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting system preference", e);
            return null;
        }
    }

    @CheckForNull
    public static Integer getConsumerAlertId(ConsumerAlerts type) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT id FROM alert_definition WHERE alert_type = ''{0}''";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, type.name()));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting a Consumer alert", e);
            return null;
        }
    }

    @CheckForNull
    public static String getTriggeredAlertEmailInfo(ConsumerAlerts alert, String column) throws SQLException {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT TOP 1 {0} FROM alert_template WHERE alert_id ="
                    + " (SELECT id FROM alert_definition WHERE alert_type = ''{1}'') AND dest_type_id"
                    + " = (SELECT id FROM alert_dest_type WHERE dest_type = ''INBOX'') AND format = ''EMAIL_TXT''";
            return utils.getDataFromSelectQuery(column, query, column, alert.name());
        } catch (D3DatabaseException e) {
            logger.error("Error getting value from alert_template table for {} column", column);
            return null;
        }
    }

    public static Integer getProviderOptionId(String externalId) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT id FROM m2_transfer_provider_option WHERE external_id = ''{0}''";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, externalId));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting a providerOptionId", e);
            return null;
        }
    }

    /**
     * This method will get the external endpoint id from the m2_endpoint table of the specified endpoint name
     *
     * @param user D3User being used in test
     * @param endpointName name of the endpoint to get id for
     * @return Interger value of enpoint id
     */
    public static Integer getExternalEndpointId(D3User user, String endpointName) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int userId = utils.getUserIdFromUserName(user.getLogin());
            String query = "SELECT id FROM m2_endpoint WHERE name = ''{0}'' AND user_id = {1,number,#}";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, endpointName, userId));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting an External Endpoint Id", e);
            return null;
        }
    }

    /**
     * This method will get the provider id of an external endpoint from the m2_endpoint_provider table of the specified endpoint name
     *
     * @param user D3User being used in test
     * @param endpointName name of the endpoint to get the provider id for
     * @return Interger value of provider enpoint id
     */
    public static Integer getExternalEndpointProviderId(D3User user, String endpointName) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int userId = utils.getUserIdFromUserName(user.getLogin());
            String query = "SELECT id FROM m2_endpoint_provider WHERE endpoint_id = (SELECT id FROM m2_endpoint WHERE name = ''{0}'' AND user_id = {1,number,#})";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, endpointName, userId));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting an External Endpoint Id", e);
            return null;
        }
    }

    public static String getOOBVerificationCode(D3User user) {
        Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
        String verificationCode = null;
        logger.info("Getting OOB code for {}", user);
        for (int i = 0; i < 10; ++i) {
            try (DatabaseUtils utils = new DatabaseUtils()) {
                verificationCode = utils.getOOBVerificationCodeFromLogin(user.getLogin());
                if (verificationCode != null) {
                    break;
                }
            } catch (SQLException | D3DatabaseException e) {
                logger.error("Error getting an oob verification code", e);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error("Issue sleeping: Interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        if (verificationCode == null) {
            logger.error("Verification code was not found");
        }
        return verificationCode;
    }

    public static String getConsumerAlertPropValue(int alertId, D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT id FROM alert_user_alert WHERE user_id = (SELECT id FROM d3_user WHERE login_id = ''{0}'') and alert_id = ''{1}''";
            int userAlertId =
                Integer.parseInt(utils.getDataFromSelectQuery("id", query, user.getLogin(), Integer.toString(alertId)));

            query = "SELECT prop_value FROM alert_user_alert_prop WHERE user_alert_id = ''{0}''";
            return utils.getDataFromSelectQuery(PROP_VALUE_COLUMN, query, Integer.toString(userAlertId));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting the Consumer Alert Prop value", e);
            return null;
        }
    }

    @CheckForNull
    public static List<String> getExistingBusinessNames() {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT business_name FROM user_profile WHERE business_name IS NOT NULL";
            return utils.getAllDataFromSelectQuery("business_name", query);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue get list of business names that have already been created", e);
            return null;
        }
    }

    public static void setAllEndpointsActiveForUser(D3User user) {
        logger.info("setting all recipients as active for user: {}", user);
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "UPDATE m2_endpoint_provider SET status = ''ACTIVE'' WHERE modified_by = ''{0}''";
            utils.executeUpdateQuery(query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue setting recipients as active", e);
        }
    }

    /**
     * Enables or disables an Account Product permission or give a new value_string
     *
     * @param productType Account product type {@link com.d3.datawrappers.account.ProductType}
     * @param permission Name column value from the account_product_attribute table ex: d3Permission.allow.check.orders
     * @param value to set as the value_string for a given account product attribute
     */

    @Step("Update Account Product permission to {value} for product type: {productType}")
    public static void updateAccountProductPermission(ProductType productType, String permission, String value) {
        String query = String.format("UPDATE apa set apa.value_string = '%s' FROM account_product_attribute AS apa JOIN account_product AS ap ON "
                + "(apa.account_product_id = ap.id) WHERE apa.name = '%s' AND ap.source_product_id = '%s'", value, permission,
            productType.toString());
        try (DatabaseUtils utils = new DatabaseUtils()) {
            utils.executeUpdateQuery(query);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error updating account product permission {} for product type {}: {}", permission, productType.toString(), e);
        }
    }

    /**
     * Returns the value string for a company attribute at the system level based on the company attribute definition given
     *
     * @param definition String value of the company attribute definition
     */
    @CheckForNull
    @Step("Get a company attribute value: {definition}")
    public static String selectCompanyAttributeValueString(String definition) {
        String query = "Select value_string " +
            "From company_attribute " +
            "WHERE definition  = ''{0}'' AND company_id = (SELECT id FROM company WHERE bank_structure = ''ROOT'')";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getDataFromSelectQuery("value_string", query, definition);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error  getting company attribute  {}", definition);
            return null;
        }
    }


    /**
     * Returns the sum of all Asset accounts that are not hidden
     */
    @CheckForNull
    public static String getSumOfUsersAssetAccounts(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT sum(available_balance) as sum FROM d3_account WHERE id IN (SELECT d3_account.id FROM d3_account, d3_user, account_product, "
                    + "user_account WHERE user_account.hidden=0 AND d3_account.id = user_account.account_id AND user_account.user_id = d3_user.id AND d3_user.login_id =''{0}''"
                    + " AND account_product.accounting_class = ''ASSET'' AND d3_account.account_product_id = account_product.id AND d3_account.deleted = 0)";
            if (!user.getToggleMode().equals(ToggleMode.NONE)) {
                query += String.format(" and d3_account.profile_type = ''%s''", user.getToggleMode());
            }

            return utils.getDataFromSelectQuery("sum", query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue get sum of user Asset accounts", e);
            return null;
        }
    }

    /**
     * Returns the sum of all Liabilities accounts that are not hidden
     */
    @CheckForNull
    public static String getSumOfUsersLiabilityAccounts(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT sum(balance) as sum FROM d3_account WHERE id IN (SELECT d3_account.id FROM d3_account, d3_user, account_product, "
                    + "user_account WHERE user_account.hidden=0 AND d3_account.id = user_account.account_id AND user_account.user_id = d3_user.id AND d3_user.login_id =''{0}'' "
                    + "AND account_product.accounting_class = ''LIABILITY'' AND d3_account.account_product_id = account_product.id)";
            if (!user.getToggleMode().equals(ToggleMode.NONE)) {
                query += String.format(" and d3_account.profile_type = ''%s''", user.getToggleMode());
            }

            return utils.getDataFromSelectQuery("sum", query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue get sum of user Liability accounts", e);
            return null;
        }
    }

    @CheckForNull
    public static String getSumOfUsersDebitTransactions(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = getTotalAmountQueryForNonToggleUser(user.getLogin(), D3Transaction.TransactionType.DEBIT, user.getToggleMode());

            return utils.getDataFromSelectQuery("sum", query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue get sum of user debit transactions", e);
            return null;
        }
    }

    @CheckForNull
    public static String getSumOfUsersCreditTransactions(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = getTotalAmountQueryForNonToggleUser(user.getLogin(), D3Transaction.TransactionType.CREDIT, user.getToggleMode());

            return utils.getDataFromSelectQuery("sum", query);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue getting sum of user credit transactions", e);
            return null;
        }
    }

    /**
     * Gets the total amount of the transactions that is used for the calculation on the dashboard current month widget
     *
     * @param login User login id
     * @param type Type of transaction to check for (Credit or Debit)
     * @param mode Toggle Mode of the user, if not a toggle user, then pass NONE
     */
    private static String getTotalAmountQueryForNonToggleUser(String login, D3Transaction.TransactionType type, ToggleMode mode) {

        String txnType = type == D3Transaction.TransactionType.CREDIT ? "0" : "1";
        String profileTypeStr = mode == ToggleMode.NONE ?
            ")\n" :
            String.format("%n             AND d3_account.profile_type = ''%s'')%n", mode.toString());

        return String.format("SELECT SUM(t2.amount) AS sum FROM\n"
            + "(SELECT amount\n"
            + "FROM d3_transaction t\n"
            + "WHERE t.account_id IN\n"
            + "      (SELECT d3_account.id\n"
            + "       FROM d3_account, d3_user, account_product, user_account\n"
            + "       WHERE d3_account.id = user_account.account_id\n"
            + "             AND user_account.hidden = 0\n"
            + "             AND user_account.excluded = 0\n"
            + "             AND user_account.user_id = d3_user.id\n"
            + "             AND d3_user.login_id = ''%1$s''\n"
            + "             AND account_product.accounting_class = ''ASSET''\n"
            + "             AND d3_account.account_product_id = account_product.id\n"
            + "             AND d3_account.deleted = 0%3$s"
            + "AND t.post_date >= DATEADD(MM, DATEDIFF(MM, 0, GETDATE()), 0)\n"
            + "AND t.txn_type = %2$s\n"
            + "UNION\n"
            + "SELECT amount\n"
            + "FROM d3_transaction_memo t\n"
            + "WHERE t.account_id IN\n"
            + "      (SELECT d3_account.id\n"
            + "       FROM d3_account, d3_user, account_product, user_account\n"
            + "       WHERE d3_account.id = user_account.account_id\n"
            + "             AND user_account.hidden = 0\n"
            + "             AND user_account.excluded = 0\n"
            + "             AND user_account.user_id = d3_user.id\n"
            + "             AND d3_user.login_id = ''%1$s''\n"
            + "             AND account_product.accounting_class = ''ASSET''\n"
            + "             AND d3_account.account_product_id = account_product.id\n"
            + "             AND d3_account.deleted = 0%3$s"
            + "      AND t.post_date >= DATEADD(MM, DATEDIFF(MM, 0, GETDATE()), 0)\n"
            + "      AND t.txn_type = %2$s) AS t2", login, txnType, profileTypeStr);
    }

    public static String getRandomUserName(String fi) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT TOP 1 login_id FROM d3_user d3u "
                    + "JOIN user_profile up ON d3u.user_profile_id = up.id "
                    + "JOIN address ad on (up.mailing_address_id = ad.id) " // NOTE(JMoravec): eventually should remove this REL31-1687
                    + "WHERE up.profile_type = ''CONSUMER'' "
                    + "AND company_id = (SELECT id FROM company WHERE source_company_id = ''{0}'') "
                    + "AND host_id LIKE ''host%'' "
                    + "ORDER BY NEWID();";
            return utils.getDataFromSelectQuery("login_id", query, fi);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue getting random user name", e);
            return null;
        }
    }

    @CheckForNull
    @Step("Get the masked user accounts from the database")
    public static List<String> getMaskedUserAccounts(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getListOfUserAccounts(user, true);
        } catch (D3DatabaseException e) {
            logger.error("Error getting masked user account", e);
            return null;
        }
    }

    @CheckForNull
    public static String getRandomCategoryNotSharedAcrossProfileTypes(String profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT TOP 1 category_group FROM category WHERE category_group IN"
                    + " (SELECT category_group FROM category GROUP BY category_group HAVING COUNT(DISTINCT profile_type) = 1)"
                    + " AND profile_type = ''{0}'' AND user_id IS NULL"
                    + " ORDER BY NEWID()";

            return utils.getDataFromSelectQuery("category_group", query, profileType);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue getting category for specific profile type", e);
            return null;
        }
    }

    @CheckForNull
    public static String getRandomCategoryOfType(List<CategoryType> categoryTypes, String profileType) {
        List<String> filter = new ArrayList<>();
        categoryTypes.forEach(categoryType -> filter.add(String.format("''%s''", categoryType.name())));
        String queryFilter = filter.stream().map(entry -> entry).collect(Collectors.joining(","));

        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = String.format("SELECT TOP 1 category_group FROM category WHERE category_group IN"
                + " (SELECT category_group FROM category WHERE category_type IN (%s))"
                + " AND profile_type = ''{0}'' AND user_id IS NULL"
                + " ORDER BY NEWID()", queryFilter);

            return utils.getDataFromSelectQuery("category_group", query, profileType);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue getting category for specific category types {} and profile type of {}. \n {}", categoryTypes.toString(), profileType,
                e);
            return null;
        }
    }

    @CheckForNull
    public static String getRandomAccountWithPostedTransaction(D3User user, D3UserProfile.ProfileType profileType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT TOP 1 account_name FROM d3_account d3a "
                    + "JOIN d3_transaction txn ON d3a.id = txn.account_id"
                    + " WHERE account_id IN (SELECT account_id FROM user_account WHERE user_id IN"
                    + " (SELECT id FROM d3_user WHERE login_id = ''{0}'')) and txn.pending = 0 and profile_type = ''{1}'' ORDER BY NEWID()";

            return utils.getDataFromSelectQuery(ACCOUNT_NAME_COLUMN, query, user.getLogin(), profileType.toString());
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue finding an account with posted transactions for specific profile type", e);
            return null;
        }
    }

    @CheckForNull
    public static List<String> getDefaultUserRecipients(D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getListOfUserRecipients(user, true);
        } catch (D3DatabaseException e) {
            logger.error("Error getting list of user recipients", e);
            return null;
        }
    }

    @CheckForNull
    @Step("Get the sum debit transactions for the budget from the DB")
    public static String getSumDebitTransactionForBudget(D3Account account) {
        int id = getAccountIdFromName(account);
        String startMonth = getPostDateDebitTransactionForBudget(account);
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT sum(amount) as sum"
                    + " FROM d3_transaction"
                    + " Where account_id={0,number,#}"
                    + " and category_id!=139 and FORMAT(post_date,''MMM'') = ''{1}''"
                    + "AND YEAR(post_date)=YEAR(GETDATE()) and txn_type=1";

            return utils.getDataFromSelectQuery("sum", query, id, startMonth);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue finding an account with posted transactions for specific profile type", e);
            return null;
        }
    }

    @CheckForNull
    @Step("Get the post date debit transaction for the budget from the DB")
    public static String getPostDateDebitTransactionForBudget(D3Account account) {
        int id = getAccountIdFromName(account);
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT FORMAT(min(post_date),''MMM'') as date"
                    + " FROM d3_transaction"
                    + " Where account_id={0,number,#}"
                    + " and category_id!=139 and YEAR(post_date)=YEAR(GETDATE()) and txn_type=1";
            return utils.getDataFromSelectQuery("date", query, id);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue finding a date for the query", e);
            return null;
        }
    }

    /**
     * Gets local_value string from the l10n_text_resource table at the system level based on the L10N name value given
     *
     * @param l10n name value of L10N enum
     */
    @CheckForNull
    public static String getL10nValueString(String l10n) throws SQLException {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT local_value FROM l10n_text_resource WHERE name = ''{0}'' AND company_id = (SELECT id FROM company WHERE bank_structure = ''ROOT'') ORDER BY local_value ASC";
            return utils.getDataFromSelectQuery("local_value", query, l10n);
        } catch (D3DatabaseException e) {
            logger.error("Error getting the L10n value");
            return null;
        }
    }

    /**
     * Gets description string from the alert_definition table based on the ConsumerAlert type given
     *
     * @param alert Consumer Alert to get the description of
     */
    public static String getAlertDescription(ConsumerAlerts alert) throws SQLException {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT description FROM alert_definition WHERE alert_type = ''{0}''";
            return utils.getDataFromSelectQuery("description", query, alert.name());
        } catch (D3DatabaseException e) {
            logger.error("Error getting alert description for {} alert", alert.name());
            return null;
        }
    }

    /**
     * Gets the d3_account id from the database using the account name
     *
     * @param account D3Account to query for
     */
    @CheckForNull
    public static Integer getAccountIdFromName(D3Account account) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String id = utils.getDataFromSelectQuery("id", "SELECT id FROM d3_account WHERE account_name = ''{0}''", account.getName());
            return Integer.valueOf(id);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting account id for account: {}", account);
            return null;
        }
    }

    @CheckForNull
    @Step("Get the sum of a user's debit transactions for the month: {month}")
    public static String getSumOfUsersDebitTransactionsInSpecifyMonth(D3User user, String month) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query =
                "SELECT sum(amount) as sum FROM d3_transaction WHERE  account_id IN (SELECT d3_account.id  FROM d3_account,user_account,d3_user "
                    + "WHERE d3_user.login_id =''{0}'' AND d3_account.id =user_account.account_id AND user_account.user_id=d3_user.id) AND "
                    + "category_id!=139 and FORMAT(post_date,''MMM'') =''{1}'' AND YEAR(post_date)=YEAR(GETDATE()) and txn_type=1";
            return utils.getDataFromSelectQuery("sum", query, user.getLogin(), month);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue get sum of user debit transactions", e);
            return null;
        }
    }

    /**
     * Get the value_string (See OverdraftStatus) from the database for the given accountId
     *
     * @param accountId Id of the d3_account
     * @param productType type of product to retrive the value of
     */
    @CheckForNull
    public static String getOverdraftValue(int accountId, OverdraftProduct productType) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT value_string FROM account_attribute WHERE name = ''{0}'' AND account_id = {1,number,#}";
            return utils.getDataFromSelectQuery("value_string", query, productType.getDbCode(), accountId);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting overdraft value for account with id: {} and productType: {}", accountId, productType.getDbCode());
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
     */
    public static boolean areOverdraftOptionsCorrect(D3Account account, OverdraftStatus atmExpected, OverdraftStatus eTransferExpected)
        throws D3DatabaseException {
        Integer accountId = getAccountIdFromName(account);
        if (accountId == null) {
            throw new D3DatabaseException("Error getting the accountId from the database");
        }

        String atmOverDraftActual = getOverdraftValue(accountId, OverdraftProduct.ATM);
        if (atmOverDraftActual == null || atmOverDraftActual.isEmpty() || !atmOverDraftActual.equals(atmExpected.getDbCode())) {
            logger.warn("ATM value was not as expected\natmOverDraftActual: {}\nExpected: {}", atmOverDraftActual, atmExpected.getDbCode());
            return false;
        }

        String eTransferActual = getOverdraftValue(accountId, OverdraftProduct.ACH_ETRANSFERS);
        if (eTransferActual == null || eTransferActual.isEmpty() || !eTransferActual.equals(eTransferExpected.getDbCode())) {
            logger.warn("ATM value was not as expected\neTransferActual: {}\nExpected: {}", eTransferActual, eTransferExpected.getDbCode());
            return false;
        }

        return true;
    }

    /**
     * Get the audit_summary id for specified Audit. See:{{@link Audits}}
     *
     * @param user D3User the test is being run for
     * @param audit Audit to verify there is a record of
     */
    @CheckForNull
    public static Integer getAuditRecordId(D3User user, Audits audit) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT TOP 1 id FROM audit_summary WHERE audit_id = ''{0}'' AND username = ''{1}'' ORDER BY created_ts DESC";
            String id = utils.getDataFromSelectQuery("id", query, audit.name(), user.getLogin());
            return Integer.valueOf(id);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting audit record: {} for user {}", audit.name(), user.getLogin(), e);
            return null;
        }
    }

    /**
     * Get a Map of audit_attributes for specified Audit. See:{{@link Audits}}
     *
     * @param user D3User the test is being run for
     * @param audit Audit to verify record of
     * @return Map <String, String> of audit attributes
     */
    public static Map<String, String> getAuditRecordAttributes(D3User user, Audits audit) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            Integer auditSummaryId = getAuditRecordId(user, audit);

            if (auditSummaryId == null) {
                logger.error("Error getting the audit recordId for user: {}, and audit: {}", user, audit);
                return null;
            }

            String query = "SELECT name, prop_value FROM audit_attribute WHERE summary_id = {0,number,#} ORDER BY {1} ASC";

            List<String> name = utils.getAllDataFromSelectQuery(NAME_VALUE_COLUMN, query, auditSummaryId, NAME_VALUE_COLUMN);
            List<String> value = utils.getAllDataFromSelectQuery(PROP_VALUE_COLUMN, query, auditSummaryId, NAME_VALUE_COLUMN);

            return IntStream.range(0, name.size()).boxed().collect(Collectors.toMap(name::get, value::get));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting audit attributes of type: {} for user {}", audit.name(), user.getLogin(), e);
            return new HashMap<>();
        }
    }

    /**
     * Get a Map of endpoint provider attributes for specified endpoint id
     *
     * @param endpointId endpoint id to get attributes for
     * @return Map <String, String> of endpoint provider attributes
     */
    public static Map<String, String> getEndpointProviderAttributes(Integer endpointId) {
        try (DatabaseUtils utils = new DatabaseUtils()) {

            String query =
                "SELECT attr_name, attr_value FROM m2_endpoint_provider_attr WHERE endpoint_provider_id = (SELECT id FROM m2_endpoint_provider WHERE endpoint_id = {0,number,#}) AND attr_value IS NOT NULL ORDER BY {1} ASC";
            List<String> name = utils.getAllDataFromSelectQuery(ATTR_NAME_COLUMN, query, endpointId, ATTR_NAME_COLUMN);
            List<String> value = utils.getAllDataFromSelectQuery("attr_value", query, endpointId, ATTR_NAME_COLUMN);

            return IntStream.range(0, name.size()).boxed().collect(Collectors.toMap(name::get, value::get));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error getting endpoint provider attributes for provider with id {}", endpointId, e);
            return new HashMap<>();
        }
    }

    @CheckForNull
    public static List<String> getNonMandatoryAlertDescription(AlertType type) {
        String query = String.format("select description from alert_definition where display_group = '%s' "
            + "and alert_class != 'MANDATORY' and alert_class != 'USER' order by alert_class, alert_type", type.toString());
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getAllDataFromSelectQuery("description", query);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error running sql", e);
            return null;
        }
    }

    @CheckForNull
    public static List<String> getAlertDescriptions(AlertType type) {
        String query = String.format(
            "select description from alert_definition where display_group = '%s' and alert_class != 'USER' order by alert_class, alert_type",
            type.toString());
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getAllDataFromSelectQuery("description", query);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error running sql", e);
            return null;
        }
    }

    /**
     * This method will get the id for the specified secure message
     *
     * @param message Secure Message to get the id for
     * @return int of secure message id
     */
    @CheckForNull
    public static Integer getSecureMessageId(SecureMessage message) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT id FROM secure_message WHERE subject =''{0}''";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, message.getSubject()));
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error retrieving id of secure message", e);
            return null;
        }
    }

    /**
     * This method will enable the specified Scheduled job and set it to run every second (* * * * * ?)
     * Once it has been run, the job will then be disabled again
     *
     * @param job Schedule job to trigger
     */
    @CheckForNull
    public static void runScheduledJob(D3ScheduledJobs job) {
        String query = "UPDATE job_trigger SET cron_expression =''* * * * * ?'', status = 3 WHERE job_name =''{0}''";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int initialFireCount = utils.getScheduledJobFireCount(job);
            utils.executeUpdateQuery(query, job.getJobName());
            stopScheduledJob(job, initialFireCount);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error running scheduled job", e);
        }
    }

    /**
     * This method will disable a scheduled job once the fire count increases by 1
     *
     * @param job Scheduled job to stop
     */
    private static void stopScheduledJob(D3ScheduledJobs job, int initialFireCount) {
        // TODO: This should throw an exception if the job fails to get increase the count
        int currentFireCount;
        String query = "UPDATE job_trigger SET status = ''2'' WHERE job_name =''{0}''";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int counter = 0;
            do {
                currentFireCount = utils.getScheduledJobFireCount(job);
                logger.info("Waiting for fire_count to be {} for scheduled job {}. Currently has count of {}",
                    initialFireCount + 1,
                    job.getJobName(),
                    currentFireCount);
                counter++;
                Thread.sleep(100);
            } while (currentFireCount == initialFireCount && counter < 1000);
            if (counter >= 1000) {
                logger.error("fire_count never was {}", initialFireCount + 1);
                // TODO throw exception here
            }
            logger.info("Scheduled job {} has been run", job.getJobName());
            utils.executeUpdateQuery(query, job.getJobName());
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Error stopping scheduled job sql", e);
        } catch (InterruptedException e) {
            logger.error("Error sleeping Thread", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Waits for the user to be in the database
     *
     * @param user user to wait for
     * @return The Id of the user, -1 if not found
     */
    public static int waitForUserToInDB(D3User user) {
        int userId = -1;
        try (DatabaseUtils dbUtils = new DatabaseUtils()) {
            // wait until user is in db
            for (int i = 0; i < 250; ++i) {
                try {
                    userId = dbUtils.getUserIdFromUserName(user.getLogin());
                    logger.info("User has been created with userid: {}", userId);
                    break;
                } catch (SQLException ignored) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        logger.error("Interrupted", e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        } catch (D3DatabaseException e) {
            logger.error("Error waiting for the user to be in the db");
        }
        return userId;
    }

    /**
     * The new environments separate the separate envs on the database by the schema. This sets the current session/connection to be under the correct
     * schema. The property to set in the connection property file is 'dbSchema'
     */
    private void setSchemaForOracle() throws SQLException {
        if (schema != null && !schema.isEmpty()) {
            String query = String.format("ALTER SESSION SET CURRENT_SCHEMA=%s", schema);
            executeUpdateQuery(query);
        } else {
            logger.error("Schema config is not set");
        }
    }

    /**
     * Connect to a database
     */
    private void connectToDatabase() throws D3DatabaseException {

        String url;

        switch (dbType) {
            case ORACLE:
                url = String.format("jdbc:oracle:thin:@//%s:%s/%s", host, port, nameOrService);
                logger.info("Connecting to the Oracle database");
                break;
            case MY_SQL:
                url = String.format("jdbc:mysql://%s%s", host, nameOrService);
                logger.info("Connecting to the MySql database");
                break;
            case SQL_SERVER:
                url = String.format("jdbc:jtds:sqlserver://%s:%s;DatabaseName=%s", host, port, nameOrService);
                logger.info("Connecting to the SqlServer database");
                break;
            default:
                throw new D3DatabaseException(String.format("%s is not supported yet", dbType));
        }
        logger.info("URL used: {}", url);

        try {
            Class.forName(dbType.getDriverString());
            conn = DriverManager.getConnection(url, user, password);
            if (dbType == ORACLE) {
                setSchemaForOracle();
            }
        } catch (ClassNotFoundException e) {
            logger.error("Error initializing SQL Driver: {}", dbType.getDriverString(), e);
            throw new D3DatabaseException("Fix the DriverName variable");
        } catch (SQLException e) {
            logger.error("Error Connecting to SQL Driver: {} with url {}", dbType.getDriverString(), url, e);
            throw new D3DatabaseException("Database Error");
        }
    }

    /**
     * Execute an update query against the database
     *
     * @param updateQuery Query to run against the database
     * @param arguments arguments to give to the updateQuery via MessageFormat.format() If arguments are given, make sure you use two single quotes to
     * equal one single quote: (conn, "UPDATE test SET test_field = ''{0}''", true)
     * @return the number of rows updated
     */
    public int executeUpdateQuery(String updateQuery, Object... arguments) throws SQLException {
        String fullQuery = updateQuery;
        if (arguments.length != 0) {
            fullQuery = MessageFormat.format(updateQuery, arguments);
        }

        try (Statement stmt = conn.createStatement()) {
            logger.info("Update Query being run: {}", fullQuery);
            int updateRows = stmt.executeUpdate(fullQuery);
            logger.info("Number of rows updated: {}", updateRows);
            return updateRows;
        }
    }

    public String getDataFromSelectQuery(String column, String query, Object... arguments) throws SQLException {
        String fullQuery;
        if (arguments.length != 0) {
            fullQuery = MessageFormat.format(query, arguments);
        } else {
            fullQuery = MessageFormat.format(query, "");
        }

        logger.info("The following query will be executed: {}", fullQuery);
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet set = stmt.executeQuery(fullQuery)) {
                set.next();
                return set.getString(column);
            }
        }
    }

    private List<String> getAllDataFromSelectQuery(String column, String query, Object... arguments) throws SQLException {
        List<String> queryResults = new ArrayList<>();
        String fullQuery = query;
        if (arguments.length != 0) {
            fullQuery = MessageFormat.format(query, arguments);
        }

        logger.info("The following query will be executed: {}", fullQuery);
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet set = stmt.executeQuery(fullQuery)) {
                while (set.next()) {
                    String result = set.getString(column);
                    queryResults.add(result);
                }
                return queryResults;
            }
        }
    }

    /**
     * Gets the UserId value from the database given a login username
     *
     * @param username Login username
     * @return id value from the d3_user table
     * @throws SQLException on connection/SQL error
     */
    public int getUserIdFromUserName(String username) throws SQLException {
        String query = "SELECT id FROM d3_user WHERE login_id = ''{0}''";
        String data = getDataFromSelectQuery("id", query, username);
        return Integer.valueOf(data);
    }

    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            logger.error("Error closing the db connection", e);
        }
    }

    private Integer getUserAccountIdFromDb(Loginable user, D3Account account) throws SQLException {

        int id = getUserIdFromUserName(user.getLogin());
        String query = MessageFormat.format("SELECT * FROM user_account WHERE user_id = {0,number,#}", id);
        try (Statement statement = conn.createStatement()) {
            try (ResultSet set = statement.executeQuery(query)) {
                logger.info("Running query: {}", query);
                Integer userAccountId = null;
                while (set.next()) {
                    int accountId = set.getInt("account_id");

                    String accountQuery = "SELECT * FROM d3_account where id = {0,number,#}";

                    String accountNameResult = getDataFromSelectQuery(ACCOUNT_NAME_COLUMN, accountQuery, accountId);
                    if (accountNameResult.equals(account.getName())) {
                        userAccountId = set.getInt("id");
                        break;
                    }
                }
                return userAccountId;
            }
        }
    }

    @CheckForNull
    public Integer getTriggeredAlertId(D3User user, D3Alert alert) throws SQLException {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT id FROM user_message WHERE user_id = (SELECT id FROM d3_user WHERE login_id = ''{0}'') and msg_type = ''{1}''";
            return Integer.valueOf(utils.getDataFromSelectQuery("id", query, user.getLogin(), alert.getAlert().name()));
        } catch (D3DatabaseException e) {
            logger.error("Error getting triggered alert id", e);
            return null;
        }
    }

    private String getOOBVerificationCodeFromLogin(String login) throws SQLException {
        String query = "SELECT verification_code from user_verification_code WHERE user_id = {0,number,#}";
        return getDataFromSelectQuery("verification_code", query, getUserIdFromUserName(login));
    }

    @CheckForNull
    private List<String> getListOfUserAccounts(D3User user, boolean getMaskedValues) {
        List<String> accounts = new ArrayList<>();
        List<String> maskedAccounts = new ArrayList<>();

        String query =

            "SELECT d3a.account_name, mv.trimmed_value FROM d3_account d3a JOIN masked_value mv"
                + " ON d3a.masked_value_id=mv.id WHERE d3a.id IN (SELECT ua.account_id FROM user_account ua WHERE ua.hidden=0 and ua.user_id IN"
                + " (SELECT d3u.id FROM d3_user d3u WHERE d3u.login_id = ''{0}''))";

        query += user.getToggleMode().equals(ToggleMode.NONE) ? " and d3a.profile_type != ''{1}''"
            : " and d3a.profile_type = ''{1}''";
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet set = stmt.executeQuery(MessageFormat.format(query, user.getLogin(), user.getToggleMode()))) {
                while (set.next()) {
                    accounts.add(set.getString(ACCOUNT_NAME_COLUMN));
                    maskedAccounts.add(String.format("%s (*%s)", set.getString(ACCOUNT_NAME_COLUMN), set.getString("trimmed_value")));
                }
                return getMaskedValues ? maskedAccounts : accounts;
            }
        } catch (SQLException e) {
            logger.error("Issue getting user accounts", e);
            return null;
        }
    }

    @CheckForNull
    private List<String> getListOfUserRecipients(D3User user, boolean onlyDefault) {
        String userLogin =
            user.getUserType().isBusinessType() ? user.getFirstCompany().getName().replaceAll("[ .]", "").toLowerCase().trim()
                : user.getLogin();

        List<String> recipients = new ArrayList<>();

        String query =
            "SELECT * FROM m2_endpoint WHERE user_id = (SELECT id FROM d3_user WHERE login_id = ''{0}'') and deleted = ''0''";

        query += onlyDefault ? " and nickname IS NULL" : null;

        try (Statement stmt = conn.createStatement()) {
            logger.info("Executing query {}", query);
            try (ResultSet set = stmt.executeQuery(MessageFormat.format(query, userLogin))) {
                while (set.next()) {
                    recipients.add(set.getString("name"));
                }
                return recipients;
            }
        } catch (SQLException e) {
            logger.error("Issue getting recipients for user", e);
            return null;
        }
    }

    /**
     * This method gets the fire_count from the job_trigger table for the specified Scheduled job
     *
     * @param job Schedule job
     * @return int value of current fire_count
     */
    private int getScheduledJobFireCount(D3ScheduledJobs job) throws SQLException {
        String query = "SELECT fire_count FROM job_trigger WHERE job_name =''{0}''";
        String data = getDataFromSelectQuery("fire_count", query, job.getJobName());
        return Integer.valueOf(data);
    }

    /**
     * This method gets the id of a random received ebill for the specified user and recipient
     * Note: Since payments can't be made on weekends, the query will return only ebills where the due date falls on a weekday
     *
     * @param user User with active ebiller and received ebills
     * @param recipient Recipient recipient to filter ebills for
     * @return int value of received ebill
     */
    @CheckForNull
    public static Integer getReceivedEBillId(D3User user, Recipient recipient) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT TOP 1 id FROM m2_ebill WHERE user_id ="
                + " (SELECT id FROM d3_user WHERE login_id = ''{0}'')"
                + " AND biller_name = ''{1}''"
                + " AND ((DATEPART(dw, due_date) + @@DATEFIRST) % 7) NOT IN (0, 1)"
                + " ORDER BY NEWID()";
            String data = utils.getDataFromSelectQuery("id", query, user.getLogin(), recipient.getName());
            return Integer.valueOf(data);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue getting Received Ebill Id", e);
            return null;
        }
    }

    /**
     * This method gets the value of the specified database column for a particular ebill in the m2_ebill table
     *
     * @param column m2_ebill column to get data from
     * @param ebillId id of ebill to filter by
     */
    @CheckForNull
    public static String getEBillAttribute(int ebillId, String column) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            String query = "SELECT TOP 1 {0} FROM m2_ebill WHERE id = {1,number,#}";
            return utils.getDataFromSelectQuery(column, query, column, ebillId);
        } catch (SQLException | D3DatabaseException e) {
            logger.error("Issue getting ebill attribute", e);
            return null;
        }
    }

    public enum DatabaseType {
        ORACLE("oracle.jdbc.driver.OracleDriver"),
        SQL_SERVER("net.sourceforge.jtds.jdbc.Driver"),
        MY_SQL("com.mysql.jdbc.Driver");

        String driverString;

        DatabaseType(String driverString) {
            this.driverString = driverString;
        }

        public String getDriverString() {
            return driverString;
        }
    }
}
