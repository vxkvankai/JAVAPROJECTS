package com.d3.datawrappers.alerts.enums;

public class BudgetThreshold {

    public enum Type {
        APPROACH("Approaches Budget By"),
        EXCEED("Exceeds Budget By");

        String thresholdType;

        Type(String thresholdType) {
            this.thresholdType = thresholdType;
        }

        @Override
        public String toString() {
            return thresholdType;
        }
    }

    public enum Value {
        AMOUNT("Amount"),
        PERCENT("Percent");
        String thresholdValue;

        Value(String thresholdValue) {
            this.thresholdValue = thresholdValue;
        }

        @Override
        public String toString() {
            return thresholdValue;
        }
    }
}
