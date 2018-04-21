package com.d3.tests.consumer.core.accounts;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.tests.annotations.RunForUserTypes;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Accounts")
@Feature("Accounts - Toggle")
public class AccountsToggleTests extends AccountsTestBase {

    @TmsLink("306257")
    @TmsLink("306258")
    @TmsLink("306261")
    @Story("Accounts Available to User")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyCorrectAccountsAreDisplayed(D3User user) {
        MyAccountsSection accounts = login(user).getHeader()
            .clickAccountsButton()
            .getMyAccountsSection();
        Assert.assertTrue(accounts.areAvailableAccountsCorrect(DatabaseUtils.getMaskedUserAccounts(user)));
    }
}
