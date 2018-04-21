package com.d3.pages.consumer.moneymovement.recipients.edit;

import static com.d3.helpers.AccountHelper.getFormattedPhoneNumber;
import static com.d3.helpers.AccountHelper.getHiddenAccountString;

import com.d3.datawrappers.recipient.CompanyBillPayManualRecipient;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class EditCompanyBillPayForm extends PageObjectBase implements EditRecipientsForm<EditCompanyBillPayForm, CompanyBillPayManualRecipient>{

    @FindBy(id = "primaryAddress.line1")
    private Element addLine1Input;

    // JMoravec: this field doesn't have an ID currently
    @FindBy(name = "primaryAddress.line2")
    private Element addLine2Input;

    @FindBy(id = "primaryAddress.city")
    private Element cityInput;

    @FindBy(id = "primaryAddress.state")
    private Element stateInput;

    @FindBy(id = "primaryAddress.postalCode")
    private Element zipCodeInput;

    @FindBy(id = "phoneNumber")
    private Element phoneNumberInput;

    @FindBy(id = "accountNumber")
    private Element billerAccountNumInput;

    @FindBy(css = "button.btn-submit")
    private Element saveButton;

    @FindBy(xpath = "//a[@href='#collapse-bill_pay']")
    private Element billPayDetailsLink;

    @FindBy(css = "button.btn-cancel")
    private Element cancelButton;
    

    public EditCompanyBillPayForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected EditCompanyBillPayForm me() {
        return this;
    }

    public EditCompanyBillPayForm clickBillPayDetailsLinks() {
        billPayDetailsLink.click();
        return this;
    }

    public EditCompanyBillPayForm enterAddress1(String address1) {
        log.info("Entering add1: {}", address1);
        addLine1Input.sendKeys(address1);
        return this;
    }
    
    

    public String getAddress1() {
        return addLine1Input.getValueAttribute();
    }

    public EditCompanyBillPayForm enterAddress2(String address2) {
        log.info("Entering add2: {}", address2);
        addLine2Input.sendKeys(address2);
        return this;
    }

    public String getAddress2() {
        return addLine2Input.getValueAttribute();
    }

    public EditCompanyBillPayForm enterCity(String city) {
        log.info("Entering city: {}", city);
        cityInput.sendKeys(city);
        return this;
    }

    public String getCity() {
        return cityInput.getValueAttribute();
    }

    public EditCompanyBillPayForm enterState(String state) {
        log.info("Entering state: {}", state);
        stateInput.sendKeys(state);
        return this;
    }

    public String getState() {
        return stateInput.getValueAttribute();
    }

    public EditCompanyBillPayForm enterZipcode(String zipcode) {
        log.info("Entering zipcode: {}", zipcode);
        zipCodeInput.sendKeys(zipcode);
        return this;
    }

    public String getZipcode() {
        return zipCodeInput.getValueAttribute();
    }

    public EditCompanyBillPayForm enterPhoneNumber(String phoneNumber) {
        log.info("Entering phone number: {}", phoneNumber);
        phoneNumberInput.sendKeys(phoneNumber);
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumberInput.getValueAttribute();
    }

    public EditCompanyBillPayForm enterBillerAccountNumber(String accountNum) {
        log.info("Entering biller account number: {}", accountNum);
        if(billerAccountNumInput.isEnabled()) {
            billerAccountNumInput.sendKeys(accountNum);
        }
        return this;
    }

    public String getBillerAccountNumber() {
        return billerAccountNumInput.getValueAttribute();
    }

    @Override
    public boolean isFormInformationCorrect(CompanyBillPayManualRecipient recipient) {
        String checkMsg = "Checking if {}: {} is on the form";

        try {
            log.info(checkMsg, "Address 1", recipient.getAdd1());
            checkIfTextEquals(getAddress1(), recipient.getAdd1());

            log.info(checkMsg, "Address 2", recipient.getAdd2());
            checkIfTextEquals(getAddress2(), recipient.getAdd2());

            log.info(checkMsg, "City", recipient.getCity());
            checkIfTextEquals(getCity(), recipient.getCity());

            log.info(checkMsg, "State", recipient.getState());
            checkIfTextEquals(getState(), recipient.getState());

            log.info(checkMsg, "Zip Code", recipient.getZipCode());
            checkIfTextEquals(getZipcode(), recipient.getZipCode());

            String phoneFormatted = getFormattedPhoneNumber(recipient.getPhoneNumber());
            log.info(checkMsg, "Phone Number", phoneFormatted);
            checkIfTextEquals(getPhoneNumber(), phoneFormatted);

            String hiddenAccount = getHiddenAccountString(recipient.getBillerAccountNumber());
            if(billerAccountNumInput.isEnabled()) {
            log.info("Checking if Account Number: {} is on the form", hiddenAccount);
            checkIfTextEquals(getBillerAccountNumber(), hiddenAccount);
            }
        } catch (TextNotDisplayedException e) {
            log.warn("Bill Pay form was not validated correctly: ", e);
            return false;
        }

        return true;
    }

    @Override
    public EditRecipientsForm<EditCompanyBillPayForm, CompanyBillPayManualRecipient> fillOutForm(CompanyBillPayManualRecipient recipient) {
        enterAddress1(recipient.getAdd1())
                .enterAddress2(recipient.getAdd2())
                .enterCity(recipient.getCity())
                .enterState(recipient.getState())
                .enterZipcode(recipient.getZipCode())
                .enterPhoneNumber(recipient.getPhoneNumber())
                .enterBillerAccountNumber(recipient.getBillerAccountNumber());
        return this;
    }

    @Override
    public EditRecipients clickSaveButton() {
        saveButton.click();
        return EditRecipients.initialize(driver, EditRecipients.class);
    }

    public boolean isSeededInfoCorrectOnForm(CompanyBillPaySeedRecipient recipient) {
        clickBillPayDetailsLinks();
        String checkMsg = "Checking if {}: {} is on the form";

        String hiddenAccount = getHiddenAccountString(recipient.getBillerAccountNumber());
        log.info(checkMsg, "Account Number", hiddenAccount);
        try {
            checkIfTextDisplayed(hiddenAccount, checkMsg);
        } catch (TextNotDisplayedException e) {
            log.warn("Seeded info was not correct", e);
            return false;
        }

        return true;
    }

    @Override
    public EditRecipients clickCancelButton(boolean isEditable) {
       if(isEditable) {
        cancelButton.click();
       }
        return EditRecipients.initialize(driver, EditRecipients.class);
    }
}
