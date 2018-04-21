package com.d3.datawrappers.messages;

import com.d3.helpers.RandomHelper;

public enum Issue {
    PROBLEM("Problem"),
    QUESTION("Question"),
    FEEDBACK("Feedback");

    private String value;

    Issue(String value) {
        this.value = value;
    }

    public String getDropdownValue() {
        return value;
    }

    public static String getRandom() {
        return RandomHelper.getRandomElementFromArray(values()).getDropdownValue();
    }

}
