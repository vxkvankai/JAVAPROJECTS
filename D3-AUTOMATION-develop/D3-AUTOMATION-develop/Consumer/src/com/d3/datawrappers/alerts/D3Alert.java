package com.d3.datawrappers.alerts;

import static java.lang.Thread.sleep;

import com.d3.database.AlertDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.messages.MessageType;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

@Slf4j
public abstract class D3Alert implements Serializable {

    protected D3Account account;
    protected ConsumerAlerts alert;
    protected AlertFrequency frequency;
    protected D3User user;
    double amountDbl;
    MessageType messageFilter;

    public D3Alert(ConsumerAlerts alert) {
        this.alert = alert;
    }

    public D3Alert(D3User user) {
        this.user = user;
    }

    /**
     * Creates a list of random D3 Alerts with random data, one of each given type.
     */
    @Nullable
    // TODO: add way to generate all alerts for same user
    public static List<D3Alert> createAllConsumerAlerts(List<D3User> users, List<Class<D3Alert>> alertsToGenerate) {
        List<D3Alert> alerts = new ArrayList<>();
        for (Class<D3Alert> alertToGenerate : alertsToGenerate) {
            try {
                D3Alert newAlert = alertToGenerate.getDeclaredConstructor(D3User.class)
                    .newInstance(users.get(alertsToGenerate.indexOf(alertToGenerate)));
                newAlert.createRandomData();
                alerts.add(newAlert);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.error("Error generating alert", e);
            }
        }
        return alerts;
    }

    public ConsumerAlerts getAlert() {
        return alert;
    }

    public String getName() {
        return alert.getDisplayedName();
    }

    public String getTriggeredAlertSubject() {
        return alert.getMessageSubject();
    }

    public AlertFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(AlertFrequency frequency) {
        this.frequency = frequency;
    }

    public D3Account getAccountUsed() {
        return account;
    }

    public double getAmount() {
        return amountDbl;
    }

    public String getAmountStr() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(amountDbl);
    }

    public MessageType getMessageFilter() {
        return messageFilter;
    }

    public D3User getUser() {
        return user;
    }

    public abstract void createRandomData();

    public abstract boolean hasFrequency();

    public abstract AlertForm getAlertForm(WebDriver driver);

    public abstract void triggerAlert() throws D3ApiException, ConduitException;

    public abstract D3ScheduledJobs jobTrigger();

    public abstract String getTriggeredAlertMessageDetails(D3User user);

    boolean hasAlertBeenCreated(D3User user, D3Alert alert) {
        Integer alertId = null;
        // wait until triggered alert is in db
        for (int i = 0; i < 1000; ++i) {
            alertId = AlertDatabaseHelper.getTriggeredAlertId(user, alert);
            if (alertId != null) {
                log.info("Alert type {} has been triggered for user {} with an id: {}", alert.getAlert().name(), user.getLogin(), alertId);
                break;
            }
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                log.error("Interrupted", ex);
                Thread.currentThread().interrupt();
            }
        }

        if (alertId == null) {
            log.error("Triggered alert has not yet been created.");
            return false;
        }

        return true;
    }

    public abstract D3Alert copy();
}
