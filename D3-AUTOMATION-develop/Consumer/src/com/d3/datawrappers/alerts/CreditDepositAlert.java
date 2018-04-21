package com.d3.datawrappers.alerts;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.settings.alerts.consumer.CreditDepositAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;

public class CreditDepositAlert extends D3Alert {

    public CreditDepositAlert(D3Account account) {
        super(ConsumerAlerts.ACCOUNT_CREDIT);
        this.account = account;
    }

    public CreditDepositAlert(D3User user) {
        super(user);
    }

   @Override
   public CreditDepositAlert copy() {
        CreditDepositAlert alert = new CreditDepositAlert(account);
        alert.user = user;
        return alert;
   }

    @Override
    public void createRandomData() {
        this.alert = ConsumerAlerts.ACCOUNT_CREDIT;
        this.account = user.getRandomAccount();
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    public AlertForm getAlertForm(WebDriver driver) {
        return CreditDepositAlertForm.initialize(driver, CreditDepositAlertForm.class);
    }

    @Override
    public void triggerAlert() throws D3ApiException {
        throw new D3ApiException(String.format("Triggering %s alert not yet implemented", this.getName()));
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        throw new NotImplementedException(String.format("getTriggeredAlertMessageDetails for %s alert not yet implemented", this.alert.name()));
    }
}
