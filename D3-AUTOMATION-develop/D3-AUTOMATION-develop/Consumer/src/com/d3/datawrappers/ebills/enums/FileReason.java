package com.d3.datawrappers.ebills.enums;

import com.d3.helpers.RandomHelper;

public enum FileReason {
    BANK("Bank"),
    BILLER_WEBSITE("Biller Website"),
    CASH("Cash"),
    CHECK("Check"),
    CONTESTED_BILL("Contested Bill"),
    MAIL("Mail"),
    NONE_SPECIFIED("None Specified"),
    NOT_PAID("Not Paid"),
    OFFICE("Office"),
    OTHER("Other"),
    PHONE("Phone"),
    ZERO_BALANCE("ZeroBalance");

    private String formatted;

    FileReason(String formatted) {
        this.formatted = formatted;
    }

    public String getFormatted() {
        return formatted;
    }

    public static FileReason getRandom() {
        return RandomHelper.getRandomElementFromArray(values());
    }

}
