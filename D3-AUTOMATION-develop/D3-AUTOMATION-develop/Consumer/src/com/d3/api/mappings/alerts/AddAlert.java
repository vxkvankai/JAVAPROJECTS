package com.d3.api.mappings.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.database.TransactionDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.alerts.ReminderAlert;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddAlert {

    private static final String ALWAYS = "ALWAYS";

    private List<UserDestination> userDestinations;
    private Boolean enabled;
    private String alertClass;
    private String alertType;
    private Integer alertId;
    private Boolean schedulable;
    private String frequency;
    private Properties properties;
    private String userAccountId;
    private Object freqAttr1;
    private Object freqAttr2;
    private Object freqAttr3; // NOSONAR need for api
    private Object freqAttr4; // NOSONAR need for api
    private String categoryId; // NOSONAR need for api
    private String description;

    public AddAlert(D3User user, D3Alert alert, int userDestination, int alertId) {
        this.alertClass = "USER";
        this.frequency = "DAYS";
        this.alertId = alertId;
        this.alertType = alert.getAlert().name();
        this.enabled = true;
        if (alert.hasFrequency()) {
            setFrequencyAttributes(alert.getFrequency());
            this.schedulable = true;
        }

        if (alert.getAccountUsed() != null) {
            this.userAccountId = String.valueOf(UserDatabaseHelper.waitForUserAccountId(user, alert.getAccountUsed()));
        }
        this.userDestinations = new ArrayList<>();
        this.userDestinations.add(UserDestination.sendToInbox(userDestination));
        setAllProperties(alert);
        if (alert.getAlert().equals(ConsumerAlerts.REMINDER)) {
            setDescription(((ReminderAlert) alert).getReminderDescription());
        }
    }

    private void setFrequencyAttributes(AlertFrequency frequency) {
        DayOfWeek[] dayOfWeek = DayOfWeek.values();
        Month[] months = Month.values();
        this.frequency = frequency.name();
        switch (frequency) {
            case DAYS:
                break;
            case WEEKLY:
                this.freqAttr1 = (Integer.toString(RandomHelper.getRandomElementFromArray(dayOfWeek).getValue()));
                break;
            case SEMI_MONTHLY:
                this.freqAttr1 = (Integer.toString(getRandomNumberInt(1, 12)));
                this.freqAttr2 = (Integer.toString(getRandomNumberInt(0, 30)));
                break;
            case MONTHLY:
                this.freqAttr1 = (Integer.toString(getRandomNumberInt(1, 12)));
                break;
            case QUARTERLY:
                this.freqAttr1 = (Integer.toString(getRandomNumberInt(1, 3)));
                this.freqAttr2 = (Integer.toString(getRandomNumberInt(0, 30)));
                break;
            case SEMI_ANNUALLY:
                setSemiAnnually(months);
                break;
            case ANNUALLY:
            default:
                this.freqAttr1 = (Integer.toString(RandomHelper.getRandomElementFromArray(months).getValue()));
                this.freqAttr2 = (Integer.toString(getRandomNumberInt(0, 30)));
                break;
        }
    }

    private void setSemiAnnually(Month[] months) {
        int month = RandomHelper.getRandomElementFromArray(months).getValue();
        int dayOfMonth = getRandomNumberInt(0, 30);
        this.freqAttr1 = Integer.toString(month);
        this.freqAttr2 = Integer.toString(dayOfMonth);
        this.freqAttr3 = month + 6;
        this.freqAttr4 = dayOfMonth;
    }

    private void setBudgetCategory() {
        this.properties = new Properties();
        setProperties(properties.categoryThreshold());
        String randomCategoryName = TransactionDatabaseHelper.getRandomCategoryName("CONSUMER");
        this.categoryId = Integer.toString(TransactionDatabaseHelper.getCategoryId(randomCategoryName, "CONSUMER"));
        this.schedulable = true;
    }

    private void setAllProperties(D3Alert alert) {
        switch (alert.getAlert()) {
            case BALANCE_THRESHOLD:
                this.properties = new Properties();
                setProperties(properties.balanceThreshold());
                this.schedulable = true;
                break;
            case BUDGET_CATEGORY_THRESHOLD:
                setBudgetCategory();
                break;
            case CHECK_CLEARED:
                this.properties = new Properties();
                setProperties(properties.checkCleared((CheckClearedAlert) alert));
                this.schedulable = true;
                break;
            case ACCOUNT_CREDIT:
                this.frequency = ALWAYS;
                this.schedulable = true;
                break;
            case TRANSACTION_MERCHANT:
                this.frequency = ALWAYS;
                this.properties = new Properties();
                this.properties = properties.merchantActivity((MerchantActivityAlert) alert);
                this.schedulable = true;
                break;
            case PAYMENT_COMING_DUE:
                this.frequency = "MONTHLY";
                this.schedulable = true;
                break;
            case BUDGET_TOTAL_THRESHOLD:
                this.properties = new Properties();
                setProperties(properties.totalBudgetThreshold());
                this.schedulable = true;
                break;
            case TRANSACTION_AMOUNT:
                this.properties = new Properties();
                setProperties(properties.transactionExceedsAmount());
                this.frequency = ALWAYS;
                this.schedulable = true;
                break;
            default:
                break;
        }
    }
}

