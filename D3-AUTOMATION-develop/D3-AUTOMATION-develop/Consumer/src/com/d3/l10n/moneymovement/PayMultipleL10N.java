package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import com.d3.l10n.L10nCommon;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for Pay Multiple
 */
@Slf4j
public class PayMultipleL10N extends L10nCommon {

    /**
     * Holds the L10N values for Pay Multiple
     */
    public enum Localization {
        DEFAULT_WORKSHEET_FILTER("mm-worksheets.filter.default.bill-pay.all", false),
        DELETE_TEMPLATE_CONFIRM_TEXT("mm-worksheets.delete.confirm.text", true),
        DELETE_TEMPLATE_CONFIRM_TITLE("mm-worksheets.delete.confirm.title", false),
        DELETE_TEMPLATE_SUCCESS_TEXT("mm-worksheets.delete.success.text", false),
        DELETE_TEMPLATE_SUCCESS_TITLE("mm-worksheets.delete.success.title", false),
        LEAVE_PAGE("mm-worksheets.leave-page.text", false),
        SUBMIT_PAYMENT_CONFIRM("mm-worksheets.submit.confirm.text", true),
        SUBMITTED_WORKSHEET_SUMMARY_AMOUNT_LABEL("mm-worksheets.submit.summary.amount", false),
        SUBMITTED_WORKSHEET_SUMMARY_CONFIRMATION_NUMBER_LABEL("mm-worksheets.submit.summary.confirmation-number", false),
        SUBMITTED_WORKSHEET_SUMMARY_DATE_LABEL("mm-worksheets.submit.summary.date", false),
        SUBMITTED_WORKSHEET_SUMMARY_FROM_LABEL("mm-worksheets.submit.summary.source", false),
        SUBMITTED_WORKSHEET_SUMMARY_MEMO_LABEL("mm-worksheets.submit.summary.memo", false),
        SUBMITTED_WORKSHEET_SUMMARY_PAYMENT_TOTAL("mm-worksheets.payment-summary.payment-total", false),
        SUBMITTED_WORKSHEET_SUMMARY_RECIPIENT_LABEL("mm-worksheets.submit.summary.recipient", false),
        SUBMITTED_WORKSHEET_SUMMARY_TITLE("mm-worksheets.submit.summary.title", false),
        WORKSHEET_NAME_EXISTS("mm-worksheets.error.name-exists", false);

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
            return hasParameters ? replaceParameters(value) : value;
        }
    }
}
