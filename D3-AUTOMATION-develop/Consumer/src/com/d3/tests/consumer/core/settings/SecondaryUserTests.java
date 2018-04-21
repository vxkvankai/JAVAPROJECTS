package com.d3.tests.consumer.core.settings;

import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.settings.users.UsersPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Settings")
@Feature("Secondary Users")
public class SecondaryUserTests extends SettingsTestBase {


    @TmsLink("307640")
    @TmsLink("75520")
    @TmsLink("307637")
    @TmsLink("307644")
    @TmsLink("307648")
    @TmsLink("307649")
    @TmsLink("307651")
    @TmsLink("307646")
    @TmsLink("307639")
    @TmsLink("307645")
    @TmsLink("307638")
    @Story("Add Secondary User")
    @Test(dataProvider = "Basic User With Bill Pay")
    public void verifyAddSecondaryUser(D3User user) {

        String savingsAccount = user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS).getName();
        String checkingAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING).getName();


        D3SecondaryUser secondaryUser = new D3SecondaryUser();

        UsersPage createSecondaryUser = login(user).getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickUsersLink()
            .clickAddUserButton()
            .enterSecondaryUserInfomation(secondaryUser)
            .continueToAccountServicesForm()
            .enableAccount(checkingAccount)
            .enableStatementsAndTransactionsForAccount(checkingAccount)
            .enableInternalTransfersForAccount(checkingAccount)
            .enterAmount("25.00").enterCount("4").enterPeriod("Daily")
            .enableBillPayForAccount(checkingAccount)
            .enterAmount("250.00").enterCount("2").enterPeriod("Weekly")
            .enableWireForAccount(checkingAccount)
            .enterAmount("100.00").enterCount("3").enterPeriod("Monthly")
            .enableAccount(savingsAccount)
            .enableStatementsAndTransactionsForAccount(savingsAccount)
            .continueToBankingServicesForm()
            .enableBankingServices(secondaryUser.getEnabledServices())
            .disableBankingServicesExcluding(secondaryUser.getEnabledServices())
            .clickSubmitButton();

        createSecondaryUser.expandUserDetails(secondaryUser.getLoginId());

        Assert.assertTrue(createSecondaryUser.areUserDetailsCorrect(secondaryUser, checkingAccount, savingsAccount),
            String.format("Secondary User details for %s are not correct", secondaryUser.getLoginId()));
    }
}
