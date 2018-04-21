package com.d3.datawrappers.account;

public enum OverdraftStatus {
    OPT_IN("A"),
    OPT_OUT("D"),
    REVOKE("R");

    String dbCode;
    OverdraftStatus(String dbCode) {
        this.dbCode = dbCode;
    }

    public String getDbCode() {
        return dbCode;
    }
}
