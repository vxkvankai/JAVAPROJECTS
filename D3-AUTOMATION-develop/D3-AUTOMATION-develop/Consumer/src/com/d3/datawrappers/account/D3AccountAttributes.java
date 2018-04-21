package com.d3.datawrappers.account;


public enum D3AccountAttributes {
    ACCOUNT_OPEN_DATE("AccountOpenDate"),
    ACCOUNT_CLOSED_DATE("AccountClosedDate"),
    ANNUAL_PERCENTAGE_RATE("Apr"),
    AVAILABLE_CASH_LIMIT("AvailableCashLimit"),
    AVAILABLE_CREDIT("AvailableCredit"),
    CASH_ADVANCE_LIMIT("CashAdvanceLimit"),
    CREDIT_LIMIT("CreditLimit"),
    FINANCE_CHARGE_SERVICE_FEE("FinanceChargeServiceFee"),
    HOLD_AMOUNT("HoldAmount"),
    INTEREST_RATE("InterestRate"),
    LAST_DEPOSIT_AMOUNT("LastDepositAmount"),
    LAST_DEPOSIT_DATE("LastDepositDate"),
    LAST_PAYMENT_AMOUNT("LatePaymentAmount"),
    LAST_PAYMENT_DATE("LastPaymentDate"),
    LAST_STATEMENT_DATE("LastStatementDate"),
    LAST_YEARS_INTEREST("LastYearsInterest"),
    LOAN_DATE("LoanDate"),
    MATURITY_DATE("MaturityDate"),
    MINIMUM_PAYMENT_DUE("MinimumPaymentDue"),
    ORIGINATION_DATE("OriginationDate"),
    OVERDRAFT_TYPE("OverdraftType"),
    OVERDRAFT_LIMIT("OverdraftLimit"),
    PAST_AMOUNT_DUE("PastAmountDue"),
    PAYMENT_DUE_DATE("PaymentDueDate"),
    POINTS_ACCRUED("PointsAccrued"),
    POINTS_REDEEMED("PointsRedeemed"),
    PREVIOUS_AMOUNT_DUE("PreviousAmountDue"),
    RATE_OF_RETURN("RateOfReturn");

    String conduitCode;

    D3AccountAttributes(String conduitCode) {
        this.conduitCode = conduitCode;
    }

    public String getConduitCode() {
        return conduitCode;
    }
}
