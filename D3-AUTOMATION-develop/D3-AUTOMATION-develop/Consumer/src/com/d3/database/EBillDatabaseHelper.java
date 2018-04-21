package com.d3.database;

import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to E-Bills
 */
@Slf4j
public class EBillDatabaseHelper {

    private EBillDatabaseHelper() {
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
    @Step("Get the received e-bill id for {user.login}'s recipient {recipient}")
    public static Integer getReceivedEBillId(D3User user, Recipient recipient) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 id FROM m2_ebill WHERE user_id ="
                + " (SELECT id FROM d3_user WHERE login_id = ?)"
                + " AND biller_name = ?"
                + " AND ((DATEPART(DW, due_date) + @@DATEFIRST) % 7) NOT IN (0, 1)"
                + " ORDER BY NEWID()";
            String data = utils.getDataFromSelectQuery("id", query, user.getLogin(), recipient.getName());
            if (data == null) {
                log.error("Error getting the ebill id, came back null, {}", query);
                return null;
            }
            return Integer.valueOf(data);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting Received Ebill Id", e);
            return null;
        }
    }

    /**
     * This method gets the value of the specified database column for a particular ebill in the m2_ebill table
     *
     * @param column m2_ebill column to get data from
     * @param ebillId id of ebill to filter by
     * @return The requested column data as string, null if error
     */
    @CheckForNull
    @Step("Get the {column} attribute for e-bill: {ebillId}")
    public static String getEBillAttribute(int ebillId, String column) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = String.format("SELECT TOP 1 %s FROM m2_ebill WHERE id = ?", column);
            return utils.getDataFromSelectQuery(column, query, ebillId);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Issue getting ebill attribute", e);
            return null;
        }
    }
}
