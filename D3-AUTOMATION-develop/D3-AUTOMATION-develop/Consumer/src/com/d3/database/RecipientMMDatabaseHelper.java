package com.d3.database;

import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Recipients and Money Movement
 */
@Slf4j
public class RecipientMMDatabaseHelper {
    private static final String ATTR_NAME_COLUMN = "attr_name";
    private static final String ERROR_CONNETING_MSG = "Error connecting to the db";

    private RecipientMMDatabaseHelper() {
    }

    /**
     * Get the id value of a provider option via an external id
     *
     * @param externalId Id value to query the database with
     * @return Integer value of the id, null otherwise
     */
    @CheckForNull
    @Step("Get the provider option id via externalId: {externalId}")
    public static Integer getProviderOptionId(String externalId) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM m2_transfer_provider_option WHERE external_id = ?";
            String results = utils.getDataFromSelectQuery("id", query, externalId);
            if (results == null) {
                log.error("Provider option id came back null, {}", query);
                return null;
            }
            return Integer.valueOf(results);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a providerOptionId", e);
            return null;
        }
    }

    /**
     * This method will get the external endpoint id from the m2_endpoint table of the specified endpoint name
     *
     * @param user D3User being used in test
     * @param endpointName name of the endpoint to get id for
     * @return Integer value of endpoint id, null otherwise
     */
    @CheckForNull
    @Step("Get the external endpoint id for {user.login} and endpoint {endpointName}")
    public static Integer getExternalEndpointId(D3User user, String endpointName) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int userId = UserDatabaseHelper.getUserIdFromUserName(user.getLogin());
            @Language("SQL") String query = "SELECT id FROM m2_endpoint WHERE name = ? AND user_id = ?";
            String result = utils.getDataFromSelectQuery("id", query, endpointName, userId);
            if (result == null) {
                log.error("External Endpoint id query came back null, {}", query);
                return null;
            }

            return Integer.valueOf(result);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting an External Endpoint Id", e);
            return null;
        }
    }

    /**
     * This method will get the provider id of an external endpoint from the m2_endpoint_provider table of the specified endpoint name
     *
     * @param user D3User being used in test
     * @param endpointName name of the endpoint to get the provider id for
     * @return Integer value of provider endpoint id
     */
    @CheckForNull
    @Step("Get {endpointName}'s provider id for {user.login}")
    public static Integer getExternalEndpointProviderId(D3User user, String endpointName) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            int userId = UserDatabaseHelper.getUserIdFromUserName(user.getLogin());
            @Language("SQL") String query = "SELECT id FROM m2_endpoint_provider WHERE endpoint_id = "
                + "(SELECT id FROM m2_endpoint WHERE name = ? AND user_id = ?)";
            String result = utils.getDataFromSelectQuery("id", query, endpointName, userId);
            if (result == null) {
                log.error("Error getting the endpoint provider, came back null, {}", query);
                return null;
            }
            return Integer.valueOf(result);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting an External Endpoint Id", e);
            return null;
        }
    }

    /**
     * Set all the endpoints (recipients) for a user as active
     *
     * @param user User to set all the endpoints active to
     */
    @Step("Set all endpoints active for {user.login}")
    public static void setAllEndpointsActiveForUser(D3User user) {
        log.info("setting all recipients as active for user: {}", user);
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "UPDATE m2_endpoint_provider SET status = 'ACTIVE' WHERE modified_by = ?";
            utils.executeUpdateQuery(query, user.getLogin());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue setting recipients as active", e);
        }
    }

    /**
     * Get the default recipients for a user
     *
     * @param user user to get the recipients of
     * @return List of names of the recipient, null if error
     */
    @CheckForNull
    @Step("Get the default user recipients for {user.login}")
    public static List<String> getDefaultUserRecipients(D3User user) {
        return getListOfUserRecipients(user, true);
    }

    /**
     * Get a Map of endpoint provider attributes for specified endpoint id
     *
     * @param endpointId endpoint id to get attributes for
     * @return Map <String, String> of endpoint provider attributes
     */
    @Step("Get the endpoint provider attributes for endpoint: {endpointId}")
    public static Map<String, String> getEndpointProviderAttributes(Integer endpointId) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT attr_name, attr_value FROM m2_endpoint_provider_attr WHERE endpoint_provider_id = "
                + "(SELECT id FROM m2_endpoint_provider WHERE endpoint_id = ?) "
                + "AND attr_value IS NOT NULL ORDER BY attr_name ASC";

            List<String> name = utils.getAllDataFromSelectQuery(ATTR_NAME_COLUMN, query, endpointId);
            List<String> value = utils.getAllDataFromSelectQuery("attr_value", query, endpointId);

            return IntStream.range(0, name.size()).boxed().collect(Collectors.toMap(name::get, value::get));
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting endpoint provider attributes for provider with id {}", endpointId, e);
            return new HashMap<>();
        }
    }

    @CheckForNull
    @Step("Get the list recipients for {user.login}")
    private static List<String> getListOfUserRecipients(D3User user, boolean onlyDefault) {
        String userLogin = user.getUserType().isBusinessType()
            ? user.getFirstCompany().getName().replaceAll("[ .]", "").toLowerCase().trim()
            : user.getLogin();

        List<String> recipients = new ArrayList<>();

        String query = "SELECT * FROM m2_endpoint WHERE user_id = (SELECT id FROM d3_user WHERE login_id = ?) and deleted = 0";

        if (onlyDefault) {
            query += "AND nickname IS NULL";
        }

        try (DatabaseUtils utils = new DatabaseUtils()) {
            try (PreparedStatement stmt = utils.getConn().prepareStatement(query)) {
                stmt.setObject(1, userLogin); // NOSONAR dynamic query

                try (ResultSet set = stmt.executeQuery()) {
                    setRecipientListFromSet(set, recipients);
                    return recipients;
                }
            }
        } catch (D3DatabaseException | SQLException e) {
            log.error(ERROR_CONNETING_MSG, e);
            return null;
        }
    }

    private static void setRecipientListFromSet(ResultSet set, List<String> recipientList) throws SQLException {
        while (set.next()) {
            recipientList.add(set.getString("name"));
        }
    }
}
