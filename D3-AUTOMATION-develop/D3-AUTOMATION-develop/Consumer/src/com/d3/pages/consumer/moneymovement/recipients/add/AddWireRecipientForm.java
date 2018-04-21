package com.d3.pages.consumer.moneymovement.recipients.add;

import com.d3.datawrappers.recipient.base.WireRecipient;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class AddWireRecipientForm extends AddRecipientForm<AddWireRecipientForm, WireRecipient> {

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

    public AddWireRecipientForm enterRoutingNumber(String routingNumber) {
        routingNumberInput.sendKeys(routingNumber);
        return this;
    }

    public AddWireRecipientForm enterAccountNumber(String accountNumber) {
        accountNumberInput.sendKeys(accountNumber);
        return this;
    }

    public AddWireRecipientForm enterFinancialInstitution(String fi) {
        financialInstitutionInput.sendKeys(fi);
        return this;
    }

    public AddWireRecipientForm enterAddress1(String add1) {
        address1Input.sendKeys(add1);
        return this;
    }

    public AddWireRecipientForm enterAddress2(String add2) {
        address2Input.sendKeys(add2);
        return this;
    }

    public AddWireRecipientForm enterCity(String city) {
        cityInput.sendKeys(city);
        return this;
    }

    public AddWireRecipientForm enterState(String state) {
        stateInput.sendKeys(state);
        return this;
    }

    public AddWireRecipientForm enterZipCode(String zip) {
        zipCodeInput.sendKeys(zip);
        return this;
    }

    public AddWireRecipientForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AddWireRecipientForm me() {
        return this;
    }

    @Override
    public AddRecipientForm enterRecipientInfo(WireRecipient recipient) {
        enterName(recipient.getName())
                .enterNickname(recipient.getNickname())
                .enterRoutingNumber(recipient.getRoutingNumber())
                .enterAccountNumber(recipient.getAccountNumber())
                .enterFinancialInstitution(recipient.getFinancialInstitution())
                .enterAddress1(recipient.getAddress1())
                .enterAddress2(recipient.getAddress2())
                .enterCity(recipient.getCity())
                .enterState(recipient.getState())
                .enterZipCode(recipient.getZipCode());
        return this;
    }
}
