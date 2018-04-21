package com.d3.datawrappers.messages;

import java.util.concurrent.ThreadLocalRandom;

public enum Topic {
    GENERAL("General"),
    LOGGING_IN("Logging In"),
    DASHBOARD("Dashboard"),
    MESSAGES("Messages"),
    ACCOUNTS("Accounts"),
    MONEY_MOVEMENT("Money Movement");

    private String value;

    Topic(String value) {
        this.value = value;
    }

    public String getDropdownValue() {
        return value;
    }

    public static String getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))].getDropdownValue();
    }

}
