package com.d3.tests.consumer.core.settings;

import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3Address;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.settings.profile.ChangeAddressForm;
import com.d3.pages.consumer.settings.profile.ChangeContactInfoForm;
import com.d3.pages.consumer.settings.profile.Profile;
import com.d3.tests.annotations.RunForUserTypes;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Settings")
@Feature("Business Profile")
@Slf4j
public class BusinessProfileToggleTests extends SettingsTestBase {

    private static final String BUSINESS_PROFILE_LINK = "Business Profile";

    @Story("User without Business Profile Access")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ENABLED)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_CONSUMER_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePageNotAvailable(D3User user) {
        Dashboard dashboard = login(user);
        Profile profile = dashboard.getHeader().clickSettingsButton();
        Assert.assertFalse(profile.isLinkPresent(BUSINESS_PROFILE_LINK));
    }

    @Flaky
    @Story("Physical Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PHYSICAL_ADDRESS_CHANGE)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePhysicalAddressChangeWhenCompanyAttributeEnabled(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        ChangeAddressForm changeBusinessAddress =
                dashboard.getHeader().clickSettingsButton().getTabs().clickBusinessProfileLink().getChangeAddressForm()
                        .enterPhysicalAddress(address)
                        .saveBusinessProfile()
                        .getChangeAddressForm();

        Assert.assertTrue(changeBusinessAddress.isPhysicalAddressCorrect(address),
                String.format("Address does not match %s %s %s %s %s", address.getAddress1(), address.getAddress2(), address.getCity(),
                        address.getState(), address.getPostalCode()));
    }

    @Story("Physical Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PHYSICAL_ADDRESS_CHANGE, enabled = false)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePhysicalAddressChangeWhenCompanyAttributeDisabled(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        ChangeAddressForm changeBusinessAddress =
                dashboard.getHeader().clickSettingsButton()
                        .getTabs()
                        .clickBusinessProfileLink()
                        .getChangeAddressForm();

        boolean ableToUpdatePhysicalAddress;
        try {
            changeBusinessAddress.enterPhysicalAddress(address);
            ableToUpdatePhysicalAddress = true;
        } catch (NoSuchElementException | InvalidElementStateException ignore) {
            log.info("Error entering address", ignore);
            ableToUpdatePhysicalAddress = false;
        }
        Assert.assertFalse(ableToUpdatePhysicalAddress);
    }

    @Story("Mailing Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_MAILING_ADDRESS_CHANGE)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfileMailingAddressChangeWhenEnabled(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        ChangeAddressForm changeBusinessAddress =
                dashboard.getHeader().clickSettingsButton().getTabs().clickBusinessProfileLink().getChangeAddressForm()
                        .uncheckSameAsPhysicalCheckbox()
                        .enterMailingAddress(address)
                        .saveBusinessProfile()
                        .getChangeAddressForm();

        Assert.assertTrue(changeBusinessAddress.isMailingAddressCorrect(address),
                String.format("Address does not match %s %s %s %s %s", address.getAddress1(), address.getAddress2(), address.getCity(),
                        address.getState(), address.getPostalCode()));
    }


    @Story("Mailing Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_MAILING_ADDRESS_CHANGE, enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PHYSICAL_ADDRESS_CHANGE)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfileMailingAddressChangeWhenDisabled(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        ChangeAddressForm changeBusinessAddress = dashboard.getHeader()
                .clickSettingsButton()
                .getTabs()
                .clickBusinessProfileLink()
                .getChangeAddressForm();

        Assert.assertThrows(NoSuchElementException.class, () -> changeBusinessAddress.enterMailingAddress(address));
    }

    @Story("Primary Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PRIMARY_EMAIL_ADDRESS_CHANGE)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePrimaryEmailAddressChangeWhenEnabled(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo =
                dashboard.getHeader().clickSettingsButton().getTabs().clickBusinessProfileLink().getChangeContactInfoForm()
                        .enterPrimaryEmailAddress(person.getEmail())
                        .saveBusinessProfile()
                        .getChangeContactInfoForm();
        Assert.assertEquals(changeBusinessContactInfo.getPrimaryEmailValue(), person.getEmail());
    }

    @Story("Primary Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PRIMARY_EMAIL_ADDRESS_CHANGE, enabled = false)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePrimaryEmailAddressChangeWhenDisabled(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo = dashboard.getHeader()
                .clickSettingsButton()
                .getTabs()
                .clickBusinessProfileLink()
                .getChangeContactInfoForm();

        Assert.assertThrows(InvalidElementStateException.class, () -> changeBusinessContactInfo.enterPrimaryEmailAddress(person.getEmail()));
    }

    @Story("Alternate Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_ALTERNATE_EMAIL_CHANGE)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfileAlternateEmailAddressChangeEnabled(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo =
                dashboard.getHeader().clickSettingsButton().getTabs().clickBusinessProfileLink().getChangeContactInfoForm()
                        .enterAlternateEmailAddress(person.getEmail())
                        .saveBusinessProfile()
                        .getChangeContactInfoForm();
        Assert.assertEquals(changeBusinessContactInfo.getAlternateEmail(), person.getEmail());
    }

    @Story("Alternate Email Address Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_ALTERNATE_EMAIL_CHANGE, enabled = false)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfileAlternateEmailAddressChangeDisabled(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo = dashboard.getHeader()
                .clickSettingsButton()
                .getTabs()
                .clickBusinessProfileLink()
                .getChangeContactInfoForm();
        Assert.assertThrows(InvalidElementStateException.class, () -> changeBusinessContactInfo.enterAlternateEmailAddress(person.getEmail()));
    }

    @Story("Phone Number Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PHONE_NUMBER_CHANGE)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePhoneNumberChangeEnabled(D3User user) {
        Person person = Fairy.create().person(PersonProperties.telephoneFormat("###-###-####"));
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo =
                dashboard.getHeader().clickSettingsButton().getTabs().clickBusinessProfileLink().getChangeContactInfoForm()
                        .enterWorkPhone(person.getTelephoneNumber())
                        .saveBusinessProfile()
                        .getChangeContactInfoForm();
        Assert.assertEquals(changeBusinessContactInfo.getWorkPhoneNoHyphens(), person.getTelephoneNumber().replace("-", ""));
    }

    @Story("Phone Number Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PHONE_NUMBER_CHANGE, enabled = false)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePhoneNumberChangeDisabled(D3User user) {
        Person person = Fairy.create().person(PersonProperties.telephoneFormat("###-###-####"));
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo = dashboard.getHeader()
                .clickSettingsButton()
                .getTabs()
                .clickBusinessProfileLink()
                .getChangeContactInfoForm();

        Assert.assertThrows(InvalidElementStateException.class,() -> changeBusinessContactInfo.enterWorkPhone(person.getTelephoneNumber()));
    }

    @Flaky
    @Story("Phone Number SMS Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PHONE_NUMBER_SMS_CHANGE)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePhoneNumberSmsChangeEnabled(D3User user) {
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo =
                dashboard.getHeader().clickSettingsButton().getTabs().clickBusinessProfileLink().getChangeContactInfoForm()
                        .enablePhoneSms()
                        .saveBusinessProfile()
                        .getChangeContactInfoForm();
        Assert.assertTrue(changeBusinessContactInfo.getSmsValue());
    }

    @Story("Phone Number SMS Change")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.BUSINESS_PROFILE_ALLOW_PHONE_NUMBER_SMS_CHANGE, enabled = false)
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessProfilePhoneNumberSmsChangeDisabled(D3User user) {
        Dashboard dashboard = login(user);
        ChangeContactInfoForm changeBusinessContactInfo = dashboard.getHeader()
                .clickSettingsButton()
                .getTabs()
                .clickBusinessProfileLink()
                .getChangeContactInfoForm();
        Assert.assertThrows(NoSuchElementException.class, changeBusinessContactInfo::enablePhoneSms);
    }
}