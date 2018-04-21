package com.d3.tests.consumer.synovus;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.tests.consumer.core.accounts.AccountsTestBase;
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

    @BeforeClass
    private void setEstatementAdapter() {
        DatabaseUtils.updateCompanyAttributeValueString(CompanyAttribute.ACCOUNTS_ESTATEMENT_ADAPTER.getDefinition(), "bdiEStatementAdapter");
    }

    @TmsLink("348235")
    @Story("E-Statements - HTML statement view")
    @Test(dataProvider = "Basic User Enrolled in Estatments")
    public void verifyEStatementsHTML(D3User user, D3Account account) {
        MyAccountsSection myAccountsSection = login(user)
                .getHeader().clickAccountsButton().getMyAccountsSection()
                .clickAccountByAccountName(account.getName()).clickViewEstatementsButton().viewEstatementHtml();
        Assert.assertTrue(myAccountsSection.isEStatementHTMLDisplayed());
    }
}

