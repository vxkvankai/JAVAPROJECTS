package com.d3.l10n.settings;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SettingsAccountsL10N {

    public enum Localization {
        NO_HIDDEN_ACCOUNTS("settings.accounts.no-hidden");

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
                    LoggerFactory.getLogger(SettingsAccountsL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
