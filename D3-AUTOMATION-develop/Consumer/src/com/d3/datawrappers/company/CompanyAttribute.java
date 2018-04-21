package com.d3.datawrappers.company;

public enum CompanyAttribute {

    ACCOUNT_BROKERAGE_ACCESS_ENABLED("accounts.brokerageAccess.enabled"),
    ACCOUNT_BROKERAGE_ACCESS_REDIRECT_URL("accounts.brokerageAccess.defaultRedirectionAdapter.url"),
    ACCOUNTS_CHECK_ORDER_ADAPTER("accounts.checkOrder.adapter.selected"),
    ACCOUNTS_CHECK_ORDER_REDIRECT_URL("accounts.checkOrder.defaultRedirectionAdapter.url"),
    ACCOUNTS_ESTATEMENT_ADAPTER("accounts.estatement.adapter.selected"),
    ACCOUNTS_ESTATEMENTS_ENABLED("accounts.estatement.enabled"),
    ACCOUNTS_ESTATEMENTS_GO_PAPPERLESS_ENABLED("accounts.estatement.goPaperless.enabled"),
    ACCOUNTS_ESTATEMENTS_PAPER_AND_ELCETRONICS_ENABLED("accounts.estatement.paperAndElectronic.enabled"),
    ACCOUNTS_ESTATEMENTS_UN_ENROLL("accounts.estatement.unenroll.enabled"),
    ACCOUNTS_STOP_PAYMENT("accounts.stopPayment.enabled"),
    ACCOUNTS_STOP_PAYMENT_CHECK_RANGES("accounts.stopPayment.checkRanges.enabled"),
    ACCOUNTS_STOP_PAYMENT_HISTORY("accounts.stopPayment.history.enabled"),
    BUSINESS_PROFILE_ENABLED("settings.business.profile.enabled"),
    BUSINESS_PROFILE_ALLOW_ALTERNATE_EMAIL_CHANGE("settings.business.profile.allow.alternateEmailAddress.change"),
    BUSINESS_PROFILE_ALLOW_MAILING_ADDRESS_CHANGE("settings.business.profile.allow.mailingAddress.change"),
    BUSINESS_PROFILE_ALLOW_PHONE_NUMBER_CHANGE("settings.business.profile.allow.phoneNumber.change"),
    BUSINESS_PROFILE_ALLOW_PHONE_NUMBER_SMS_CHANGE("settings.business.profile.allow.phoneNumber.sms.change"),
    BUSINESS_PROFILE_ALLOW_PHYSICAL_ADDRESS_CHANGE("settings.business.profile.allow.physicalAddress.change"),
    BUSINESS_PROFILE_ALLOW_PRIMARY_EMAIL_ADDRESS_CHANGE("settings.business.profile.allow.primaryEmailAddress.change"),
    EXPEDITED_PAYMENT_ENABLED("moneyMovement.billPay.checkFreeAdapter.expeditedPayment.enabled"),
    MONEY_MOVEMENT_EXTERNAL_TRANSFER_CUTOFF("moneyMovement.external.transfer.cutoff"),
    MONEY_MOVEMENT_FEDWIRE_TRANSFER_CUTOFF("moneyMovement.fedwire.transfer.cutoff"),
    MONEY_MOVEMENT_INTERNAL_TRANSFER_CUTOFF("moneyMovement.internal.transfer.cutoff"),
    OUT_OF_BAND_VERIFICATION("settings.security.outOfBandVerification.enabled"),
    OUT_OF_BAND_VERIFICATION_ON_LOGIN("settings.security.outOfBandVerification.login.enabled"),
    SETTINGS_PROFILE_ALLOW_ALTERNATE_EMAIL_ADDRESS_CHANGE("settings.profile.allow.alternateEmailAddress.change"),
    SETTINGS_PROFILE_ALLOW_MAILING_ADDRESS_CHANGE("settings.profile.allow.mailingAddress.change"),
    SETTINGS_PROFILE_ALLOW_NAME_CHANGE("settings.profile.allow.name.change"),
    SETTINGS_PROFILE_ALLOW_PHONE_NUMBER_CHANGE("settings.profile.allow.phoneNumber.change"),
    SETTINGS_PROFILE_ALLOW_PHONE_NUMBER_SMS_CHANGE("settings.profile.allow.phoneNumber.sms.change"),
    SETTINGS_PROFILE_ALLOW_PHYSICAL_ADDRESS_CHANGE("settings.profile.allow.physicalAddress.change"),
    SETTINGS_PROFILE_ALLOW_PRIMARY_EMAIL_ADDRESS_CHANGE("settings.profile.allow.primaryEmailAddress.change"),
    TOGGLE("dashboard.smallBusiness.toggle.enabled"),
    USER_MANAGEMENT("settings.usermanagement.enabled"),
    TRANSACTIONS_RUNNING_BALANCE("transactions.runningBalance.enabled"),
    TRANSACTIONS_RUNNING_BALANCE_AUTOMATIC_DISPLAY("transactions.runningBalance.automaticDisplay.enabled"),
    TRANSACTIONS_RUNNING_BALANCE_FOR_POSTED_ONLY("transactions.runningBalance.forPostedOnly.enabled"),
    ON_US_TRANSFER_ENABLED("moneyMovement.onUsTransfer.enabled"),
    TRANSACTIONS_RUNNING_BALANCE_PREFERRED_WITH_PENDING("transactions.runningBalance.preferredWithPending"),
    ON_US_ROUTING_NUMBERS("moneyMovement.onUsTransfer.routingNumbers"),
    GO_PAPERLESS_ENABLED("accounts.estatement.goPaperless.enabled");

    protected String definition;

    CompanyAttribute(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }
}
