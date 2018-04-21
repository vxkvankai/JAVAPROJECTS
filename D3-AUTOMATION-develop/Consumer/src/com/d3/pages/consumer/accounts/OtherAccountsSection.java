package com.d3.pages.consumer.accounts;

import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class OtherAccountsSection extends Accounts<OtherAccountsSection> {

    @FindBy(css = "button.dropdown-toggle")
    private Element addAnAccountButtonDropdown;

    @FindBy(className = "add-offline-account")
    private Element addOfflineAccountSelection;

    @FindBy(id = "product")
    private Select addOfflineAccountType;

    @FindBy(name = "nickname")
    private Element addOfflineAccountNicknameInput;

    @FindBy(name = "balance")
    private Element addOfflineAccountBalanceInput;

    @FindBy(css = "button.primary.save")
    private Element addOfflineAccountSaveButton;

    @FindBy(css = "button.edit")
    private List<Element> editAccountButton;

    @FindBy(id = "institution")
    private Element institutionField;

    @FindBy(xpath = ".//div[contains(@class,'aa-suggestion aa-cursor')]")
    private Element institutionOption;

    @FindBy(css = ".next.btn.btn-default.primary")
    private Element nextButton;

    @FindBy(name = "WEBF_t_Username")
    private Element userNameField;

    @FindBy(name = "WEBF_t_Password")
    private Element passwordField;

    @FindBy(css = "div.row-fluid")
    private List<Element> onlineAccountTypes;

    @FindBy(css = ".btn.btn-default.dropdown-toggle")
    private Element addAccountButton;

    @FindBy(css = ".add-external-account")
    private Element addOnlineAccount;

    @FindBy(xpath = ".//*[contains(.,'Last Updated')]/div[contains(@class,'value')]")
    private Element lastUpdatedValue;

    @FindBy(css = "button.refresh-account")
    private Element refreshAccountButton;

    @FindBy(xpath = ".//*[contains(.,'Payment Due Date')]/div[contains(@class,'value')]")
    private Element paymentDueLabel;

    @FindBy(xpath = ".//*[contains(.,'Last Successful Update')]/div[contains(@class,'value')]")
    private Element lastSuccessfulLabel;

    @FindBy(css = "button.change-credentials")
    private Element updatedAccountButton;

    @FindBy(css = ".btn.btn-default.change-credentials")
    private Element changeLoginButton;

    @FindBy(xpath = "//button[contains(text(), 'Add Institution')]")
    private Element addInstitutionButton;

    @FindBy(xpath = "//input[@name='url']")
    private Element institutionWebAddressField;

    @FindBy(xpath = "//button[contains(text(), 'Request External Institution')]")
    private Element requestExternalInstitutionButton;

    @FindBy(xpath = "//input[@name='name']")
    private Element institutionNameField;

    @FindBy(css = "button.delete")
    private Element removeAccountButton;

    @FindBy(css = "button.ok")
    private Element confirmRemoveAccountButton;

    @FindBy(css = ".toggle-details.btn-link")
    private Element viewMoreButton;

    @FindBy(css = "button.change-credentials")
    private Element changeCredentialsButton;

    @FindBy(name = "repeat_WEBF_t_Password")
    private Element repeatPasswordField;

    @FindBy(css = ".next.btn.btn-default.primary")
    private Element saveButton;

    public OtherAccountsSection(WebDriver driver) {
        super(driver);
    }

    @Override
    protected OtherAccountsSection me() {
        return this;
    }

    private Element getOnlineAccountElement(String accountType) {
        By by = By.xpath(String.format("//div[@class='account-name' and contains(text(), '%s')]", accountType));
        return new D3Element(driver.findElement(by));
    }

    @Step("Click the Add An Account Dropdown")
    public OtherAccountsSection clickAddAnAccountDropdown() {
        addAnAccountButtonDropdown.click();
        return this;
    }

    @Step("Click the Add Offline Account selection")
    public OtherAccountsSection clickOfflineAccountSelection() {
        addOfflineAccountSelection.click();
        waitForSpinner();
        return this;
    }

    @Step("Select the offline account type: {type}")
    public OtherAccountsSection selectOfflineAccountTypeByText(String type) {
        // maybe make this an enum?
        addOfflineAccountType.selectByText(type);
        return this;
    }

    @Step("Enter the Offline acocunt name: {name}")
    public OtherAccountsSection enterOfflineAccountName(String name) {
        addOfflineAccountNicknameInput.sendKeys(name);
        waitForSpinner();
        return this;
    }

    @Step("Enter the offline account balance: {amount}")
    public OtherAccountsSection enterOfflineAccountBalance(String amount) {
        addOfflineAccountBalanceInput.sendKeys(amount);
        return this;
    }

    @Step("Click the add Offline account save button")
    public OtherAccountsSection clickAddOfflineAccountSaveButton() {
        addOfflineAccountSaveButton.click();
        waitForSpinner();
        return this;
    }


    @Step("Click the add online account button")
    public OtherAccountsSection clickAddOnlineAccount() {
        addAccountButton.click();
        addOnlineAccount.click();
        return this;
    }

    @Step("Fill the institution field with text: {text}")
    public OtherAccountsSection fillInstitutionField(String text) {
        // NOTE (JMoravec): Don't add the tab key here, otherwise the system won't recognize existing corps
        institutionField.sendKeys(true, true, false, text);
        waitForSpinner();
        waitForSpinner();
        return this;
    }

    @Step("Click the next button")
    public OtherAccountsSection clickNextButton() {
        nextButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the next button and wait for text to disappear: {institutionToWaitForDisappear}")
    public OtherAccountsSection clickNextButtonSave(String institutionToWaitForDisappear) {
        nextButton.click();
        waitUntilTextNotPresent(institutionToWaitForDisappear);
        waitForSpinner();
        return this;
    }

    @Step("Enter the user name: {username}")
    public OtherAccountsSection enterUserName(String username) {
        userNameField.sendKeys(username);
        return this;
    }

    @Step("Enter the password: {password}")
    public OtherAccountsSection enterPassword(String password) {
        passwordField.sendKeys(password);
        return this;
    }

    @Step("Click the add Institution button")
    public OtherAccountsSection clickAddInstitutionButton() {
        addInstitutionButton.click();
        waitForSpinner();
        return this;
    }


    @Step("Click the change credentials button")
    public OtherAccountsSection clickChangeCredentialsButton() {
        changeCredentialsButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Enter the institution web address: {webAddress}")
    public OtherAccountsSection enterInstitutionWebAddress(String webAddress) {
        institutionWebAddressField.sendKeys(webAddress);
        return this;
    }

    @Step("enter the institution name: {name}")
    public OtherAccountsSection enterInstitutionName(String name) {
        institutionNameField.sendKeys(name);
        return this;
    }

    @Step("Click the request external institution button")
    public OtherAccountsSection clickRequestExternalInstitutionButton() {
        requestExternalInstitutionButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Enter a new password: {password}")
    public OtherAccountsSection enterNewPassword(String password) {
        repeatPasswordField.sendKeys(password);
        return this;
    }

    @Step("Add credentials to online account via user: {user}")
    public OtherAccountsSection addCredentialsToOnlineAccount(D3User user) {
        return enterUserName(user.getLogin())
            .clickNextButton()
            .enterPassword(user.getPassword())
            .clickNextButton();
    }

    @Step("Get the online account number: {text}")
    public String getOnlineAccountNumber(String text) {
        By by = By.xpath(String.format("//form[@class='account-selection']/div[contains(.,'%s')]", text));
        return driver.findElement(by).getText().replaceAll("[^0-9]", "");
    }

    @Step("Select the account online type: {text}")
    public void selectAccountOnlineType(String text) {
        for (int i = 1; i < onlineAccountTypes.size(); ++i) {
            if (onlineAccountTypes.get(i).getText().contains(text)) {
                By by = By.xpath(String.format("(//input[@class='selected'])[%s]", i));
                onlineAccountTypes.get(i).findElement(by).click();
                break;
            }
        }
    }

    @Step("Check the option: {accountType}")
    public OtherAccountsSection checkOption(String accountType) {
        selectAccountOnlineType(accountType);
        return this;
    }

    public boolean isOnlineAccountformationDisplayed() {

        logger.info("Looking for the value of Due Date Label");
        if (paymentDueLabel.getText().isEmpty()) {
            logger.warn("Online account labels are empty");
            return false;
        }
        logger.info("Looking for the value of last successful Label");
        if (lastSuccessfulLabel.getText().isEmpty()) {
            logger.warn("Online account labels are empty");
            return false;
        }
        return true;
    }

    @Step("Check if the online account labels are displayed")
    public boolean areOnlineAccountLabelsDisplayed() {

        try {
            String errorMsg = "View more details: '%s' was not found on the DOM.";
            checkIfTextDisplayed("Payment Due Date", errorMsg);
            checkIfTextDisplayed("Last Successful", errorMsg);

        } catch (TextNotDisplayedException e) {
            logger.warn("Online account field check failed:", e);
            return false;
        }

        return true;
    }

    @Step("Check if the updated account button is displayed")
    public boolean updatedAccountButtonIsDisplayed() {
        try {
            return updatedAccountButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Check if the change login button is displayed")
    public boolean changeLoginButtonIsDisplayed() {
        try {
            return changeLoginButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Check if the online account button is displayed")
    public boolean isOnlineAccountDisplayed(String accountType) {
        try {
            return getOnlineAccountElement(accountType).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Get the last updated value text")
    public String getLastUpdateValue() {
        return lastUpdatedValue.getText();
    }

    @Step("Click the Refresh account button")
    public OtherAccountsSection clickRefreshAccountButton() {
        refreshAccountButton.click();
        waitForSmallSpinner();
        waitForSpinner();
        return this;
    }

    @Step("Click the view more button")
    public OtherAccountsSection clickViewMoreButton() {
        viewMoreButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the Save button")
    public OtherAccountsSection clickSaveButton() {
        saveButton.click();
        waitForSmallSpinner();
        waitForSpinner();
        return this;
    }

    @Step("Click the remove account button")
    public OtherAccountsSection clickRemoveAccountButton() {
        removeAccountButton.click();
        return this;
    }

    @Step("Click the confirm remove account button")
    public OtherAccountsSection clickConfirmRemoveAccountButton() {
        confirmRemoveAccountButton.click();
        waitForSpinner();
        return this;
    }
}

