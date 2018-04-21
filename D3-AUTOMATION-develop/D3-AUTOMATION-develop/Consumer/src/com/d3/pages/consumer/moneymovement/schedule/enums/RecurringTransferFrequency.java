package com.d3.pages.consumer.moneymovement.schedule.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum RecurringTransferFrequency {
    EVERY_WEEK("Every Week"),
    EVERY_TWO_WEEKS("Every Two Weeks"),
    EVERY_MONTH("Every Month"),
    EVERY_MONTH_ON_LAST_DAY("Last Day of Every Month"),
    EVERY_TWO_MONTHS("Every Two Months"),
    EVERY_THREE_MONTHS("Every Three Months"),
    EVERY_SIX_MONTHS("Every Six Months"),
    EVERY_YEAR("Every Year");

    private String dropdownValue;

    RecurringTransferFrequency(String dropdownValue) {
        this.dropdownValue = dropdownValue;
    }

    public String getDropdownValue() {
        return dropdownValue;
    }

    public static RecurringTransferFrequency getRandom() {
        return values()[(ThreadLocalRandom.current().nextInt(values().length))];
    }

    public static RecurringTransferFrequency getRandomBillPay() {
        RecurringTransferFrequency frequency = getRandom();
        while (frequency == EVERY_MONTH_ON_LAST_DAY || frequency == EVERY_SIX_MONTHS || frequency == EVERY_YEAR) {
            // Note (Jmoravec): Every six months and every year for bill pay causes some issues with scheduling next instance due to how
            // bill pay works, this is expected behavior.
            frequency = getRandom();
        }
        return frequency;
    }
}
