package com.d3.datawrappers.ebills.enums;

public enum Status {
    FILED("Filed"),
    PAID("Paid"),
    UNPAID("Unpaid");

    private String formatted;

    Status(String formatted) {
        this.formatted = formatted;
    }

    public String getFormatted() {
        return formatted;
    }
}
