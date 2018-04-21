package com.d3.database;

import com.d3.datawrappers.alerts.AlertType;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3DatabaseException;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.List;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Alerts
 */
@Slf4j
public class AlertDatabaseHelper {
    private static final String PROP_VALUE_COLUMN = "prop_value";
    private static final String DESCRIPTION_VALUE_COUMN = "description";

    private AlertDatabaseHelper() {
    }

    /**
     * Get the id for an alert for a given alert type from the database
     *
     * @param type The alert type to get the id of
     * @return Integer value of the ID, null otherwise
     */
    @CheckForNull
    @Step("Get the id of alert type: {type}")
    public static Integer getConsumerAlertId(ConsumerAlerts type) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM alert_definition WHERE alert_type = ?";
            String returnValue = utils.getDataFromSelectQuery("id", query, type.name());
            if (returnValue == null) {
                log.error("Alert id came back null {}", type);
                return null;
            }
            return Integer.valueOf(returnValue);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting a Consumer alert", e);
            return null;
        }
    }

    /**
     * Get a triggered alert's information about email.
     *
     * @param alert What type of alert to query for
     * @param column Which column of the db to get information
     * @return String of the returned column info, null on error
     */
    @CheckForNull
    @Step("Get {column} email data from the db for {alert}")
    public static String getTriggeredAlertEmailInfo(ConsumerAlerts alert, String column) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 " + column + " FROM alert_template WHERE alert_id = "
                + "(SELECT id FROM alert_definition WHERE alert_type = ?) "
                + "AND dest_type_id = (SELECT id FROM alert_dest_type WHERE dest_type = 'INBOX') "
                + "AND FORMAT = 'EMAIL_TXT'";
            return utils.getDataFromSelectQuery(column, query, alert.name());
        } catch (D3DatabaseException | SQLException e) {
            log.error("Error getting value from alert_template table for {} column", column, e);
            return null;
        }
    }

    /**
     * Get a Consumer alert Prop value for a user and given alert ID
     *
     * @param alertId ID of the alert to query
     * @param user User to query for
     * @return String value of the prop, null otherwise
     */
    @CheckForNull
    @Step("Get a consumer alert prop value for {user.login} and alertId: {alertId}")
    public static String getConsumerAlertPropValue(int alertId, D3User user) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM alert_user_alert WHERE user_id = "
                + "(SELECT id FROM d3_user WHERE login_id = ?) AND alert_id = ?";
            String result = utils.getDataFromSelectQuery("id", query, user.getLogin(), Integer.toString(alertId));
            if (result == null) {
                log.error("Error getting the user alert id, came back null {}", alertId);
                return null;
            }
            int userAlertId = Integer.parseInt(result);

            @Language("SQL") String query2 = "SELECT prop_value FROM alert_user_alert_prop WHERE user_alert_id = ?";
            return utils.getDataFromSelectQuery(PROP_VALUE_COLUMN, query2, Integer.toString(userAlertId));
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting the Consumer Alert Prop value", e);
            return null;
        }
    }

    /**
     * Gets description string from the alert_definition table based on the ConsumerAlert type given
     *
     * @param alert Consumer Alert to get the description of
     * @return The alert description, null otherwise
     */
    @CheckForNull
    @Step("Get {alert}'s description")
    public static String getAlertDescription(ConsumerAlerts alert) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT description FROM alert_definition WHERE alert_type = ?";
            return utils.getDataFromSelectQuery(DESCRIPTION_VALUE_COUMN, query, alert.name());
        } catch (D3DatabaseException | SQLException e) {
            log.error("Error getting alert description for {} alert", alert.name(), e);
            return null;
        }
    }

    /**
     * Get the non mandatory alert descriptions from the db
     *
     * @param type non-mandatory Alert type to get the desciption of
     * @return List of descriptions, null if error
     */
    @CheckForNull
    @Step("Get the non mandatory {type}'s alert descriptions")
    public static List<String> getNonMandatoryAlertDescription(AlertType type) {
        @Language("SQL") String query = "SELECT description FROM alert_definition WHERE display_group = ? "
            + "AND alert_class != 'MANDATORY' "
            + "AND alert_class != 'USER' "
            + "ORDER BY alert_class, alert_type";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getAllDataFromSelectQuery(DESCRIPTION_VALUE_COUMN, query, type.toString());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error running sql", e);
            return null;
        }
    }

    /**
     * Get an alert description for a specific alert type
     *
     * @param type Alert type to get the description of
     * @return List of descriptions
     */
    @CheckForNull
    @Step("Get {type}'s alert description")
    public static List<String> getAlertDescriptions(AlertType type) {
        @Language("SQL") String query = "SELECT description FROM alert_definition "
            + "WHERE display_group = ? "
            + "AND alert_class != 'USER' "
            + "ORDER BY alert_class, alert_type";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getAllDataFromSelectQuery(DESCRIPTION_VALUE_COUMN, query, type.toString());
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error running sql", e);
            return null;
        }
    }

    /**
     * Get a triggered alert's id value from the db
     *
     * @param user user who the alert belongs to
     * @param alert Which alert to get the id value of
     * @return Id value of the user_message, null if error
     */
    @CheckForNull
    @Step("Get the triggered {alert} id for {user.login}")
    public static Integer getTriggeredAlertId(D3User user, D3Alert alert) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT id FROM user_message WHERE user_id = "
                + "(SELECT id FROM d3_user WHERE login_id = ?) "
                + "AND msg_type = ?";
            String results = utils.getDataFromSelectQuery("id", query, user.getLogin(), alert.getAlert().name());
            if (results == null) {
                log.error("Error getting id, came back null, {}", alert);
                return null;
            }
            return Integer.valueOf(results);
        } catch (D3DatabaseException | SQLException e) {
            log.error("Error getting triggered alert id", e);
            return null;
        }
    }
}
