package com.d3.l10n.settings;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for Settings accounts
 */
@Slf4j
public class SettingsAccountsL10N {

    /**
     * Holds the L10N values for Settings accounts
     */
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
                    this.value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
