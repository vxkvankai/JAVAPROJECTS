package com.d3.l10n.planning;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class GoalsL10N {

    public enum Localization {
        BIRTH_DATE_REQUIRED("planning.goals.validate.birthday", false),
        DELETE_TEXT("planning.goals.delete.text", false),
        DELETE_TITLE("planning.goals.delete.title", false),
        END_DATE_REQUIRED("planning.goals.validate.end-date", false),
        GOAL_AMOUNT_RANGE("planning.goals.validate.target-amount.range", false),
        GOAL_AMOUNT_REQUIRED("planning.goals.validate.target-amount.required", false),
        GOAL_NAME_REQUIRED("planning.goals.validate.name", false),
        NO_GOALS("planning.goals.no-goals", false),
        RATE_OF_RETURN_DETAIL("planning.goals.detail.return-rate", true),
        RETIREMENT_AGE_RANGE("planning.goals.validate.retirement-age.range", false);

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
                    LoggerFactory.getLogger(GoalsL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }

    /**
     * Replaces any text between underscores that contain percentage or rate with %s%
     * ex: RATE_OF_RETURN_DETAIL -> "A __rate__% expected rate of return is applied to your goal"
     * returns "A %s%% expected rate of return is applied to your goal"
     *
     * @return String local_value from l10n_text_resource table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("__(\\B(?:rate|percentage)\\B)__", "%s%");
    }
}
