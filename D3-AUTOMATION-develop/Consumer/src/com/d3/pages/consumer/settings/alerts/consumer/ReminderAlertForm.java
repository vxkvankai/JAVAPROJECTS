package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.ReminderAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class ReminderAlertForm extends AlertForm<ReminderAlertForm, ReminderAlert> implements AlertDetails<ReminderAlert> {

    @FindBy(id = "description")
    private Element description;

    public ReminderAlertForm(WebDriver driver) {
        super(driver);
    }

    public ReminderAlertForm enterDescription(String reminder) {
        description.sendKeys(reminder);
        return this;
    }

    @Override
    protected ReminderAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(ReminderAlert alert) {
        enterDescription(alert.getReminderDescription());
        selectFrequency(alert.getFrequency());
        selectFrequencyAttributes(alert.getFrequency());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(ReminderAlert alert) {
        String errorMsg = "%s: %s for Reminder Alert was not found on the DOM.";

        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getReminderDescription(), errorMsg, "User Reminder Message");
            checkIfTextDisplayed(alert.getFrequency().toString(), errorMsg, "Alert Frequency");
        } catch (TextNotDisplayedException e) {
            logger.warn("Reminder Alert was not validated", e);
            return false;
        }

        return true;
    }
}
