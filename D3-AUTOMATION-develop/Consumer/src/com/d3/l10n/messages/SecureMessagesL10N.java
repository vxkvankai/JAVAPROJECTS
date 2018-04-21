package com.d3.l10n.messages;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class SecureMessagesL10N {
    public enum Localization {
        EMPTY("messages.secure.empty.template"),
        TOPIC_REQUIRED("messages.secure.validate.topic.required"),
        ISSUE_REQUIRED("messages.secure.validate.issue.required"),
        SUBJECT_REQUIRED("messages.secure.validate.subject.required"),
        BODY_REQUIRED("messages.secure.validate.body.required");

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
                    LoggerFactory.getLogger(SecureMessagesL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
