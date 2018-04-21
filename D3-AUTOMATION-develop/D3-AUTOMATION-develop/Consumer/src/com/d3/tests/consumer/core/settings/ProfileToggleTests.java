package com.d3.tests.consumer.core.settings;

import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3Address;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.settings.profile.ChangeAddressForm;
import com.d3.pages.consumer.settings.profile.ChangeContactInfoForm;
import com.d3.pages.consumer.settings.profile.ChangeNameForm;
import com.d3.pages.consumer.settings.profile.Profile;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Settings")
@Feature("Profile")
public class ProfileToggleTests extends SettingsTestBase {

    @Flaky
    @Story("Change Name")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_NAME_CHANGE)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfileChangeNameWhenEnabled(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeNameForm changeNameForm = dashboard
            .getHeader()
            .clickSettingsButton()
            .getChangeNameForm()
            .changeNameFields(person)
            .clickSaveButton()
            .getChangeNameForm();

        Assert.assertTrue(changeNameForm.isNameCorrect(person));
    }

    @Story("Change Name")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_NAME_CHANGE, enabled = false)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfileChangeNameWhenDisabled(D3User user) {
        boolean ableToChangeNameFields;
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        Profile profile = dashboard.getHeader()
            .clickSettingsButton();
        try {
            profile.getChangeNameForm();
            ableToChangeNameFields = true;
        } catch (NoSuchElementException | InvalidElementStateException ignore) {
            ableToChangeNameFields = false;
        }
        Assert.assertFalse(ableToChangeNameFields);
    }

    @Story("Physical Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PHYSICAL_ADDRESS_CHANGE)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePhysicalAddressChangeWhenEnabled(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeAddressForm changeAddress = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeAddressForm()
            .enterPhysicalAddress(address)
            .clickSaveButton()
            .getChangeAddressForm();

        Assert.assertTrue(changeAddress.isPhysicalAddressCorrect(address),
            String.format("Address does not match %s %s %s %s %s", address.getAddress1(), address.getAddress2(), address.getCity(),
                address.getState(), address.getPostalCode()));
    }

    @Story("Physical Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PHYSICAL_ADDRESS_CHANGE, enabled = false)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePhysicalAddressChangeWhenDisabled(D3User user) {
        boolean ableToUpdatePhysicalAddress;
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeAddressForm changeAddress = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeAddressForm();
        try {
            changeAddress.enterPhysicalAddress(address);
            ableToUpdatePhysicalAddress = true;
        } catch (NoSuchElementException | InvalidElementStateException ignore) {
            ableToUpdatePhysicalAddress = false;
        }
        Assert.assertFalse(ableToUpdatePhysicalAddress);
    }

    @Story("Mailing Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_MAILING_ADDRESS_CHANGE)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfileMailingAddressChangeWhenEnabled(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeAddressForm changeAddress = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeAddressForm()
            .uncheckSameAsPhysicalCheckbox()
            .enterMailingAddress(address)
            .clickSaveButton()
            .getChangeAddressForm();

        Assert.assertTrue(changeAddress.isMailingAddressCorrect(address),
            String.format("Address does not match %s %s %s %s %s", address.getAddress1(), address.getAddress2(), address.getCity(),
                address.getState(), address.getPostalCode()));
    }

    @Story("Mailing Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_MAILING_ADDRESS_CHANGE, enabled = false)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfileMailingAddressChangeWhenDisabled(D3User user) {
        boolean ableToUpdateMailingAddress;
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeAddressForm changeAddress = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeAddressForm();
        try {
            changeAddress.enterMailingAddress(address);
            ableToUpdateMailingAddress = true;
        } catch (NoSuchElementException | InvalidElementStateException ignore) {
            ableToUpdateMailingAddress = false;
        }
        Assert.assertFalse(ableToUpdateMailingAddress);
    }

    @Flaky
    @Story("Primary Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PRIMARY_EMAIL_ADDRESS_CHANGE)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePrimaryEmailAddressChangeWhenEnabled(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeContactInfoForm changeContactInfo = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm();

        changeContactInfo.enterPrimaryEmailAddress(person.getEmail())
            .clickSaveButton()
            .getChangeContactInfoForm();

        Assert.assertEquals(changeContactInfo.getPrimaryEmailValue(), person.getEmail());
    }

    @Story("Primary Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PRIMARY_EMAIL_ADDRESS_CHANGE, enabled = false)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePrimaryEmailAddressChangeWhenDisabled(D3User user) {
        boolean ableToUpdatePrimaryEmail;
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeContactInfoForm changeContactInfo = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm();

        try {
            changeContactInfo.enterPrimaryEmailAddress(person.getEmail());
            ableToUpdatePrimaryEmail = changeContactInfo.getPrimaryEmailValue().equalsIgnoreCase(person.getEmail());
        } catch (NoSuchElementException | InvalidElementStateException ignore) {
            ableToUpdatePrimaryEmail = false;
        }
        Assert.assertFalse(ableToUpdatePrimaryEmail, "User is able to update their primary email when they are not supposed to");
    }

    @Story("Alternate Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_ALTERNATE_EMAIL_ADDRESS_CHANGE)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfileAlternateEmailAddressChangeWhenEnabled(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeContactInfoForm changeContactInfo = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm()
            .enterAlternateEmailAddress(person.getEmail())
            .clickSaveButton()
            .getChangeContactInfoForm();

        Assert.assertEquals(changeContactInfo.getAlternateEmail(), person.getEmail());
    }

    @Story("Alternate Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_ALTERNATE_EMAIL_ADDRESS_CHANGE, enabled = false)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfileAlternateEmailAddressChangeWhenDisabled(D3User user) {
        boolean ableToUpdateAlternateEmail;
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeContactInfoForm changeContactInfo = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm();
        try {
            changeContactInfo.enterAlternateEmailAddress(person.getEmail());
            ableToUpdateAlternateEmail = changeContactInfo.getAlternateEmailValue().equalsIgnoreCase(person.getEmail());
        } catch (NoSuchElementException | InvalidElementStateException e) {
            ableToUpdateAlternateEmail = false;
        }
        Assert.assertFalse(ableToUpdateAlternateEmail);
    }

    @Flaky
    @Story("Phone Number Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PHONE_NUMBER_CHANGE)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePhoneNumberChangeWhenEnabled(D3User user) {
        Person person = Fairy.create().person(PersonProperties.telephoneFormat("###-###-####"));
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        Profile profilePage = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm()
            .enterHomePhoneNumber(person.getTelephoneNumber())
            .enterWorkPhone(person.getTelephoneNumber())
            .enterMobilePhone(person.getTelephoneNumber())
            .clickSaveButton();

        Assert.assertFalse(profilePage.isTextDisplayed("Server error"), "Error saving the form");

        ChangeContactInfoForm changeContactInfoForm = profilePage.getChangeContactInfoForm();

        Assert.assertEquals(changeContactInfoForm.getHomePhoneNoHyphens(), person.getTelephoneNumber().replace("-", ""));
        Assert.assertEquals(changeContactInfoForm.getWorkPhoneNoHyphens(), person.getTelephoneNumber().replace("-", ""));
        Assert.assertEquals(changeContactInfoForm.getMobilePhoneNoHyphens(), person.getTelephoneNumber().replace("-", ""));
    }

    @Story("Phone Number Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PHONE_NUMBER_CHANGE, enabled = false)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePhoneNumberChangeWhenDisabled(D3User user) {
        boolean ableToUpdatePhoneNumbers;
        Person person = Fairy.create().person(PersonProperties.telephoneFormat("###-###-####"));
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeContactInfoForm changeContactInfo = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm();
        try {
            changeContactInfo.enterHomePhoneNumber(person.getTelephoneNumber());
            ableToUpdatePhoneNumbers = changeContactInfo.getHomePhoneNumber().equalsIgnoreCase(person.getTelephoneNumber());
        } catch (NoSuchElementException | InvalidElementStateException ignore) {
            ableToUpdatePhoneNumbers = false;
        }
        Assert.assertFalse(ableToUpdatePhoneNumbers);
    }

    @Flaky
    @Story("Phone Number SMS Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PHONE_NUMBER_SMS_CHANGE)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePhoneNumberSmsChangeWhenEnabled(D3User user) {
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        Profile profilePage = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm()
            .enablePhoneSms()
            .clickSaveButton();

        Assert.assertFalse(profilePage.isTextDisplayed("Server error"), "Error saving the form");

        ChangeContactInfoForm contactInfoForm = profilePage.getChangeContactInfoForm();

        Assert.assertTrue(contactInfoForm.getSmsValue());
    }

    @Flaky
    @Story("Phone Number SMS Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PHONE_NUMBER_SMS_CHANGE, enabled = false)
    @Test(dataProvider = "Get All User Types")
    public void verifyProfilePhoneNumberSmsChangeWhenDisabled(D3User user) {
        boolean ableToUpdatePhoneNumberSms;
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeContactInfoForm changeContactInfo = dashboard.getHeader()
            .clickSettingsButton()
            .getChangeContactInfoForm();
        try {
            changeContactInfo.enablePhoneSms();
            ableToUpdatePhoneNumberSms = true;
        } catch (NoSuchElementException ignore) {
            ableToUpdatePhoneNumberSms = false;
        }
        Assert.assertFalse(ableToUpdatePhoneNumberSms);
    }
}
