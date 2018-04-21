package com.d3.tests.consumer.core.settings;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.settings.accounts.Accounts;
import com.d3.tests.annotations.DataRequiresCompanyAttributes;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import com.d3.tests.consumer.core.accounts.AccountsTestBase;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AccountsTests extends AccountsTestBase {


    @TmsLink("509859")
    @Story("Change from Electronic to Paper")
    @DataRequiresCompanyAttributes
    @RunWithCompanyAttribute(attribute = CompanyAttribute.GO_PAPERLESS_ENABLED, resetAfterTest = true)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_ENABLED, resetAfterTest = true)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_PAPER_AND_ELCETRONIC_ENABLED, resetAfterTest = true)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_ESTATEMENTS_UN_ENROLL, resetAfterTest = true)
    @Test(dataProvider = "Basic User Enrolled in Estatments")
    public void verifyChangeFromElectronicToPaper(D3User user, D3Account account) {
        Accounts accounts = login(user).getHeader().clickSettingsButton()
            .getTabs().clickAccountsLink()
            .selectEstatementAccountToUnenroll(account.getName(), D3Account.Estatement.PAPER)
            .clickCloseDisclosure()
            .clickEstatementSaveButton();
        MyAccountsSection myAccountSection = accounts.getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());

        Assert.assertTrue(myAccountSection.isEstatementEnrollButtonDisplayed(), "E-statement Button not displayed");

    }


}
