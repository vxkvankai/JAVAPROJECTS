package com.d3.datawrappers.user.enums;

public enum AccountAction {
    ASSOCIATE("a"),
    DISASSOCIATE("d");
    private final String conduitCode;

    AccountAction(String conduitCode) {
        this.conduitCode = conduitCode;
    }

    public String getConduitCode() {
        return conduitCode;
    }
}
