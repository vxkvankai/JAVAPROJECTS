package com.d3.tests.consumer.core.accounts;


import static com.d3.helpers.AccountHelper.getHiddenAccountString;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.L10nCommon;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.pages.consumer.accounts.OtherAccountsSection;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Accounts")
@Feature("Online Accounts")
@Slf4j
public class AccountsAggregationTests extends AccountsTestBase {

    private static final String ACCOUNT_TYPE = "Credit Card";
    private static final String INSTITUTION = "Saturna Capital Corporation";
    private static final String ACCOUNT_NAME = "My Credit Card";
    private static final String ERROR_MSG = "Error message not shown";
    private static final String ACCOUNT_NOT_CREATED = "The account was not created";

    @Flaky // small spinner is stupid
    @TmsLink("522325")
    @Story("Add Online Account")
    @Test(dataProvider = "Basic User")
    public void validateOnlineAccountAddIsSuccessful(D3User user) {
        OtherAccountsSection otherAccountsSection = login(user)
            .getHeader().clickAccountsButton()
            .getOtherAccountsSection()
            .clickAddOnlineAccount()
            .fillInstitutionField(INSTITUTION)
            .clickNextButton()
            .addCredentialsToOnlineAccount(user)
            .checkOption(ACCOUNT_TYPE)
            .clickNextButtonSave(INSTITUTION);
        Assert.assertTrue(otherAccountsSection.isOnlineAccountDisplayed(ACCOUNT_NAME), ACCOUNT_NOT_CREATED);
    }

    @TmsLink("522328")
    @Story("Online Account - Change Login Credentials")
    @Test(dataProvider = "Basic User")
    public void validateOnlineAccountChangeLoginCredentialsIsSuccessful(D3User user) {
        String password = getRandomString(7);
        log.info("The new password is {}", password);

        OtherAccountsSection otherAccountsSection = login(user).getHeader()
            .clickAccountsButton()
            .getOtherAccountsSection()
            .clickAddOnlineAccount()
            .fillInstitutionField(INSTITUTION)
            .clickNextButton()
            .addCredentialsToOnlineAccount(user)
            .checkOption(ACCOUNT_TYPE)
            .clickNextButtonSave(INSTITUTION);

        Assert.assertTrue(otherAccountsSection.isOnlineAccountDisplayed(ACCOUNT_TYPE), ACCOUNT_NOT_CREATED);
        otherAccountsSection.clickAccountByAccountName(ACCOUNT_NAME)
            .clickChangeCredentialsButton()
            .enterUserName(user.getLogin())
            .clickNextButton()
            .enterPassword(password)
            .enterNewPassword(password + "22")
            .clickSaveButton();

        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.AGGREGATION_ERROR_PASSWORD.getValue()), ERROR_MSG);
        otherAccountsSection.enterPassword(password)
            .enterNewPassword(password)
            .clickSaveButton();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.PASSWORD_SUCCESS.getValue()),
            "Successful message not shown");
    }

    @Flaky
    @TmsLink("522326")
    @Story("Online Account - Details Display Correctly")
    @Test(dataProvider = "Basic User")
    public void validateOnlineAccountDetailAndViewMoreDetailsDisplayCorrectly(D3User user) {
        OtherAccountsSection otherAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getOtherAccountsSection()
            .clickAddOnlineAccount()
            .fillInstitutionField(INSTITUTION)
            .clickNextButton()
            .addCredentialsToOnlineAccount(user)
            .checkOption(ACCOUNT_TYPE);
        String accountNumber = otherAccountsSection.getOnlineAccountNumber(ACCOUNT_TYPE);

        otherAccountsSection.clickNextButton();
        Assert.assertTrue(otherAccountsSection.isOnlineAccountDisplayed(ACCOUNT_TYPE), ACCOUNT_NOT_CREATED);

        otherAccountsSection.clickAccountByAccountName(ACCOUNT_NAME);

        Assert.assertTrue(otherAccountsSection.getAccountNumber().endsWith(getHiddenAccountString(accountNumber)),
            "The account number is not correct");
        Assert.assertTrue(otherAccountsSection.isStatusAccountCorrect(D3Account.AccountStatus.OPEN.name()), "The account status is not correct");
        Assert.assertTrue(otherAccountsSection.isInstitutionAccountCorrect(INSTITUTION), "The institution account is not correct ");
        Assert.assertTrue(otherAccountsSection.removeAccountButtonIsDisplayed(), "The remove account button is not displayed");
        Assert.assertTrue(otherAccountsSection.editAccountButtonIsDisplayed(), "The edit account button is not displayed");

        otherAccountsSection.clickViewMoreButton();
        Assert.assertTrue(otherAccountsSection.isOnlineAccountformationDisplayed(), "The information is not displayed");
        Assert.assertTrue(otherAccountsSection.areOnlineAccountLabelsDisplayed(), "The labels are not displayed");
        Assert.assertTrue(otherAccountsSection.updatedAccountButtonIsDisplayed(), "The updated account button is no displayed");
        Assert.assertTrue(otherAccountsSection.isTextDisplayed("Exclude"), "The exclude account button is no displayed");
        Assert.assertTrue(otherAccountsSection.changeLoginButtonIsDisplayed(), "Change Login button is no displayed");


    }


    @TmsLink("522330")
    @Story("Online Account - Field Character Validation")
    @Test(dataProvider = "Basic User")
    public void validateOnlineAccountFieldsOnlyAcceptValidCharacters(D3User user) {
        String randomInstituteName = RandomHelper.getRandomString(10);
        String notAllowedCharacters = "`<>^|\"";
        String badUrl = "http://www.a%sa.com";

        OtherAccountsSection otherAccountsSection = login(user).getHeader().clickAccountsButton()
            .getOtherAccountsSection()
            .clickAddOnlineAccount();
        otherAccountsSection.fillInstitutionField(randomInstituteName)
            .clickNextButton();

        // verify error message shows
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.AGGREGATION_SELECT_INSTITUTION.getValue()),
            ERROR_MSG);
        otherAccountsSection.clickAddInstitutionButton()
            .enterInstitutionName("") // Clear out field for the test
            .clickRequestExternalInstitutionButton();

        Assert.assertTrue(otherAccountsSection.isTextDisplayed("Enter a Name"), ERROR_MSG);
        Assert.assertTrue(otherAccountsSection.isTextDisplayed("Enter a Url"), ERROR_MSG);

        // validate name field
        for (char badChar : notAllowedCharacters.toCharArray()) {
            otherAccountsSection.enterInstitutionName(String.valueOf(badChar))
                .clickRequestExternalInstitutionButton();
            Assert.assertTrue(otherAccountsSection.isTextDisplayed(L10nCommon.Localization.CHARACTERS_NOT_ALLOWED.getValue()),
                String.format("Error message for bad char ('%s')in name not shown", badChar));
        }

        otherAccountsSection.enterInstitutionName(randomInstituteName);

        for (char badChar : notAllowedCharacters.toCharArray()) {
            String completeBadUrl = String.format(badUrl, String.valueOf(badChar));
            otherAccountsSection.enterInstitutionWebAddress(completeBadUrl)
                .clickRequestExternalInstitutionButton();
            Assert.assertTrue(otherAccountsSection.isTextDisplayed(L10nCommon.Localization.ENTER_VALID_URL.getValue()),
                String.format("Error message for bad url:'%s' not shown", completeBadUrl));
        }
    }

    @TmsLink("522329")
    @Story("Online Account - Add Institution")
    @Test(dataProvider = "Basic User")
    public void validateOnlineAccountAddInstitutionIsSuccessful(D3User user) {
        String newInstitution = RandomHelper.getRandomString(10);
        String newInstAddress = "https://www.bellco.org/";

        OtherAccountsSection otherAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getOtherAccountsSection()
            .clickAddOnlineAccount()
            .fillInstitutionField(newInstitution)
            .clickNextButton();

        // verify that the institution wasn't listed
        Assert.assertTrue(otherAccountsSection.isTextDisplayed("Please select a supported institution."), ERROR_MSG);

        otherAccountsSection.clickAddInstitutionButton()
            .enterInstitutionWebAddress(newInstAddress)
            .clickRequestExternalInstitutionButton();

        // verify the message is displayed
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.REQUEST_SUBMITTED.getValue()),
            "Confirmation message not shown");
    }

    @TmsLink("522327")
    @Story("Account Aggregations")
    @Test(dataProvider = "Basic User")
    public void validateOnlineAccountUpdateIsSuccessful(D3User user) {

        OtherAccountsSection otherAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getOtherAccountsSection()
            .clickAddOnlineAccount()
            .fillInstitutionField(INSTITUTION)
            .clickNextButton()
            .addCredentialsToOnlineAccount(user)
            .checkOption(ACCOUNT_TYPE)
            .clickSaveButton();

        // get the first value of the last updated field
        String timeLogBeforeUpdate = otherAccountsSection.clickAccountByAccountName(ACCOUNT_NAME)
            .getLastUpdateValue();

        // get the second value of the last updated field
        String timeLogAfterUpdate = otherAccountsSection.clickRefreshAccountButton()
            .clickAccountByAccountName(ACCOUNT_NAME)
            .getLastUpdateValue();

        // update until the value changes
        while (timeLogBeforeUpdate.equals(timeLogAfterUpdate)) {
            timeLogAfterUpdate = otherAccountsSection.clickRefreshAccountButton()
                .clickAccountByAccountName(ACCOUNT_NAME)
                .getLastUpdateValue();
        }

        Assert.assertNotEquals(timeLogBeforeUpdate, timeLogAfterUpdate,
            "The time before update and time after update were the same");
    }
}
