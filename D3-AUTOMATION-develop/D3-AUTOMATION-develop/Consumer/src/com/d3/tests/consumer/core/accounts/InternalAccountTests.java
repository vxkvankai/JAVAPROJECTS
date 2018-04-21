package com.d3.tests.consumer.core.accounts;

import static com.d3.helpers.AccountHelper.getHiddenAccountString;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.L10nCommon;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.dashboard.Dashboard;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Accounts")
@Feature("Internal Accounts")
@Slf4j
public class InternalAccountTests extends AccountsTestBase {

    @TmsLink("522278")
    @Story("Internal Account - Edit Nickname")
    @Test(dataProvider = "Basic User with existing accounts")
    public void verifyEditingInternalAccountNickname(D3User user, D3Account account) {
        String accountNickNameValue = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        String accountName = account.getName();

        // Goes to the Account Tab
        MyAccountsSection accounts = login(user).getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(accountName)
            .clickEditAccountButton()
            .enterNickName(accountNickNameValue)
            .clickSaveAccountButton();

        // Here we validate the changes on the nickname
        Assert.assertEquals(accounts.getValueActiveAccount(), accountNickNameValue);
    }

    @TmsLink("522280")
    @Story("Internal Account - Account Name field validation")
    @Test(dataProvider = "Basic User")
    public void verifyAccountNameIsRequiredEditAccountWithSpecialCharacter(D3User user) {
        String accountName = user.getFirstAccount().getName();
        String initialNickName = "Account NickName";
        String nickNameAllowedCharacter = "Account 12 NickName 34 with allowed speciall char -.´+[]{}";
        String nickNameNotAllowedCharacter = "Account NickName with21 n0t 4ll0w3d  sp521eciall 2char ~ ' < > ^ | ";
        //verify name should not be empty
        MyAccountsSection myAccountsPage = login(user).getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(accountName)
            .clickEditAccountButton()
            .enterNickName(initialNickName)
            .clickSaveAccountButton()
            .clickEditAccountButton()
            .enterNickName("")
            .clickSaveAccountButton();
        Assert.assertTrue(myAccountsPage.isTextPresent(AccountsL10N.Localization.NICKNAME_REQUIRED.getValue()));

        //verify allowed special characters
        myAccountsPage.enterNickName(nickNameAllowedCharacter)
            .clickSaveAccountButton();

        try {
            myAccountsPage.clickEditAccountButton();
        } catch (WebDriverException e) {
            // Note (Jmoravec): it doesn't seem to register clicking the first click save name, thus this block
            log.error("Error clicking edit account button, trying to click save again: ", e);
            myAccountsPage.clickSaveAccountButton()
                .clickEditAccountButton();
        }

        //verify not allowed special characters
        myAccountsPage.enterNickName(nickNameNotAllowedCharacter)
            .clickSaveAccountButton();
        Assert.assertTrue(myAccountsPage.isTextPresent(L10nCommon.Localization.CHARACTERS_NOT_ALLOWED.getValue()));
    }

    @Flaky // Not sure why sometimes failing, fails finding balance or waiting for an element to be clickable
    @TmsLink("522277")
    @Story("Internal Account Details Displayed Correctly")
    @Test(dataProvider = "Basic User with existing accounts")
    public void validateAssetAndLiabilityAccountsAndDetailDisplayCorrectly(D3User user, D3Account account) {
        String accountName = account.getName();
        Dashboard dashboard = login(user);
        MyAccountsSection myAccountsSection = dashboard.getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(accountName);

        Assert.assertTrue(myAccountsSection.isBalanceCorrect(account.getAvailableBalanceStr()), "The balance is not correct");
        Assert.assertEquals(getHiddenAccountString(account.getNumber()), myAccountsSection.getAccountNumber(), "The account number is not correct");
        Assert.assertTrue(myAccountsSection.isStatusAccountCorrect(account.getStatus()), "The account status is no correct");
        Assert.assertTrue(myAccountsSection.isProductAccountCorrect(account.getProductType().getName()), "The product account is not correct ");
    }

}
