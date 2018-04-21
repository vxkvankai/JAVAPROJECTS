package com.d3.database;

import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3DatabaseException;
import com.d3.monitoring.audits.Audits;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.CheckForNull;


/**
 * Helper class that does actions and queries against the database related to Audits
 */
@Slf4j
public class AuditDatabaseHelper {

    private static final String NAME_VALUE_COLUMN = "name";
    private static final String PROP_VALUE_COLUMN = "prop_value";

    private AuditDatabaseHelper() {
    }

    /**
     * Get the audit_summary id for specified Audit. See:{{@link Audits}}
     *
     * @param user D3User the test is being run for
     * @param audit Audit to verify there is a record of
     * @return The id value of the audit record, null if error
     */
    @CheckForNull
    @Step("Get the record id for {audit} and {user.login}")
    public static Integer getAuditRecordId(D3User user, Audits audit) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "SELECT TOP 1 id FROM audit_summary WHERE audit_id = ? AND username = ? ORDER BY created_ts DESC";
            String id = utils.getDataFromSelectQuery("id", query, audit.name(), user.getLogin());
            if (id == null) {
                log.error("Error getting the id from the audit record, came back null {}", audit);
                return null;
            }
            return Integer.valueOf(id);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting audit record: {} for user {}", audit.name(), user.getLogin(), e);
            return null;
        }
    }

    /**
     * Get a Map of audit_attributes for specified Audit. See:{{@link Audits}}
     *
     * @param user D3User the test is being run for
     * @param audit Audit to verify record of
     * @return Map <String, String> of audit attributes, empty hashmap if error
     */
    @Step("Get the the {audit} record attributes for {user.login}")
    public static Map<String, String> getAuditRecordAttributes(D3User user, Audits audit) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            Integer auditSummaryId = getAuditRecordId(user, audit);

            if (auditSummaryId == null) {
                log.error("Error getting the audit recordId for user: {}, and audit: {}", user, audit);
                return null;
            }

            @Language("SQL") String query = "SELECT name, prop_value FROM audit_attribute WHERE summary_id = ? "
                + "ORDER BY " + NAME_VALUE_COLUMN + " ASC";

            List<String> name = utils.getAllDataFromSelectQuery(NAME_VALUE_COLUMN, query, auditSummaryId);
            List<String> value = utils.getAllDataFromSelectQuery(PROP_VALUE_COLUMN, query, auditSummaryId);

            return IntStream.range(0, name.size()).boxed().collect(Collectors.toMap(name::get, value::get));
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error getting audit attributes of type: {} for user {}", audit.name(), user.getLogin(), e);
            return new HashMap<>();
        }
    }
}
