package com.d3.tests.consumer.core.transactions;

import com.d3.database.AuditDatabaseHelper;
import com.d3.database.TransactionDatabaseHelper;
import com.d3.datawrappers.CategoryType;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.L10nCommon;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.transactions.SingleTransactionPage;
import com.d3.pages.consumer.transactions.TransactionsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

@Epic("Transactions")
@Feature("Categorization")
public class CategorizationTests extends TransactionsTestBase {

    /**
     * Will login and go to the Transactions page, search for a posted transaction, expand the transaction and click the Add Category button
     *
     * @param user D3User that test will be using
     * @param postedTransaction the posted transaction to search for
     * @return Transactions page
     */
    private SingleTransactionPage addCategoryCommonSteps(D3User user, D3Transaction postedTransaction) {
        return login(user)
            .getHeader()
            .clickTransactionsButton()
            .searchForDescription(postedTransaction.getName())
            .clickFirstPostedTransaction()
            .clickCategoryName()
            .clickAddCategoryButton();
    }

    @TmsLink("522684")
    @Story("Expense Category Required Fields")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyExpenseCategoryRequiredFields(D3User user, D3Transaction postedTransaction) {
        SingleTransactionPage transactionsPage = addCategoryCommonSteps(user, postedTransaction)
            .clickExpenseCategoryButton()
            .clickAddCategorySubmitButton();
        Assert.assertTrue(transactionsPage.isTextPresent("Enter a Name")); //No L10N for this

        transactionsPage.enterCategoryName(RandomHelper.getRandomString(10))
            .clickSubcategoryCheckbox()
            .clickAddCategorySubmitButton();
        Assert.assertTrue(transactionsPage.isTextPresent("Enter a Group")); //No L10N for this
    }

    @TmsLink("522685")
    @Story("Income Category Required Fields")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyIncomeCategoryRequiredFields(D3User user, D3Transaction postedTransaction) {
        SingleTransactionPage transactionsPage = addCategoryCommonSteps(user, postedTransaction)
            .clickIncomeCategoryButton()
            .clickAddCategorySubmitButton();
        Assert.assertTrue(transactionsPage.isTextPresent("Enter a Name")); //No L10N for this

        transactionsPage.enterCategoryName(RandomHelper.getRandomString(10))
            .clickSubcategoryCheckbox()
            .clickAddCategorySubmitButton();
        Assert.assertTrue(transactionsPage.isTextPresent("Enter a Group")); //No L10N for this
    }

    @TmsLink("522692")
    @Story("Adding Expense Category")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyAddingNewExpenseCategory(D3User user, D3Transaction postedTransaction) {
        String invalidCharacters = "~`<>^|\"";
        String validCharacters = "$#@!%&";
        String newCategoryName = String.format("Expense Category %s%s%s",
            validCharacters.toCharArray()[ThreadLocalRandom.current().nextInt(validCharacters.toCharArray().length)],
            RandomHelper.getRandomString(5),
            validCharacters.toCharArray()[ThreadLocalRandom.current().nextInt(validCharacters.toCharArray().length)]);

        SingleTransactionPage transactionsPage = addCategoryCommonSteps(user, postedTransaction)
            .clickExpenseCategoryButton();

        // validate name field
        for (char badChar : invalidCharacters.toCharArray()) {
            transactionsPage.enterCategoryName(String.valueOf(badChar)).clickAddCategorySubmitButton();
            Assert.assertTrue(transactionsPage.isTextDisplayed(L10nCommon.Localization.CHARACTERS_NOT_ALLOWED.getValue()),
                String.format("Error message for bad char ('%s')in name not shown", badChar));
        }

        transactionsPage.enterCategoryName(newCategoryName).clickAddCategorySubmitButton();
        Assert.assertTrue(transactionsPage.doesCategoryExist(newCategoryName));

        transactionsPage.categorizeTransaction(newCategoryName).clickIgnoreButton();
        Assert.assertEquals(transactionsPage.getTransactionCategory(), newCategoryName);

        //verify audit record gets created for Adding Category
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.SETTINGS_CATEGORY_ADD));
        Assert.assertEquals(String.valueOf(TransactionDatabaseHelper.getCategoryId(newCategoryName, user.getProfile().getType().toString())),
            AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.SETTINGS_CATEGORY_ADD)
                .get(AuditAttribute.SETTINGS_CATEGORY_ID.getAttributeName()));
    }

    @TmsLink("522693")
    @Story("Adding Income Category")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyAddingNewIncomeCategory(D3User user, D3Transaction postedTransaction) {
        String invalidCharacters = "~`<>^|\"";
        String validCharacters = "$#@!%&";
        String newCategoryName = String.format("Income Category %s%s%s",
            validCharacters.toCharArray()[ThreadLocalRandom.current().nextInt(validCharacters.toCharArray().length)],
            RandomHelper.getRandomString(5),
            validCharacters.toCharArray()[ThreadLocalRandom.current().nextInt(validCharacters.toCharArray().length)]);
        SingleTransactionPage transactionsPage = addCategoryCommonSteps(user, postedTransaction)
            .clickIncomeCategoryButton();

        // validate name field
        for (char badChar : invalidCharacters.toCharArray()) {
            transactionsPage.enterCategoryName(String.valueOf(badChar)).clickAddCategorySubmitButton();
            Assert.assertTrue(transactionsPage.isTextDisplayed(L10nCommon.Localization.CHARACTERS_NOT_ALLOWED.getValue()),
                String.format("Error message for bad char ('%s')in name not shown", badChar));
        }

        transactionsPage.enterCategoryName(newCategoryName).clickAddCategorySubmitButton();
        Assert.assertTrue(transactionsPage.doesCategoryExist(newCategoryName));

        transactionsPage.categorizeTransaction(newCategoryName).clickIgnoreButton();
        Assert.assertEquals(transactionsPage.getTransactionCategory(), newCategoryName);

        //verify audit record gets created for Adding Category
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.SETTINGS_CATEGORY_ADD));
        Assert.assertEquals(String.valueOf(TransactionDatabaseHelper.getCategoryId(newCategoryName, user.getProfile().getType().toString())),
            AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.SETTINGS_CATEGORY_ADD)
                .get(AuditAttribute.SETTINGS_CATEGORY_ID.getAttributeName()));
    }

    @TmsLink("522599")
    @Story("Adding Expense Subcategory")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyAddingNewExpenseSubCategory(D3User user, D3Transaction postedTransaction) {
        String parentCategory = TransactionDatabaseHelper.getRandomCategoryOfType(CategoryType.getExpenseCategoryTypes(),
            user.getProfile().getType().toString());
        String subcategoryName = String.format("Expense SubCategory %s", RandomHelper.getRandomString(5));

        TransactionsPage transactionsPage = login(user).getHeader()
            .clickTransactionsButton()
            .searchForDescription(postedTransaction.getName());
        int initialSize = transactionsPage.getNumberOfTransactionsShown();

        SingleTransactionPage singleTransactionPage = transactionsPage.clickFirstPostedTransaction()
            .clickCategoryName()
            .clickAddCategoryButton()
            .clickExpenseCategoryButton()
            .enterCategoryName(subcategoryName)
            .clickSubcategoryCheckbox()
            .selectParentCategory(parentCategory)
            .clickAddCategorySubmitButton()
            .expandSubcategories(parentCategory);
        Assert.assertTrue(singleTransactionPage.doesCategoryExist(subcategoryName));

        singleTransactionPage.categorizeTransaction(subcategoryName);
        Assert.assertTrue(singleTransactionPage.isTextDisplayed("transaction(s) were found that could be recategorized."));

        transactionsPage = singleTransactionPage.clickViewSimilar()
            .selectAllTransactionsToBeRenamed()
            .clickSaveSelectedButton()
            .searchForTerm(subcategoryName);
        Assert.assertEquals(transactionsPage.getNumberOfTransactionsShown(), initialSize, "Not all the transactions were recategorized");

        singleTransactionPage = transactionsPage.clickFirstPostedTransaction();
        Assert.assertEquals(singleTransactionPage.getTransactionCategory(), String.format("%s/%s", parentCategory, subcategoryName));

        //verify audit record gets created for Adding Category
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.SETTINGS_CATEGORY_ADD));
        Assert.assertEquals(String.valueOf(TransactionDatabaseHelper.getCategoryId(subcategoryName, user.getProfile().getType().toString())),
            AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.SETTINGS_CATEGORY_ADD)
                .get(AuditAttribute.SETTINGS_CATEGORY_ID.getAttributeName()));
    }

    @TmsLink("522603")
    @Story("Adding Income Subcategory")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyAddingNewIncomeSubCategory(D3User user, D3Transaction postedTransaction) {
        String parentCategory = TransactionDatabaseHelper.getRandomCategoryOfType(CategoryType.getIncomeCategoryTypes(), user.getProfile().getType().toString());
        String subcategoryName = String.format("Income SubCategory %s", RandomHelper.getRandomString(5));

        TransactionsPage transactionsPage = login(user).getHeader()
            .clickTransactionsButton()
            .searchForDescription(postedTransaction.getName());
        int initialSize = transactionsPage.getNumberOfTransactionsShown();

        SingleTransactionPage singleTransactionPage = transactionsPage.clickFirstPostedTransaction()
            .clickCategoryName()
            .clickAddCategoryButton()
            .clickIncomeCategoryButton()
            .enterCategoryName(subcategoryName)
            .clickSubcategoryCheckbox()
            .selectParentCategory(parentCategory)
            .clickAddCategorySubmitButton()
            .expandSubcategories(parentCategory);
        Assert.assertTrue(singleTransactionPage.doesCategoryExist(subcategoryName));

        singleTransactionPage.categorizeTransaction(subcategoryName);
        Assert.assertTrue(singleTransactionPage.isTextDisplayed("transaction(s) were found that could be recategorized."));

        transactionsPage = singleTransactionPage.clickViewSimilar()
            .selectAllTransactionsToBeRenamed()
            .clickSaveSelectedButton()
            .searchForTerm(subcategoryName);
        Assert.assertEquals(transactionsPage.getNumberOfTransactionsShown(), initialSize, "Not all the transactions were recategorized");

        singleTransactionPage = transactionsPage.clickFirstPostedTransaction();
        Assert.assertEquals(singleTransactionPage.getTransactionCategory(), String.format("%s/%s", parentCategory, subcategoryName));

        //verify audit record gets created for Adding Category
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.SETTINGS_CATEGORY_ADD));
        Assert.assertEquals(String.valueOf(TransactionDatabaseHelper.getCategoryId(subcategoryName, user.getProfile().getType().toString())),
            AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.SETTINGS_CATEGORY_ADD)
                .get(AuditAttribute.SETTINGS_CATEGORY_ID.getAttributeName()));
    }
}
