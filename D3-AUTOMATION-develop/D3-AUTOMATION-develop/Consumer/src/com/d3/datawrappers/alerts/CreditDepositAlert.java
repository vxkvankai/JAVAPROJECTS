package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.conduit.ConduitBuilder;
import com.d3.database.AlertDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.CreditDepositAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

@Slf4j
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
    public void triggerAlert() throws D3ApiException, ConduitException {
        Integer alertId = AlertDatabaseHelper.getConsumerAlertId(this.getAlert());
        if (alertId == null) {
            log.error("Error getting id's for the user");
            throw new D3ApiException("Error generating a user with alert triggered");
        }

        D3Transaction transaction = new D3Transaction(JFairyCompany.createNewCompany().getName(), RandomHelper.getRandomElementFromArray(D3Transaction.TransactionType.values()),
            getRandomNumber(50, 100),
            DateTime.now().toDate(),
            false);
        log.info("Creating transaction: {}", transaction);
        getAccountUsed().addTransaction(transaction);

        ConduitBuilder builder = new ConduitBuilder().addAccounts(getAccountUsed());
        builder.uploadFileToApi(user.getCuID(), getConsumerBaseUrl(user.getCuID()), getConsumerApiVersion());
        Assert.assertTrue(hasAlertBeenCreated(getUser(), this), String.format("%s alert was not created", this.getAlert().name()));
    }

    @Override
    public D3ScheduledJobs jobTrigger() {
        throw new NotImplementedException(String.format("%s alert is triggered through conduit", this.alert.name()));
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        return String.format(alert.getMessageDetails(), getAccountUsed().getName());
    }
}
