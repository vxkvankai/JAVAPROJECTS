package com.d3.datawrappers.user.enums;

public enum UserLevel {
    PRIMARY("p"),
    SECONDARY("s");
    private final String conduitCode;

    UserLevel(String conduitCode) {
        this.conduitCode = conduitCode;
    }

    public String getConduitCode() {
        return conduitCode;
    }
}
