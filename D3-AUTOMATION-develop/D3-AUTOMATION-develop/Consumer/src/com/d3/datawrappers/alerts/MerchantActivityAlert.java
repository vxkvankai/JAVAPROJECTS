package com.d3.datawrappers.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.conduit.ConduitBuilder;
import com.d3.database.AlertDatabaseHelper;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.messages.MessageType;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.settings.alerts.consumer.MerchantActivityAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import io.codearte.jfairy.Fairy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
public class MerchantActivityAlert extends D3Alert {

    private String merchantName;

    public MerchantActivityAlert(String merchantName) {
        super(ConsumerAlerts.TRANSACTION_MERCHANT);
        this.merchantName = merchantName;
        this.messageFilter = MessageType.TRANSACTIONS;
    }

    public MerchantActivityAlert(D3User user) {
        super(user);
    }

    public String getMerchantName() {
        return merchantName;
    }

    @Override
    public MerchantActivityAlert copy() {
        MerchantActivityAlert alert = new MerchantActivityAlert(this.merchantName);
        alert.user = user;
        return alert;
    }

    @Override
    public void createRandomData() {
        this.alert = ConsumerAlerts.TRANSACTION_MERCHANT;
        this.messageFilter = MessageType.TRANSACTIONS;
        this.merchantName = Fairy.create().company().getName();
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return MerchantActivityAlertForm.initialize(driver, MerchantActivityAlertForm.class);
    }

    @Override
    public void triggerAlert() throws D3ApiException, ConduitException {
        Integer alertId = AlertDatabaseHelper.getConsumerAlertId(this.getAlert());
        if (alertId == null) {
            log.error("Error getting id's for the user");
            throw new D3ApiException("Error generating a user with alert triggered");
        }
        String propValue = AlertDatabaseHelper.getConsumerAlertPropValue(alertId, user);

        D3Transaction transaction = new D3Transaction(propValue, D3Transaction.TransactionType.DEBIT,
            getRandomNumber(25, 500),
            DateTime.now().toDate(),
            false);
        log.info("Creating transaction: {}", transaction);
        user.getFirstAccount().addTransaction(transaction);

        ConduitBuilder builder = new ConduitBuilder().addAccounts(user.getFirstAccount());
        builder.uploadFileToApi(user.getCuID(), getConsumerBaseUrl(user.getCuID()), getConsumerApiVersion());
        Assert.assertTrue(hasAlertBeenCreated(getUser(), this), String.format("%s alert was not created", this.getAlert().name()));
    }

    @Override
    public D3ScheduledJobs jobTrigger() {
        throw new NotImplementedException(String.format("%s alert is triggered through conduit", this.alert.name()));
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        DateTime date = DateTime.now();
        return String.format(
            alert.getMessageDetails(),
            getMerchantName(), user.getFirstAccount(),
            String.format("%s, %s %s, %s", DayOfWeek.of(date.getDayOfWeek()).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                Month.of(date.getMonthOfYear()).getDisplayName(TextStyle.FULL, Locale.ENGLISH), date.getDayOfMonth(),
                String.valueOf(date.getYear())));
    }
}
