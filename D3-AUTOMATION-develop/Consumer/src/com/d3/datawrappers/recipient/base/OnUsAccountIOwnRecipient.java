package com.d3.datawrappers.recipient.base;

import static com.d3.helpers.RandomHelper.getRandomNumberString;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.AccountHelper;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditRecipients;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class OnUsAccountIOwnRecipient extends AccountIOwnRecipient {

    public OnUsAccountIOwnRecipient(String name, String nickname, String accountNumber, String financialInstitution, String routingNumber,
            AccountType accountType) {
        super(name, nickname, accountNumber, financialInstitution, routingNumber, accountType);
        type = RecipientType.BANK_ACCOUNT;
        who = RecipientWho.PERSON;
    }


    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public ProviderOption getProviderOption() {
        return ProviderOption.CORE_ON_US_TRANSFER;
    }

    @Override
    public By getEditButtonBy() {
        return By.xpath("//div[@id='heading-ach']//i[contains(@class, 'glyphicon-pencil')]");
    }


    @Override
    public Recipient getNewRandomVersion() {
        OnUsAccountIOwnRecipient newRecipient = OnUsAccountIOwnRecipient.createRandomOnUsAccountRecipient();
        newRecipient.setName(this.getName());
        newRecipient.setAccountNumber(this.getAccountNumber());
        newRecipient.setAccountType(this.getAccountType());
        newRecipient.setFinancialInstitution(this.getFinancialInstitution());
        newRecipient.setRoutingNumber(this.getRoutingNumber());
        return newRecipient;
    }

    @Override
    public void addApiRecipient(MoneyMovementApiHelper api) throws D3ApiException {
        api.addRecipient(new ApiRecipient(this));
    }

    @Override
    public boolean isFormValid(WebDriver driver) {
        try {
            // Can't edit on us recipients
            driver.findElement(getEditButtonBy()).click();
            return false;
        } catch (NoSuchElementException e) {
            return EditRecipients.initialize(driver, EditRecipients.class)
                    .expandOnUsDetails()
                    .areOnUsDetailsCorrect(this);
        }
    }

    public static OnUsAccountIOwnRecipient createRandomOnUsAccountRecipient() {
        Person person = Fairy.create().person();
        String routingNumber = AccountHelper.getRandomOnUsRoutingNumber();
        String accountNumber = getRandomNumberString(10, true);
        String financialInst = Fairy.create().company().getName();
        return new OnUsAccountIOwnRecipient(person.getFullName(), person.getCompany().getName(), accountNumber, financialInst, routingNumber,
                AccountType.getRandom());
    }

    @Override
    public String getAuditTransferString() {
        return TransferMethod.ON_US.toString();
    }

    @Override
    public boolean hasNoteField() {
        return true;
    }
}