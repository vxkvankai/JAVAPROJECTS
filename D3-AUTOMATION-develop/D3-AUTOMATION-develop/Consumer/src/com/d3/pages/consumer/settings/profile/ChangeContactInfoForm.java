package com.d3.pages.consumer.settings.profile;

import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.businessprofile.BusinessProfile;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import io.codearte.jfairy.producer.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class ChangeContactInfoForm extends PageObjectBase {

    private static final String REMOVE_HYPHEN_REGEX = "[()\\s-]+";

    @FindBy(xpath = "//button[@data-type='EMAIL']")
    private Element addEmailButton;

    @FindBy(xpath = "//button[@data-type='PHONE']")
    private Element addPhoneButton;

    @FindBy(xpath = "//input[@id=(//label[text()='Primary Email']/@for)]")
    private Element primaryEmailInput;

    @FindBy(xpath = "//input[@id=(//label[text()='Alternate Email']/@for)]")
    private Element alternateEmailInput;

    @FindBy(xpath = "//input[@id=(//label[text()='Additional Email']/@for)]")
    private Element additionalEmailInput;

    @FindBy(xpath = "//input[@id=(//label[text()='Home Phone']/@for)]")
    private Element homePhoneInput;

    @FindBy(xpath = "//input[@id=(//label[text()='Mobile Phone']/@for)]")
    private Element mobilePhoneInput;

    @FindBy(xpath = "//input[@id=(//label[text()='Work Phone']/@for)]")
    private Element workPhoneInput;

    @FindBy(xpath = "//button[.='Cancel']")
    private List<Element> cancelButton;

    @FindBy(xpath = "//button[.='Save']")
    private List<Element> saveButtons;

    @FindBy(name = "sms")
    private CheckBox smsCheckbox;

    public ChangeContactInfoForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ChangeContactInfoForm me() {
        return this;
    }

    public ChangeContactInfoForm clickCancelButton() {
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
        return BusinessProfile.initialize(driver, BusinessProfile.class);
    }

    public ChangeContactInfoForm enterHomePhoneNumber(String phoneNumber) {
        log.info("Entering Home Phone: {}", phoneNumber);
        homePhoneInput.sendKeys(phoneNumber);
        return this;
    }

    public String getHomePhoneNumber() {
        return homePhoneInput.getValueAttribute();
    }

    public ChangeContactInfoForm enterWorkPhone(String phoneNumber) {
        log.info("Entering Work Phone: {}", phoneNumber);
        workPhoneInput.sendKeys(phoneNumber);
        return this;
    }

    public ChangeContactInfoForm enterMobilePhone(String phoneNumber) {
        log.info("Entering Mobile Phone: {}", phoneNumber);
        mobilePhoneInput.sendKeys(phoneNumber);
        return this;
    }

    public ChangeContactInfoForm enterPrimaryEmailAddress(String email) {
        log.info("Entering Primary Email Address: {}", email);
        primaryEmailInput.sendKeys(email);
        return this;
    }

    public ChangeContactInfoForm enterAlternateEmailAddress(String email) {
        log.info("Entering Alternate Email Address: {}", email);
        alternateEmailInput.sendKeys(email);
        return this;
    }

    public String getAlternateEmailValue() {
        return alternateEmailInput.getValueAttribute();
    }

    public ChangeContactInfoForm enablePhoneSms() {
        log.info("Enabling Phone Sms");
        smsCheckbox.check();
        return this;
    }

    public ChangeContactInfoForm disablePhoneSms() {
        log.info("Disabling Phone Sms");
        smsCheckbox.uncheck();
        return this;
    }

    public ChangeContactInfoForm clickAddEmailButton() {
        log.info("Clicking Add Email button");
        addEmailButton.click();
        return this;
    }

    public ChangeContactInfoForm clickAddPhoneButton() {
        log.info("Clicking Add Phone button");
        addPhoneButton.click();
        return this;
    }

    public boolean isContactInfoCorrect(Person person) {
        String enteredPhoneNumber = person.getTelephoneNumber().replaceAll(REMOVE_HYPHEN_REGEX, "");

        try {
            log.info("Checking if Primary Email Address value is correct");
            checkIfTextEquals(getPrimaryEmailValue(), person.getEmail());

            log.info("Checking if Alternate Email Address value is correct");
            checkIfTextEquals(getAlternateEmail(), person.getCompanyEmail());

            log.info("Checking if Home Phone value is correct");
            checkIfTextEquals(getHomePhoneNoHyphens(), enteredPhoneNumber);

            log.info("Checking if Work Phone value is correct");
            checkIfTextEquals(getWorkPhoneNoHyphens(), enteredPhoneNumber);

            log.info("Checking if Mobile Phone value is correct");
            checkIfTextEquals(getMobilePhoneNoHyphens(), enteredPhoneNumber);
        } catch (TextNotDisplayedException e) {
            log.warn("Contact info was not validated", e);
            return false;
        }

        return true;
    }

    public String getPrimaryEmailValue() {
        return primaryEmailInput.getValueAttribute();
    }

    public String getAlternateEmail() {
        return alternateEmailInput.getValueAttribute();
    }

    public String getHomePhoneNoHyphens() {
        return homePhoneInput.getValueAttribute().replaceAll(REMOVE_HYPHEN_REGEX, "");
    }

    public String getWorkPhoneNoHyphens() {
        return workPhoneInput.getValueAttribute().replaceAll(REMOVE_HYPHEN_REGEX, "");
    }

    public String getMobilePhoneNoHyphens() {
        return mobilePhoneInput.getValueAttribute().replaceAll(REMOVE_HYPHEN_REGEX, "");
    }

    public boolean getSmsValue() {
        return smsCheckbox.isChecked();
    }

}

