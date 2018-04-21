package com.d3.tests.consumer.core.settings;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.alerts.AlertType;
import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.alerts.PaymentComingDueAlert;
import com.d3.datawrappers.alerts.ReminderAlert;
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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@SuppressWarnings("unchecked")
@Epic("Settings")
@Feature("Alerts")
public class AlertTests extends SettingsTestBase {

    @Story("Add Consumer Alerts")
    @TmsLink("3468")
    @TmsLink("3469")
    @TmsLink("3470")
    @TmsLink("3471")
    @TmsLink("3472")
    @TmsLink("16562")
    @TmsLink("16563")
    @TmsLink("3473")
    @TmsLink("3474")
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

    @Flaky //TODO add all trigger alert types. Will need @RunForSpecificAlerts until then
    @TmsLink("523197")
    @TmsLink("523158")
    @TmsLink("523202")
    @Story("Edit Consumer Alerts")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class, MerchantActivityAlert.class, PaymentComingDueAlert.class, ReminderAlert.class})
    @Test(dataProvider = "User with Specific Consumer Alerts Added")
    public void editConsumerAlerts(D3User user, D3Alert alert) throws ConduitException, D3ApiException {
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);

        dashboard.getHeader().clickSettingsButton().getTabs().clickAlertsLink()
            .editAlert(alert)
            .fillOutForm(alert)
            .clickEditAlertSaveButton().expandAlert(alert);
        alert.triggerAlert();

        Notices notices = dashboard.getHeader().clickMessagesButton();
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertSubject()));
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertMessageDetails(user)));

    }

    @TmsLink("523229")
    @TmsLink("523226")
    @TmsLink("523222")
    @TmsLink("523223")
    @TmsLink("523224")
    @TmsLink("523225")
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
        List<String> normalAlerts = DatabaseUtils.getNonMandatoryAlertDescription(type);
        List<String> allAlertDescriptions = DatabaseUtils.getAlertDescriptions(type);

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
