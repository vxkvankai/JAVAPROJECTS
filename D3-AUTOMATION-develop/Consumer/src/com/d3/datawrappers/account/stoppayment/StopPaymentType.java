package com.d3.datawrappers.account.stoppayment;

public enum StopPaymentType {

    HISTORY("Get My Stop Payment History"),
    SINGLE("Single"),
    RANGE("Range");

    private String formatted;

    StopPaymentType(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }
}
