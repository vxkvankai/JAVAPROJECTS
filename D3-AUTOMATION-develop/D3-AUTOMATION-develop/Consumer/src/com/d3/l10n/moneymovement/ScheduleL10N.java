package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import com.d3.l10n.L10nCommon;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for Schedule Transfer
 */
@Slf4j
public class ScheduleL10N extends L10nCommon {

    /**
     * Holds the L10N values for Schedule Transfer
     */
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
                    value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? ScheduleL10N.replaceParametersSchedule(value) : value;
        }
    }
}
