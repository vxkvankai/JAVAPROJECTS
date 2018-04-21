package com.d3.pages.consumer.moneymovement.recipients.edit.base;

import static com.d3.helpers.AccountHelper.getFormattedPhoneNumber;

import com.d3.datawrappers.recipient.base.BillAddressRecipient;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditRecipients;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public abstract class EditBillAddressRecipientForm<T extends EditBillAddressRecipientForm, R extends BillAddressRecipient> extends PageObjectBase
        implements EditRecipientsForm<T, R> {

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

    @FindBy(name = "phoneNumber")
    private Element phoneNumberInput;

    @FindBy(css = "button.btn-submit")
    private Element saveButton;

    public EditBillAddressRecipientForm(WebDriver driver) {
        super(driver);
    }

    protected abstract boolean isFinalFormInfoValid(R recipient);

    @Override
    protected abstract T me();

    public T enterAdd1(String add1) {
        address1Input.sendKeys(add1);
        return me();
    }

    public T enterAdd2(String add2) {
        address2Input.sendKeys(add2);
        return me();
    }

    public T enterCity(String city) {
        cityInput.sendKeys(city);
        return me();
    }

    public T enterState(String state) {
        stateInput.sendKeys(state);
        return me();
    }

    public T enterZip(String zip) {
        zipCodeInput.sendKeys(zip);
        return me();
    }

    public T enterPhone(String phone) {
        phoneNumberInput.sendKeys(phone);
        return me();
    }

    public EditRecipients clickSaveButton() {
        saveButton.click();
        return EditRecipients.initialize(driver, EditRecipients.class);
    }

    @Override
    public boolean isFormInformationCorrect(R billAddressRecipient) {

        try {
            log.info("Checking if address 1: {} is on the form", billAddressRecipient.getAdd1());
            checkIfTextEquals(getAddress1(), billAddressRecipient.getAdd1());

            log.info("Checking if address 2: {} is on the form", billAddressRecipient.getAdd2());
            checkIfTextEquals(getAddress2(), billAddressRecipient.getAdd2());

            log.info("Checking if city: {} is on the form", billAddressRecipient.getCity());
            checkIfTextEquals(getCity(), billAddressRecipient.getCity());

            log.info("Checking if state: {} is on the form", billAddressRecipient.getState());
            checkIfTextEquals(getState(), billAddressRecipient.getState());

            log.info("Checking if zip: {} is on the form", billAddressRecipient.getZipCode());
            checkIfTextEquals(getZipCode(), billAddressRecipient.getZipCode());

            String formattedRecipientPhoneNumber = getFormattedPhoneNumber(billAddressRecipient.getPhoneNumber());
            log.info("Checking if phone: {} is on the form", formattedRecipientPhoneNumber);
            checkIfTextEquals(getPhoneNumber(), formattedRecipientPhoneNumber);
        } catch (TextNotDisplayedException e) {
            log.warn("Bill address recipient is not correct", e);
            return false;
        }

        return isFinalFormInfoValid(billAddressRecipient);
    }

    public String getAddress1() {
        return address1Input.getValueAttribute();
    }

    public String getAddress2() {
        return address2Input.getValueAttribute();
    }

    public String getCity() {
        return cityInput.getValueAttribute();
    }

    public String getState() {
        return stateInput.getValueAttribute();
    }

    public String getZipCode() {
        return zipCodeInput.getValueAttribute();
    }

    public String getPhoneNumber() {
        return phoneNumberInput.getValueAttribute();
    }
}
