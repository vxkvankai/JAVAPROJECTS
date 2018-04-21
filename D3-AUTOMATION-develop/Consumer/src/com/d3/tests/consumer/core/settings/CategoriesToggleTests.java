package com.d3.tests.consumer.core.settings;

import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.settings.categories.Categories;
import com.d3.tests.annotations.RunForUserTypes;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Settings")
@Feature("Categories")
public class CategoriesToggleTests extends SettingsTestBase {

    private static final String CATEGORY_NAME = "TEST CATEGORY";

    @Story("Custom User Categories")
    @TmsLink("307815")
    @TmsLink("307816")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Add Categories For Specific Users")
    public void verifyCustomCategoriesShowInCorrectProfile(D3User user) {
        Dashboard dashboard = login(user);
        Categories categories = dashboard.getHeader().clickSettingsButton().getTabs().clickCategoriesLink().clickCategory(CATEGORY_NAME);
        dashboard.getHeader().changeToggleMode(user);
        Assert.assertTrue(categories.noCustomCategoriesAvailable());
    }
}
