package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberString;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.conduit.ConduitBuilder;
import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.messages.MessageType;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.CheckClearedAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

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
        Integer alertId = DatabaseUtils.getConsumerAlertId(this.getAlert());
        if (alertId == null) {
            LoggerFactory.getLogger(CheckClearedAlert.class).error("Error getting id's for the user");
            throw new D3ApiException("Error generating a user with alert triggered");
        }
        String propValue = DatabaseUtils.getConsumerAlertPropValue(alertId, user);

        D3Transaction transaction = new D3Transaction(JFairyCompany.createNewCompany().getName(), propValue, D3Transaction.TransactionType.DEBIT,
                getRandomNumber(50, 900),
                DateTime.now().toDate(),
                false);
        LoggerFactory.getLogger(CheckClearedAlert.class).info("Creating transaction: {}", transaction);
        this.getAccountUsed().addTransaction(transaction);

        ConduitBuilder builder = new ConduitBuilder().addAccounts(this.getAccountUsed());
        builder.uploadFileToApi(user.getCuID(), getConsumerBaseUrl(user.getCuID()), getConsumerApiVersion());
        Assert.assertTrue(hasAlertBeenCreated(user, this));
    }


    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyy");
        DateTime date = DateTime.now();
        return String.format(alert.getMessageDetails(), checkNumber, this.getAccountUsed(), formatter.print(date));
    }
}
