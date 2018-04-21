package com.d3.datawrappers.messages;

public enum MessageType {
    ACCOUNTS("Account Messages"),
    ALL_NOTICE_MESSAGES("All Messages"),
    ALL_SECURE_MESSAGES("All Secure Messages"),
    CAMPAIGN("FI Messages"),
    MONEY_MOVEMENT("Money Movement Messages"),
    PLANNING("Planning Messages"),
    RECEIVED("Received"),
    SENT("Sent"),
    SECURITY("Security Messages"),
    TRANSACTIONS("Transaction Messages");

    String filterText;

    MessageType(String filterText) {
        this.filterText = filterText;
    }

    public String getFilterText() {
        return filterText;
    }
}
