package com.d3.datawrappers.recipient.base;

import java.util.concurrent.ThreadLocalRandom;

public enum AccountType {
    CHECKING("Checking"),
    SAVINGS("Savings");

    private String dropdownCode;

    AccountType(String code) {
        dropdownCode = code;
    }

    public String getDropdownCode() {
        return dropdownCode;
    }

    public static AccountType getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))];
    }
}
