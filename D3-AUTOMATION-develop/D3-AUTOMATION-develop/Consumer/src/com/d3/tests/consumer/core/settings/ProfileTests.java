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
import io.qameta.allure.TmsLink;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;


@Epic("Settings")
@Feature("Profile")
public class ProfileTests extends SettingsTestBase {

    @TmsLink("288104")
    @Story("Update Contact Info")
    @Test(dataProvider = "Basic User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PHONE_NUMBER_CHANGE)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_PRIMARY_EMAIL_ADDRESS_CHANGE)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_ALTERNATE_EMAIL_ADDRESS_CHANGE)
    public void verifyUpdateContactInfo(D3User user) {
        Person person = Fairy.create().person(PersonProperties.telephoneFormat("###-###-####"));
        String telephoneNumber = person.getTelephoneNumber();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeContactInfoForm changeContactInfo = dashboard.getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickProfileLink()
            .getChangeContactInfoForm();

        changeContactInfo.enterHomePhoneNumber(telephoneNumber)
            .enterWorkPhone(telephoneNumber)
            .enterMobilePhone(telephoneNumber)
            .enterPrimaryEmailAddress(person.getEmail())
            .enterAlternateEmailAddress(person.getCompanyEmail())
            .clickSaveButton()
            .getChangeContactInfoForm();

        Assert.assertTrue(changeContactInfo.isContactInfoCorrect(person), "The contact information for the user was not what was expected");
    }

    @TmsLink("288107")
    @Story("Update Physical Address")
    @Test(dataProvider = "Basic User")
    public void verifyUpdatePhysicalAddress(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);

        ChangeAddressForm changeAddress = dashboard
            .getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickProfileLink()
            .getChangeAddressForm();

        Profile profilePage = changeAddress
            .enterPhysicalAddress(address)
            .clickSaveButton();

        try {
            // save button doesn't always work correctly
            changeAddress = profilePage.getChangeAddressForm();
        } catch (NoSuchElementException e) {
            changeAddress = changeAddress.clickSaveButton().getChangeAddressForm();
        }

        Assert.assertTrue(changeAddress.isPhysicalAddressCorrect(address),
            String.format("Address does not match %s %s %s %s %s", address.getAddress1(), address.getAddress2(), address.getCity(),
                address.getState(), address.getPostalCode()));
    }

    @Flaky
    @TmsLink("288108")
    @Story("Update Mailing Address")
    @Test(dataProvider = "Basic User")
    public void verifyUpdateMailingAddressNoCheckbox(D3User user) {
        D3Address address = D3Address.getRandomAddress();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeAddressForm changeAddress = dashboard.getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickProfileLink()
            .getChangeAddressForm();

        Profile profilePage = changeAddress.uncheckSameAsPhysicalCheckbox()
            .enterMailingAddress(address)
            .clickSaveButton();

        try {
            // save button doesn't always work correctly
            changeAddress = profilePage.getChangeAddressForm();
        } catch (NoSuchElementException e) {
            changeAddress = changeAddress.clickSaveButton().getChangeAddressForm();
        }

        Assert.assertTrue(changeAddress.isMailingAddressCorrect(address),
            String.format("Address does not match %s %s %s %s %s", address.getAddress1(), address.getAddress2(), address.getCity(),
                address.getState(), address.getPostalCode()));
    }

    @TmsLink("288109")
    @Story("Update Mailing Address")
    @Test(dataProvider = "Basic User")
    public void verifyUpdateMailingAddressCheckbox(D3User user) {
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeAddressForm changeAddress = dashboard
            .getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickProfileLink()
            .getChangeAddressForm();

        changeAddress.checkSameAsPhysicalCheckbox()
            .clickSaveButton()
            .getChangeAddressForm()
            .uncheckSameAsPhysicalCheckbox();

        Assert.assertEquals(changeAddress.getPhysicalAddress(), changeAddress.getMailingAddress(), "Mailing Address did not match Physical Address");
    }

    @TmsLink("288104")
    @Story("Change Name")
    @Test(dataProvider = "Basic User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.SETTINGS_PROFILE_ALLOW_NAME_CHANGE)
    public void verifyChangeName(D3User user) {
        Person person = Fairy.create().person();
        Dashboard dashboard = login(user);
        Assert.assertNotNull(dashboard);
        ChangeNameForm changeNameForm = dashboard.getHeader().clickSettingsButton().getTabs()
            .clickProfileLink()
            .getChangeNameForm()
            .changeNameFields(person)
            .clickSaveButton()
            .getChangeNameForm();

        Assert.assertTrue(changeNameForm.isNameCorrect(person));
    }
}
