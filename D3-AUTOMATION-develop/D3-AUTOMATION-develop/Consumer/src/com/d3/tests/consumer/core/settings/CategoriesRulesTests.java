package com.d3.tests.consumer.core.settings;

import com.d3.datawrappers.user.D3User;
import com.d3.l10n.settings.CategoriesL10N;
import com.d3.l10n.settings.RulesL10N;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.settings.categories.Categories;
import com.d3.pages.consumer.settings.rules.Rules;
import com.d3.pages.consumer.transactions.SingleTransactionPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Settings")
@Feature("Categories")
public class CategoriesRulesTests extends SettingsTestBase {

    @Flaky
    @TmsLink("288116")
    @Story("Rename Category")
    @Test(dataProvider = "User with Category Added")
    public void verifyRenameCategory(D3User user) {
        String newCategoryName = "Renamed Category";
        Dashboard dashboard = login(user);
        Categories categories = dashboard.getHeader()
            .clickSettingsButton()
            .getTabs()
            .clickCategoriesLink()
            .clickCategory("TEST CATEGORY")
            .changeCategoryName(newCategoryName);

        SingleTransactionPage transactions = categories.getHeader()
            .clickTransactionsButton()
            .clickFirstPostedTransaction()
            .clickCategoryName();
        Assert.assertTrue(transactions.doesCategoryExist(newCategoryName),
            String.format("Category Name %s was not displayed", newCategoryName));
    }

    @Flaky
    @TmsLink("288119")
    @Story("Delete Category Rule")
    @Test(dataProvider = "User with Category Rule")
    public void verifyDeleteCategoryRule(D3User user) {
        Dashboard dashboard = login(user);
        Rules rules = dashboard.getHeader().clickSettingsButton().getTabs().clickRulesLink();

        rules.clickDeleteRuleButton();
        Assert.assertTrue(rules.isTextDisplayed(RulesL10N.Localization.CATEGORY_DELETE_CONFIRM.getValue()),
            "Delete category rule messaging did not display");

        rules.clickDeleteRuleConfirmButton();
        Assert.assertTrue(rules.isTextDisplayed(RulesL10N.Localization.NO_CATEGORY_RULES.getValue()), "Category rule is still displayed");
    }

    @TmsLink("288121")
    @Story("Delete Category")
    @Test(dataProvider = "User with Category Added")
    public void verifyDeleteCategory(D3User user) {
        Dashboard dashboard = login(user);
        Categories categories = dashboard.getHeader().clickSettingsButton().getTabs().clickCategoriesLink();

        categories.clickCategory("TEST CATEGORY")
            .clickDeleteButton()
            .clickDeleteConfirmButton();
        Assert.assertTrue(categories.isTextDisplayed(CategoriesL10N.Localization.HELP.getValue()), "User Category is still displayed");
    }

    @TmsLink("288122")
    @Story("Rename Category Rule")
    @Test(dataProvider = "User with Renaming Rule")
    public void verifyDeleteRenameRule(D3User user) {
        Dashboard dashboard = login(user);
        Rules rules = dashboard.getHeader().clickSettingsButton().getTabs().clickRulesLink();

        rules.clickDeleteRuleButton();
        Assert.assertTrue(rules.isTextDisplayed(RulesL10N.Localization.RENAMING_DELETE_CONFIRM.getValue()),
            "Delete renaming rule messaging did not display");

        rules.clickDeleteRuleConfirmButton();
        Assert.assertTrue(rules.isTextDisplayed(RulesL10N.Localization.NO_RENAMING_RULES.getValue()), "Renaming rule is still displayed");
    }
}
