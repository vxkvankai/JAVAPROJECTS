package com.d3.pages.consumer.settings.accounts;

import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class Accounts extends BaseConsumerPage {

    @FindBy(xpath = "//button[contains(text(), 'Show')]")
    private Element showAccountButton;

    @FindBy(css = "div.hidden-accounts li.entity-summary.account")
    private List<Element> hiddenAccounts;






    public Accounts(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Accounts me() {
        return this;
    }

    public Accounts clickAnyShowAccountButton() {
        showAccountButton.click();
        waitForSpinner();
        return this;
    }

    public Accounts clickShowAccountButton(String accountName) {
        By by = By.xpath(String.format(".//*[contains(text(), '%s')]", accountName));
        By showButtonBy = By.xpath(".//button[contains(text(), 'Show')]");
        for (Element account : hiddenAccounts) {
            try {
                account.findElement(by);
                account.findElement(showButtonBy).click();
                return this;
            } catch (NoSuchElementException e) {
                logger.info("This is not the hidden account you're looking for. Trying next one");
            }

        }
        throw new WebDriverException(String.format("No account was found with the name: %s", accountName));
    }
}
