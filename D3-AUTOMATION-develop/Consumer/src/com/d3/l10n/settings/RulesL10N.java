package com.d3.l10n.settings;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class RulesL10N {

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
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(RulesL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }
    }
}
