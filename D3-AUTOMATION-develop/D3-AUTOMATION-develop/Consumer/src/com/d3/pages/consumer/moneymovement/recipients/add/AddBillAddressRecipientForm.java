package com.d3.pages.consumer.moneymovement.recipients.add;

import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class AddBillAddressRecipientForm extends AddRecipientForm<AddBillAddressRecipientForm, PersonBillAddressRecipient> {

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

    @FindBy(name = "email")
    private Element emailInput;

    public AddBillAddressRecipientForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AddBillAddressRecipientForm me() {
        return this;
    }

    public AddBillAddressRecipientForm enterAddress1(String address1) {
        log.info("Entering address 1: {}", address1);
        address1Input.sendKeys(address1);
        return this;
    }

    public AddBillAddressRecipientForm enterAddress2(String address2) {
        log.info("Entering address 2: {}", address2);
        address2Input.sendKeys(address2);
        return this;
    }

    public AddBillAddressRecipientForm enterCity(String city) {
        log.info("Entering City: {}", city);
        cityInput.sendKeys(city);
        return this;
    }

    public AddBillAddressRecipientForm enterState(String state) {
        log.info("Entering State: {}", state);
        stateInput.sendKeys(state);
        return this;
    }

    public AddBillAddressRecipientForm enterZipCode(String zip) {
        log.info("Entering Zip: {}", zip);
        zipCodeInput.sendKeys(zip);
        return this;
    }

    public AddBillAddressRecipientForm enterPhoneNumber(String phone) {
        log.info("Entering phone number: {}", phone);
        phoneNumberInput.sendKeys(phone);
        return this;
    }

    public AddBillAddressRecipientForm enterEmail(String email) {
        log.info("Entering email: {}", email);
        emailInput.sendKeys(email);
        return this;
    }

    @Override
    public AddRecipientForm enterRecipientInfo(PersonBillAddressRecipient recipient) {
        enterName(recipient.getName())
                .enterNickname(recipient.getNickname())
                .enterAddress1(recipient.getAdd1())
                .enterAddress2(recipient.getAdd2())
                .enterCity(recipient.getCity())
                .enterState(recipient.getState())
                .enterZipCode(recipient.getZipCode())
                .enterPhoneNumber(recipient.getPhoneNumberOriginal());
        if (recipient.getWho() == RecipientWho.PERSON) {
            enterEmail(recipient.getEmail());
        }
        return this;
    }

}
