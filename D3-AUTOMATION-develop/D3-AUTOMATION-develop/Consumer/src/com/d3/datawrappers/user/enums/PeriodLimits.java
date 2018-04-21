package com.d3.datawrappers.user.enums;

import com.d3.helpers.RandomHelper;

public enum PeriodLimits {
    DAILY("Daily"),
    MONTHLY("Monthly"),
    WEEKLY("Weekly");

    private String formatted;

    PeriodLimits(String formatted) {
        this.formatted = formatted;
    }

    public static PeriodLimits getRandom() {
        return RandomHelper.getRandomElementFromArray(PeriodLimits.values());
    }

    public String getFormatted() {
        return formatted;
    }
}
