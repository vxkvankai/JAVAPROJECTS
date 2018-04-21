package com.d3.l10n.messages;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class NoticesL10N {

    public enum Localization {
        EMPTY("messages.empty.template", true);

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
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(NoticesL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }

    /**
     * Replaces any HTML tag elements and text between with %s. ex: EMPTY -> "You have no messages or alerts. Alerts can be configured in the <a
     * href="#settings/alerts">__settings__</a> tab." returns "You have no messages or alerts. Alerts can be configured in the %s tab."
     *
     * @return String local_value from l10n_text_resource table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("<[a-zA-Z].*?</[a-zA-Z].*?>", "%s");
    }
}
