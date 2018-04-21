package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import com.d3.l10n.L10nCommon;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N keys for Bill Pay Enrollment
 */
@Slf4j
public class BillPayEnrollmentL10N extends L10nCommon {

    /**
     * Holds the L10N keys for Bill Pay Enrollment
     */
    public enum Localization {
        ENROLL_ADDRESS("billpay-enroll.address", false),
        ENROLL_EMAIL("billpay-enroll.email", false),
        ENROLL_ERROR("billpay-enroll.error.title", false),
        ENROLL_PHONE_NUMBER("billpay-enroll.phone", false),
        ENROLL_SUCCESS_TEXT("billpay-enroll.success.text", false),
        ENROLL_SUCCESS_TITLE("billpay-enroll.success.title", false),
        ENROLL_VALIDATE_ACCOUNT("billpay-enroll.validate.account", false),
        INFO_NEEDED("billpay-enroll.info-needed", true),
        START("billpay-enroll.start", false);

        private String dbKey;
        private String value = "";
        private boolean hasParameters;

        Localization(String dbKey, boolean hasParameters) {
            this.dbKey = dbKey;
            this.hasParameters = hasParameters;
        }


        public String getValue() {
            if (value == null) {
                value = "";
            }
            if (value.isEmpty()) {
                try {
                    value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }
}
