package com.d3.datawrappers.recipient;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.datawrappers.recipient.base.BillAddressRecipient;
import com.d3.datawrappers.recipient.base.PersonRecipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.user.D3Address;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.moneymovement.recipients.add.AddBillAddressRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditPersonBillAddrRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Address;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;


public class PersonBillAddressRecipient extends BillAddressRecipient implements PersonRecipient {

    private String email;

    public PersonBillAddressRecipient(String name, D3Address address, String phoneNumber, String email) {
        super(name, address, phoneNumber, "");
        who = RecipientWho.PERSON;
        this.email = email;
    }

    /**
     * Creates a random recipient. If boolean value 'expedited' is true, it will set the name of the recipient to start with 'O'
     * so it's eligible for overnight payments when Expedited Payments is enabled in control
     *
     * @param expedited whether the recipient needs to be eligible for expedited payments
     * @return PersonBillAddressRecipient
     */
    public static PersonBillAddressRecipient createRandomRecipient(boolean expedited) {
        Person person = Fairy.create().person(PersonProperties.telephoneFormat("###-###-####"));
        Address address = person.getAddress();
        String name = expedited ? String.format("O%s", person.getFullName()) : person.getFullName();

        PersonBillAddressRecipient recipient =
            new PersonBillAddressRecipient(name, new D3Address(address), person.getTelephoneNumber(), person.getEmail());
        recipient.setNickname(person.getCompany().getName());
        return recipient;
    }

    public static List<PersonBillAddressRecipient> createListOfBadDataRecipients(String badString) {
        List<PersonBillAddressRecipient> list = new ArrayList<>();

        PersonBillAddressRecipient recipient = createRandomRecipient(false);
        recipient.setName(badString);
        list.add(recipient);

        recipient = createRandomRecipient(false);
        recipient.setNickname(badString);
        list.add(recipient);

        recipient = createRandomRecipient(false);
        recipient.setAdd1(badString);
        list.add(recipient);

        recipient = createRandomRecipient(false);
        recipient.setAdd2(badString);
        list.add(recipient);

        recipient = createRandomRecipient(false);
        recipient.setCity(badString);
        list.add(recipient);

        return list;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public EditRecipientsForm getEditRecipientsForm(WebDriver driver) {
        return EditPersonBillAddrRecipientForm.initialize(driver, EditPersonBillAddrRecipientForm.class);
    }

    @Override
    public AddRecipientForm getAddRecipientForm(WebDriver driver) {
        return AddBillAddressRecipientForm.initialize(driver, AddBillAddressRecipientForm.class);
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
    public PersonBillAddressRecipient getNewRandomVersion() {
        return PersonBillAddressRecipient.createRandomRecipient(false);
    }

    @Override
    public void addApiRecipient(MoneyMovementApiHelper api) throws D3ApiException {
        api.addRecipient(new ApiRecipient(this));
    }

    @Override
    public boolean eligibleForExpedite() {
        return this.getName().startsWith("O");
    }

    @Override
    public ProviderOption getProviderOption() {
        return ProviderOption.REGULAR_PAYMENT;
    }

    @Override
    public boolean isBillPay() {
        return true;
    }

}
