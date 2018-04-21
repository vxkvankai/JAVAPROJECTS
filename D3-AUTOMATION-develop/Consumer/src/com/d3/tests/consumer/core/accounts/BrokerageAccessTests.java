package com.d3.tests.consumer.core.accounts;


import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BrokerageAccessTests extends AccountsTestBase {


    @TmsLink("522544")
    @Story("Brokerage Access")
    @Test(dataProvider = "Basic User with existing accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNT_BROKERAGE_ACCESS_ENABLED, enabled = true)
    public void verifyBrokerageAccessWhenEnabledForAccountProduct(D3User user, D3Account account) {
        DatabaseUtils.updateAccountProductPermission(account.getProductType(), "d3Permission.allow.brokerage.access", "true");
        String url = DatabaseUtils.selectCompanyAttributeValueString("accounts.brokerageAccess.defaultRedirectionAdapter.url") + "/";

        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());
        Assert.assertTrue(myAccountsSection.isBrokerageAccessButtonDisplayed(), "The brokerage-access button is not displayed");
        myAccountsSection.clickBrokerageAccessButton();
        Assert.assertEquals(driver.getCurrentUrl(), url, "The site http://www.mybrokerage.com is not displayed");
    }
}
