package com.d3.pages.consumer.moneymovement.schedule.enums;

public enum RecurringTransferEndType {
    END_DATE("End Date"),
    INDEFINITE("Indefinite"),
    NUMBER_OF_TRANSACTIONS("Number of Transactions");

    private String dropdownValue;

    RecurringTransferEndType(String dropdownValue) {
        this.dropdownValue = dropdownValue;
    }

    public String getDropdownValue() {
        return dropdownValue;
    }
}
