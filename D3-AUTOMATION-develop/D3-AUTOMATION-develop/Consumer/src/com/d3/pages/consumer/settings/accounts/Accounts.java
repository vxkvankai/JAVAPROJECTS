package com.d3.pages.consumer.settings.accounts;

import com.d3.datawrappers.account.D3Account;
import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class Accounts extends BaseConsumerPage {

    @FindBy(xpath = "//button[contains(text(), 'Show')]")
    private Element showAccountButton;

    @FindBy(css = "div.hidden-accounts li.entity-summary.account")
    private List<Element> hiddenAccounts;


    @FindBy(css = "div.modal-action-container button.cancel")
    private Element closeDisclosureButton;

    @FindBy(css = "button.save")
    private Element estatementSaveButton;

    @FindBy(css = "li.estmt-account")
    private List<Element> eStatementAccountList;

    @FindBy(name = "type")
    private List<Select> eStatementDeliveryMethod;


    public Accounts(WebDriver driver) {
        super(driver);
    }


    @Override
    protected Accounts me() {
        return this;
    }

    public Accounts selectEstatementAccountToUnenroll(String accountName, D3Account.Estatement preferenceValue) {

        for (int i = 0; i < eStatementAccountList.size(); i++) {
            if (eStatementAccountList.get(i).getText().contains(accountName)) {
                eStatementDeliveryMethod.get(i).selectByValue(preferenceValue.name());
                break;
            }
        }
        return this;
    }

    public Accounts clickCloseDisclosure() {
        closeDisclosureButton.click();
        return this;
    }

    public Accounts clickEstatementSaveButton() {
        estatementSaveButton.click();
        return this;
    }

    public Accounts clickAnyShowAccountButton() {
        showAccountButton.click();
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
                log.info("This is not the hidden account you're looking for. Trying next one", e);
            }

        }
        throw new WebDriverException(String.format("No account was found with the name: %s", accountName));
    }


}
