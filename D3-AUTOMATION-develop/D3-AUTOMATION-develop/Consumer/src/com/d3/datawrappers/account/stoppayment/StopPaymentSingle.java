package com.d3.datawrappers.account.stoppayment;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.pages.consumer.accounts.stoppayment.StopPaymentSingleForm;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import io.codearte.jfairy.Fairy;
import org.openqa.selenium.WebDriver;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class StopPaymentSingle extends D3StopPayment {

    public double getStopPaymentAmount() {
        return amount;
    }

    public String getStopPaymentAmountStr() {
        return new DecimalFormat("#.00").format(amount);
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public String getCheckNumberStr() {
        return String.valueOf(checkNumber);
    }

    private double amount;
    private int checkNumber;

    public StopPaymentSingle(double amount, int checkNumber, String payee, StopPaymentReason reason, String status) {
        super(payee, reason, status);
        this.amount = amount;
        this.checkNumber = checkNumber;
        this.type = StopPaymentType.SINGLE;
    }

    public static StopPaymentSingle createRandomStopPayment(StopPaymentReason reason) {
        double amount = getRandomNumber(100, 999);
        int checkNumber = getRandomNumberInt(600, 3000);
        String payee = Fairy.create().company().getName();
        return new StopPaymentSingle(amount, checkNumber, payee, reason, SUCCESS);
    }

    public static StopPaymentSingle createRandomStopPaymentAlreadyPosted(StopPaymentReason reason) {
        double amount = getRandomNumber(100, 999);
        int checkNumber = 300;
        String payee = Fairy.create().company().getName();
        return new StopPaymentSingle(amount, checkNumber, payee, reason, PAYMENT_ALREADY_POSTED);
    }

    public static StopPaymentSingle createRandomInvalidStopPayment(StopPaymentReason reason) {
        int[] invalidChecks = {400, 500};
        double amount = getRandomNumber(100, 999);
        int checkNumber = invalidChecks[(ThreadLocalRandom.current().nextInt(invalidChecks.length))];
        String payee = Fairy.create().company().getName();
        String status = checkNumber == 400 ? SERVER_ERROR : UNABLE_TO_CONNECT_TO_HOST;
        return new StopPaymentSingle(amount, checkNumber, payee, reason, status);
    }

    @Override
    public StopPaymentForm getStopPaymentForm(WebDriver driver) {
        return StopPaymentSingleForm.initialize(driver, StopPaymentSingleForm.class);
    }
}
