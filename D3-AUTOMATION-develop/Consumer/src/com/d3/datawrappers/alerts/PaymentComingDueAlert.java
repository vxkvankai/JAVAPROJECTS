package com.d3.datawrappers.alerts;

import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.conduit.ConduitBuilder;
import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3AccountAttribute;
import com.d3.datawrappers.account.D3AccountAttributes;
import com.d3.datawrappers.common.AttributeType;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.messages.MessageType;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.PaymentComingDueAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class PaymentComingDueAlert extends D3Alert {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyy-MM-dd");
    private static final DateTime ONE_WEEK_FROM_TODAY = DateTime.now().plusDays(7);


    public PaymentComingDueAlert(D3Account account) {
        super(ConsumerAlerts.PAYMENT_COMING_DUE);
        this.account = account;
        this.messageFilter = MessageType.ACCOUNTS;
    }

    public PaymentComingDueAlert(D3User user) {
        super(user);
    }

    @Override
    public PaymentComingDueAlert copy() {
        PaymentComingDueAlert alert = new PaymentComingDueAlert(getUser());
        alert.alert = this.alert;
        alert.account = this.account;
        alert.messageFilter = this.messageFilter;
        return alert;
    }

    @Override
    public void createRandomData() {
        this.alert = ConsumerAlerts.PAYMENT_COMING_DUE;
        this.messageFilter = MessageType.ACCOUNTS;
        this.account = user.getFirstAccountByAccountingClass(D3Account.AccountingClass.LIABILITY);
    }

    @Override
    public boolean hasFrequency() {
        return false;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return PaymentComingDueAlertForm.initialize(driver, PaymentComingDueAlertForm.class);
    }

    @Override
    public void triggerAlert() throws D3ApiException, ConduitException {
        Integer alertId = DatabaseUtils.getConsumerAlertId(this.getAlert());
        if (alertId == null) {
            LoggerFactory.getLogger(CheckClearedAlert.class).error("Error getting id's for the user");
            throw new D3ApiException("Error generating a user with alert triggered");
        }

        List<D3AccountAttribute> attributeList = new ArrayList<>();
        attributeList.add(new D3AccountAttribute(D3AccountAttributes.MINIMUM_PAYMENT_DUE.getConduitCode(), AttributeType.MONEY, true, 0,
            RandomHelper.getRandomCurrencyValue(25, 200)));
        attributeList.add(new D3AccountAttribute(D3AccountAttributes.PAYMENT_DUE_DATE.getConduitCode(), AttributeType.DATE, true, 0,
            DATE_FORMAT.print(ONE_WEEK_FROM_TODAY)));

        getAccountUsed().addAttributes(attributeList);

        ConduitBuilder builder = new ConduitBuilder().addAccounts(this.getAccountUsed());
        builder.uploadFileToApi(user.getCuID(), getConsumerBaseUrl(user.getCuID()), getConsumerApiVersion());

        DatabaseUtils.runScheduledJob(D3ScheduledJobs.DAILY_ALERTS);
        Assert.assertTrue(hasAlertBeenCreated(user, this));
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        return String.format(alert.getMessageDetails(), getAccountUsed().getName(), DATE_FORMAT.print(ONE_WEEK_FROM_TODAY));
    }


}
