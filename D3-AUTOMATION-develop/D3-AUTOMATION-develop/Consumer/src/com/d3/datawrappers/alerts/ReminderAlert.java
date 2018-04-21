package com.d3.datawrappers.alerts;

import com.d3.database.DatabaseHelper;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.messages.MessageType;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.settings.alerts.consumer.ReminderAlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class ReminderAlert extends D3Alert {

    public String getReminderDescription() {
        return reminder;
    }

    private String reminder;

    public ReminderAlert(String reminder, AlertFrequency frequency) {
        super(ConsumerAlerts.REMINDER);
        this.reminder = reminder;
        this.frequency = frequency;
        this.messageFilter = MessageType.PLANNING;
    }

    public ReminderAlert(D3User user) {
        super(user);
    }

    @Override
    public ReminderAlert copy() {
        ReminderAlert alert = new ReminderAlert(this.reminder, this.frequency);
        alert.user = user;
        return alert;
    }

    @Override
    public void createRandomData() {
        this.messageFilter = MessageType.PLANNING;
        this.alert = ConsumerAlerts.REMINDER;
        this.reminder = String.format("Reminder Alert for %s", user.getLogin());
        this.frequency = AlertFrequency.getRandom();
    }

    @Override
    public boolean hasFrequency() {
        return true;
    }

    @Override
    public AlertForm getAlertForm(WebDriver driver) {
        return ReminderAlertForm.initialize(driver, ReminderAlertForm.class);
    }

    @Override
    public void triggerAlert() {
        DatabaseHelper.runScheduledJob(this.jobTrigger());
        Assert.assertTrue(DatabaseHelper.waitForJobToRun(this.jobTrigger()), String.format("%s alert was not triggered", this.getAlert().name()));
        DatabaseHelper.stopScheduledJob(this.jobTrigger());
        Assert.assertTrue(hasAlertBeenCreated(getUser(), this), String.format("%s alert was not created", this.getAlert().name()));
    }

    @Override
    public D3ScheduledJobs jobTrigger() {
        return D3ScheduledJobs.SCHEDULED_ALERTS;
    }

    @Override
    public String getTriggeredAlertMessageDetails(D3User user) {
        return String.format(alert.getMessageDetails(), getReminderDescription());
    }


}
