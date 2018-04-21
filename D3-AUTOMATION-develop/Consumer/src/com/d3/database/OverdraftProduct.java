package com.d3.database;

public enum OverdraftProduct {

    ACH_ETRANSFERS("d3General.overdraft.preferences.ach"),
    ATM("d3General.overdraft.preferences.reg");

    String dbCode;
    OverdraftProduct(String dbCode) {
        this.dbCode = dbCode;
    }

    public String getDbCode() {
        return dbCode;
    }
}
