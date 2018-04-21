package com.d3.datawrappers.account.stoppayment;

import java.util.concurrent.ThreadLocalRandom;

public enum StopPaymentReason {
    DESTROYED("Destroyed"),
    LOST("Lost"),
    NONE("None"),
    OTHER("Other"),
    STOLEN("Stolen"),
    VOID("Void");

    private String formatted;

    StopPaymentReason(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }

    public static StopPaymentReason getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))];
    }
}
