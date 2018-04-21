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
import com.d3.pages.consumer.settings.alerts.consumer.TransactionAmountExceedsAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class TransactionAmountExceedsAlert extends D3Alert {

    private static final ImmutableList<String> TRANSACTION_TYPES = ImmutableList.of("All", "Credit", "Debit");

    public String getTransactionType() {
        return transactionType;
    }

    public D3Transaction getTransaction() {
        return transaction;
    }

    private String transactionType;
    private D3Transaction transaction;

    public TransactionAmountExceedsAlert(D3Account account, double amount, String transactionType) {
        super(ConsumerAlerts.TRANSACTION_AMOUNT);
        this.account = account;
        this.amountDbl = amount;
        this.transactionType = transactionType;
    }

    public TransactionAmountExceedsAlert(D3User user) {
        super(user);
    }

    @Override
    public TransactionAmountExceedsAlert copy() {
        TransactionAmountExceedsAlert alert = new TransactionAmountExceedsAlert(this.account, this.amountDbl, this.transactionType);
        alert.user = user;
        return alert;
    }

    @Override
    public void createRandomData() {
        this.alert = ConsumerAlerts.TRANSACTION_AMOUNT;
        this.account = user.getRandomAccount();
        this.amountDbl = getRandomNumber(100, 999);
        this.transactionType = TRANSACTION_TYPES.get(ThreadLocalRandom.current().nextInt(TRANSACTION_TYPES.size()));
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return TransactionAmountExceedsAlertForm.initialize(driver, TransactionAmountExceedsAlertForm.class);
    }

    @Override
    public void triggerAlert() throws D3ApiException, ConduitException {
        Integer alertId = AlertDatabaseHelper.getConsumerAlertId(this.getAlert());
        if (alertId == null) {
            log.error("Error getting id's for the user");
            throw new D3ApiException("Error generating a user with alert triggered");
        }

        D3Transaction.TransactionType type = this.transactionType.equalsIgnoreCase(D3Transaction.TransactionType.CREDIT.name())
            ? D3Transaction.TransactionType.CREDIT : D3Transaction.TransactionType.DEBIT;

        this.transaction = new D3Transaction(JFairyCompany.createNewCompany().getName(), type,
            getRandomNumber((int) this.amountDbl + 1, (int) this.amountDbl + 50),
            DateTime.now().toDate(),
            false);
        log.info("Creating transaction: {}", transaction);
        getAccountUsed().addTransaction(getTransaction());

        ConduitBuilder builder = new ConduitBuilder().addAccounts(this.getAccountUsed());
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
        return String.format(alert.getMessageDetails(), String.format("$%s", getTransaction().getAmountString()), getAccountUsed().getName(), getTransaction().getName(),
            String.format("%s, %s %s, %s", DayOfWeek.of(date.getDayOfWeek()).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                Month.of(date.getMonthOfYear()).getDisplayName(TextStyle.FULL, Locale.ENGLISH), date.getDayOfMonth(),
                String.valueOf(date.getYear())));
    }


}
