package com.d3.tests.consumer.core.accounts;


import com.d3.database.AttributeDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.enums.AccountProductAttributes;
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
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNT_BROKERAGE_ACCESS_ENABLED)
    public void verifyBrokerageAccessWhenEnabledForAccountProduct(D3User user, D3Account account) {
        AttributeDatabaseHelper.updateAccountProductAttribute(account.getProductType(), AccountProductAttributes.ALLOW_BROKERAGE_ACCESS, true);
        String url = AttributeDatabaseHelper.selectCompanyAttributeValueString("accounts.brokerageAccess.defaultRedirectionAdapter.url") + "/";

        MyAccountsSection myAccountsSection = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());
        Assert.assertTrue(myAccountsSection.isBrokerageAccessButtonDisplayed(), "The brokerage-access button is not displayed");
        myAccountsSection.clickBrokerageAccessButton()
            .waitForUrlToBe(url);
    }
}
