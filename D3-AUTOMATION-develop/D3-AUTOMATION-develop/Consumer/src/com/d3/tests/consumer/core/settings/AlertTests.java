package com.d3.tests.consumer.core.settings;

import com.d3.database.AlertDatabaseHelper;
import com.d3.datawrappers.alerts.AlertType;
import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.CreditDepositAlert;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.alerts.TransactionAmountExceedsAlert;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.l10n.settings.AlertsL10N;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.messages.Notices;
import com.d3.pages.consumer.settings.alerts.Alerts;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.tests.annotations.RunForSpecificAlerts;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@SuppressWarnings("unchecked")
@Epic("Settings")
@Feature("Alerts")
@Slf4j
public class AlertTests extends SettingsTestBase {

    private static final String ERROR_TRIGGERING_ALERT = "Error triggering alert";

    /**
     * Logs in, goes to the alert page, edits given alert, fills out the form, clicks save, then expands the alert
     *
     * @param dashboard Dashboard instance
     * @param alert D3Alert to edit
     * @return AlertForm
     */
    private AlertForm editAlertCommonSteps(Dashboard dashboard, D3Alert alert) {
        return dashboard.getHeader().clickSettingsButton().getTabs().clickAlertsLink()
            .editAlert(alert)
            .fillOutForm(alert)
            .clickEditAlertSaveButton()
            .expandAlert(alert);
    }

    @Story("Add Consumer Alerts")
    @TmsLink("1003")
    @TmsLink("3467")
    @TmsLink("3468")
    @TmsLink("3469")
    @TmsLink("3470")
    @TmsLink("3471")
    @TmsLink("3472")
    @TmsLink("3473")
    @TmsLink("3474")
    @TmsLink("16562")
    @Test(dataProvider = "User with No Consumer Alerts")
    public void addConsumerAlerts(D3Alert alert) {
        Dashboard dashboard = login(alert.getUser());
        Assert.assertNotNull(dashboard);
        Alerts alertPage = dashboard.getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickAlertsLink()
            .addAlert(alert)
            .fillOutForm(alert)
            .clickAddAlertSaveButton();
        Assert.assertFalse(alertPage.isTextDisplayed("Account is required."));
        AlertForm alertForm = alertPage.expandAlert(alert);

        Assert.assertTrue(alertForm.isAlertInformationCorrect(alert));
    }

    @TmsLink("523158")
    @TmsLink("523159")
    @TmsLink("523160")
    @TmsLink("523212")
    @Story("Edit Consumer Alerts")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class, CreditDepositAlert.class, MerchantActivityAlert.class, TransactionAmountExceedsAlert.class})
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void editConsumerAlertsTriggeredByConduit(D3Alert alert) {
        Dashboard dashboard = login(alert.getUser());
        Assert.assertNotNull(dashboard);
        editAlertCommonSteps(dashboard, alert);
        try {
            alert.triggerAlert();
        } catch (D3ApiException | ConduitException e) {
            log.error(ERROR_TRIGGERING_ALERT, e);
            Assert.fail(ERROR_TRIGGERING_ALERT);
        }

        Notices notices = dashboard.getHeader().clickMessagesButton();
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertSubject()));
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertMessageDetails(alert.getUser())));

    }

    @Flaky
    @TmsLink("523156")
    @TmsLink("523157")
    @TmsLink("523197")
    @TmsLink("523203")
    @Story("Edit Consumer Alerts")
    @RunForSpecificAlerts(d3AlertTrigger = {D3ScheduledJobs.DAILY_ALERTS})
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void editConsumerAlertsTriggeredByDailyAlertsJob(D3Alert alert) {
        Dashboard dashboard = login(alert.getUser());
        Assert.assertNotNull(dashboard);
        editAlertCommonSteps(dashboard, alert);
        try {
            alert.triggerAlert();
        } catch (D3ApiException | ConduitException e) {
            log.error(ERROR_TRIGGERING_ALERT, e);
            Assert.fail(ERROR_TRIGGERING_ALERT);
        }

        Notices notices = dashboard.getHeader().clickMessagesButton();
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertSubject()));
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertMessageDetails(alert.getUser())));

    }

    @Flaky
    @TmsLink("523154")
    @TmsLink("523202")
    @Story("Edit Consumer Alerts")
    @RunForSpecificAlerts(d3AlertTrigger = {D3ScheduledJobs.SCHEDULED_ALERTS})
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void editConsumerAlertsTriggeredByScheduledAlertsJob(D3Alert alert) {
        Dashboard dashboard = login(alert.getUser());
        Assert.assertNotNull(dashboard);
        editAlertCommonSteps(dashboard, alert);
        try {
            alert.triggerAlert();
        } catch (D3ApiException | ConduitException e) {
            log.error(ERROR_TRIGGERING_ALERT, e);
            Assert.fail(ERROR_TRIGGERING_ALERT);
        }

        Notices notices = dashboard.getHeader().clickMessagesButton();
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertSubject()));
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertMessageDetails(alert.getUser())));

    }

    @TmsLink("523222")
    @TmsLink("523223")
    @TmsLink("523224")
    @TmsLink("523225")
    @TmsLink("523226")
    @TmsLink("523227")
    @TmsLink("523228")
    @TmsLink("523229")
    @TmsLink("523230")
    @TmsLink("523231")
    @Story("Delete Consumer Alerts")
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void deleteConsumerAlerts(D3Alert alert) {
        Dashboard dashboard = login(alert.getUser());
        Assert.assertNotNull(dashboard);
        Alerts deleteAlert = dashboard
            .getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickAlertsLink()
            .deleteAlert(alert);
        Assert.assertTrue(deleteAlert.isTextDisplayed(AlertsL10N.Localization.DELETE_CONFIRM.getValue()));
        deleteAlert.confirmDeletingAlert();
        Assert.assertTrue(deleteAlert.isAlertDeleted(alert));
    }

    @TmsLink("523215")
    @TmsLink("523220")
    @TmsLink("523221")
    @TmsLink("523218")
    @Story("Alert Filter Dropdown")
    @Test(dataProvider = "Basic User with Alert Types")
    public void checkIfAlertsAreShown(D3User user, AlertType type) {
        List<String> normalAlerts = AlertDatabaseHelper.getNonMandatoryAlertDescription(type);
        List<String> allAlertDescriptions = AlertDatabaseHelper.getAlertDescriptions(type);

        Assert.assertNotNull(normalAlerts, "Error getting alerts");
        Assert.assertNotNull(allAlertDescriptions, "Error getting alerts");

        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);

        Alerts alertsPage = dashboard
            .getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickAlertsLink()
            .filterByAlertType(type);
        for (String normalAlert : normalAlerts) {
            Assert.assertTrue(alertsPage.isTextPresent(normalAlert), String.format("Alert was not present on the screen: %s", normalAlert));
        }
        alertsPage.clickMandatoryButton();
        for (String alert : allAlertDescriptions) {
            // Alert text doesn't show up for out of band and verify destination alerts. this is expected behavior
            if (!alert.toLowerCase().contains("out of band") && !alert.toLowerCase().contains("verify destination")) {
                Assert.assertTrue(alertsPage.isTextPresent(alert), String.format("Alert was not present on the screen: %s", alert));
            }
        }
    }
}
