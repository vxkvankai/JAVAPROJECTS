package com.d3.l10n.planning;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import com.d3.l10n.L10nCommon;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N keys for Goals
 */
@Slf4j
public class GoalsL10N extends L10nCommon {

    /**
     * Holds the L10N keys for Goals
     */
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
                    this.value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParametersGoals(value) : value;
        }
    }
}
