package com.d3.l10n;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the common L10N keys and some helper methods for sub L10N classes
 */
@Slf4j
public class L10nCommon {

    /**
     * Replaces any HTML tag elements and text between with %s. ex: EMPTY -> "You have no messages or alerts. Alerts can be configured in the <a
     * href="#settings/alerts">__settings__</a> tab." returns "You have no messages or alerts. Alerts can be configured in the %s tab."
     *
     * @return String local_value from l10n_text_resource table.
     */
    protected static String replaceParameters(String value) {
        return value.replaceAll("<[a-zA-Z].*?</[a-zA-Z].*?>", "%s").replaceAll("__([a-zA-Z]+)__", "%s");
    }

    protected static String replaceParametersSchedule(String value) {
        return value.replaceAll("__(\\B(?:amount|thresholdAmount)\\B)__", "\\$%s").replaceAll("__([a-zA-Z]+)__", "%s")
            .replaceAll("\\{(.*?)\\}", "\\$%s");
    }

    protected static String replaceParametersGoals(String value) {
        return value.replaceAll("__(\\B(?:rate|percentage)\\B)__", "%s%");
    }

    /**
     * Holds the common L10N keys
     */
    public enum Localization {
        OOB_VERIFICATION_SENT("out-of-band.description"),
        CHARACTERS_NOT_ALLOWED("pattern.text-field"),
        ENTER_VALID_URL("pattern.url"),
        ENTER_ONLY_DIGITS("pattern.digits");

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
