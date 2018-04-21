package com.d3.datawrappers.goals;

import java.util.concurrent.ThreadLocalRandom;

public enum GoalType {
    RETIREMENT("Retirement"),
    SAVINGS("Savings");

    private String formatted;

    GoalType(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }

    public static GoalType getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))];
    }

}
