package com.d3.pages.consumer.settings.users;

import com.d3.support.D3Element;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class AccountServices extends PageObjectBase {

    private static final String VALUE = "value";

    private D3Element accountSlider(String accountName) {
        By by = By.xpath(String.format("//span[@class='label-text'][contains(text(), '%s')]/preceding-sibling::*[1]", accountName));
        return new D3Element(driver.findElement(by));
    }

    private boolean accountEnabled(String accountName) {
        By by = By.xpath(String.format("//span[@class='label-text'][contains(text(), '%s')]/preceding-sibling::*[2]", accountName));
        return (driver.findElement(by).isSelected());
    }

    @FindBy(xpath = "//span[@class='label-text']")
    private List<Element> accountsDisplayed;

    @FindBy(css = "button.btn-next")
    private Element nextButton;

    @FindBy(name = "hasStatementsTransactions")
    private List<Element> statementAndTransactionsCheckbox;

    @FindBy(name = "hasTransfer")
    private List<Element> internalTransfersCheckbox;

    @FindBy(name = "hasBillPay")
    private List<Element> billPayCheckbox;

    @FindBy(name = "hasWire")
    private List<Element> wireCheckbox;

    @FindBy(name = "amount")
    private List<Element> perAccountTransactionLimit;

    @FindBy(name = "count")
    private List<Element> countLimit;

    @FindBy(xpath = "//select[@name='period']")
    private List<Element> periodLimit;

    public AccountServices(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AccountServices me() {
        return this;
    }

    public AccountServices enableAccount(String accountName) {
        if (!accountEnabled(accountName)) {
            accountSlider(accountName).click();
        }
        return this;
    }

    public AccountServices disableAccount(String accountName) {
        if (accountEnabled(accountName)) {
            accountSlider(accountName).click();
        }
        return this;
    }

    public BankingServices continueToBankingServicesForm() {
        nextButton.click();
        return BankingServices.initialize(driver, BankingServices.class);
    }

    public AccountServices enableStatementsAndTransactionsForAccount(String accountName) {
        for (int x = 0; x < accountsDisplayed.size(); x++) {
            if (accountsDisplayed.get(x).getText().contains(accountName) && !statementAndTransactionsCheckbox.get(x).isSelected()) {
                    statementAndTransactionsCheckbox.get(x).click();
            }
        }
        return this;
    }

    public AccountServices enableInternalTransfersForAccount(String accountName) {
        for (int x = 0; x < accountsDisplayed.size(); x++) {
            if (accountsDisplayed.get(x).getText().contains(accountName) && !internalTransfersCheckbox.get(x).isSelected()) {
                    internalTransfersCheckbox.get(x).click();
            }
        }
        return this;
    }

    public AccountServices enableBillPayForAccount(String accountName) {
        for (int x = 0; x < accountsDisplayed.size(); x++) {
            if (accountsDisplayed.get(x).getText().contains(accountName) && !billPayCheckbox.get(x).isSelected()) {
                    billPayCheckbox.get(x).click();
            }
        }
        return this;
    }

    public AccountServices enableWireForAccount(String accountName) {
        for (int x = 0; x < accountsDisplayed.size(); x++) {
            if (accountsDisplayed.get(x).getText().contains(accountName) && !wireCheckbox.get(x).isSelected()) {
                    wireCheckbox.get(x).click();
            }
        }
        return this;
    }

    public AccountServices enterAmount(String amount) {
        for (int x = 0; x < perAccountTransactionLimit.size(); x++) {
            if (perAccountTransactionLimit.get(x).isEnabled() && perAccountTransactionLimit.get(x).getAttribute(VALUE).equals("")) {
                perAccountTransactionLimit.get(x).sendKeys(amount);
            }
        }
        return this;

    }

    public AccountServices enterCount(String count) {
        for (int x = 0; x < countLimit.size(); x++) {
            if (countLimit.get(x).isEnabled() && countLimit.get(x).getAttribute(VALUE).equals("")) {
                countLimit.get(x).sendKeys(count);
            }
        }
        return this;

    }

    public AccountServices enterPeriod(String period) {
        for (Element aPeriodLimit : periodLimit) {
            if (aPeriodLimit.isEnabled() && aPeriodLimit.getAttribute(VALUE).isEmpty()) {
                    aPeriodLimit.findElement(By.xpath("./option[contains(text(), '" + period + "')]")).click();
            }
        }
        return this;
    }

}
