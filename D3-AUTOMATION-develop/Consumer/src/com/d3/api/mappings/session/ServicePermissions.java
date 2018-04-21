
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicePermissions {

    @SerializedName("settings.dashboard")
    @Expose
    private SettingsDashboard settingsDashboard;
    @SerializedName("dashboard.plan")
    @Expose
    private DashboardPlan dashboardPlan;
    @SerializedName("settings.alerts")
    @Expose
    private SettingsAlerts settingsAlerts;
    @SerializedName("money-movement.schedule")
    @Expose
    private MoneyMovementSchedule moneyMovementSchedule;
    @SerializedName("self-service.faq")
    @Expose
    private SelfServiceFaq selfServiceFaq;
    @SerializedName("settings.rules")
    @Expose
    private SettingsRules settingsRules;
    @SerializedName("settings.usermanagement")
    @Expose
    private SettingsUsermanagement settingsUsermanagement;
    @SerializedName("settings.security")
    @Expose
    private SettingsSecurity settingsSecurity;
    @SerializedName("messages.secure")
    @Expose
    private MessagesSecure messagesSecure;
    @SerializedName("money-movement.popmoney")
    @Expose
    private MoneyMovementPopmoney moneyMovementPopmoney;
    @SerializedName("settings.accounts")
    @Expose
    private SettingsAccounts settingsAccounts;
    @SerializedName("money-movement.recipients")
    @Expose
    private MoneyMovementRecipients moneyMovementRecipients;
    @SerializedName("planning.budget")
    @Expose
    private PlanningBudget planningBudget;
    @SerializedName("settings.categories")
    @Expose
    private SettingsCategories settingsCategories;
    @SerializedName("money-movement.multiple")
    @Expose
    private MoneyMovementMultiple moneyMovementMultiple;
    @SerializedName("self-service.request-forms")
    @Expose
    private SelfServiceRequestForms selfServiceRequestForms;
    @SerializedName("money-movement.payment-approvals")
    @Expose
    private MoneyMovementPaymentApprovals moneyMovementPaymentApprovals;
    @SerializedName("transactions.positive-pay")
    @Expose
    private TransactionsPositivePay transactionsPositivePay;
    @SerializedName("settings.mobile")
    @Expose
    private SettingsMobile settingsMobile;
    @SerializedName("planning.goals")
    @Expose
    private PlanningGoals planningGoals;
    @SerializedName("transactions")
    @Expose
    private Transactions transactions;
    @SerializedName("dashboard.manage")
    @Expose
    private DashboardManage dashboardManage;
    @SerializedName("money-movement.enroll")
    @Expose
    private MoneyMovementEnroll moneyMovementEnroll;
    @SerializedName("messages.notices")
    @Expose
    private MessagesNotices messagesNotices;
    @SerializedName("money-movement.ebills")
    @Expose
    private MoneyMovementEbills moneyMovementEbills;
    @SerializedName("settings.business.profile")
    @Expose
    private SettingsBusinessProfile settingsBusinessProfile;
    @SerializedName("settings.profile")
    @Expose
    private SettingsProfile settingsProfile;
    @SerializedName("accounts")
    @Expose
    private Accounts accounts;
    @SerializedName("money-movement.ach-upload")
    @Expose
    private MoneyMovementAchUpload moneyMovementAchUpload;

    public SettingsDashboard getSettingsDashboard() {
        return settingsDashboard;
    }

    public void setSettingsDashboard(SettingsDashboard settingsDashboard) {
        this.settingsDashboard = settingsDashboard;
    }

    public DashboardPlan getDashboardPlan() {
        return dashboardPlan;
    }

    public void setDashboardPlan(DashboardPlan dashboardPlan) {
        this.dashboardPlan = dashboardPlan;
    }

    public SettingsAlerts getSettingsAlerts() {
        return settingsAlerts;
    }

    public void setSettingsAlerts(SettingsAlerts settingsAlerts) {
        this.settingsAlerts = settingsAlerts;
    }

    public MoneyMovementSchedule getMoneyMovementSchedule() {
        return moneyMovementSchedule;
    }

    public void setMoneyMovementSchedule(MoneyMovementSchedule moneyMovementSchedule) {
        this.moneyMovementSchedule = moneyMovementSchedule;
    }

    public SelfServiceFaq getSelfServiceFaq() {
        return selfServiceFaq;
    }

    public void setSelfServiceFaq(SelfServiceFaq selfServiceFaq) {
        this.selfServiceFaq = selfServiceFaq;
    }

    public SettingsRules getSettingsRules() {
        return settingsRules;
    }

    public void setSettingsRules(SettingsRules settingsRules) {
        this.settingsRules = settingsRules;
    }

    public SettingsUsermanagement getSettingsUsermanagement() {
        return settingsUsermanagement;
    }

    public void setSettingsUsermanagement(SettingsUsermanagement settingsUsermanagement) {
        this.settingsUsermanagement = settingsUsermanagement;
    }

    public SettingsSecurity getSettingsSecurity() {
        return settingsSecurity;
    }

    public void setSettingsSecurity(SettingsSecurity settingsSecurity) {
        this.settingsSecurity = settingsSecurity;
    }

    public MessagesSecure getMessagesSecure() {
        return messagesSecure;
    }

    public void setMessagesSecure(MessagesSecure messagesSecure) {
        this.messagesSecure = messagesSecure;
    }

    public MoneyMovementPopmoney getMoneyMovementPopmoney() {
        return moneyMovementPopmoney;
    }

    public void setMoneyMovementPopmoney(MoneyMovementPopmoney moneyMovementPopmoney) {
        this.moneyMovementPopmoney = moneyMovementPopmoney;
    }

    public SettingsAccounts getSettingsAccounts() {
        return settingsAccounts;
    }

    public void setSettingsAccounts(SettingsAccounts settingsAccounts) {
        this.settingsAccounts = settingsAccounts;
    }

    public MoneyMovementRecipients getMoneyMovementRecipients() {
        return moneyMovementRecipients;
    }

    public void setMoneyMovementRecipients(MoneyMovementRecipients moneyMovementRecipients) {
        this.moneyMovementRecipients = moneyMovementRecipients;
    }

    public PlanningBudget getPlanningBudget() {
        return planningBudget;
    }

    public void setPlanningBudget(PlanningBudget planningBudget) {
        this.planningBudget = planningBudget;
    }

    public SettingsCategories getSettingsCategories() {
        return settingsCategories;
    }

    public void setSettingsCategories(SettingsCategories settingsCategories) {
        this.settingsCategories = settingsCategories;
    }

    public MoneyMovementMultiple getMoneyMovementMultiple() {
        return moneyMovementMultiple;
    }

    public void setMoneyMovementMultiple(MoneyMovementMultiple moneyMovementMultiple) {
        this.moneyMovementMultiple = moneyMovementMultiple;
    }

    public SelfServiceRequestForms getSelfServiceRequestForms() {
        return selfServiceRequestForms;
    }

    public void setSelfServiceRequestForms(SelfServiceRequestForms selfServiceRequestForms) {
        this.selfServiceRequestForms = selfServiceRequestForms;
    }

    public MoneyMovementPaymentApprovals getMoneyMovementPaymentApprovals() {
        return moneyMovementPaymentApprovals;
    }

    public void setMoneyMovementPaymentApprovals(MoneyMovementPaymentApprovals moneyMovementPaymentApprovals) {
        this.moneyMovementPaymentApprovals = moneyMovementPaymentApprovals;
    }

    public TransactionsPositivePay getTransactionsPositivePay() {
        return transactionsPositivePay;
    }

    public void setTransactionsPositivePay(TransactionsPositivePay transactionsPositivePay) {
        this.transactionsPositivePay = transactionsPositivePay;
    }

    public SettingsMobile getSettingsMobile() {
        return settingsMobile;
    }

    public void setSettingsMobile(SettingsMobile settingsMobile) {
        this.settingsMobile = settingsMobile;
    }

    public PlanningGoals getPlanningGoals() {
        return planningGoals;
    }

    public void setPlanningGoals(PlanningGoals planningGoals) {
        this.planningGoals = planningGoals;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public DashboardManage getDashboardManage() {
        return dashboardManage;
    }

    public void setDashboardManage(DashboardManage dashboardManage) {
        this.dashboardManage = dashboardManage;
    }

    public MoneyMovementEnroll getMoneyMovementEnroll() {
        return moneyMovementEnroll;
    }

    public void setMoneyMovementEnroll(MoneyMovementEnroll moneyMovementEnroll) {
        this.moneyMovementEnroll = moneyMovementEnroll;
    }

    public MessagesNotices getMessagesNotices() {
        return messagesNotices;
    }

    public void setMessagesNotices(MessagesNotices messagesNotices) {
        this.messagesNotices = messagesNotices;
    }

    public MoneyMovementEbills getMoneyMovementEbills() {
        return moneyMovementEbills;
    }

    public void setMoneyMovementEbills(MoneyMovementEbills moneyMovementEbills) {
        this.moneyMovementEbills = moneyMovementEbills;
    }

    public SettingsBusinessProfile getSettingsBusinessProfile() {
        return settingsBusinessProfile;
    }

    public void setSettingsBusinessProfile(SettingsBusinessProfile settingsBusinessProfile) {
        this.settingsBusinessProfile = settingsBusinessProfile;
    }

    public SettingsProfile getSettingsProfile() {
        return settingsProfile;
    }

    public void setSettingsProfile(SettingsProfile settingsProfile) {
        this.settingsProfile = settingsProfile;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public MoneyMovementAchUpload getMoneyMovementAchUpload() {
        return moneyMovementAchUpload;
    }

    public void setMoneyMovementAchUpload(MoneyMovementAchUpload moneyMovementAchUpload) {
        this.moneyMovementAchUpload = moneyMovementAchUpload;
    }

}
