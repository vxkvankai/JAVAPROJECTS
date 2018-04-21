package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class BillPayEnrollmentL10N {

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
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(BillPayEnrollmentL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }

    /**
     * Replaces any HTML tag elements and text between with %s. ex: INFO_NEEDED -> "Bill Pay Enrollment requires your first and last name, home phone,
     * and physical address. You can fill in this information on the <a href="#settings">Profile</a> screen prior to enrolling." returns "Bill Pay
     * Enrollment requires your first and last name, home phone, and physical address. You can fill in this information on the %s screen prior to
     * enrolling."
     *
     * @return String local_value from l10n_text_resource table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("<[a-zA-Z].*?</[a-zA-Z].*?>", "%s");
    }
}
