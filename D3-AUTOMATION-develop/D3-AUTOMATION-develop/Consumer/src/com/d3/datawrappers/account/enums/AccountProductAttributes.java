package com.d3.datawrappers.account.enums;

public enum AccountProductAttributes {
    ALLOW_BROKERAGE_ACCESS("d3Permission.allow.brokerage.access"),
    ALLOW_ESTATEMENTS("d3permission.allow.estatements"),
    OVERDRAFT_PROTECTION("d3Permission.overdraft.protection"),
    PREFERRED_BALANCE("d3General.preferred.balance"),
    PRINCIPAL_ONLY_PAYMENT("d3Permission.principal.only.payment"),
    STOP_PAYMENT("d3Permission.stop.payment");

    protected String attributeName;

    AccountProductAttributes(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
