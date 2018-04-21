package com.d3.l10n.dashboard;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N Keys for the dashboard
 */
@Slf4j
public class DashboardL10N {

    /**
     * Holds the L10N Keys for the dashboard
     */
    public enum Localization {
        CASHFLOW_STATUS("dashboard.cashflow.status"),
        DEPOSIT_ACCOUNTS("dashboard.accounts.deposit-accounts"),
        NO_BUDGET("dashboard.budget.forecast.none"),
        PAY_TRANSFER_WIDGET("dashboard.widget.pay-transfer.btn"),
        QUICKPAY("dashboard.widget.quick-pay"),
        ZELLE_WIDGET("dashboard.widget.zelle.btn");

        private String dbKey;
        private String value = "";

        Localization(String dbKey) {
            this.dbKey = dbKey;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }
    }
}
