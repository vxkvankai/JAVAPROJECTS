package com.d3.api.mappings.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.alerts.ReminderAlert;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class AddAlert {

    private static final String ALWAYS = "ALWAYS";

    @SerializedName("userDestinations")
    @Expose
    private List<UserDestination> userDestinations = null;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("alertClass")
    @Expose
    private String alertClass;
    @SerializedName("alertType")
    @Expose
    private String alertType;
    @SerializedName("alertId")
    @Expose
    private Integer alertId;
    @SerializedName("schedulable")
    @Expose
    private Boolean schedulable;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("userAccountId")
    @Expose
    private String userAccountId;
    @SerializedName("freqAttr1")
    @Expose
    private Object freqAttr1;
    @SerializedName("freqAttr2")
    @Expose
    private Object freqAttr2;
    @SerializedName("freqAttr3")
    @Expose
    private Object freqAttr3;
    @SerializedName("freqAttr4")
    @Expose
    private Object freqAttr4;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("description")
    @Expose
    private String description;

    public List<UserDestination> getUserDestinations() {
        return userDestinations;
    }

    public void setUserDestinations(List<UserDestination> userDestinations) {
        this.userDestinations = userDestinations;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAlertClass() {
        return alertClass;
    }

    public void setAlertClass(String alertClass) {
        this.alertClass = alertClass;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public Boolean getSchedulable() {
        return schedulable;
    }

    public void setSchedulable(Boolean schedulable) {
        this.schedulable = schedulable;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Object getFreqAttrOneTime() {
        return freqAttr1;
    }

    public void setFreqAttr1(Object freqAttrOneTime) {
        this.freqAttr1 = freqAttrOneTime;
    }

    public Object getFreqAttr2() {
        return freqAttr2;
    }

    public void setFreqAttr2(Object freqAttr2) {
        this.freqAttr2 = freqAttr2;
    }

    public Object getFreqAttr3() {
        return freqAttr3;
    }

    public void setFreqAttr3(Object freqAttr3) {
        this.freqAttr3 = freqAttr3;
    }

    public Object getFreqAttr4() {
        return freqAttr4;
    }

    public void setFreqAttr4(Object freqAttr4) {
        this.freqAttr4 = freqAttr4;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddAlert(D3User user, D3Alert alert, int userDestination, int alertId) {
        this.alertClass = "USER";
        this.frequency = "DAYS";
        this.alertId = alertId;
        this.alertType = alert.getAlert().name();
        this.enabled = true;
        if(alert.hasFrequency()) {
            setFrequencyAttributes(alert.getFrequency());
            this.schedulable = true;
        }

        if(alert.getAccountUsed() != null) {
            this.userAccountId = String.valueOf(DatabaseUtils.waitForUserAccountId(user, alert.getAccountUsed()));
        }
        this.userDestinations = new ArrayList<>();
        this.userDestinations.add(UserDestination.sendToInbox(userDestination));
        setProperties(alert);
        if (alert.getAlert().equals(ConsumerAlerts.REMINDER)) {
            setDescription(((ReminderAlert) alert).getReminderDescription());
        }
    }

    private void setFrequencyAttributes(AlertFrequency frequency) {
        DayOfWeek[] dayofWeek = DayOfWeek.values();
        Month[] months = Month.values();
        this.frequency = frequency.name();
        switch (frequency) {
            case DAYS:
                break;
            case WEEKLY:
                this.freqAttr1 = (Integer.toString(RandomHelper.getRandomElementFromArray(dayofWeek).getValue()));
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
                int month = RandomHelper.getRandomElementFromArray(months).getValue();
                int dayOfMonth = getRandomNumberInt(0, 30);
                this.freqAttr1 = Integer.toString(month);
                this.freqAttr2 = Integer.toString(dayOfMonth);
                this.freqAttr3 = month + 6;
                this.freqAttr4 = dayOfMonth;
                break;
            case ANNUALLY:
            default:
                this.freqAttr1 = (Integer.toString(RandomHelper.getRandomElementFromArray(months).getValue()));
                this.freqAttr2 = (Integer.toString(getRandomNumberInt(0, 30)));
                break;
        }
    }

    private void setProperties(D3Alert alert) {
        switch (alert.getAlert()) {
            case BALANCE_THRESHOLD:
                this.properties = new Properties();
                setProperties(properties.balanceThreshold());
                this.schedulable = true;
                break;
            case BUDGET_CATEGORY_THRESHOLD:
                this.properties = new Properties();
                setProperties(properties.categoryThreshold());
                String randomCategoryName = DatabaseUtils.getRandomCategoryName("CONSUMER");
                this.categoryId = Integer.toString(DatabaseUtils.getCategoryId(randomCategoryName, "CONSUMER"));
                this.schedulable = true;
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

