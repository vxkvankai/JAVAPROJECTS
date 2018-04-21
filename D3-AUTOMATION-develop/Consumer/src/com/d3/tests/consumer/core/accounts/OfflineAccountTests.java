package com.d3.tests.consumer.core.accounts;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.l10n.common.CommonL10N;
import com.d3.l10n.transactions.TransactionsL10N;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.accounts.OtherAccountsSection;
import com.d3.pages.consumer.transactions.TransactionsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Accounts")
@Feature("Offline Accounts")
public class OfflineAccountTests extends AccountsTestBase {

    private static final String ACCOUNT_NAME_NO_APPEAR_ERROR_MSG = "Account name: %s did not appear";
    private static final String BALANCE_NO_APPEAR_ERROR_MSG = "Balance: %s did not appear";

    private OtherAccountsSection createOfflineAccountCommon(D3User user, D3Account account) {
        return login(user).getHeader().clickAccountsButton().getOtherAccountsSection().clickAddAnAccountDropdown().clickOfflineAccountSelection()
            .selectOfflineAccountTypeByText(account.getProductType().getName())
            .enterOfflineAccountName(account.getName())
            .enterOfflineAccountBalance(account.getAvailableBalanceStr())
            .clickAddOfflineAccountSaveButton();
    }

    @TmsLink("523082")
    @Story("Offline Account - Add Asset Account")
    @Test(dataProvider = "Basic User")
    public void addOfflineAccount(D3User user) {
        D3Account offlineAccount = D3Account.generateOfflineAccount(user, ProductType.ASSET);
        OtherAccountsSection otherAccountsSection = login(user).getHeader().clickAccountsButton().getOtherAccountsSection();

        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.OTHER_ACCOUNTS_SECTION.getValue()));

        otherAccountsSection.clickAddAnAccountDropdown()
            .clickOfflineAccountSelection();

        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.ADD_OFFLINE.getValue()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.FORM_TYPE.getValue()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.FORM_NAME.getValue()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.FORM_BALANCE.getValue()));

        otherAccountsSection.selectOfflineAccountTypeByText(offlineAccount.getProductType().getName())
            .enterOfflineAccountName(offlineAccount.getName())
            .enterOfflineAccountBalance(offlineAccount.getAvailableBalanceStr())
            .clickAddOfflineAccountSaveButton();

        Assert.assertTrue(otherAccountsSection.isTextDisplayed(offlineAccount.getName()),
            String.format(ACCOUNT_NAME_NO_APPEAR_ERROR_MSG, offlineAccount.getName()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(String.format("$%s", offlineAccount.getAvailableBalanceStr())),
            String.format(BALANCE_NO_APPEAR_ERROR_MSG, offlineAccount.getAvailableBalanceStr()));
    }

    @TmsLink("522543")
    @Story("Offline Account - Add Liability Account")
    @Test(dataProvider = "Basic User")
    public void verifyOfflineLiabilityAccountIsCreated(D3User user) {
        D3Account offlineAccount = D3Account.generateOfflineAccount(user, ProductType.LIABILITY);
        OtherAccountsSection otherAccountsSection = createOfflineAccountCommon(user, offlineAccount);

        Assert.assertTrue(otherAccountsSection.isTextDisplayed(offlineAccount.getName()),
            String.format(ACCOUNT_NAME_NO_APPEAR_ERROR_MSG, offlineAccount.getName()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(String.format("$%s", offlineAccount.getAvailableBalanceStr())),
            String.format(BALANCE_NO_APPEAR_ERROR_MSG, offlineAccount.getAvailableBalanceStr()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(offlineAccount.getProductType().getName()),
            String.format("Account type: %s did not appear", offlineAccount.getProductType().getName()));
    }

    @TmsLink("522542")
    @Story("Add Offline Account - Required Fields")
    @Test(dataProvider = "Basic User")
    public void validateRequiredFieldsOnCreateOfflineAccount(D3User user) {
        D3Account offlineAccount = D3Account.generateOfflineAccount(user, ProductType.LIABILITY);

        OtherAccountsSection otherAccountsSection = login(user).getHeader()
            .clickAccountsButton()
            .getOtherAccountsSection()
            .clickAddAnAccountDropdown();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.OTHER_ACCOUNTS_SECTION.getValue()));

        //verify empty fields 
        otherAccountsSection.clickOfflineAccountSelection()
            .clickAddOfflineAccountSaveButton();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.PRODUCT_REQUIRED.getValue()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.NICKNAME_REQUIRED.getValue()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.BALANCE_REQUIRED.getValue()));

        //verify not allowed special characters
        otherAccountsSection.selectOfflineAccountTypeByText(offlineAccount.getProductType().getName())
            .enterOfflineAccountName("Account NickName with21 n0t 4ll0w3d  sp521eciall 2char ~ ' < > ^ | ")
            .clickAddOfflineAccountSaveButton();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue()));

        //verify allowed special characters
        otherAccountsSection.enterOfflineAccountName(offlineAccount.getName())
            .enterOfflineAccountBalance(offlineAccount.getAvailableBalanceStr())
            .clickAddOfflineAccountSaveButton();

        try {
            otherAccountsSection.clickAccountByAccountName(offlineAccount.getName());
        } catch (WebDriverException e) {
            // Note (Jmoravec): Doesn't always seem to click the save button correctly
            logger.warn("Error clicking new account details, ", e);
            otherAccountsSection.clickAddOfflineAccountSaveButton()
                .clickAccountByAccountName(offlineAccount.getName());
        }
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(offlineAccount.getName()),
            String.format(ACCOUNT_NAME_NO_APPEAR_ERROR_MSG, offlineAccount.getName()));
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(String.format("$%s", offlineAccount.getAvailableBalanceStr())),
            String.format(BALANCE_NO_APPEAR_ERROR_MSG, offlineAccount.getAvailableBalanceStr()));
    }

    @TmsLink("347710")
    @Story("Edit Offline Account - Required Fields")
    @Test(dataProvider = "Basic User with Offline Liability Account")
    public void verifyOfflineAccountRequiredFieldsOnEdit(D3User user, D3Account offlineAccount) {
        OtherAccountsSection otherAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getOtherAccountsSection();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(offlineAccount.getName()),
            String.format(ACCOUNT_NAME_NO_APPEAR_ERROR_MSG, offlineAccount.getName()));

        Assert.assertTrue(otherAccountsSection.isTextDisplayed(String.format("$%s", offlineAccount.getAvailableBalanceStr())),
            String.format(BALANCE_NO_APPEAR_ERROR_MSG, offlineAccount.getAvailableBalanceStr()));

        //Edit
        //verify name should not be empty
        otherAccountsSection.clickAccountByAccountName(offlineAccount.getName())
            .clickEditAccountButton()
            .enterOfflineAccountName("")
            .clickAddOfflineAccountSaveButton();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.NICKNAME_REQUIRED.getValue()));

        //verify allowed special characters
        otherAccountsSection.enterOfflineAccountName("Account 12 NickName 34 with allowed special char -.´+[]{}")
            .clickAddOfflineAccountSaveButton();
        try {
            otherAccountsSection.clickEditAccountButton();
        } catch (WebDriverException e) {
            logger.warn("Issue clicking edit button: ", e);
            // Note (Jmoravec): The save button doesn't always work correctly here
            otherAccountsSection.clickAddOfflineAccountSaveButton()
                .clickEditAccountButton();
        }

        //verify not allowed special characters
        otherAccountsSection.enterOfflineAccountName("Account NickName with21 n0t 4ll0w3d  sp521eciall 2char ~ ' < > ^ | ")
            .clickAddOfflineAccountSaveButton();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue()));
    }

    @TmsLink("347711")
    @Story("Offline Account - Delete account with transactions")
    @Test(dataProvider = "Basic User With Offline Account Transaction")
    public void verifyOfflineAccountRemoveIsSuccessfulAndAuditEventRecordTransactionsAreDeleted(D3User user, D3Account account, D3Transaction offlineTxn) {
        OtherAccountsSection otherAccountsSection = login(user).getHeader()
            .clickAccountsButton()
            .getOtherAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickRemoveAccountButton();
        Assert.assertTrue(otherAccountsSection.isTextDisplayed(AccountsL10N.Localization.DELETE_OFFLINE_ACCOUNT.getValue()));
        otherAccountsSection.clickConfirmRemoveAccountButton();

        Assert.assertFalse(otherAccountsSection.isTextDisplayed(account.getName()));
        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, Audits.CREATE_OFFLINE_ACCOUNT));

        //verify offline account transaction is removed with account removal
        TransactionsPage transactionsPage = otherAccountsSection.getHeader()
            .clickTransactionsButton()
            .searchForDescription(offlineTxn.getName());
        Assert.assertTrue(transactionsPage.isTextDisplayed(TransactionsL10N.Localization.NO_TRANSACTIONS_AVAILABLE.getValue()));
    }
}
