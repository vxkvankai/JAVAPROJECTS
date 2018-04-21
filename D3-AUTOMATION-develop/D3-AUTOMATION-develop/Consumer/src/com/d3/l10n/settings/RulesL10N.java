package com.d3.l10n.settings;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for Rules
 */
@Slf4j
public class RulesL10N {

    /**
     * Holds the L10N values for Rules
     */
    public enum Localization {
        CATEGORY_DELETE_CONFIRM("settings.rules.category.confirm-text"),
        NO_CATEGORY_RULES("settings.rules.category.none"),
        NO_RENAMING_RULES("settings.rules.renaming.none"),
        RENAMING_DELETE_CONFIRM("settings.rules.renaming.confirm-text");

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
