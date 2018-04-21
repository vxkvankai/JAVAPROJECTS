package com.d3.tests.consumer.core.login;


import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.l10n.dashboard.DashboardL10N;
import com.d3.l10n.login.LoginL10N;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.termsofservice.TermsOfService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Login")
@Feature("Terms of Service")
public class TermsOfServiceTest extends LoginTestBase {

    @TmsLink("287914")
    @Story("Terms Of Service")
    @Test(dataProvider = "Basic User")
    public void verifyTermsOfServiceAndLogOut(D3User user) throws D3ApiException {
        TermsOfService termsOfService = loginTOS(user);

        Assert.assertEquals(termsOfService.getTosTitleText().trim(), LoginL10N.Localization.TOS_TITLE.getValue(),
            "TOS Title was not correct");

        Dashboard dashboard = termsOfService.toggleAcceptCheckBox()
            .clickSubmit();

        Assert.assertTrue(dashboard.isTextDisplayed(DashboardL10N.Localization.DEPOSIT_ACCOUNTS.getValue()),
            "Deposit Accounts text was not shown");

        dashboard.getHeader().clickLogout();

        Assert.assertTrue(loginPage.isTextNotPresent(LoginL10N.Localization.USERNAME.getValue())
            , "Username was still shown, so logout didn't work");
    }
}
