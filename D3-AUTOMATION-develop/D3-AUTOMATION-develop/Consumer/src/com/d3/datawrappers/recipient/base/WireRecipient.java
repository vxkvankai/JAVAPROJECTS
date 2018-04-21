package com.d3.datawrappers.recipient.base;

import static com.d3.helpers.RandomHelper.getRandomNumberString;
import static com.d3.helpers.RandomHelper.getRandomRoutingNumber;

import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.recipient.CompanyWireRecipient;
import com.d3.datawrappers.recipient.PersonWireRecipient;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.user.D3Address;
import com.d3.pages.consumer.moneymovement.recipients.add.AddWireRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditWireRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class WireRecipient extends Recipient {

    private String routingNumber;
    private String accountNumber;
    private String financialInstitution;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;

    public WireRecipient(String name, String routingNumber, String accountNumber, String financialInstitution, D3Address address) {
        super(name);
        type = RecipientType.WIRE;
        this.routingNumber = routingNumber;
        this.accountNumber = accountNumber;
        this.financialInstitution = financialInstitution;
        this.address1 = address.getAddress1();
        this.address2 = address.getAddress2();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getPostalCode();
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getFinancialInstitution() {
        return financialInstitution;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public EditRecipientsForm getEditRecipientsForm(WebDriver driver) {
        return EditWireRecipientForm.initialize(driver, EditWireRecipientForm.class);
    }

    @Override
    public AddRecipientForm getAddRecipientForm(WebDriver driver) {
        return AddWireRecipientForm.initialize(driver, AddWireRecipientForm.class);
    }

    @Override
    public By getEditButtonBy() {
        return By.xpath("//div[@id='heading-fedwire']//i[contains(@class, 'glyphicon-pencil')]");
    }

    @Override
    public boolean wasSeeded() {
        return false;
    }

    public static PersonWireRecipient createRandomPersonRecipient() {
        Person person = Fairy.create().person();
        D3Address address = new D3Address(person.getAddress());

        String routingNumber = getRandomRoutingNumber();
        String accountNumber = getRandomNumberString(10, true);
        PersonWireRecipient recipient = new PersonWireRecipient(person.getFullName(), routingNumber, accountNumber, person.getCompany().getName(), address);
        recipient.setNickname(person.getCompany().getName());
        return recipient;
    }

    public static CompanyWireRecipient createRandomCompanyRecipient() {
        Person person = Fairy.create().person();
        D3Address address = new D3Address(person.getAddress());
        JFairyCompany company = JFairyCompany.createNewCompany();

        String routingNumber = getRandomRoutingNumber();
        String accountNumber = getRandomNumberString(10, true);
        CompanyWireRecipient recipient =
                new CompanyWireRecipient(company.getName(), routingNumber, accountNumber, person.getCompany().getName(), address);
        recipient.setNickname(company.getDomain());
        return recipient;
    }

    @Override
    public ProviderOption getProviderOption() {
        return ProviderOption.FEDWIRE_TRANSFER;
    }
    
    @Override
    public boolean isBillPay() {
        return false;
    }

    @Override
    protected String getAuditTransferString() {
        return TransferMethod.FEDWIRE.toString();
    }
}
