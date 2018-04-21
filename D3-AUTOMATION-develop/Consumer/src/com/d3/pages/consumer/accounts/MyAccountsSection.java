package com.d3.pages.consumer.accounts;


import static com.d3.helpers.AccountHelper.verifyAccounts;
import static com.d3.helpers.WebdriverHelper.switchToNewBrowserTab;

import com.d3.datawrappers.account.stoppayment.D3StopPayment;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.helpers.DateAndCurrencyHelper;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import com.d3.support.wrappers.base.Radio;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyAccountsSection extends Accounts<MyAccountsSection> {


    // for some reason same locator as the exclude button, but we should seperate them in case it changes
    @FindBy(css = "button.exclude-account")
    private Element includeAccountButton;

    @FindBy(css = "button.primary.ok")
    private Element continueButton;

    @FindBy(css = "button.cancel")
    private Element cancelButton;

    @FindBy(css = "button.hide-account")
    private Element hideAccountButton;

    @FindBy(css = "button.estmt-enroll")
    private Element estatementEnrollButton;

    @FindBy(css = "button.next-view")
    private Element estatementNextButton;

    @FindBy(name = "enrollCode")
    private Element estatementCodeTextBox;

    @FindBy(css = "button.save-view")
    private Element estatementSaveButton;

    @FindBy(css = "button.stop-pmt")
    private Element stopPaymentButton;

    @FindBy(name = "type")
    private Select stopPaymentType;

    @FindBy(css = "button.overdraft-access")
    private Element overdraftButton;

    @FindBy(name = "enrollCode")
    private Element enrollCodeInput;

    @FindBy(css = "button.next-view")
    private Element enrollNextButton;

    @FindBy(name = "ATM_DEBIT_CARD_TRANSACTIONS")
    private List<Radio> debitCardOverDraftRadios;

    @FindBy(name = "CHECK_ELECTRONIC_TRANSACTIONS")
    private List<Radio> checkETransactionsRadios;

    @FindBy(css = "button.download-disclosure")
    private Element downloadDisclosureButton;

    @FindBy(css = "button.save-view")
    private Element overdraftSaveButton;

    @FindBy(css = "button.order-checks")
    private Element orderChecksButton;

    @FindBy(css = "div.account-name")
    private List<Element> availableAccounts;

    @FindBy(xpath = ".//*[contains(.,'Status')]/div[contains(@class,'value')]")
    private Element statusLabel;

    @FindBy(xpath = ".//*[contains(.,'Product')]/div[contains(@class,'value')]")
    private Element productLabel;

    @FindBy(xpath = "//div[contains(.,'Account Number')]/div[contains(@class,'value')]")
    private Element accountNumberLabel;

    @FindBy(css = "li.entity.active span.balance")
    private Element balanceLabel;

    @FindBy(id = "nickname")
    private Element nickNameInput;

    @FindBy(css = "button.save")
    private Element saveAccountButton;

    @FindBy(css = "li.entity.active div.account-name")
    private Element activeAccountName;

    @FindBy(css = "button.ok")
    private Element continueHideAccount;

    @FindBy(css = "button.brokerage-access")
    private Element brokerageAccessButton;

    @FindBy(css = "button.view-estmts")
    private Element viewEstatementButton;

    @FindBy(css = "a.estmt-label")
    private List<Element> estatementHtmlLink;

    @FindBy(css = "button.go-paperless")
    private Element goPaperlessButton;

    @FindBy(name = "paperlessOnly")
    private CheckBox paperlessCheckBox;

    public MyAccountsSection(WebDriver driver) {
        super(driver);
    }

    @Step("Click the go paperless button")
    public MyAccountsSection clickGoPaperlessButton() {
        goPaperlessButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the Go Paperless check box")
    public MyAccountsSection clickGoPaperlessCheckBox() {
        paperlessCheckBox.check();
        return this;
    }

    @Step("Click the E-statement enroll button")
    public MyAccountsSection clickEstatementEnrollButton() {
        estatementEnrollButton.click();
        return this;
    }

    @Step("Click the estatement next button")
    public MyAccountsSection clickEstatementNextButton() {
        estatementNextButton.click();
        return this;
    }

    @Step("Enter the estatement code: {code}")
    public MyAccountsSection enterEstatementCode(String code) {
        estatementCodeTextBox.sendKeys(code);
        return this;
    }

    @Step("Click the estatement save button")
    public MyAccountsSection clickEstatementSaveButton() {
        estatementSaveButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the continue button")
    public MyAccountsSection clickContinueButton() {
        continueButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the hide account button")
    public MyAccountsSection clickHideAccountButton() {
        hideAccountButton.click();
        return this;
    }

    @Step("Click the Include Account button")
    public MyAccountsSection clickIncludeAccountButton() {
        includeAccountButton.click();
        return this;
    }

    @Step("Click the stop payment button")
    public MyAccountsSection clickStopPaymentButton() {
        stopPaymentButton.click();
        return this;
    }

    @Step("Select the stop payment type: {stopPayment}")
    public StopPaymentForm selectStopPaymentType(D3StopPayment stopPayment) {
        stopPaymentType.selectByText(stopPayment.getType().toString());
        return stopPayment.getStopPaymentForm(driver);
    }

    @Step("Click the overdraft Button")
    public MyAccountsSection clickOverdraftButton() {
        overdraftButton.click();
        waitForSpinner();
        return this;
    }

    /**
     * Returns true if the overdraft save button is enabled, false otherwise
     */
    @Step("Check if the overdraft save button is enabled")
    public boolean isOverDraftSaveButtonEnabled() {
        try {
            return overdraftSaveButton.isEnabled();
        } catch (NoSuchElementException e) {
            logger.error("No save button found", e);
            return false;
        }
    }

    @Step("Enter enroll code: {code}")
    public MyAccountsSection enterEnrollCode(String code) {
        enrollCodeInput.sendKeys(code);
        return this;
    }

    @Step("Click the enroll next button")
    public MyAccountsSection clickEnrollNextButton() {
        enrollNextButton.click();
        waitForSpinner();
        return this;
    }

    private Radio getRadioElement(List<Radio> radios, boolean optIn) {
        String value = optIn ? "OPTED_IN" : "OPTED_OUT";
        return radios.stream().filter(radioButton -> radioButton.getValueAttribute().equalsIgnoreCase(value)).findFirst()
            .orElseThrow(() -> new NoSuchElementException("Radio button not found"));
    }

    /**
     * Returns true if the ATM radio button opt in or out is selected
     *
     * @param optIn set to True to check the Opt-in radio button, false for the opt-out button
     */
    @Step("Check if the ATM overdraft option is selected")
    public boolean isATMOverdraftOptionSelected(boolean optIn) {
        return getRadioElement(debitCardOverDraftRadios, optIn).isSelected();
    }

    @Step("Select the ATM opt in or opt out radio: {optIn}")
    public MyAccountsSection selectATMOptInOrOptOut(boolean optIn) {
        getRadioElement(debitCardOverDraftRadios, optIn).click();
        return this;
    }

    /**
     * Returns true if the e-transactions radio button opt in or out is selected
     *
     * @param optIn set to True to check the Opt-in radio button, false for the opt-out button
     */
    @Step("Is the E-Transactions Overdraft Option selected")
    public boolean isETransactionsOverdraftOptionSelected(boolean optIn) {
        return getRadioElement(checkETransactionsRadios, optIn).isSelected();
    }

    @Step("Select the E-Transactions opt in or opt out radio: {optIn}")
    public MyAccountsSection selectETransactionsOptInOrOptOut(boolean optIn) {
        getRadioElement(checkETransactionsRadios, optIn).click();
        return this;
    }

    @Step("Click the Download disclosure button")
    public MyAccountsSection clickDownloadDisclosureButton() {
        downloadDisclosureButton.click();
        return this;
    }

    @Step("Click the Overdraft Save button")
    public MyAccountsSection clickOverdraftSaveButton() {
        overdraftSaveButton.click();
        return this;
    }

    @Step("Check if the available accounts are correct")
    public boolean areAvailableAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, availableAccounts, Element::getText, false);
    }

    @Step("Enter the nick name: {nickname}")
    public MyAccountsSection enterNickName(String nickName) {
        nickNameInput.sendKeys(nickName);
        return this;
    }

    @Step("Click the save account button")
    public MyAccountsSection clickSaveAccountButton() {
        saveAccountButton.click();
        waitForSpinner();
        return this;
    }

    @Step("Check if the balance is correct: {balance}")
    public boolean isBalanceCorrect(String balance) {
        return DateAndCurrencyHelper.getAmountWithDollarSign(balance).equals(balanceLabel.getText());
    }

    @Step("Check if the product account is correct: {product}")
    public boolean isProductAccountCorrect(String product) {
        return product.equals(productLabel.getText());
    }

    @Step("Get the value active account")
    public String getValueActiveAccount() {
        return activeAccountName.getText();
    }

    @Step("Click the continue hide account button")
    public MyAccountsSection clickContinueHideAccount() {
        continueHideAccount.click();
        waitForSpinner();
        return this;
    }

    @Step("Click the view E-statements button")
    public MyAccountsSection clickViewEstatementsButton() {
        viewEstatementButton.click();
        return this;
    }

    @Step("View the E-Statement HTML")
    public MyAccountsSection viewEstatementHtml() {
        estatementHtmlLink.get((ThreadLocalRandom.current().nextInt(estatementHtmlLink.size()))).click();
        switchToNewBrowserTab(driver);
        waitForSpinner();
        return this;
    }

    @Step("Check if the hide account button is displayed")
    public boolean isHideAccountButtonDisplayed() {
        try {
            return hideAccountButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Click the cancel button")
    public MyAccountsSection clickCancelButton() {
        cancelButton.click();
        return this;
    }

    @Step("Click the brockerage access button")
    public MyAccountsSection clickBrokerageAccessButton() {
        brokerageAccessButton.click();
        waitForSpinner();
        switchToNewBrowserTab(driver);
        waitForSpinner();
        return this;
    }

    /**
     * Clicks the Order Checks button (which opens a new browser tab), then switches the WebDriver focus to the new tab
     *
     * @return MyAccountSection
     */
    @Step("Click the order checks button")
    public MyAccountsSection clickOrderChecksButton() {
        orderChecksButton.click();
        waitForSpinner();
        switchToNewBrowserTab(driver);
        return this;
    }

    /**
     * Checks to see if Stop Payment dropdown contains specified Type option
     *
     * @return true if option is available and can be selected, otherwise return false
     */
    @Step("Check if the stop payment option is available")
    public boolean isStopPaymentOptionAvailable(D3StopPayment stopPayment) {
        return stopPaymentType.optionEqualsText(stopPayment.getType().toString());
    }

    @Step("Check if the E-Statement enroll button is displayed")
    public boolean isEstatementEnrollButtonDisplayed() {
        try {
            return estatementEnrollButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if the Stop Payment button is displayed for an account
     *
     * @return true if button is displayed, otherwise false
     */
    @Step("Check if the stop payment button is displayed")
    public boolean isStopPaymentButtonDisplayed() {
        try {
            return stopPaymentButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Check if the brokerage access button is displayed")
    public boolean isBrokerageAccessButtonDisplayed() {
        try {
            return brokerageAccessButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if the Estatement HTML is displayed correctly for an account
     *
     * @return true if HTML is displayed, otherwise false
     */
    @Step("Check if the E-Statment HTML is displayed")
    public boolean isEStatementHTMLDisplayed() {
        String errorMsg = "EStatement Monthly Statement section: {} was not found on the DOM";
        try {
            checkIfTextDisplayed("Other Debits", errorMsg);
            checkIfTextDisplayed("Deposits/Other Credits", errorMsg);
            checkIfTextDisplayed("Balance Summary", errorMsg);
            checkIfTextDisplayed("Interest Summary", errorMsg);
            checkIfTextDisplayed("Overdraft/Return Item Fees", errorMsg);
        } catch (TextNotDisplayedException e) {
            logger.warn("EStatement Monthly Statement section check failed:", e);
            return false;
        }

        return true;
    }

    @Override
    protected MyAccountsSection me() {
        return this;
    }

}
