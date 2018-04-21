package com.d3.datawrappers.alerts.enums;

public enum BalanceThreshold {
    LT("Less Than"),
    GT("Greater Than");

    String type;

    BalanceThreshold(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
