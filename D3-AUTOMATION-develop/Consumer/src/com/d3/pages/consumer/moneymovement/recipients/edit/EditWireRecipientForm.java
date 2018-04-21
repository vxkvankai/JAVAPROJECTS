package com.d3.pages.consumer.moneymovement.recipients.edit;

import static com.d3.helpers.AccountHelper.getHiddenAccountString;

import com.d3.datawrappers.recipient.base.WireRecipient;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class EditWireRecipientForm extends PageObjectBase implements EditRecipientsForm<EditWireRecipientForm, WireRecipient> {

    @FindBy(name = "routingNumber")
    private Element routingNumberInput;

    @FindBy(name = "accountNumber")
    private Element accountNumberInput;

    @FindBy(name = "fiName")
    private Element financialInstitutionInput;

    @FindBy(name = "primaryAddress.line1")
    private Element address1Input;

    @FindBy(name = "primaryAddress.line2")
    private Element address2Input;

    @FindBy(name = "primaryAddress.city")
    private Element cityInput;

    @FindBy(name = "primaryAddress.state")
    private Element stateInput;

    @FindBy(name = "primaryAddress.postalCode")
    private Element zipCodeInput;

    @FindBy(css = "button.btn-submit")
    private Element saveButton;
    
    @FindBy(css = "button.btn-cancel")
    private Element cancelButton;
    
    public String getRoutingNumber() {
        return routingNumberInput.getValueAttribute();
    }

    public EditWireRecipientForm enterRoutingNumber(String routingNumber) {
        routingNumberInput.sendKeys(routingNumber);
        return this;
    }

    public String getAccountNumber() {
        return accountNumberInput.getValueAttribute();
    }

    public EditWireRecipientForm enterAccountNumber(String accountNumber) {
        accountNumberInput.sendKeys(accountNumber);
        return this;
    }

    public String getFinancialInstitution() {
        return financialInstitutionInput.getValueAttribute();
    }

    public EditWireRecipientForm enterFinancialInstitution(String fi) {
        financialInstitutionInput.sendKeys(fi);
        return this;
    }

    public String getAddress1() {
        return address1Input.getValueAttribute();
    }

    public EditWireRecipientForm enterAddress1(String add1) {
        address1Input.sendKeys(add1);
        return this;
    }

    public String getAddress2() {
        return address2Input.getValueAttribute();
    }

    public EditWireRecipientForm enterAddress2(String add2) {
        address2Input.sendKeys(add2);
        return this;
    }

    public String getCity() {
        return cityInput.getValueAttribute();
    }

    public EditWireRecipientForm enterCity(String city) {
        cityInput.sendKeys(city);
        return this;
    }

    public String getState() {
        return stateInput.getValueAttribute();
    }

    public EditWireRecipientForm enterState(String state) {
        stateInput.sendKeys(state);
        return this;
    }

    public String getZipCode() {
        return zipCodeInput.getValueAttribute();
    }

    public EditWireRecipientForm enterZipCode(String zipcode) {
        zipCodeInput.sendKeys(zipcode);
        return this;
    }

    public EditWireRecipientForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected EditWireRecipientForm me() {
        return this;
    }

    @Override
    public boolean isFormInformationCorrect(WireRecipient wireRecipient) {
        try {
            logger.info("Checking if routing number: {} is on the form", wireRecipient.getRoutingNumber());
            checkIfTextEquals(getRoutingNumber(), wireRecipient.getRoutingNumber());

            String hiddenAccount = getHiddenAccountString(wireRecipient.getAccountNumber());
            logger.info("Checking if Account Number: {} is on the form", hiddenAccount);
            checkIfTextEquals(getAccountNumber(), hiddenAccount);

            logger.info("Checking if financial institution: {} is on the form", wireRecipient.getFinancialInstitution());
            checkIfTextEquals(getFinancialInstitution(), wireRecipient.getFinancialInstitution());

            logger.info("Checking if address 1: {} is on the form", wireRecipient.getAddress1());
            checkIfTextEquals(getAddress1(), wireRecipient.getAddress1());

            logger.info("Checking if address 2: {} is on the form", wireRecipient.getAddress2());
            checkIfTextEquals(getAddress2(), wireRecipient.getAddress2());

            logger.info("Checking if city: {} is on the form", wireRecipient.getCity());
            checkIfTextEquals(getCity(), wireRecipient.getCity());

            logger.info("Checking if state: {} is on the form", wireRecipient.getState());
            checkIfTextEquals(getState(), wireRecipient.getState());

            logger.info("Checking if zip: {} is on the form", wireRecipient.getZipCode());
            checkIfTextEquals(getZipCode(), wireRecipient.getZipCode());
        } catch (TextNotDisplayedException e) {
            logger.warn("Wire Recipient form was not validated", e);
            return false;
        }

        return true;
    }

    @Override
    public EditWireRecipientForm fillOutForm(WireRecipient wireRecipient) {
        enterRoutingNumber(wireRecipient.getRoutingNumber())
                .enterAccountNumber(wireRecipient.getAccountNumber())
                .enterFinancialInstitution(wireRecipient.getFinancialInstitution())
                .enterAddress1(wireRecipient.getAddress1())
                .enterAddress2(wireRecipient.getAddress2())
                .enterCity(wireRecipient.getCity())
                .enterState(wireRecipient.getState())
                .enterZipCode(wireRecipient.getZipCode());
        return this;
    }

    @Override
    public EditRecipients clickSaveButton() {
        saveButton.click();
        waitForSpinner();
        return EditRecipients.initialize(driver, EditRecipients.class);
    }

    
    @Override
    public EditRecipients clickCancelButton(boolean isEditable) {
       if(isEditable) {
        cancelButton.click();
        waitForSpinner();
       }
        return EditRecipients.initialize(driver, EditRecipients.class);
    }
}
