package com.d3.datawrappers.recipient;

import static com.d3.helpers.RandomHelper.getRandomNumberString;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.api.mappings.recipients.SearchResults;
import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.recipient.base.CompanyRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientType;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.moneymovement.recipients.add.AddExistingBillPayCompanyForm;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditCompanyBillPayForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import com.google.common.collect.ImmutableList;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyBillPaySeedRecipient extends CompanyBillPayManualRecipient implements CompanyRecipient {

    private String merchantZip;

    //NOTE: Any merchant name that begins with a letter before N is E-Bill Capable when testing against the checkfree 4.2 simulator. Live connection will be different
    //TODO: Figure out why Company Seeded Recipients added through api are not Auto Pay Eligible
    private static final ImmutableList<String> EBILL_CAPABLE_RECIPIENTS = ImmutableList.of("1st Source Bank Mortgage", "American Family Insurance", "Discover Bank", "Metropolitan Utilities District");

    @Override
    public boolean isEditable() {
        return false;
    }

    public CompanyBillPaySeedRecipient(String name) {
        super(name);
        who = RecipientWho.COMPANY;
        type = RecipientType.BILL_AND_ADDRESS;
    }

    @Override
    public EditRecipientsForm getEditRecipientsForm(WebDriver driver) {
        return EditCompanyBillPayForm.initialize(driver, EditCompanyBillPayForm.class);
    }

    @Override
    public AddRecipientForm getAddRecipientForm(WebDriver driver) {
        return AddExistingBillPayCompanyForm.initialize(driver, AddExistingBillPayCompanyForm.class);
    }

    @Override
    public boolean wasSeeded() {
        return true;
    }

    @Override
    public Recipient getNewRandomVersion() {
        Recipient recipient = CompanyBillPayManualRecipient.createRandomRecipient(false);
        recipient.setName(getName());
        return recipient;
    }

    @Override
    public void addApiRecipient(MoneyMovementApiHelper api) throws D3ApiException {
        SearchResults results = api.getSearchResultsForBillPay(this.getName());
        api.addRecipient(new ApiRecipient(this, results));
    }

    public static CompanyBillPaySeedRecipient createRandomExistingRecipient(String existingRecipientName) {
        JFairyCompany company = JFairyCompany.createNewCompany();
        CompanyBillPaySeedRecipient recipient = new CompanyBillPaySeedRecipient(existingRecipientName);
        recipient.setNickname(company.getName());
        recipient.setBillerAccountNumber(getRandomNumberString(10, true));
        recipient.setMerchantZip(getRandomNumberString(5, false));
        return recipient;
    }

    /**
     * Method will shuffle a copy of the EBILL_CAPABLE_RECIPIENTS to get a random order
     * then stream the list (with a limit of 2) and create CompanyBillPaySeedRecipients for each name
     *
     * @return List of 2 CompanyBillPaySeedRecipients that are ebill capable
     */
    public static List<CompanyBillPaySeedRecipient> createEBillCapableRecipients() {
        List<CompanyBillPaySeedRecipient> ebillRecipients = new ArrayList<>();
        List<String> copiedList = new ArrayList<>(EBILL_CAPABLE_RECIPIENTS);
        Collections.shuffle(copiedList);
        copiedList.stream()
            .limit(2)
            .collect(Collectors.toList())
            .forEach(recipient -> {
                CompanyBillPaySeedRecipient newRecipient = createRandomExistingRecipient(recipient);
                newRecipient.setNickname(null);
                ebillRecipients.add(newRecipient);
            });
        return ebillRecipients;
    }

    @Override
    public ProviderOption getProviderOption() {
        return ProviderOption.REGULAR_PAYMENT;
    }

    public String getMerchantZip() {
        return merchantZip;
    }

    public void setMerchantZip(String merchantZip) {
        this.merchantZip = merchantZip;
    }

    @Override
    public boolean isFormValid(WebDriver driver) {
        return ((EditCompanyBillPayForm) this.getEditRecipientsForm(driver)).isSeededInfoCorrectOnForm(this);
    }

    @Override
    public boolean isBillPay() {
        return true;
    }


}
