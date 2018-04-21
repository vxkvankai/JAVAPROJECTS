package com.d3.pages.consumer.moneymovement.recipients.add;

import com.d3.datawrappers.recipient.CompanyBillPayManualRecipient;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class AddBillPayManualForm extends AddRecipientForm<AddBillPayManualForm, CompanyBillPayManualRecipient>{

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

    @FindBy(id = "accountNumber")
    private Element billerAccountNumberInput;

    public AddBillPayManualForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AddBillPayManualForm me() {
        return this;
    }

    public AddBillPayManualForm enterAddress1(String address1) {
        logger.info("Entering address 1: {}", address1);
        address1Input.sendKeys(address1);
        return this;
    }

    public AddBillPayManualForm enterAddress2(String address2) {
        logger.info("Entering address 2: {}", address2);
        address2Input.sendKeys(address2);
        return this;
    }

    public AddBillPayManualForm enterCity(String city) {
        logger.info("Entering City: {}", city);
        cityInput.sendKeys(city);
        return this;
    }

    public AddBillPayManualForm enterState(String state) {
        logger.info("Entering State: {}", state);
        stateInput.sendKeys(state);
        return this;
    }

    public AddBillPayManualForm enterZipCode(String zip) {
        logger.info("Entering Zip: {}", zip);
        zipCodeInput.sendKeys(zip);
        return this;
    }

    public AddBillPayManualForm enterPhoneNumber(String phone) {
        logger.info("Entering phone number: {}", phone);
        phoneNumberInput.sendKeys(phone);
        return this;
    }

    public AddBillPayManualForm enterBillerAccountInfo(String number) {
        logger.info("Entering biller account number; {]", number);
        billerAccountNumberInput.sendKeys(number);
        return this;
    }

    @Override
    public AddRecipientForm enterRecipientInfo(CompanyBillPayManualRecipient recipient) {
        enterName(recipient.getName())
                .enterNickname(recipient.getNickname())
                .enterAddress1(recipient.getAdd1())
                .enterAddress2(recipient.getAdd2())
                .enterCity(recipient.getCity())
                .enterState(recipient.getState())
                .enterZipCode(recipient.getZipCode())
                .enterPhoneNumber(recipient.getPhoneNumber())
                .enterBillerAccountInfo(recipient.getBillerAccountNumber());
        return this;
    }
}
