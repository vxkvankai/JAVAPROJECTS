package com.d3.l10n.settings;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for security
 */
@Slf4j
public class SecurityL10N {

    /**
     * Holds the L10N values for security
     */
    public enum Localization {
        CHANGE_PASSWORD_SUCCESS("password.change.success"),
        CHANGE_SECURITY_QUESTIONS_SUCCESS("security-question.change.success"),
        CHANGE_USERNAME_SUCCESS("username.change.success"),
        CHANGE_USERNAME_WARNING("username.change.warning"),
        PASSWORD_PATTERN_DESCRIPTION("password.pattern-description");

        private String dbKey;
        private String value = "";

        Localization(String dbKey) {
            this.dbKey = dbKey;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
