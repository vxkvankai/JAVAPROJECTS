package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberString;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.conduit.ConduitBuilder;
import com.d3.database.AlertDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.messages.MessageType;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.CheckClearedAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

@Slf4j
public class CheckClearedAlert extends D3Alert {

    public String getCheckNumber() {
        return checkNumber;
    }

    private String checkNumber;

    public CheckClearedAlert(D3Account account, String checkNumber) {
        super(ConsumerAlerts.CHECK_CLEARED);
        this.account = account;
        this.checkNumber = checkNumber;
        this.messageFilter = MessageType.TRANSACTIONS;
    }

    public CheckClearedAlert(D3User user) {
        super(user);
    }

    @Override
    public CheckClearedAlert copy() {
        CheckClearedAlert alert = new CheckClearedAlert(this.account, this.checkNumber);
        alert.user = user;
        return alert;
    }

    @Override
    public void createRandomData() {
        this.messageFilter = MessageType.TRANSACTIONS;
        this.alert = ConsumerAlerts.CHECK_CLEARED;
        this.account = RandomHelper.getRandomElementFromList(user.getAssetAccounts());
        this.checkNumber = getRandomNumberString(4, false);
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return CheckClearedAlertForm.initialize(driver, CheckClearedAlertForm.class);
    }

    @Override
    public void triggerAlert() throws D3ApiException, ConduitException {
        Integer alertId = AlertDatabaseHelper.getConsumerAlertId(this.getAlert());
        if (alertId == null) {
            log.error("Error getting id's for the user");
            throw new D3ApiException("Error generating a user with alert triggered");
        }
        String propValue = AlertDatabaseHelper.getConsumerAlertPropValue(alertId, user);

        D3Transaction transaction = new D3Transaction(JFairyCompany.createNewCompany().getName(), propValue, D3Transaction.TransactionType.DEBIT,
            getRandomNumber(50, 900),
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
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyy");
        DateTime date = DateTime.now();
        return String.format(alert.getMessageDetails(), getCheckNumber(), getAccountUsed(), formatter.print(date));
    }
}
