package com.d3.datawrappers.recipient.base;

import static com.d3.helpers.RandomHelper.getRandomNumberString;
import static com.d3.helpers.RandomHelper.getRandomRoutingNumber;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.AccountHelper;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddAccountIOwnRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditAccountIOwnRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountIOwnRecipient extends Recipient {

    private String accountNumber;
    private String financialInstitution;
    private String routingNumber;
    private AccountType accountType;


    // TODO add pending/nonpending types

    public AccountIOwnRecipient(String name, String nickname, String accountNumber, String financialInstitution, String routingNumber,
            AccountType accountType) {
        super(name);
        type = RecipientType.ACCOUNT_I_OWN;
        setNickname(nickname);
        this.accountNumber = accountNumber;
        this.financialInstitution = financialInstitution;
        this.routingNumber = routingNumber;
        this.accountType = accountType;
        this.deleteText = "";
        who=RecipientWho.ACCOUNT;
    }

    @Override
    public ProviderOption getProviderOption() {
        return ProviderOption.ACH_TRANSFER;
    }

    @Override
    public boolean isEditable() {
        // when pending
        return true;
    }

    @Override
    public EditRecipientsForm getEditRecipientsForm(WebDriver driver) {
        return EditAccountIOwnRecipientForm.initialize(driver, EditAccountIOwnRecipientForm.class);
    }

    @Override
    public AddRecipientForm getAddRecipientForm(WebDriver driver) {
        return AddAccountIOwnRecipientForm.initialize(driver, AddAccountIOwnRecipientForm.class);
    }

    @Override
    public By getEditButtonBy() {
        return By.xpath("//div[@id='heading-ach']//i[contains(@class, 'glyphicon-pencil')]");
    }

    @Override
    public boolean wasSeeded() {
        return false;
    }

    @Override
    public Recipient getNewRandomVersion() {
        return AccountIOwnRecipient.createRandomAccountRecipient();
    }

    @Override
    public void addApiRecipient(MoneyMovementApiHelper api) throws D3ApiException {
        api.addRecipient(new ApiRecipient(this));
    }

    @Override
    protected String getAuditTransferString() {
        return TransferMethod.ACH.toString();
    }

    @Override
    public boolean eligibleForExpedite() {
        return false;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public String getMaskedAccountNumber() {
        return AccountHelper.getHiddenAccountString(accountNumber);
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(String financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public static AccountIOwnRecipient createRandomAccountRecipient() {
        Person person = Fairy.create().person();
        String routingNumber = getRandomRoutingNumber();
        String accountNumber = getRandomNumberString(10, true);
        String financialInst = Fairy.create().company().getName();
        return new AccountIOwnRecipient(person.getFullName(), person.getCompany().getName(), accountNumber, financialInst, routingNumber,
                AccountType.getRandom());
    }

    @Override
    public boolean isBillPay() {
        return false;
    }

    @Override
    public boolean hasNoteField() {
        return false;
    }

}
