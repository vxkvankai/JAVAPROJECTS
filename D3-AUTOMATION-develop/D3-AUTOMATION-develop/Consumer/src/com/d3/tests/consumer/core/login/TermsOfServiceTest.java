package com.d3.tests.consumer.core.login;


import static com.d3.pages.consumer.AssertionsBase.assertThat;

import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.login.LoginPage;
import com.d3.pages.consumer.termsofservice.TermsOfService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

@Epic("Login")
@Feature("Terms of Service")
public class TermsOfServiceTest extends LoginTestBase {

    @TmsLink("287914")
    @Story("Terms Of Service")
    @Test(dataProvider = "Basic User")
    public void verifyTermsOfServiceAndLogOut(D3User user) throws D3ApiException {
        TermsOfService termsOfService = loginTOS(user);
        assertThat(termsOfService).atPage();

        Dashboard dashboard = termsOfService
            .toggleAcceptCheckBox()
            .clickSubmit();
        assertThat(dashboard).atPage();

        LoginPage login = dashboard.getHeader().clickLogout();
        assertThat(login).atPage()
            .doesNotDisplayText(user.getProfile().getFirstName());
    }
}
