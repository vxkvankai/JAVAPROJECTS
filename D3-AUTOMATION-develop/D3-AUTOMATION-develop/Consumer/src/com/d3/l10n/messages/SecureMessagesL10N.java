package com.d3.l10n.messages;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for Secure Messages
 */
@Slf4j
public class SecureMessagesL10N {

    /**
     * Holds the L10N values for Secure Messages
     */
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
                    value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
