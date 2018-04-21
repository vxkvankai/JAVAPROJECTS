package com.d3.pages.consumer.moneymovement.recipients.add;

import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientType;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddAccountIOwnRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class AddRecipient extends MoneyMovementBasePage {

    @FindBy(xpath = "//button[contains(text(), 'Setup an account I own')]")
    private Element accountIOwnButton;

    @FindBy(xpath = "//button[contains(text(), 'Setup a company to pay')]")
    private Element companyToPayButton;

    @FindBy(xpath = "//button[contains(text(), 'Setup a person to pay')]")
    private Element personToPayButton;

    @FindBy(xpath = "//button[contains(text(), 'I have a bill/address information')]")
    private Element billAddressInfoButton;

    @FindBy(xpath = "//button[contains(text(), 'I have wire information')]")
    private Element wireInfoButton;

    @FindBy(xpath = "//button[contains(text(), 'I have bank account information')]")
    private Element bankAccountInfoButton;

    @FindBy(xpath = "//select[@name='accountType']/following-sibling::span")
    private Element accountTypeSelect;

    public AddRecipient(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AddRecipient me() {
        return this;
    }

    public AddAccountIOwnRecipientForm clickAccountIOwnButton() {
        log.info("Clicking Account I own button");
        accountIOwnButton.click();
        return AddAccountIOwnRecipientForm.initialize(driver, AddAccountIOwnRecipientForm.class);
    }

    public AddAccountIOwnRecipientForm clickBankAccountIOwnButton() {
        log.info("I have bank account information");
        bankAccountInfoButton.click();
        return AddAccountIOwnRecipientForm.initialize(driver, AddAccountIOwnRecipientForm.class);
    }

    public AddRecipient clickCompanyToPayButton() {
        log.info("Clicking Company to pay button");
        companyToPayButton.click();
        return this;
    }

    public AddRecipient clickPersonToPayButton() {
        log.info("Clicking Person to Pay button");
        personToPayButton.click();
        return this;
    }

    public AddWireRecipientForm clickWireAddressButton() {
        log.info("Clicking I have Wire button");
        wireInfoButton.click();
        return AddWireRecipientForm.initialize(driver, AddWireRecipientForm.class);
    }

    public AddRecipientForm clickBillAddressInfoButton(Recipient recipient) {
        log.info("Clicking I have Bill/Address info button");
        billAddressInfoButton.click();
        if (recipient.getWho() == RecipientWho.PERSON) {
            return AddBillAddressRecipientForm.initialize(driver, AddBillAddressRecipientForm.class);
        }
        CompanyBillPaySearch searchPage = CompanyBillPaySearch.initialize(driver, CompanyBillPaySearch.class);
        // if seeded use the search tool
        if (recipient.wasSeeded()) {
            searchPage.enterSearchTerm(recipient.getName());
            searchPage.clickSearchButton();
            return AddExistingBillPayCompanyForm.initialize(driver, AddExistingBillPayCompanyForm.class);
        } else {
            // else the recipient will be a manual entry
            return searchPage.clickManualButton();
        }
    }

    public AddRecipientForm clickRecipientTypeButton(Recipient recipient) {
        switch (recipient.getType()) {
            case BILL_AND_ADDRESS:
                return clickBillAddressInfoButton(recipient);
            case ACCOUNT_I_OWN:
            case BANK_ACCOUNT:
                return clickBankAccountIOwnButton();
            case WIRE:
                return clickWireAddressButton();
            default:
                throw new UnsupportedOperationException("Not implemented yet");
        }
    }

    public AddRecipient clickRecipientWho(Recipient recipient) {
        if (recipient.getWho() == RecipientWho.PERSON) {
            return clickPersonToPayButton();
        } else if (recipient.getWho() == RecipientWho.COMPANY) {
            return clickCompanyToPayButton();
        } else {
            throw new UnsupportedOperationException("Not implemented yet (Or use a different method if you're testing Account I Own)");
        }


    }

    public AddRecipientForm getToFormPage(Recipient recipient) {
        AddRecipientForm form;
        if (recipient.getType() == RecipientType.ACCOUNT_I_OWN) {
            form = clickAccountIOwnButton();
        } else {
            form = clickRecipientWho(recipient).clickRecipientTypeButton(recipient);
        }
        log.info("Creating new recipient: {}", recipient);
        return form;
    }

    public RecipientsPage createNewRecipient(Recipient recipient) {
        return createNewRecipientNoSave(recipient).clickSaveButton();
    }

    public AddRecipientForm createNewRecipientNoSave(Recipient recipient) {
        return getToFormPage(recipient).enterRecipientInfo(recipient);
    }


}
