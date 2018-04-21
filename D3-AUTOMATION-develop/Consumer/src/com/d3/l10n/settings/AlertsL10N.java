package com.d3.l10n.settings;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class AlertsL10N {

    public enum Localization {
        DELETE_CONFIRM("settings.alerts.delete.title");

        private String dbKey;
        private String value = "";

        Localization(String dbKey) {
            this.dbKey = dbKey;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(AlertsL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
