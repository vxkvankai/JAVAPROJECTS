package com.d3.pages.consumer.settings.profile;

import com.d3.datawrappers.user.D3Address;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.businessprofile.BusinessProfile;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class ChangeAddressForm extends PageObjectBase {

    @FindBy(id = "physicalAddress-line1")
    private Element physicalAddressLine1;

    @FindBy(id = "physicalAddress-line2")
    private Element physicalAddressLine2;

    @FindBy(id = "physicalAddress-line3")
    private Element physicalAddressLine3;

    @FindBy(id = "physicalAddress-line4")
    private Element physicalAddressLine4;

    @FindBy(id = "physicalAddress-city")
    private Element physicalAddressCity;

    @FindBy(id = "physicalAddress-state")
    private Element physicalAddressStateInput;

    @FindBy(id = "physicalAddress-postal-code")
    private Element physicalAddressZipCode;

    @FindBy(id = "mailingAddress-line1")
    private Element mailingAddressLine1;

    @FindBy(id = "mailingAddress-line2")
    private Element mailingAddressLine2;

    @FindBy(id = "mailingAddress-line3")
    private Element mailingAddressLine3;

    @FindBy(id = "mailingAddress-line4")
    private Element mailingAddressLine4;

    @FindBy(id = "mailingAddress-city")
    private Element mailingAddressCity;

    @FindBy(id = "mailingAddress-state")
    private Element mailingAddressStateInput;

    @FindBy(id = "mailingAddress-postal-code")
    private Element mailingAddressZipCode;

    @FindBy(name = "sapa")
    private CheckBox sameAsPhysicalAddress;

    @FindBy(xpath = "//button[.='Cancel']")
    private List<Element> cancelButton;

    @FindBy(xpath = "//button[.='Save']")
    private List<Element> saveButtons;

    public ChangeAddressForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ChangeAddressForm me() {
        return this;
    }

    public ChangeAddressForm clickCancelButton() {
        log.info("Clicking cancel button");
        getDisplayedElement(cancelButton).click();
        return this;
    }

    public Profile clickSaveButton() {
        log.info("Clicking save button on Settings Profile page");
        getDisplayedElement(saveButtons).click();
        Profile profile =  Profile.initialize(driver, Profile.class);
        if (!profile.isChangeAddressButtonDisplayed()) {
            getDisplayedElement(saveButtons).click();
        }

        return profile;
    }

    public BusinessProfile saveBusinessProfile() {
        log.info("Clicking save button on Settings Business Profile page");
        getDisplayedElement(saveButtons).click();
        BusinessProfile profile =  BusinessProfile.initialize(driver, BusinessProfile.class);
        if (!profile.isChangeAddressButtonDisplayed()) {
            getDisplayedElement(saveButtons).click();
        }
        return profile;
    }

    public ChangeAddressForm enterPhysicalAddress1(String address) {
        log.info("Entering Physical Address Line 1: {}", address);
        physicalAddressLine1.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterPhysicalAddress2(String address) {
        log.info("Entering Physical Address Line 2: {}", address);
        physicalAddressLine2.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterPhysicalAddress3(String address) {
        log.info("Entering Physical Address Line 3: {}", address);
        physicalAddressLine3.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterPhysicalAddress4(String address) {
        log.info("Entering Physical Address Line 4: {}", address);
        physicalAddressLine4.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterPhysicalAddressCity(String city) {
        log.info("Entering Physical Address City: {}", city);
        physicalAddressCity.sendKeys(city);
        return this;
    }

    public ChangeAddressForm selectPhysicalAddressState(String state) {
        log.info("Entering Physical Address State: {}", state);
        physicalAddressStateInput.sendKeys(state);
        return this;
    }

    public ChangeAddressForm enterPhysicalAddressZipCode(String zipcode) {
        log.info("Entering Physical Address Zip Code: {}", zipcode);
        physicalAddressZipCode.sendKeys(zipcode);
        return this;
    }

    public ChangeAddressForm enterPhysicalAddress (D3Address address) {
        enterPhysicalAddress1(address.getAddress1());
        enterPhysicalAddress2(address.getAddress2());
        enterPhysicalAddressCity(address.getCity());
        selectPhysicalAddressState(address.getState());
        enterPhysicalAddressZipCode(address.getPostalCode());
        return this;
    }

    public ChangeAddressForm enterMailingAddress1(String address) {
        log.info("Entering Mailing Address Line 1: {}", address);
        mailingAddressLine1.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterMailingAddress2(String address) {
        log.info("Entering Mailing Address Line 2: {}", address);
        mailingAddressLine2.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterMailingAddress3(String address) {
        log.info("Entering Mailing Address Line 3: {}", address);
        mailingAddressLine3.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterMailingAddress4(String address) {
        log.info("Entering Mailing Address Line 4: {}", address);
        mailingAddressLine4.sendKeys(address);
        return this;
    }

    public ChangeAddressForm enterMailingAddressCity(String city) {
        log.info("Entering Mailing Address City: {}", city);
        mailingAddressCity.sendKeys(city);
        return this;
    }

    public ChangeAddressForm selectMailingAddressState(String state) {
        log.info("Entering Mailing Address State: {}", state);
        mailingAddressStateInput.sendKeys(state);
        return this;
    }

    public ChangeAddressForm enterMailingAddressZipCode(String zipcode) {
        log.info("Entering Mailing Address Zip Code: {}", zipcode);
        mailingAddressZipCode.sendKeys(zipcode);
        return this;
    }

    public ChangeAddressForm enterMailingAddress (D3Address address) {
        enterMailingAddress1(address.getAddress1());
        enterMailingAddress2(address.getAddress2());
        enterMailingAddressCity(address.getCity());
        selectMailingAddressState(address.getState());
        enterMailingAddressZipCode(address.getPostalCode());
        return this;
    }

    public ChangeAddressForm uncheckSameAsPhysicalCheckbox() {
        sameAsPhysicalAddress.uncheck();
        return this;
    }

    public ChangeAddressForm checkSameAsPhysicalCheckbox() {
        sameAsPhysicalAddress.check();
        return this;
    }

    public boolean isPhysicalAddressCorrect(D3Address address) {

        try {
            log.info("Checking if Physical Address Line 1: {} is correct", address.getAddress1());
            checkIfTextEquals(getAddress1(physicalAddressLine1), address.getAddress1());

            log.info("Checking if Physical Address Line 2: {} is correct", address.getAddress2());
            checkIfTextEquals(getAddress2(physicalAddressLine2), address.getAddress2());

            log.info("Checking if Physical Address City: {} is correct", address.getCity());
            checkIfTextEquals(getCity(physicalAddressCity), address.getCity());

            log.info("Checking if Physical Address State: {} is correct", address.getState());
            checkIfTextEquals(getState(physicalAddressStateInput), address.getState());

            log.info("Checking if Physical Address Zip Code: {} is correct", address.getPostalCode());
            checkIfTextEquals(getZipCode(physicalAddressZipCode), address.getPostalCode());
        } catch (TextNotDisplayedException e) {
            log.warn("Physical address validation failed", e);
            return false;
        }

        return true;
    }

    public boolean isMailingAddressCorrect(D3Address address) {

        try {
            log.info("Checking if Mailing Address Line 1: {} is correct", address.getAddress1());
            checkIfTextEquals(getAddress1(mailingAddressLine1), address.getAddress1());

            log.info("Checking if Mailing Address Line 2: {} is correct", address.getAddress2());
            checkIfTextEquals(getAddress2(mailingAddressLine2), address.getAddress2());

            log.info("Checking if Mailing Address City: {} is correct", address.getCity());
            checkIfTextEquals(getCity(mailingAddressCity), address.getCity());

            log.info("Checking if Mailing Address State: {} is correct", address.getState());
            checkIfTextEquals(getState(mailingAddressStateInput), address.getState());

            log.info("Checking if Mailing Address Zip Code: {} is correct", address.getPostalCode());
            checkIfTextEquals(getZipCode(mailingAddressZipCode), address.getPostalCode());
        } catch (TextNotDisplayedException e) {
            log.warn("Mailing address was not validated", e);
            return false;
        }

        return true;
    }

    public String getAddress1(Element element) {
        return element.getValueAttribute();
    }

    public String getAddress2(Element element) {
        return element.getValueAttribute();
    }

    public String getCity(Element element) {
        return element.getValueAttribute();
    }

    public String getState(Element element) { return element.getValueAttribute(); }

    public String getZipCode(Element element) {
        return element.getValueAttribute();
    }

    public String getPhysicalAddress() {
        return String.format("%s %s %s %s %s", getAddress1(physicalAddressLine1), getAddress2(physicalAddressLine2), getCity(physicalAddressCity),
                getState(physicalAddressStateInput), getZipCode(physicalAddressZipCode));
    }

    public String getMailingAddress() {
        return String.format("%s %s %s %s %s", getAddress1(mailingAddressLine1), getAddress2(mailingAddressLine2), getCity(mailingAddressCity),
                getState(mailingAddressStateInput), getZipCode(mailingAddressZipCode));
    }
}
