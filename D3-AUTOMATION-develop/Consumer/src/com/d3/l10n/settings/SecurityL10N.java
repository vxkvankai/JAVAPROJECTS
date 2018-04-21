package com.d3.l10n.settings;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SecurityL10N {

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
                    value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(SecurityL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
