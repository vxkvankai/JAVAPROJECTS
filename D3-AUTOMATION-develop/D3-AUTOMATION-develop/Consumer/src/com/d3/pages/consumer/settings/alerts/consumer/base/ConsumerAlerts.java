package com.d3.pages.consumer.settings.alerts.consumer.base;

import com.d3.database.AlertDatabaseHelper;
import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.CheckForNull;

/**
 * Enum that describes the different Consumer alert types
 */
@Slf4j
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

    protected String description = "";
    protected String displayedName = "";
    protected String inboxSubject = "";
    protected String messageDetails = "";

    public static ConsumerAlerts getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))];
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

    public String getDisplayedName() {
        if (displayedName.isEmpty()) {
            try {
                displayedName = DatabaseHelper.getL10nValueString(String.format("settings.alerts.type.%s", this.toString().toLowerCase()));
            } catch (D3DatabaseException e) {
                log.error("Error getting the L10N value string for the setting alert type: {}. {}", this.toString(), e);
            }
        }
        return displayedName;
    }

    public String getDescription() {
        if (description.isEmpty()) {
            description = AlertDatabaseHelper.getAlertDescription(this);
            if (description == null) {
                log.error("Error getting the alert description the setting alert type: {}.", this);
                description = "";
            }
        }
        return description;
    }

    public String getMessageSubject() {
        if (inboxSubject.isEmpty()) {
            inboxSubject = AlertDatabaseHelper.getTriggeredAlertEmailInfo(this, "subject");
            if (inboxSubject == null) {
                log.error("Error getting the alert message subject for the setting alert type: {}.", this);
                inboxSubject = "";
            }
        }
        return inboxSubject;
    }

    @CheckForNull
    public String getMessageDetails() {
        if (messageDetails.isEmpty()) {
            messageDetails = AlertDatabaseHelper.getTriggeredAlertEmailInfo(this, "template1");
            if (messageDetails == null) {
                log.error("Error getting the alert message details for the setting alert type: {}.", this);
                messageDetails = "";
            }
        }
        return messageDetails.isEmpty() ? null : replaceParameters(messageDetails);
    }
}

