package com.d3.tests.consumer.core.accounts;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.tests.annotations.DataRequiresCompanyAttributes;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Accounts")
@Feature("E-Statements")
public class EStatementTests extends AccountsTestBase {

    private static final String PERMISSION = "d3permission.allow.estatements";

    @BeforeClass
    private void setProductAttributePermissions() {
        DatabaseUtils.updateAccountProductPermission(ProductType.DEPOSIT_CHECKING, PERMISSION, "true");
        DatabaseUtils.updateAccountProductPermission(ProductType.DEPOSIT_SAVINGS, PERMISSION, "true");
        DatabaseUtils.updateAccountProductPermission(ProductType.CREDIT_CARD, PERMISSION, "false");
    }

    @TmsLink("522551")
    @Story("E-Statements Disabled at Company Attribute Level")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.GO_PAPERLESS_ENABLED, enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_ENABLED, enabled = false)
    @Test(dataProvider = "Basic User with existing Asset accounts")
    public void verifyEstatementEnrollmentNotAvailable(D3User user, D3Account account) {
        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());

        Assert.assertFalse(myAccountsSection.isEstatementEnrollButtonDisplayed(), "E-Statement button is displayed when disabled in Control");
    }

    @TmsLink("522549")
    @Story("E-Statement Enrollment")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.GO_PAPERLESS_ENABLED, enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_ENABLED)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_ENABLED)
    @DataRequiresCompanyAttributes
    @Test(dataProvider = "Basic User with existing Asset accounts")
    public void verifyEstatementEnrollment(D3User user, D3Account account) {
        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickEstatementEnrollButton()
            .enterEstatementCode(ENROLLMENT_CODE)
            .clickEstatementNextButton()
            .clickEstatementSaveButton();
        Assert.assertTrue(myAccountsSection.isTextDisplayed(AccountsL10N.Localization.ESTATEMENT_ENROLL_SUCCESS.getValue()));
    }

    @TmsLink("509873")
    @Story("Go Paperless enrollment")
    @DataRequiresCompanyAttributes
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_ENABLED)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.GO_PAPERLESS_ENABLED, resetAfterTest = true)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_GO_PAPPERLESS_ENABLED, resetAfterTest = true)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_PAPER_AND_ELCETRONICS_ENABLED, resetAfterTest = true)
    @Test(dataProvider = "Basic User Enrolled in Estatments")
    public void verifyGoPaperless(D3User user, D3Account account) {
        MyAccountsSection myAccountSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickGoPaperlessButton()
            .enterEnrollCode(ENROLLMENT_CODE)
            .clickEstatementNextButton();
        Assert.assertTrue(myAccountSection.isTextDisplayed(AccountsL10N.Localization.GO_PAPERLESS_TITLE.getValue()),
            "Go Paperless title not Present");

        myAccountSection.clickGoPaperlessCheckBox()
            .clickEstatementSaveButton();

        Assert.assertTrue(myAccountSection.isTextDisplayed(AccountsL10N.Localization.GO_PAPPERLESS_ENROLL_SUCCESS.getValue()),
            "Go paperless enroll failure");
        Assert.assertEquals(DatabaseUtils.getPreferenceForGoPaperless(account), "ELECTRONIC",
            "Preference for go paperless did not match the expected");
    }

    @TmsLink("522552")
    @Story("E-Statements Disabled at Account Product Level")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.GO_PAPERLESS_ENABLED, enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_ENABLED)
    @Test(dataProvider = "Basic User with existing Liability accounts")
    public void verifyEstatementEnrollmentNotAvailableWhenDisabledForAccountProduct(D3User user, D3Account account) {
        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());

        Assert.assertFalse(myAccountsSection.isEstatementEnrollButtonDisplayed(),
            String.format("E-Statement Enrollment available with permission disabled for account product %s",
                account.getProductType().toString()));
    }


}