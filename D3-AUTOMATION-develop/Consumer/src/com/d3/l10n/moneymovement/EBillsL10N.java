package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class EBillsL10N {
    public enum Localization {
        ADDITIONAL_DETAILS_LINK("ebills.received.details.link.info"),
        AUTOPAY_ACTIVE("ebills.active.autopay"),
        AUTOPAY_DELETE_CONFIRM_TITLE("ebills.autopay.confirm.title"),
        AUTOPAY_DELETE_CONFIRM_TEXT("ebills.autopay.confirm.text"),
        AUTOPAY_STATUS_ACCOUNT("ebills.autopay.status.account"),
        AUTOPAY_STATUS_ALERT_SCHEDULED("ebills.autopay.status.alert.scheduled"),
        AUTOPAY_STATUS_ALERT_SENT("ebills.autopay.status.alert.sent"),
        AUTOPAY_STATUS_AMOUNT("ebills.autopay.status.amount"),
        AUTOPAY_STATUS_FIXED_AMOUNT("ebills.autopay.status.fixed-amount"),
        AUTOPAY_STATUS_LEAD_DAYS("ebills.autopay.status.lead-days"),
        AUTOPAY_STATUS_MAX_AMOUNT("ebills.autopay.status.max-amount"),
        AUTOPAY_STATUS_PAY_ON("ebills.autopay.status.pay-on"),
        AUTOPAY_VALIDATE_FIXED_AMOUNT("ebills.autopay.validate.fixed-amount"),
        AUTOPAY_VALIDATE_LEAD_DAYS("ebills.autopay.validate.lead-days"),
        AUTOPAY_VALIDATE_PAY_AMOUNT("ebills.autopay.validate.pay-amount"),
        AUTOPAY_VALIDATE_PAY_ON("ebills.autopay.validate.pay-on"),
        BALANCE("ebills.received.details.balance"),
        FILE_EBILL("ebills.file.title"),
        FILE_NOTE("ebills.file.note.label"),
        FILED_NOTE("ebills.received.details.filed-note"),
        FILE_REASON("ebills.file.reason"),
        FILED_REASON("ebills.received.details.filed-reason"),
        MINIMUM_DUE("ebills.received.details.min-due"),
        NO_ACTIVE_EBILLERS("ebills.none.active"),
        NO_EBILLS_RECEIVED("ebills.none.received"),
        REMOVE_EBILLER_TEXT("ebills.active.confirm.text"),
        REMOVE_EBILLER_TITLE("ebills.active.confirm.title"),
        SUPRESS_PAPER_STATEMENT_SUCCESS("ebills.active.success.text");

        private String dbKey;
        private String value = "";

        Localization(String dbKey) {
            this.dbKey = dbKey;
        }


        public String getValue() {
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(BillPayEnrollmentL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }
    }
}

