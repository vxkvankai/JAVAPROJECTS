package com.d3.l10n.accounts;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

/**
 * Holds the L10N keys for Accounts
 */
@Slf4j
public class AccountsL10N {

    /**
     * Replaces any text between underscores %s. If text between underscores contains amount it replaces it with $%s ex: STOP_PAYMENT_RANGE_SUCCESS ->
     * "Your stop payment request for check number __checkNumber__ issued to __payee__ for the amount of __amount__ has been received and will be
     * processed. Tracking Number: __number__. Additional InfoText: __statusText__." returns "Your stop payment request for check number %s issued to
     * %s for the amount of $%s has been received and will be processed. Tracking Number: %s. Additional InfoText: %s."
     *
     * @return String local_value from l10n_text_resource table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("__(\\B(?:amount)\\B)__", "\\$%s").replaceAll("__([a-zA-Z]+)__", "%s");
    }

    /**
     * Enum holding the keys for Accounts L10Ns
     */
    public enum Localization {
        ADD_OFFLINE("accounts.add.offline", false),
        AGGREGATION_ERROR_PASSWORD("accounts.agg.error.password", false),
        AGGREGATION_SELECT_INSTITUTION("accounts.agg.select.institution", false),
        BALANCE_REQUIRED("accounts.validate.balance.required", false),
        DELETE_OFFLINE_ACCOUNT("accounts.delete.offline.text", false),
        ESTATEMENT_ENROLL_SUCCESS("accounts.estmt.enrollment.success.title", false),
        GO_PAPERLESS_TITLE("accounts.estmt.go-paperless", false),
        GO_PAPPERLESS_ENROLL_SUCCESS("accounts.estmt.go-paperless.success.title", false),
        EXCLUDED("accounts.excluded", false),
        EXCLUDE_ASSET_CONFIRM("accounts.asset.exclude.text.confirm", false),
        EXCLUDE_LIABILITY_CONFIRM("accounts.liability.exclude.text.confirm", false),
        FORM_BALANCE("accounts.form.balance", false),
        FORM_NAME("accounts.form.name", false),
        FORM_TYPE("accounts.form.type", false),
        HIDE_CONFIRM("accounts.hideAccount.confirmation", false),
        INCLUDE_ASSET_CONFIRM("accounts.asset.exclude.text.undo", false),
        INCLUDE_LIABILITY_CONFIRM("accounts.liability.exclude.text.undo", false),
        NICKNAME_REQUIRED("accounts.validate.nickname.required", false),
        OTHER_ACCOUNTS_SECTION("accounts.section.other", false),
        PASSWORD_SUCCESS("accounts.password.success", false),
        PRODUCT_REQUIRED("accounts.validate.product.required", false),
        REG_E_ENROLL_ELECTRONIC_TRANSACTIONS("reg-e.enroll.success.check-electronic-transactions.text", false),
        REG_E_ENROLL_SUCCESS_TITLE("reg-e.enroll.success.title", false),
        REG_E_ENROLL_ATM_DEBIT_TRANSACTIONS("reg-e.enroll.success.atm-debit-card-transactions.text", false),
        REG_E_UNENROLL_SUCCESS_TITLE("reg-e.unenroll.success.title", false),
        REG_E_UNENROLL_ATM_DEBIT_TRANSACTIONS("reg-e.unenroll.success.atm-debit-card-transactions.text", false),
        REG_E_UNENROLL_ELECTRONIC_TRANSACTIONS("reg-e.unenroll.success.check-electronic-transactions.text", false),
        REQUEST_SUBMITTED("accounts.agg.request.complete", false),
        STOP_PAYMENT_CONFIRM("accounts.stop.confirm", false),
        STOP_PAYMENT_RANGE_FAIL("accounts.stopPayment.failed.range", true),
        STOP_PAYMENT_RANGE_SUCCESS("accounts.stopPayment.success.range", true),
        STOP_PAYMENT_RANGE_VALIDATE("accounts.stop.validate.check.start-end", false),
        STOP_PAYMENT_SINGLE_SUCCESS("accounts.stopPayment.success.single", true);

        private String dbKey;
        private String value = "";
        private boolean hasParameters;

        Localization(@Nonnull String dbKey, boolean hasParameters) {
            this.dbKey = dbKey;
            this.hasParameters = hasParameters;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseHelper.getL10nValueString(dbKey);
                    if (value == null) {
                        log.error("Database l10n for {} came back null, setting to blank", dbKey);
                        value = "";
                    }
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }
}
