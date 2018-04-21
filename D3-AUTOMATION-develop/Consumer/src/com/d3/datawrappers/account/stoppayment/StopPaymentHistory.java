package com.d3.datawrappers.account.stoppayment;

import com.d3.pages.consumer.accounts.stoppayment.StopPaymentHistoryForm;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import org.openqa.selenium.WebDriver;

public class StopPaymentHistory extends D3StopPayment {

    public StopPaymentHistory() {
        super(null, null, null);
        this.type = StopPaymentType.HISTORY;
    }

    @Override
    public StopPaymentForm getStopPaymentForm(WebDriver driver) {
        return StopPaymentHistoryForm.initialize(driver, StopPaymentHistoryForm.class);
    }
}
