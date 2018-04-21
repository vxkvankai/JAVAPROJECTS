package com.d3.pages.consumer.moneymovement.recipients.edit;

import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditBillAddressRecipientForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class EditPersonBillAddrRecipientForm extends EditBillAddressRecipientForm<EditPersonBillAddrRecipientForm, PersonBillAddressRecipient> {

    @FindBy(name = "email")
    private Element emailInput;
    
    @FindBy(css = "button.btn-cancel")
    private Element cancelButton;

    public EditPersonBillAddrRecipientForm(WebDriver driver) {
        super(driver);
    }

    public String getEmail() {
        return emailInput.getValueAttribute();
    }

    public EditPersonBillAddrRecipientForm enterEmail(String email) {
        emailInput.sendKeys(email);
        return this;
    }

    @Override
    protected boolean isFinalFormInfoValid(PersonBillAddressRecipient recipient) {
        log.info("Checking if email: {} is on the form", recipient.getEmail());
        try {
            checkIfTextEquals(getEmail(), recipient.getEmail());
        } catch (TextNotDisplayedException e) {
            log.warn("Recipient final form info was not validated", e);
            return false;
        }

        return true;
    }

    @Override
    public EditPersonBillAddrRecipientForm fillOutForm(PersonBillAddressRecipient recipient) {
        return enterAdd1(recipient.getAdd1())
                .enterAdd2(recipient.getAdd2())
                .enterCity(recipient.getCity())
                .enterState(recipient.getState())
                .enterZip(recipient.getZipCode())
                .enterPhone(recipient.getPhoneNumber())
                .enterEmail(recipient.getEmail());
    }

    @Override
    protected EditPersonBillAddrRecipientForm me() {
        return this;
    }
    
    @Override
    public EditRecipients clickCancelButton(boolean isEditable) {
       if(isEditable) {
        cancelButton.click();
       }
        return EditRecipients.initialize(driver, EditRecipients.class);
    }
}
