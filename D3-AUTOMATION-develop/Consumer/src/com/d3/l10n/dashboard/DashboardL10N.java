package com.d3.l10n.dashboard;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DashboardL10N {

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
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(DashboardL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }
    }
}
