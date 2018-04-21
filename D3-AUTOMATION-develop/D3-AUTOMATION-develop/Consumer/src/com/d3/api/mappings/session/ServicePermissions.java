
package com.d3.api.mappings.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicePermissions {
    private SettingsDashboard settingsDashboard;
    private DashboardPlan dashboardPlan;
    private SettingsAlerts settingsAlerts;
    private MoneyMovementSchedule moneyMovementSchedule;
    private SelfServiceFaq selfServiceFaq;
    private SettingsRules settingsRules;
    private SettingsUsermanagement settingsUsermanagement;
    private SettingsSecurity settingsSecurity;
    private MessagesSecure messagesSecure;
    private MoneyMovementPopmoney moneyMovementPopmoney;
    private SettingsAccounts settingsAccounts;
    private MoneyMovementRecipients moneyMovementRecipients;
    private PlanningBudget planningBudget;
    private SettingsCategories settingsCategories;
    private MoneyMovementMultiple moneyMovementMultiple;
    private SelfServiceRequestForms selfServiceRequestForms;
    private MoneyMovementPaymentApprovals moneyMovementPaymentApprovals;
    private TransactionsPositivePay transactionsPositivePay;
    private SettingsMobile settingsMobile;
    private PlanningGoals planningGoals;
    private Transactions transactions;
    private DashboardManage dashboardManage;
    private MoneyMovementEnroll moneyMovementEnroll;
    private MessagesNotices messagesNotices;
    private MoneyMovementEbills moneyMovementEbills;
    private SettingsBusinessProfile settingsBusinessProfile;
    private SettingsProfile settingsProfile;
    private Accounts accounts;
    private MoneyMovementAchUpload moneyMovementAchUpload;
}
