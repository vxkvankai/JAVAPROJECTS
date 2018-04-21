package com.d3.pages.consumer.moneymovement.recipients.edit;

import com.d3.datawrappers.recipient.base.AccountIOwnRecipient;
import com.d3.datawrappers.recipient.base.AccountType;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class EditAccountIOwnRecipientForm extends PageObjectBase implements EditRecipientsForm<EditAccountIOwnRecipientForm, AccountIOwnRecipient> {

    @FindBy(id = "accountNumber")
    private Element accountNumberInput;

    @FindBy(id = "accountType")
    private Select accountTypeSelect;

    @FindBy(id = "fiName")
    private Element fiNameInput;

    @FindBy(id = "routingNumber")
    private Element routingNumberInput;

    @FindBy(css = "button.btn-submit")
    private Element saveButton;
    
    @FindBy(css = "button.btn-cancel")
    private Element cancelButton;

    public EditAccountIOwnRecipientForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected EditAccountIOwnRecipientForm me() {
        return this;
    }

    public EditAccountIOwnRecipientForm enterAccountNumber(String accountNumber) {
        logger.info("Entering account number: {}", accountNumber);
        accountNumberInput.sendKeys(accountNumber);
        return this;
    }

    public String getAccountNumber() {
        return accountNumberInput.getValueAttribute();
    }

    public EditAccountIOwnRecipientForm selectAccountType(AccountType type) {
        logger.info("Selecting Account type: {}", type);
        accountTypeSelect.selectByValue(type.toString());
        return this;
    }

    public String getAccountType() {
        return accountTypeSelect.getValueAttribute();
    }

    public EditAccountIOwnRecipientForm enterFinancialInstitutionName(String name) {
        logger.info("Entering Financial inst name: {}", name);
        fiNameInput.sendKeys(name);
        return this;
    }

    public String getFinancialInstName() {
        return fiNameInput.getValueAttribute();
    }

    public EditAccountIOwnRecipientForm enterRoutingNumber(String routingNum) {
        logger.info("Entering routing number: {}", routingNum);
        routingNumberInput.sendKeys(routingNum);
        return this;
    }

    public String getRoutingNumber() {
        return routingNumberInput.getValueAttribute();
    }

    @Override
    public boolean isFormInformationCorrect(AccountIOwnRecipient recipient) {
        String checkMsg = "Checking if {}: {} is on the form";

        try {
            logger.info(checkMsg, "account number", recipient.getAccountNumber());
            checkIfTextEquals(getAccountNumber(), recipient.getMaskedAccountNumber());

            logger.info(checkMsg, "account type", recipient.getAccountType());
            checkIfTextEquals(getAccountType(), recipient.getAccountType().toString());

            logger.info(checkMsg, "financial inst name", recipient.getFinancialInstitution());
            checkIfTextEquals(getFinancialInstName(), recipient.getFinancialInstitution());

            logger.info(checkMsg, "routing number", recipient.getRoutingNumber());
            checkIfTextEquals(getRoutingNumber(), recipient.getRoutingNumber());
        } catch (TextNotDisplayedException e) {
            logger.warn("Edit Recipient Form is not correct", e);
            return false;
        }

        return true;
    }

    @Override
    public EditRecipientsForm<EditAccountIOwnRecipientForm, AccountIOwnRecipient> fillOutForm(AccountIOwnRecipient recipient) {
        enterAccountNumber(recipient.getAccountNumber())
                .selectAccountType(recipient.getAccountType())
                .enterFinancialInstitutionName(recipient.getFinancialInstitution())
                .enterRoutingNumber(recipient.getRoutingNumber());
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
