package com.d3.datawrappers.account.stoppayment;

import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.pages.consumer.accounts.stoppayment.StopPaymentRangeForm;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import io.codearte.jfairy.Fairy;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ThreadLocalRandom;

public class StopPaymentRange extends D3StopPayment {

    public int getStartCheck() {
        return startCheck;
    }

    public String getStartCheckStr() {
        return String.valueOf(startCheck);
    }

    public int getEndCheck() {
        return endCheck;
    }

    public String getEndCheckStr() {
        return String.valueOf(endCheck);
    }

    private int startCheck;
    private int endCheck;

    public StopPaymentRange(int startCheck, int endCheck, String payee, StopPaymentReason reason, String status) {
        super(payee, reason, status);
        this.startCheck = startCheck;
        this.endCheck = endCheck;
        this.type = StopPaymentType.RANGE;
    }

    public static StopPaymentRange createRandomStopPaymentRange(StopPaymentReason reason) {
        int startCheck = getRandomNumberInt(600, 3000);
        int endCheck = startCheck + getRandomNumberInt(5, 20);
        String payee = Fairy.create().company().getName();
        return new StopPaymentRange(startCheck, endCheck, payee, reason, SUCCESS);
    }

    public static StopPaymentRange createRandomStopPaymentRangeAlreadyPosted(StopPaymentReason reason) {
        int startCheck = 300;
        int endCheck = getRandomNumberInt(301, 399);
        String payee = Fairy.create().company().getName();
        return new StopPaymentRange(startCheck, endCheck, payee, reason, PAYMENT_ALREADY_POSTED);
    }

    public static StopPaymentRange createRandomInvalidStopPaymentRange(StopPaymentReason reason) {
        int startCheck = getRandomNumberInt(600, 3000);
        int endCheck = startCheck - getRandomNumberInt(5, 20);
        String payee = Fairy.create().company().getName();
        return new StopPaymentRange(startCheck, endCheck, payee, reason, null);
    }

    public static StopPaymentRange createRandomNonExistentStopPaymentRange(StopPaymentReason reason) {
        int[] nonExistentChecks = {400, 500};
        int startCheck = nonExistentChecks[(ThreadLocalRandom.current().nextInt(nonExistentChecks.length))];
        int endCheck = startCheck + getRandomNumberInt(5, 20);
        String payee = Fairy.create().company().getName();
        String status = startCheck == 400 ? SERVER_ERROR : UNABLE_TO_CONNECT_TO_HOST;
        return new StopPaymentRange(startCheck, endCheck, payee, reason, status);
    }

    @Override
    public StopPaymentForm getStopPaymentForm(WebDriver driver) {
        return StopPaymentRangeForm.initialize(driver, StopPaymentRangeForm.class);
    }
}
