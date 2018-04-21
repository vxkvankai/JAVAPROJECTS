package com.d3.pages.consumer.accounts;


import static org.awaitility.Awaitility.await;

import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Accounts<T extends Accounts> extends BaseConsumerPage {

    @FindBy(xpath = ".//*[contains(.,'Status')]/div[contains(@class,'value')]")
    private Element statusLabel;
    @FindBy(xpath = ".//*[contains(.,'Institution')]/div[contains(@class,'value')]")
    private Element institutiontLabel;
    @FindBy(css = "button.edit")
    private List<Element> editAccountButton;
    @FindBy(css = ".btn.delete")
    private Element removeAccountButton;
    @FindBy(xpath = "//div[contains(.,'Account Number')]/div[contains(@class,'value')]")
    private Element accountNumberLabel;
    @FindBy(css = "button.exclude-account")
    private Element excludeAccountButton;

    public Accounts(WebDriver driver) {
        super(driver);
    }

    @Override
    protected abstract T me();

    public MyAccountsSection getMyAccountsSection() {
        return MyAccountsSection.initialize(driver, MyAccountsSection.class);
    }

    public OtherAccountsSection getOtherAccountsSection() {
        return OtherAccountsSection.initialize(driver, OtherAccountsSection.class);
    }

    private D3Element accountDiv(String name) {
        By by = By.xpath(String.format("//div[contains(text(),'%s')]", name));
        return new D3Element(driver.findElement(by));
    }

    @Step("Click the Exclude Account Button")
    public T clickExcludeAccountButton() {
        excludeAccountButton.click();
        return me();
    }

    @Step("Click the {accountName} account")
    public T clickAccountByAccountName(String accountName) {
        accountDiv(accountName).click();
        waitForSmallSpinner();
        waitForSpinner();
        try {
            new WebDriverWait(driver, 15, 300).until(driver1 -> editAccountButtonIsDisplayed());
        } catch (TimeoutException e) {
            log.warn("Edit button didn't show up, trying to click again", e);
            accountDiv(accountName).click();
            waitForSmallSpinner();
            waitForSpinner();
        }
        return me();
    }

    @Step("Check if Status label is {status}")
    public boolean isStatusAccountCorrect(String status) {
        return statusLabel.getText().equals(status);
    }

    @Step("Get the account number")
    public String getAccountNumber() {
        return accountNumberLabel.getText();
    }

    @Step("Click the Edit Account Button")
    public T clickEditAccountButton() {
        getDisplayedElement(editAccountButton).click();
        return me();
    }


    @Step("Check if the remove account button is displayed")
    public boolean removeAccountButtonIsDisplayed() {
        try {
            return removeAccountButton.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Error finding button", e);
            return false;
        }
    }

    @Step("Check if the edit account button is displayed")
    public boolean editAccountButtonIsDisplayed() {
        boolean isDisplayed = false;
        for (Element button : editAccountButton) {
            try {
                if (button.isDisplayed()) {
                    isDisplayed = true;
                    break;
                }
            } catch (NoSuchElementException e) {
                log.info("Element was not present", e);
            }
        }
        return isDisplayed;
    }

    @Step("Is the institution label set to {institution}")
    public boolean isInstitutionAccountCorrect(String institution) {
        return institutiontLabel.getText().equals(institution);

    }

    @Step("Wait for the small spinner")
    void waitForSmallSpinner() {
        await("Waiting until small spinner is gone").atMost(15, TimeUnit.SECONDS).until(smallSpinnerIsGone(driver));
    }

    private Callable<Boolean> smallSpinnerIsGone(WebDriver driver) {
        return () -> (boolean) ((JavascriptExecutor) driver).executeScript("return document.querySelector('span.sync-status.pending') == null")
            && (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
    }
}
