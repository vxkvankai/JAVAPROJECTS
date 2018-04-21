package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class PayMultipleL10N {

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
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(PayMultipleL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }

    /**
     * Replaces any HTML tag elements and text between underscores with %s. ex: DELETE_TEMPLATE_CONFIRM_TEXT -> "Note: Any payments already scheduled via this template <u>will not
     * be deleted</u>. If you wish to delete previously scheduled payments you can do so from the <em>Money Movement Schedule</em> tab. Please confirm
     * that you wish to delete the payment template." returns "Note: Any payments already scheduled via this template %s. If you wish to delete
     * previously scheduled payments you can do so from the %s tab. Please confirm that you wish to delete the payment template."
     *
     * @return String local_value from l10n_text_resource table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("<[a-zA-Z].*?</[a-zA-Z].*?>", "%s").replaceAll("__([a-zA-Z]+)__", "%s");
    }
}
