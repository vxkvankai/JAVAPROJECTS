package com.d3.pages.consumer.settings.alerts.consumer.base;

import java.util.concurrent.ThreadLocalRandom;

public enum AlertFrequency {
    DAYS("Daily"),
    WEEKLY("Every Week"),
    SEMI_MONTHLY("Twice A Month"),
    MONTHLY("Every Month"),
    QUARTERLY("Every Three Months"),
    SEMI_ANNUALLY("Every Six Months"),
    ANNUALLY("Every Year");

    protected String frequency;

    AlertFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return frequency;
    }

    public static AlertFrequency getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))];
    }

}

