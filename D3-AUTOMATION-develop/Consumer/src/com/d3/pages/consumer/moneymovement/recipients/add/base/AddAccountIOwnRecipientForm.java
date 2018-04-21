package com.d3.pages.consumer.moneymovement.recipients.add.base;

import com.d3.datawrappers.recipient.base.AccountIOwnRecipient;
import com.d3.datawrappers.recipient.base.AccountType;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class AddAccountIOwnRecipientForm extends AddRecipientForm<AddAccountIOwnRecipientForm, AccountIOwnRecipient>{

    @FindBy(id = "accountNumber")
    private Element accountNumberInput;

    @FindBy(id = "accountType")
    private Select accountTypeSelect;

    @FindBy(id = "fiName")
    private Element fiNameInput;

    @FindBy(id = "routingNumber")
    private Element routingNumberInput;

    public AddAccountIOwnRecipientForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AddAccountIOwnRecipientForm me() {
        return this;
    }

    public AddAccountIOwnRecipientForm enterAccountNumber(String accountNumber) {
        logger.info("Entering account Number: {}", accountNumber);
        accountNumberInput.sendKeys(accountNumber);
        return this;
    }

    public AddAccountIOwnRecipientForm selectAccountType(AccountType type) {
        logger.info("Selecting Account type: {}", type);
        accountTypeSelect.selectByValue(type.toString());
        return this;
    }

    public AddAccountIOwnRecipientForm enterFinancialInstitution(String fiName) {
        logger.info("Entering Financial Institution: {}", fiName);
        fiNameInput.sendKeys(fiName);
        return this;
    }

    public AddAccountIOwnRecipientForm enterRoutingNumber(String routingNum) {
        logger.info("Entering Routing number: {}", routingNum);
        routingNumberInput.sendKeys(routingNum);
        return this;
    }

    @Override
    public AddRecipientForm enterRecipientInfo(AccountIOwnRecipient recipient) {
        return enterName(recipient.getName())
                .enterNickname(recipient.getNickname())
                .enterAccountNumber(recipient.getAccountNumber())
                .selectAccountType(recipient.getAccountType())
                .enterFinancialInstitution(recipient.getFinancialInstitution())
                .enterRoutingNumber(recipient.getRoutingNumber());
    }
}
