package com.d3.datawrappers.ebills.enums;


public class AutoPay {

    public enum AmountType {
        AMOUNT_DUE("Amount Due"),
        FIXED_AMOUNT("Fixed Amount");

        String type;

        AmountType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public enum PayOn {
        DUE_DATE("Due Date"),
        LEAD_DAYS("Before Due Date"),
        RECEIPT("Upon Receipt");

        String whenToPay;

        PayOn(String whenToPay) {
            this.whenToPay = whenToPay;
        }

        @Override
        public String toString() {
            return whenToPay;
        }
    }

    public enum DaysBefore {
        ONE("One"),
        TWO("Two"),
        THREE("Three"),
        FOUR("Four"),
        FIVE("Five");

        String days;

        DaysBefore(String days) {
            this.days = days;
        }

        @Override
        public String toString() {
            return days;
        }
    }
}
