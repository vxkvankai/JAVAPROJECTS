package com.d3.tests.consumer.core.accounts;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Accounts")
@Feature("Order Checks")
public class CheckOrderTests extends AccountsTestBase {

    @TmsLink("509929")
    @Story("Default Check Order Redirection Adapter")
    @Test(dataProvider = "Basic User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_CHECK_ORDER_ADAPTER, value = "defaultCheckOrderRedirectionAdapter")
    public void verifyOrderChecksWithDefaultAdapter(D3User user) {
        String redirectUrl = DatabaseUtils.selectCompanyAttributeValueString(CompanyAttribute.ACCOUNTS_CHECK_ORDER_REDIRECT_URL.getDefinition());
        Assert.assertNotNull(redirectUrl, "Redirect Url was not found in the db");

        login(user).getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING).getName())
            .clickOrderChecksButton();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.startsWith(redirectUrl), String.format("Current Url: %s, Expected Url: %s", currentUrl, redirectUrl));
    }
}
