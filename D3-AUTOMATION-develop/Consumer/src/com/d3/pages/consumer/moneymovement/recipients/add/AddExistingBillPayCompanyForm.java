package com.d3.pages.consumer.moneymovement.recipients.add;

import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class AddExistingBillPayCompanyForm extends AddRecipientForm<AddExistingBillPayCompanyForm, CompanyBillPaySeedRecipient> {

    @FindBy(name = "accountNumber")
    private Element billerAccountNumberInput;

    @FindBy(id = "merchantZip")
    private Element merchantZipcode;

    public AddExistingBillPayCompanyForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AddExistingBillPayCompanyForm me() {
        return this;
    }

    public AddExistingBillPayCompanyForm enterBillerAccountNumber(String accountNumber) {
        logger.info("Entering biller account number: {}", accountNumber);
        billerAccountNumberInput.sendKeys(accountNumber);
        return this;
    }

    public AddExistingBillPayCompanyForm enterMerchantZipcode(String zipcode) {
        logger.info("Entering merchant zip code: {}", zipcode);
        merchantZipcode.sendKeys(zipcode);
        return this;
    }

    @Override
    public AddRecipientForm enterRecipientInfo(CompanyBillPaySeedRecipient recipient) {
        enterNickname(recipient.getNickname()).enterBillerAccountNumber(recipient.getBillerAccountNumber());
        char letter = recipient.getName().charAt(0);
        if ((letter % 2) != 0) {
            logger.info("Recipient name starts with an 'odd' letter, entering zipcode");
            enterMerchantZipcode(recipient.getMerchantZip());
        } else {
            logger.info("Recipient name starts with an 'even' letter, not entering zip");
        }
        return this;
    }
}
