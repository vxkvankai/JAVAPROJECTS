package com.d3.tests.consumer.core.accounts;

import com.d3.database.AccountDatabaseHelper;
import com.d3.database.AttributeDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.OverdraftStatus;
import com.d3.datawrappers.account.enums.AccountProductAttributes;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3DatabaseException;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.dashboard.Dashboard;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.SQLException;

@Epic("Accounts")
@Feature("Overdraft")
@Slf4j
public class OverdraftTests extends AccountsTestBase {

    private static final String OVERDRAFT_ENROLL_CODE = "springboard";
    private static final String OVERDRAFT_ERROR_MSG = "Error getting overdraft options";

    /**
     * Common steps for Overdraft/Reg-e tests: Enables Overdraft for the system, as well as enabling overdraft protection for the given account type.
     * After enabling these systems, the browser will navigate to the account page and click on the given account
     *
     * @param user User to login as
     * @param account Account to navigate to and and enable overdraft protection for
     * @return The MyAccountsSection with the given account info expanded
     */
    private MyAccountsSection overdraftCommonSteps(D3User user, D3Account account) {
        AttributeDatabaseHelper.updateCompanyAttributeValueString(CompanyAttribute.ACCOUNTS_OVERDRAFT_PROTECTION, true);
        AttributeDatabaseHelper.updateCompanyAttributeValueString(CompanyAttribute.ACCOUNTS_OVERDRAFT_PROTECTION_PDF_VERIFICATION, true);
        AttributeDatabaseHelper.updateCompanyAttributeValueString(CompanyAttribute.ACCOUNTS_OVERDRAFT_PROTECTION_MOBILE, true);
        AttributeDatabaseHelper.updateCompanyAttributeValueString(CompanyAttribute.ACCOUNTS_OVERDRAFT_PROTECTION_CHECKS, true);
        AttributeDatabaseHelper.updateAccountProductAttribute(account.getProductType(), AccountProductAttributes.OVERDRAFT_PROTECTION, true);

        Dashboard dashboard = login(user);
        return dashboard.getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());
    }

    @TmsLink("347433")
    @Story("Overdraft Opt In")
    @Test(dataProvider = "Basic User")
    public void verifyAccountOptIn(D3User user) {
        D3Account accountToUse = user.getFirstAccount();
        MyAccountsSection myAccountsSection = overdraftCommonSteps(user, accountToUse);

        myAccountsSection.clickOverdraftButton()
            .enterEnrollCode(OVERDRAFT_ENROLL_CODE)
            .clickEnrollNextButton()
            .selectATMOptInOrOptOut(true)
            .selectETransactionsOptInOrOptOut(true)
            .clickDownloadDisclosureButton()
            .clickOverdraftSaveButton();

        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.REG_E_ENROLL_SUCCESS_TITLE.getValue()));
        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.REG_E_ENROLL_ATM_DEBIT_TRANSACTIONS.getValue()));
        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.REG_E_ENROLL_ELECTRONIC_TRANSACTIONS.getValue()));

        try {
            Assert.assertTrue(AccountDatabaseHelper.areOverdraftOptionsCorrect(accountToUse, OverdraftStatus.OPT_IN, OverdraftStatus.OPT_IN),
                "Overdraft db status not as expected");
        } catch (D3DatabaseException e) {
            log.error(OVERDRAFT_ERROR_MSG, e);
            Assert.fail(OVERDRAFT_ERROR_MSG);
        }

        // verify refreshing the page doesn't lose overdraft opt in
        myAccountsSection.reloadPage();
        myAccountsSection.clickAccountByAccountName(accountToUse.getName());
        Assert.assertTrue(myAccountsSection.isTextDisplayed("Overdraft Un-Enrollment"));

        //verify the correct radios are selected
        myAccountsSection.clickOverdraftButton();
        Assert.assertTrue(myAccountsSection.isATMOverdraftOptionSelected(true), "ATM Overdraft opt in button was not selected");
        Assert.assertTrue(myAccountsSection.isETransactionsOverdraftOptionSelected(true), "E-transaction overdraft opt in button was not selected");

        myAccountsSection.selectATMOptInOrOptOut(false)
            .selectETransactionsOptInOrOptOut(false)
            .clickDownloadDisclosureButton()
            .clickOverdraftSaveButton();

        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.REG_E_UNENROLL_SUCCESS_TITLE.getValue()));
        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.REG_E_UNENROLL_ATM_DEBIT_TRANSACTIONS.getValue()));
        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.REG_E_UNENROLL_ELECTRONIC_TRANSACTIONS.getValue()));
        try {
            Assert.assertTrue(AccountDatabaseHelper.areOverdraftOptionsCorrect(accountToUse, OverdraftStatus.REVOKE, OverdraftStatus.REVOKE),
                "Overdraft db status not as expected");
        } catch (D3DatabaseException e) {
            log.error(OVERDRAFT_ERROR_MSG, e);
            Assert.fail(OVERDRAFT_ERROR_MSG);
        }

        myAccountsSection.reloadPage();
        myAccountsSection.clickAccountByAccountName(accountToUse.getName());
        Assert.assertTrue(myAccountsSection.isTextDisplayed("Overdraft Enrollment"));

        //verify the correct radios are selected
        myAccountsSection.clickOverdraftButton()
            .enterEnrollCode(ENROLLMENT_CODE)
            .clickEnrollNextButton();

        Assert.assertTrue(myAccountsSection.isATMOverdraftOptionSelected(false), "ATMOverdraft opt-out button was not selected");
        Assert.assertTrue(myAccountsSection.isETransactionsOverdraftOptionSelected(false), "E-transaction opt-out button was not selected");
    }

    @TmsLink("340566")
    @Story("Overdraft Opt In")
    @Test(dataProvider = "Basic User")
    public void verifyDownloadDisclosureButtonDisabled(D3User user) {
        MyAccountsSection myAccountsSection = overdraftCommonSteps(user, user.getFirstAccount());
        myAccountsSection.clickOverdraftButton()
            .enterEnrollCode(OVERDRAFT_ENROLL_CODE)
            .clickEnrollNextButton();
        Assert.assertFalse(myAccountsSection.isOverDraftSaveButtonEnabled());
        // verify that you can't click the button
        Assert.assertThrows(TimeoutException.class, myAccountsSection::clickOverdraftSaveButton);
    }
}
