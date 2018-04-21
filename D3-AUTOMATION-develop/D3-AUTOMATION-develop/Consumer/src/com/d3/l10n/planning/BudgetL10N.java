package com.d3.l10n.planning;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N keys for Budget
 */
@Slf4j
public class BudgetL10N {

    /**
     * Holds the L10N keys for Budget
     */
    public enum Localization {
        DELETE("planning.budget.btn.delete"),
        DELETE_LABEL("planning.budget.delete.label");

        private String dbKey;
        private String value = "";

        Localization(String dbKey) {
            this.dbKey = dbKey;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
