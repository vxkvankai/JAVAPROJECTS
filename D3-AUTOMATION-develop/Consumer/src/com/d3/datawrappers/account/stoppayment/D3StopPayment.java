package com.d3.datawrappers.account.stoppayment;

import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import org.openqa.selenium.WebDriver;

public abstract class D3StopPayment {

    protected static final String PAYMENT_ALREADY_POSTED = "Payment already posted";
    protected static final String SERVER_ERROR = "Server error. Please try again.";
    protected static final String SUCCESS = "Success";
    protected static final String UNABLE_TO_CONNECT_TO_HOST = "Unable to connect to host";
    private static final String TRACKING_NUMBER = "123456789";

    public String getPayee() {
        return payee;
    }

    public StopPaymentReason getReason() {
        return reason;
    }

    public StopPaymentType getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getTrackingNumber() {
        return TRACKING_NUMBER;
    }

    String payee;
    StopPaymentReason reason;
    StopPaymentType type;
    String status;

    public D3StopPayment(String payee, StopPaymentReason reason, String status) {
        this.payee = payee;
        this.reason = reason;
        this.status = status;
    }

    public abstract StopPaymentForm getStopPaymentForm(WebDriver driver);

    @Override
    public String toString() {
        return String.format("payee: %s, reason: %s, status: %s", payee, reason, status);
    }
}

