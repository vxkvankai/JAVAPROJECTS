package com.d3.pages.consumer.settings.alerts.consumer.base;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.alerts.AccountBalanceAlert;
import com.d3.datawrappers.alerts.BalanceThresholdAlert;
import com.d3.datawrappers.alerts.BudgetCategoryThresholdAlert;
import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.CreditDepositAlert;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.alerts.PaymentComingDueAlert;
import com.d3.datawrappers.alerts.ReminderAlert;
import com.d3.datawrappers.alerts.TotalBudgetThresholdAlert;
import com.d3.datawrappers.alerts.TransactionAmountExceedsAlert;
import com.d3.datawrappers.alerts.enums.BalanceThreshold;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;

public enum ConsumerAlerts {
    ACCOUNT_CREDIT,
    BALANCE_THRESHOLD,
    BUDGET_CATEGORY_THRESHOLD,
    BUDGET_TOTAL_THRESHOLD,
    CHECK_CLEARED,
    PAYMENT_COMING_DUE,
    PERIODIC_BALANCE,
    REMINDER,
    TRANSACTION_AMOUNT,
    TRANSACTION_MERCHANT;

    public static ConsumerAlerts getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))];
    }

    protected String description = "";
    protected String displayedName = "";
    protected String inboxSubject = "";
    protected String messageDetails = "";

    public String getDisplayedName() {
        if (displayedName.isEmpty()) {
            try {
                displayedName = DatabaseUtils.getL10nValueString(String.format("settings.alerts.type.%s", this.toString().toLowerCase()));
            } catch (SQLException e) {
                LoggerFactory.getLogger(ConsumerAlerts.class)
                        .error("Error getting the L10N value string for the setting alert type: {}. {}", this.toString(), e);
            }
        }
        return displayedName;
    }

    public String getDescription() {
        if (description.isEmpty()) {
            try {
                description = DatabaseUtils.getAlertDescription(this);
            } catch (SQLException e) {
                LoggerFactory.getLogger(ConsumerAlerts.class)
                        .error("Error getting the alert description the setting alert type: {}. {}", this.toString(), e);
            }
        }
        return description;
    }

    public String getMessageSubject() {
        if (inboxSubject.isEmpty()) {
            try {
                inboxSubject = DatabaseUtils.getTriggeredAlertEmailInfo(this, "subject");
            } catch (SQLException e) {
                LoggerFactory.getLogger(ConsumerAlerts.class)
                        .error("Error getting the alert message subject for the setting alert type: {}. {}", this.toString(), e);
            }
        }
        return inboxSubject;
    }

    @Nullable
    public String getMessageDetails() {
        if (messageDetails.isEmpty()) {
            try {
                messageDetails = DatabaseUtils.getTriggeredAlertEmailInfo(this, "template1");
            } catch (SQLException e) {
                LoggerFactory.getLogger(ConsumerAlerts.class)
                        .error("Error getting the alert message details for the setting alert type: {}. {}", this.toString(), e);
                return null;
            }
        }
        return messageDetails != null ? replaceParameters(messageDetails) : null;


    }

    /**
     * Replaces any text between curly brackets {{text}} with %s.
     * ex: TRANSACTION_MERCHANT message template -> "Transaction merchant matches {{TX_DESCRIPTION}} for {{ACCOUNT_NAME}} on {{TX_DATE}}."
     * returns "Transaction merchant matches %s for %s on %s."
     *
     * @return String template1 column from alert_template table.
     */
    private static String replaceParameters(String value) {
        return value.replaceAll("\\{\\{(.*?)\\}\\}", "%s");
    }

}

