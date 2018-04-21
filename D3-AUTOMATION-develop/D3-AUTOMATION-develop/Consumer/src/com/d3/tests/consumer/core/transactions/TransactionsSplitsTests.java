package com.d3.tests.consumer.core.transactions;

import com.d3.datawrappers.user.D3User;
import com.d3.helpers.DateAndCurrencyHelper;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.L10nCommon;
import com.d3.l10n.transactions.TransactionsL10N;
import com.d3.pages.consumer.transactions.SingleTransactionPage;
import com.d3.pages.consumer.transactions.TransactionsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.DecimalFormat;

@Epic("Transactions")
@Feature("Split Transactions")
public class TransactionsSplitsTests extends TransactionsTestBase {

    private static final String NOT_ALLOWED_CHARACTERS = "~`<>^|\"";
    private final DecimalFormat df = new DecimalFormat("#.00");
    private final String splitAmount = df.format(RandomHelper.getRandomNumber(1, 9));
    private final String splitNegativeAmount = df.format(RandomHelper.getRandomNumber(-9, -1));
    private final String validMemo = RandomHelper.getRandomString(5) + RandomHelper.getRandomNumberString(5, true);
    private final String splitAmount2 = df.format(RandomHelper.getRandomNumber(1, 9));

    @Flaky
    @TmsLink("347686")
    @Story("Add Transaction Split")
    @Story("Transaction Split Required Fields")
    @Test(dataProvider = "Basic User")
    public void verifyRequiredFieldsAndSaveSplitTransactionsIsSuccessful(D3User user) {
        TransactionsPage transactionsPage = login(user)
            .getHeader()
            .clickTransactionsButton()
            .clickShowUncategorizedButton();

        // Select the first transaction and click on Splits, then save without entering data
        SingleTransactionPage singleTransaction = transactionsPage.clickFirstPostedTransaction()
            .clickOnSplitButton()
            .clickOnSaveSplit();

        // Verify message
        Assert.assertTrue(singleTransaction.isTextPresent(TransactionsL10N.Localization.INVALID_SPLIT_AMOUNT.getValue()));

        // Now enter an invalid Memo and an amount
        singleTransaction.enterAmount(splitAmount);
        for (char badChar : NOT_ALLOWED_CHARACTERS.toCharArray()) {
            singleTransaction.enterSplitMemoText(String.valueOf(badChar)).clickOnSaveSplit();
            Assert.assertTrue(singleTransaction.isTextDisplayed(L10nCommon.Localization.CHARACTERS_NOT_ALLOWED.getValue()),
                String.format("Error message for bad char ('%s')in name not shown", badChar));
        }

        // Verify amount entered 
        Assert.assertEquals(singleTransaction.getSplitValue(), DateAndCurrencyHelper.getAmountWithDollarSign(splitAmount));

        // Finally enter a valid Memo value
        singleTransaction.enterSplitMemoText(validMemo)
            .clickOnSaveSplit();

        // Verify Memo and Amount are displayed in the page
        Assert.assertTrue(singleTransaction.isTextPresent(validMemo));
        Assert.assertTrue(singleTransaction.isTextPresent(DateAndCurrencyHelper.getAmountWithDollarSign(splitAmount)));
    }

    @Flaky
    @TmsLink("304956")
    @Story("Add Multiple Splits")
    @Test(dataProvider = "Basic User")
    public void verifyMultipleTransactionSplitAddIsSuccessful(D3User user) {
        SingleTransactionPage transactionsPage = login(user)
            .getHeader()
            .clickTransactionsButton()
            .clickShowUncategorizedButton()
            .clickFirstPostedTransaction()
            .clickOnSplitButton()
            .enterAmount(splitAmount)
            .enterSplitMemoText(validMemo)
            .selectRandomCategoryItem()
            .clickAddSplitButton()
            .enterAmount(splitNegativeAmount)
            .enterSplitMemoText(validMemo)
            .clickOnSaveSplit();

        // Verify Memo and Amount are displayed in the page
        Assert.assertTrue(transactionsPage.isTextPresent(validMemo));
        Assert.assertTrue(transactionsPage.isTextPresent(splitAmount));
        // Verify no negatives are stored
        Assert.assertFalse(transactionsPage.isTextPresent(DateAndCurrencyHelper.getAmountWithDollarSign(splitNegativeAmount)));
    }

    @TmsLink("522553")
    @Story("Modify Transaction Splits")
    @Test(dataProvider = "Basic User")
    public void verifyTransactionSplitCanBeChangedAndNewSplitAdded(D3User user) {
        SingleTransactionPage transactionsPage = login(user)
            .getHeader()
            .clickTransactionsButton()
            .clickShowUncategorizedButton()
            .clickFirstPostedTransaction()
            .clickOnSplitButton()
            .enterAmount(splitAmount)
            .enterSplitMemoText(validMemo)
            .selectRandomCategoryItem()
            .clickOnSaveSplit();

        // Check split values
        Assert.assertTrue(transactionsPage.isTextPresent(validMemo));
        Assert.assertTrue(transactionsPage.isTextPresent(splitAmount));

        // Now change the split values
        transactionsPage.clickOnChangeSplit()
            .enterAmount(splitAmount2)
            .enterSplitMemoText(validMemo)
            .selectRandomCategoryItem()
            .clickOnSaveSplit();

        // Verify amount changed 
        Assert.assertTrue(transactionsPage.isTextPresent(splitAmount2));

        // Add a new split
        transactionsPage.clickOnChangeSplit()
            .clickAddSplitButton()
            .enterAmount(splitAmount)
            .enterSplitMemoText(validMemo)
            .clickOnSaveSplit();

        // Verify Memo, Amount and Amount2 are displayed in the page
        Assert.assertTrue(transactionsPage.isTextPresent(validMemo));
        Assert.assertTrue(transactionsPage.isTextPresent(splitAmount2));
        Assert.assertTrue(transactionsPage.isTextPresent(splitAmount));
    }
}
