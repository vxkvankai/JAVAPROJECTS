package com.d3.l10n.transactions;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import com.d3.l10n.L10nCommon;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for Transactions
 */
@Slf4j
public class TransactionsL10N extends L10nCommon {

    /**
     * Holds the L10N values for Transactions
     */
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
                    this.value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }
}

