package com.d3.tests.consumer.core.selfservice;

import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.selfservice.FAQs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Self Service")
@Feature("FAQs")
public class SelfServiceTests extends SelfServiceTestBase {

    @TmsLink("347936")
    @Story("Verifying FAQ Content")
    @Test(dataProvider = "Basic User")
    public void verifyFAQContent(D3User user) {
        Dashboard dashboard = login(user);
        FAQs faqs = dashboard.getHeader().clickSelfServiceButton();
        Assert.assertTrue(faqs.isCorrectInformationDisplayed());
    }
}