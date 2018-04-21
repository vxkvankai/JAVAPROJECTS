package com.d3.pages.consumer.settings.alerts.consumer.base;

import static com.d3.helpers.RandomHelper.getRandomNumberInt;

import com.d3.datawrappers.alerts.D3Alert;
import com.d3.pages.consumer.settings.alerts.Alerts;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AlertForm<T extends AlertForm, A extends D3Alert> extends PageObjectBase implements AlertDetails<A> {

    @FindBy(name = "userAccountId")
    private Select accountDropdown;

    @FindBy(css = "li.alert-entity.active button.saveAlert")
    private Element addAlertSaveButton;

    @FindBy(css = "div.entity-edit button.saveAlert")
    private Element editAlertSaveButton;

    @FindBy(css = "button.cancelSaveAlert")
    private Element cancelButton;

    @FindBy(name = "frequency")
    private Select frequencyDropdown;

    @FindBy(xpath = "//label[@for='freqAttr1']")
    private Element firstFrequencyLabel;

    @FindBy(name = "freqAttr1")
    private Select firstFrequencyAttribute;

    @FindBy(xpath = "//label[@for='freqAttr2']")
    private Element secondFrequencyLabel;

    @FindBy(name = "freqAttr2")
    private Select secondFrequencyAttribute;

    @FindBy(css = "div.description")
    private List<Element> alertName;

    @FindBy(css = "li.alert-entity.active button.editAlert")
    private Element editAlert;

    public AlertForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected abstract T me();

    @Step("Select the account: {accountName}")
    public T selectAccount(String accountName) {
        accountDropdown.selectByTextContains(accountName);
        return me();
    }

    @Step("Check if the Account {accountName} is available")
    public boolean isAccountAvailable(String accountName) {
        return accountDropdown.optionContainsText(accountName);
    }

    @Step("Select frequency: {frequency}")
    public T selectFrequency(AlertFrequency frequency) {
        frequencyDropdown.selectByValue(frequency.name());
        return me();
    }

    @Step("Select the first frequency attribute: {attribute}")
    private T selectFrequencyAttribute1(String attribute) {
        logger.info("Selecting {} for {}", attribute, firstFrequencyLabel.getText());
        firstFrequencyAttribute.selectByValue(attribute);
        return me();
    }

    @Step("Select the second frequency attribute: {attribute}")
    private T selectFrequencyAttribute2(String attribute) {
        logger.info("Selecting {} for {}", attribute, secondFrequencyLabel.getText());
        secondFrequencyAttribute.selectByValue(attribute);
        return me();
    }

    @Step("Click the add alert save button")
    public Alerts clickAddAlertSaveButton() {
        addAlertSaveButton.click();
        waitForSpinner();
        return Alerts.initialize(driver, Alerts.class);
    }

    @Step("Click the edit alert save button")
    public Alerts clickEditAlertSaveButton() {
        editAlertSaveButton.click();
        waitForSpinner();
        return Alerts.initialize(driver, Alerts.class);
    }

    @Step("fill out the form with alert: {alert}")
    public abstract AlertForm fillOutForm(A alert);

    protected void selectFrequencyAttributes(AlertFrequency frequency) {
        DayOfWeek[] dayOfWeek = DayOfWeek.values();
        Month[] months = Month.values();

        switch (frequency) {
            case DAYS:
                break;
            case WEEKLY:
                selectFrequencyAttribute1(Integer.toString(dayOfWeek[ThreadLocalRandom.current().nextInt(dayOfWeek.length)].getValue()));
                break;
            case SEMI_MONTHLY:
                getRandomNumberInt(1, 12);
                selectFrequencyAttribute1(Integer.toString(getRandomNumberInt(1, 12)));
                selectFrequencyAttribute2(Integer.toString(getRandomNumberInt(0, 30)));
                break;
            case MONTHLY:
                selectFrequencyAttribute1(Integer.toString(getRandomNumberInt(1, 12)));
                break;
            case QUARTERLY:
                selectFrequencyAttribute1(Integer.toString(getRandomNumberInt(1, 3)));
                selectFrequencyAttribute2(Integer.toString(getRandomNumberInt(0, 30)));
                break;
            case SEMI_ANNUALLY:
            case ANNUALLY:
            default:
                selectFrequencyAttribute1(Integer.toString(months[ThreadLocalRandom.current().nextInt(months.length)].getValue()));
                selectFrequencyAttribute2(Integer.toString(getRandomNumberInt(0, 30)));
                break;
        }
    }
}
