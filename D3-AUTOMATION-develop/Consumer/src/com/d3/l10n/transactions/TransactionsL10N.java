package com.d3.l10n.transactions;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class TransactionsL10N {

    public enum Localization {

        CHECK_RANGE_VALIDATE_END_REQUIRED("transaction.search.check-range.validate.end.required", false),
        CHECK_RANGE_VALIDATE_START_REQUIRED("transaction.search.check-range.validate.start.required", false),
        INVALID_SPLIT_AMOUNT("transaction.split.valid", false),
        SEARCH_CHECK_RANGE_VALIDATE_RANGE("transaction.search.check-range.validate.range", true),
        DELETE_TRANSACTION_MESSAGE("transaction.delete.message", false),
        NO_TRANSACTIONS_AVAILABLE("transaction.pagination.empty-results", false),
        NO_MORE_TRANSACTIONS_FOUND("transaction.pagination.no-more-results", false),
        SCHEDULE_ON_US_MESSAGE("schedule.form.disclosure.on_us.create", false),
        NO_RECENT_TRANSACTIONS("transaction.pagination.recent-transactions.no-transactions", false);

        private String dbKey;
        private String value = "";
        private boolean hasParameters;

        Localization(String dbKey, boolean hasParameters) {
            this.dbKey = dbKey;
            this.hasParameters = hasParameters;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(TransactionsL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value; 
        }
    }


    /**
     * Replaces any text between underscores %s. If text between underscores contains amount it replaces it with $%s ex:
     * SEARCH_CHECK_RANGE_VALIDATE_RANGE -> "End check number must be greater than or equal to __startCheckNumber__" returns "End check number must be
     * greater than or equal to  %s"
     *
     * @return String local_value from l10n_text_resource table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("__([a-zA-Z]+)__", "%s");
    }
}

