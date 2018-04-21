package com.d3.l10n.settings;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for User information
 */
@Slf4j
public class UsersL10N {

    /**
     * Holds the L10N values for User information
     */
    public enum Localization {
        NO_MONEY_MOVEMENT_ACCESS("user-mgmt.account.mm.no-mm"),
        NO_SERVICES("user-mgmt.services.no-services"),
        STATEMENTS_AND_TRANSACTION_ACCESS("user-mgmt.account.mm.access");

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
