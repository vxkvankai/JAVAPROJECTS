package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class ScheduleL10N {

    public enum Localization {
        RECURRING_END_DATE("schedule.recurring.end-date.text", true),
        RECURRING_INDEFINITE("schedule.recurring.indefinite.text", true),
        RECURRING_MODAL_TEXT("schedule.form.add.modal.recurring.text", true),
        RECURRING_NUMBER_OF_TXNS("schedule.recurring.occurrences.text", true),
        SINGLE_TRANSFER_MODAL_TEXT("schedule.form.add.modal.text", true),
        SUBHEADING("schedule.subheading", false),
        THRESHOLD_DETAIL("schedule.detail.threshold", true),
        ENTER_AMOUNT_ERROR("schedule.transfer.validate.amount.required", false),
        ENTER_SCHEDULED_DATE_ERROR("schedule.transfer.validate.date.required", false),
        RANGE_VALIDATION_ERROR("schedule.transfer.validate.amount.range", true),
        CONFIRM_DELETE_MSG("schedule.detail.delete.modal.text", false),
        CANCELED_STATUS_TEXT("schedule.status.cancelled", false),
        CANCELED_TRANSFER_TEXT("schedule.transfer.cancelled", false),
        PENDING_TRANSFER_TEXT("schedule.status.pending", false),
        PROCESSED_TRANSFER_TEXT("schedule.status.processed", false),
        EXPEDITED_TITLE("schedule.form.expedited.title", false),
        SUCCESS_TEXT_SCHEDULED_TRANSACTION("schedule.form.result.text.success", false),
        SUCCESS_TITLE_SCHEDULED_TRANSACTION("schedule.form.result.title.success", false);

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
                    value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(ScheduleL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }

    /**
     * Replaces any text between underscores or curly brackets with %s. If text between underscores contains amount or thresholdAmount it replaces it
     * with $%s ex: RECURRING_MODAL_TEXT -> "Are you sure you wish to schedule __amount__ transactions to __destination__ from __source__
     * __frequency__?" returns "Are you sure you wish to schedule $%s transactions to %s from %s %s?"
     *
     * @return String local_value from l10n_text_resource table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("__(\\B(?:amount|thresholdAmount)\\B)__", "\\$%s").replaceAll("__([a-zA-Z]+)__", "%s")
                .replaceAll("\\{(.*?)\\}", "\\$%s");
    }
}
