package com.d3.datawrappers.recipient;

import static com.d3.helpers.RandomHelper.getRandomNumberString;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.datawrappers.recipient.base.BillAddressRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.user.D3Address;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.moneymovement.recipients.add.AddBillPayManualForm;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditCompanyBillPayForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Address;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CompanyBillPayManualRecipient extends BillAddressRecipient {

    private String billerAccountNumber;

    public CompanyBillPayManualRecipient(String name, D3Address address, String phoneNumber, String nickname) {
        super(name, address, phoneNumber, nickname);
        this.who = RecipientWho.COMPANY;
        
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    public CompanyBillPayManualRecipient(String name) {
        super(name);
    }

    public String getBillerAccountNumber() {
        return billerAccountNumber;
    }

    public void setBillerAccountNumber(String billerAccountNumber) {
        this.billerAccountNumber = billerAccountNumber;
    }

    @Override
    public ProviderOption getProviderOption() {
        return ProviderOption.REGULAR_PAYMENT;
    }

    @Override
    public boolean isBillPay() {
        return true;
    }

    @Override
    public EditRecipientsForm getEditRecipientsForm(WebDriver driver) {
        return EditCompanyBillPayForm.initialize(driver, EditCompanyBillPayForm.class);
    }

    @Override
    public AddRecipientForm getAddRecipientForm(WebDriver driver) {
        return AddBillPayManualForm.initialize(driver, AddBillPayManualForm.class);
    }

    @Override
    public By getEditButtonBy() {
        return By.xpath("//div[@id='heading-bill_pay']//i[contains(@class, 'glyphicon-pencil')]");
    }

    @Override
    public boolean wasSeeded() {
        return false;
    }

    @Override
    public Recipient getNewRandomVersion() {
        Recipient recipient = CompanyBillPayManualRecipient.createRandomRecipient(false);
        recipient.setName(getName());
        return recipient;
    }

    @Override
    public void addApiRecipient(MoneyMovementApiHelper api) throws D3ApiException {
        api.addRecipient(new ApiRecipient(this));
    }

    @Override
    public boolean eligibleForExpedite() {
        return this.getName().startsWith("E");
    }


    /**
     * Creates a random recipient. If boolean value 'expedited' is true, it will set the name of the recipient to start with 'E'
     * so it's eligible for expedited payments when Expedited Payments is enabled in control
     *
     * @param expedited whether the recipient needs to be eligible for expedited payments
     * @return CompanyBillPayManualRecipient
     */
    public static CompanyBillPayManualRecipient createRandomRecipient(boolean expedited) {
        Person person = Fairy.create().person(PersonProperties.telephoneFormat("###-###-####"));
        Address address = person.getAddress();
        String name = expedited ? String.format("E%s", person.getFullName()) : person.getFullName();

        CompanyBillPayManualRecipient recipient =
                new CompanyBillPayManualRecipient(name, new D3Address(address), person.getTelephoneNumber(), person.getEmail());
        recipient.setNickname(person.getCompany().getName());
        recipient.setBillerAccountNumber(getRandomNumberString(10, true));
        return recipient;
    }

}
