package com.d3.tests.consumer.core.transactions;

import static com.d3.helpers.DateAndCurrencyHelper.getEndDate;
import static com.d3.helpers.RandomHelper.getRandomNumberString;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.database.AuditDatabaseHelper;
import com.d3.database.TransactionDatabaseHelper;
import com.d3.datawrappers.CategoryType;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.account.enums.AccountProductAttributes;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.DateAndCurrencyHelper;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.L10nCommon;
import com.d3.l10n.transactions.TransactionsL10N;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.transactions.SingleTransactionPage;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.tests.annotations.RunWithAccountProductAttribute;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

@Epic("Transactions")
@Feature("Transactions")
public class TransactionsTests extends TransactionsTestBase {

    private static final String LOCALIZED_MSG_ERROR = "The localized message is not present";
    private static final String TRANSACTION_RENAMING_MSG = "transaction(s) were found that could be renamed.";

    @TmsLink("288061")
    @Story("Download Transactions")
    @Test(dataProvider = "Basic User")
    public void verifyDownloadCsvAllTransactions(D3User user) {
        TransactionsPage transactionsPage = login(user).getHeader().clickTransactionsButton();
        transactionsPage.downloadTransactions(TransactionsPage.DownloadType.CSV);
        // TODO: This test doesn't actually verify the file downloaded correctly or its contents
    }

    @TmsLink("288062")
    @Story("Transactions Search")
    @Test(dataProvider = "Basic User")
    public void verifySearchTextInTransactions(D3User user) {
        D3Transaction transactionToSearch = user.getRandomTransaction();
        Assert.assertNotNull(transactionToSearch, "Transaction was null");

        TransactionsPage transactionsPage = login(user).getHeader().clickTransactionsButton();
        transactionsPage.searchForTerm(transactionToSearch.getAmountString());

        Assert.assertTrue(transactionsPage.isTextDisplayed(String.format("Term: %s", transactionToSearch.getAmountString())));
        Assert.assertTrue(transactionsPage.isTextDisplayed(TransactionsL10N.Localization.NO_MORE_TRANSACTIONS_FOUND.getValue()));

        Assert.assertEquals(transactionsPage.getNumberOfTransactionsShown(), 1,
            "More than one transaction was shown after searching for the unique transaction");
    }

    @TmsLink("288063")
    @Story("Transactions Search")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifySearchCalendarInTransactions(D3User user, D3Transaction transaction) {
        DateTime transactionDate = new DateTime(transaction.getPostDate());
        DateTimeFormatter df = DateTimeFormat.forPattern("MM/dd/yyy");
        String startDateSearch = df.print(transactionDate.minusDays(1));
        String endDateSearch = df.print(transactionDate.plusDays(1));

        TransactionsPage transactionPage = login(user)
            .getHeader()
            .clickTransactionsButton();

        int noSearchTotal = transactionPage.getNumberOfTransactionsShown();

        transactionPage.clickDateRangeButton()
            .enterStartDate(startDateSearch)
            .enterEndDate(endDateSearch)
            .clickDateSearchButton();
        int searchTotal = transactionPage.getNumberOfTransactionsShown();

        Assert.assertTrue(searchTotal < noSearchTotal,
            String.format("The search did not lower the amount of transactions on the screen: before: %s, After: %s", noSearchTotal, searchTotal));
        Assert.assertTrue(transactionPage.isTransactionNameShown(transaction.getName()), "Name of the transaction is not shown");
        Assert.assertTrue(transactionPage.isTextDisplayed(transaction.getAmountString()), "Amount of the transaction is not shown");
        Assert.assertTrue(transactionPage.isTextDisplayed(TransactionsL10N.Localization.NO_MORE_TRANSACTIONS_FOUND.getValue()));
    }

    @TmsLink("288095")
    @Story("Show More Transactions")
    @Test(dataProvider = "Basic User With Many Transactions")
    public void verifyShowMoreButtonForTransactions(D3User user) {
        TransactionsPage page = login(user).getHeader().clickTransactionsButton().clickShowMoreButton().scrollUntilNoMoreTransactions();
        Assert.assertTrue(page.isTextPresent(TransactionsL10N.Localization.NO_MORE_TRANSACTIONS_FOUND.getValue()));
    }

    @TmsLink("301307")
    @Story("Show Uncategorized Transactions")
    @Test(dataProvider = "Basic User")
    public void verifyShowUncategorizedTransactions(D3User user) {
        TransactionsPage page = login(user).getHeader()
            .clickTransactionsButton()
            .clickShowUncategorizedButton();
        Assert.assertTrue(page.areOnlyUncategorizedTransactionsShown(), "There are categorized transactions shown");
    }

    @Flaky
    @TmsLink("522557")
    @TmsLink("288097")
    @Story("Renaming Transactions")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyIgnoreButtonForSimilarTransactionsWhenRenaming(D3User user, D3Transaction postedTransaction) {
        String renamedTransaction = getRandomString(10);

        SingleTransactionPage singleTransactionPage = login(user).getHeader()
            .clickTransactionsButton()
            .searchForDescription(postedTransaction.getName())
            .clickFirstPostedTransaction()
            .changeActiveTransactionName(renamedTransaction);
        Assert.assertTrue(singleTransactionPage.isTextDisplayed(TRANSACTION_RENAMING_MSG));

        TransactionsPage page = singleTransactionPage.clickIgnoreButton()
            .getTransactionPage();
        Assert.assertTrue(page.isTransactionNameShown(postedTransaction.getName()));
        Assert.assertTrue(page.isTransactionNameShown(renamedTransaction));

        String expectedActualId = TransactionDatabaseHelper.getRenamedTransactionId(renamedTransaction);
        String auditId = AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.SETTINGS_RENAMING_RULE_ADD)
            .getOrDefault(AuditAttribute.SETTINGS_RENAMING_RULE_ID.getAttributeName(), "");

        Assert.assertNotNull(expectedActualId);
        Assert.assertFalse(expectedActualId.isEmpty()); //NOSONAR null check is above

        Assert.assertNotNull(auditId);
        Assert.assertFalse(auditId.isEmpty());

        Assert.assertEquals(expectedActualId, auditId, "Audit record not found");
    }

    @TmsLink("304954")
    @Story("Transactions View Image")
    @Test(dataProvider = "Basic User with Image Transaction")
    public void verifyViewImageForTransactions(D3User user, D3Transaction transactionToSearch) {
        SingleTransactionPage page = login(user).getHeader()
            .clickTransactionsButton()
            .searchForDescription(transactionToSearch.getName())
            .clickFirstPostedTransaction()
            .clickViewImageButton();
        Assert.assertTrue(page.isCheckImageShown(), "Check Image was not shown");
    }

    @Flaky // Note (Jmoravec): fails randomly due to an nonchange event not triggering correctly when changing the name/description
    @TmsLink("522557")
    @Story("Show More Transactions")
    @Test(dataProvider = "Basic User With Posted Transaction")
    public void verifyEditPostedTransactionIsSuccessWithAuditAndActivityRecordsGenerated(D3User user, D3Transaction postedTransaction) {
        String transactionToEdit = postedTransaction.getName();
        String newTransactionName = getRandomString(6);
        SingleTransactionPage transactionPage = login(user)
            .getHeader()
            .clickTransactionsButton()
            .searchForDescription(transactionToEdit)
            .clickFirstPostedTransaction()
            .changeActiveTransactionName(newTransactionName);

        Assert.assertTrue(transactionPage.isTextPresent(TRANSACTION_RENAMING_MSG));

        transactionPage.clickViewSimilar();
        Assert.assertTrue(transactionPage.isTextDisplayed(transactionToEdit));

        TransactionsPage page = transactionPage.selectSimilarTransactions()
            .clickSaveSelectedButton()
            .searchForDescription(newTransactionName);
        Assert.assertEquals(page.getNumberOfTransactionsShown(), 2, "Search results don't match");

        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.SETTINGS_RENAMING_RULE_ADD),
            "Audit renaming rule not found");
    }

    @TmsLink("347559")
    @Story("Delete Offline Transaction")
    @Test(dataProvider = "Basic User With Offline Account Transaction")
    public void verifyDeleteOfflineTransactionWithAuditAndActivityRecordsGenerated(D3User user, D3Account account, D3Transaction offlineTxn) {
        SingleTransactionPage transactionPage = login(user).getHeader()
            .clickTransactionsButton()
            .filterByAccounts(Collections.singletonList(account))
            .clickOnTransactionByName(offlineTxn.getName())
            .clickOnDeleteTransactionButton();
        Assert.assertTrue(transactionPage.isTextDisplayed(TransactionsL10N.Localization.DELETE_TRANSACTION_MESSAGE.getValue()));

        transactionPage.clickOnConfirmDeleteTransactionButton();
        Assert.assertTrue(transactionPage.isTextDisplayed(TransactionsL10N.Localization.NO_TRANSACTIONS_AVAILABLE.getValue()));

        //Audit Assertions
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.OFFLINE_TRANSACTION_DELETE));
        Assert.assertEquals(offlineTxn.getAmountString(),
            String.format("%.02f", Double.parseDouble(AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.OFFLINE_TRANSACTION_DELETE)
                .get(AuditAttribute.AMOUNT.getAttributeName()))));
    }


    // TODO add TMS link
    @Story("Transactions")
    @Test(dataProvider = "Basic User")
    public void validateRequiredFields(D3User user) {
        String notAllowedCharacters = "~&`<>^|\"";
        List<D3Transaction> transactions = user.getFirstAccount().getTransactionsWithCheckNumbers();
        D3Transaction transactionToSearch = transactions.get(0);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyy");

        TransactionsPage transactionsPage = login(user)
            .getHeader()
            .clickTransactionsButton();

        String startDate = df.format(transactionToSearch.getPostDate());
        String endDate = df.format(getEndDate(transactionToSearch.getPostDate()));

        transactionsPage.clickDateRangeButton()
            .enterStartDate(startDate)
            .enterEndDate(endDate)
            .clickOnClearDatesButton();

        Assert.assertTrue(transactionsPage.areRemovedDates(), "Dates were not cleared");

        transactionsPage.clickCheckRangeButton()
            .clickOnSearchForCheckRangeButton();

        Assert.assertTrue(
            transactionsPage.isTextDisplayed(TransactionsL10N.Localization.CHECK_RANGE_VALIDATE_END_REQUIRED.getValue()),
            "The required message for end check number was not displayed");
        Assert.assertTrue(
            transactionsPage.isTextDisplayed(TransactionsL10N.Localization.CHECK_RANGE_VALIDATE_START_REQUIRED.getValue()),
            "The required message for start check number was not displayed");

        for (char badChar : notAllowedCharacters.toCharArray()) {
            transactionsPage.enterStartCheck(String.valueOf(badChar))
                .enterEndCheck(String.valueOf(badChar))
                .clickOnSearchForCheckRangeButton();
            Assert.assertTrue(
                transactionsPage.areRequiredFieldMessagesDisplayed(L10nCommon.Localization.ENTER_ONLY_DIGITS.getValue()),
                String.format("Error message for bad char ('%s')in name not shown", badChar));
        }

        for (char badChar : notAllowedCharacters.toCharArray()) {
            transactionsPage.enterStartCheck(String.valueOf(badChar))
                .enterEndCheck("")
                .clickOnSearchForCheckRangeButton();
            Assert.assertTrue(transactionsPage.isTextDisplayed(L10nCommon.Localization.ENTER_ONLY_DIGITS.getValue()),
                String.format("Error message for bad char ('%s')in name not shown", badChar));
            Assert.assertTrue(
                transactionsPage.isTextDisplayed(TransactionsL10N.Localization.CHECK_RANGE_VALIDATE_END_REQUIRED.getValue()),
                "The required message for end check number was not displayed");
        }
    }

    @TmsLink("306167")
    @Story("Transactions")
    @Test(dataProvider = "Basic User")
    public void validateSuccessfulSearchesByNameDateAndCheckRanges(D3User user) {

        List<D3Transaction> transactions = user.getFirstAccount().getTransactionsWithCheckNumbers();
        D3Transaction transactionToSearch = transactions.get(0);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyy");

        TransactionsPage transactionsPage = login(user).getHeader().clickTransactionsButton();
        transactionsPage.searchForDescription(transactionToSearch.getName());

        Assert.assertTrue(transactionsPage.allTransactionsWithWordInNameDisplayed(transactionToSearch.getName()),
            "Some transactions does not contains the  word in the  name");

        String startDate = df.format(transactionToSearch.getPostDate());
        String endDate = df.format(getEndDate(transactionToSearch.getPostDate()));

        transactionsPage.clickDateRangeButton()
            .enterStartDate(startDate)
            .enterEndDate(endDate)
            .clickDateSearchButton();

        Assert.assertTrue(transactionsPage.transactionsHaveDatesInRangeDate(startDate, endDate, transactionToSearch.getName()));

        transactionsPage.clickCheckRangeButton()
            .enterStartCheck(transactionToSearch.getCheckNumber())
            .enterEndCheck(transactionToSearch.getCheckNumber()).clickOnSearchForCheckRangeButton();

        Assert.assertTrue(
            transactionsPage.isTransactionShown(transactionToSearch) && transactionsPage.getNumberOfTransactionsShown() == 1,
            "The single transaction was not displayed ");

        String startCheck = transactionToSearch.getCheckNumber();
        String endCheck = transactions.get(transactions.size() - 1).getCheckNumber();
        transactionsPage.removeFilters()
            .enterStartCheck(startCheck)
            .enterEndCheck(endCheck)
            .clickOnSearchForCheckRangeButton();

        Assert.assertTrue(transactionsPage.areCorrectTransactionsDisplayedByCheckNumber(startCheck, endCheck),
            "The transactions are not in the check range ");

    }


    @Flaky // Note (Jmoravec): flaky on onChange event when renaming name/description
    @TmsLink("347685")
    @TmsLink("288098")
    @Story("Renaming Transactions")
    @Test(dataProvider = "Basic User")
    public void verifyViewSimilarButtonWorksWhenRenamingTransaction(D3User user) {
        D3Transaction transactionToSearch;
        String newTransactionName = getRandomString(10);
        do {
            transactionToSearch = user.getRandomTransaction();
            Assert.assertNotNull(transactionToSearch, "No Transactions found for the user");
        } while (transactionToSearch.isPending());

        TransactionsPage page = login(user).getHeader().clickTransactionsButton();

        int initialSize = page.searchForDescription(transactionToSearch.getName())
            .getNumberOfTransactionsShown();
        Assert.assertTrue(initialSize > 1, "Only one transaction was found");

        SingleTransactionPage singleTransactionPage = page.clickFirstPostedTransaction()
            .changeActiveTransactionName(newTransactionName);

        Assert.assertTrue(singleTransactionPage.isTextDisplayed(TRANSACTION_RENAMING_MSG));

        page = singleTransactionPage.clickViewSimilar()
            .selectAllTransactionsToBeRenamed()
            .clickSaveSelectedButton()
            .searchForDescription(newTransactionName);

        Assert.assertEquals(page.getNumberOfTransactionsShown(), initialSize, "Not all the transactions were renamed");
        String expectedActualId = TransactionDatabaseHelper.getRenamedTransactionId(newTransactionName);
        String auditId = AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.SETTINGS_RENAMING_RULE_ADD)
            .getOrDefault(AuditAttribute.SETTINGS_RENAMING_RULE_ID.getAttributeName(), "");

        Assert.assertNotNull(expectedActualId);
        Assert.assertFalse(expectedActualId.isEmpty()); // NOSONAR null check is above

        Assert.assertNotNull(auditId);
        Assert.assertFalse(auditId.isEmpty());

        Assert.assertEquals(expectedActualId, auditId, "Audit record not found");
    }

    @TmsLink("522438")
    @Story("Transactions")
    @Test(dataProvider = "Basic User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE_AUTOMATIC_DISPLAY)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE_FOR_POSTED_ONLY, enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE_PREFERRED_WITH_PENDING)
    @RunWithAccountProductAttribute(accountProduct = ProductType.DEPOSIT_CHECKING, attribute = AccountProductAttributes.PREFERRED_BALANCE, value = "BALANCE")
    public void validateRunningBalanceWithPreferredRunningBalanceSettingEnableAndAccountProductPreferredBalanceIsBalance(D3User user) {
        D3Account account = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        Dashboard page = login(user).getHeader().clickDashboardButton();

        String dashboardBalance = page.getAccountBalance(account.getName());
        Assert.assertEquals(dashboardBalance, DateAndCurrencyHelper.getAmountWithDollarSign(account.getBalanceStr()),
            "The dashboard balance is not equals to balance account");

        TransactionsPage transactionsPage = page.getHeader()
            .clickTransactionsButton()
            .filterByAccounts(Collections.singletonList(account));
        Assert.assertTrue(transactionsPage.runningBalanceIsCorrectForTheFirstPostedTransaction(dashboardBalance),
            "The running balance for first posted transaction is not correct");
        Assert.assertTrue(transactionsPage.isRunningBalanceCorrect(), "The running balance was not correct");

    }


    @Flaky
    @TmsLink("522457")
    @Story("Transactions")
    @Test(dataProvider = "Basic User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE_AUTOMATIC_DISPLAY)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE_FOR_POSTED_ONLY)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.TRANSACTIONS_RUNNING_BALANCE_PREFERRED_WITH_PENDING, enabled = false)
    @RunWithAccountProductAttribute(accountProduct = ProductType.DEPOSIT_CHECKING, attribute = AccountProductAttributes.PREFERRED_BALANCE, value = "AVAILABLE")
    public void validateRunningBalanceWithPreferredSettingDisablePostedOnlyEnableAndAccountProductPreferredBalanceIsAvailable(D3User user) {
        D3Account account = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);

        Dashboard page = login(user).getHeader().clickDashboardButton();
        String avaibleBalance = page.getAccountBalance(account.getName());
        Assert.assertEquals(avaibleBalance, DateAndCurrencyHelper.getAmountWithDollarSign(account.getAvailableBalanceStr()),
            "The dashboard balance is not the available balance");

        TransactionsPage transactionsPage = page.getHeader().clickTransactionsButton().filterByAccounts(Collections.singletonList(account));
        Assert.assertTrue(transactionsPage.runningBalanceIsCorrectForTheFirstPostedTransaction(avaibleBalance),
            "The running balance was not correct");
        Assert.assertTrue(transactionsPage.isRunningBalancePendingTransactionEmpty(), "Pending transactions are showing running balance");
        Assert.assertTrue(transactionsPage.isRunningBalanceCorrect(), "Available balance is not shown in transaction");
    }


    @TmsLink("306162")
    @Story("Transactions")
    @Test(dataProvider = "Basic User")
    public void validateInvalidSearchesAreUnsuccessfulAndDisplayLocalizedMessages(D3User user) {
        String invalidTerm = "QA";
        String endCheck = "1000";
        String startCheck = "1";
        DateTimeFormatter df = DateTimeFormat.forPattern("MM/dd/yyy");
        String pastDate = df.print(RandomHelper.getRandomPastDate());
        String futureDate = df.print(RandomHelper.getRandomFutureDate());

        TransactionsPage page = login(user).getHeader().clickTransactionsButton()
            .searchForDescription(invalidTerm);
        Assert.assertTrue(page.isTextDisplayed(TransactionsL10N.Localization.NO_TRANSACTIONS_AVAILABLE.getValue()), LOCALIZED_MSG_ERROR);

        page.clickDateRangeButton().enterStartDate(futureDate)
            .enterEndDate(pastDate).clickDateSearchButton();

        Assert.assertTrue(page.areDatesChanged(pastDate), "The dates were not changed");

        page.clickCheckRangeButton()
            .enterStartCheck(endCheck)
            .enterEndCheck(startCheck).clickSearchRangeButton();

        Assert.assertTrue(page.isTextDisplayed(String.format(TransactionsL10N.Localization.SEARCH_CHECK_RANGE_VALIDATE_RANGE.getValue(), endCheck)),
            LOCALIZED_MSG_ERROR);
        page.clickClearRangeButton();

        Assert.assertTrue(page.isTextDisplayed(TransactionsL10N.Localization.NO_TRANSACTIONS_AVAILABLE.getValue()), LOCALIZED_MSG_ERROR);
        Assert.assertTrue(page.areRangeInputsAllHidden(), "The check ranges are not hidden");
    }


    @Flaky // (Jmoravec) Note: Passing locally, failing in Jenkins consistently on the audit record
    @TmsLink("522545")
    @Story("Validate edit offline Transaction with audit and activity records")
    @Test(dataProvider = "Basic User With Offline Account Transaction")
    public void verifyEditOfflineTransactionWithAuditAndActivityRecordsGenerated(D3User user, D3Account account, D3Transaction offlineTxn) {
        String newTransactionName = getRandomString(10);

        login(user).getHeader()
            .clickTransactionsButton()
            .filterByAccounts(Collections.singletonList(account))
            .clickOnTransactionByName(offlineTxn.getName())
            .changeActiveTransactionName(newTransactionName)
            .changeMemoTransaction(getRandomString(10));

        String auditId = AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.TRANSACTION_UPDATE)
            .getOrDefault(AuditAttribute.DESCRIPTION.getAttributeName(), "");

        Assert.assertNotNull(auditId);
        Assert.assertFalse(auditId.isEmpty());
        Assert.assertEquals(auditId, newTransactionName);
    }

    @TmsLink("304955")
    @Story("Verify create offline account with  audit and event records")
    @Test(dataProvider = "Basic User with Offline Liability Account")
    public void verifyCreateOfflineTransactionWitAuditAndEventGenerated(D3User user, D3Account account) {
        D3Transaction transaction = D3Transaction.generateRandomTransaction();
        transaction.setMemo(getRandomString(10));
        transaction.setCheckNumber(getRandomNumberString(10, true));
        String category = TransactionDatabaseHelper.getRandomCategoryOfType(CategoryType.getIncomeCategoryTypes(), user.getProfile().getType().toString());

        //Create Offline Transaction
        TransactionsPage transactionPage = login(user).getHeader().clickTransactionsButton();
        transactionPage.filterByAccounts(Collections.singletonList(account))
            .clickAddTransaction()
            .fillAddTransactionForm(transaction)
            .openCategoryOptions()
            .categorizeTransaction(category)
            .clickSaveTransaction();
        Assert.assertTrue(transactionPage.isTextDisplayed(transaction.getMemo()));

        //Audit Assertions
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.OFFLINE_TRANSACTION_CREATE));
    }
}

