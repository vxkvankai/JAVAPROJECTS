package com.d3.pages.consumer.settings.alerts;

import static com.d3.helpers.WebdriverHelper.waitUntilClickableBy;

import com.d3.datawrappers.alerts.AlertType;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.SettingsBasePage;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class Alerts extends SettingsBasePage {

    @FindBy(css = "div.alertsButtonGroup button")
    private Element addAlertButton;

    @FindBy(css = "li.alert-entity.active button.editAlert")
    private Element editAlert;

    @FindBy(css = "div.description")
    private List<Element> alertName;

    @FindBy(css = "li.alert-entity.active button.deleteAlert")
    private Element deleteAlert;

    @FindBy(css = "button.submit-one")
    private Element deleteAlertContinue;

    @FindBy(xpath = "//button[text()='Show Mandatory']")
    private Element showMandatoryButton;

    @FindBy(id = "nav-primary-title")
    private Element alertFilterDropdown;

    private Element alertTypeLink(AlertType type) {
        By by = By.xpath(String.format("//a[@data-display-group='%s']", type.toString()));
        return new D3Element(driver.findElement(by));
    }

    public Alerts(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Alerts me() {
        return this;
    }

    @Step("Select alert {alertName}")
    private Element selectAlertToAdd(String alertName) {
        By byLink = By.linkText(String.format("%s", alertName));
        return waitUntilClickableBy(driver, byLink);
    }

    @Step("Select alert: {alert}")
    public AlertForm selectAlertToAdd(D3Alert alert) {
        selectAlertToAdd(alert.getName()).click();
        return alert.getAlertForm(driver);
    }

    @Step("Expand alert: {alert}")
    public AlertForm expandAlert(D3Alert alert) {
        log.info("Attempting to expand first alert with type: {}", alert.getName());
        alertName.stream().filter(name -> name.getText().contains(alert.getName())).findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("No Alert found for %s", alert.getName()))).click();
        return alert.getAlertForm(driver);
    }

    @Step("Delete alert: {alert}")
    public Alerts deleteAlert(D3Alert alert) {
        expandAlert(alert);
        deleteAlert.click();
        return this;
    }

    @Step("Confirm deleting alert")
    public Alerts confirmDeletingAlert() {
        deleteAlertContinue.click();
        return this;
    }

    @Step("Edit alert: {alert}")
    public AlertForm editAlert(D3Alert alert) {
        expandAlert(alert);
        editAlert.click();
        return alert.getAlertForm(driver);
    }

    @Step("Add alert: {alert}")
    public AlertForm addAlert(D3Alert alert) {
        addAlertButton.click();
        selectAlertToAdd(alert);
        return alert.getAlertForm(driver);
    }

    @Step("Check if the alert is deleted: {alert}")
    public boolean isAlertDeleted(D3Alert alert) {
        String errorMsg = "%s: %s is still displayed on the DOM for Alert page";
        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
        } catch (TextNotDisplayedException e) {
            log.warn("Alert was not deleted", e);
            return true;
        }
        return false;
    }

    @Step("Filter by alert type: {type}")
    public Alerts filterByAlertType(AlertType type) {
        alertFilterDropdown.click();
        alertTypeLink(type).click();
        return this;
    }

    @Step("Click mandatory button")
    public Alerts clickMandatoryButton() {
        showMandatoryButton.click();
        return this;
    }
}
