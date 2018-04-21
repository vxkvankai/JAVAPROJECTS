package com.d3.pages.consumer.accounts;


import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


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
                logger.info("Element was not present");
                isDisplayed = false;
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
        logger.info("Waiting for small spinner");
        // sometimes this takes a long time
        new WebDriverWait(driver, 15, 300).until(driver1 ->
            isGenericSpinnerClassNotPresent(driver1, By.cssSelector("span.sync-status.pending")));

        logger.info("Small spinner is no longer there");
    }
}
