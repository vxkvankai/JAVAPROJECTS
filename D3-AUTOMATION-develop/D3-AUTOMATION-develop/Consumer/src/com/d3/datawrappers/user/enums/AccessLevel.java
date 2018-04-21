package com.d3.datawrappers.user.enums;

public enum AccessLevel {
    FULL_ACCESS("Full Access"),
    NO_ACCESS("No Access"),
    VIEW_ONLY("View Only");

    private String formatted;

    AccessLevel(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }
}