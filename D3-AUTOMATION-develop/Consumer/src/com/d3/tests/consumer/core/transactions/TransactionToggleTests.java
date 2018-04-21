package com.d3.tests.consumer.core.transactions;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.D3UserProfile;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.tests.annotations.RunForUserTypes;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

@Epic("Transactions")
@Feature("Transactions")
public class TransactionToggleTests extends TransactionsTestBase {

    @Story("Available User Accounts")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyAvailableAccountsToUser(D3User user) {
        Dashboard dashboard = login(user);
        TransactionsPage transactions = dashboard.getHeader().clickTransactionsButton();
        Assert.assertTrue(transactions.areAvailableAccountsCorrect(DatabaseUtils.getMaskedUserAccounts(user)));
    }

    @Flaky
    @Story("Account Used On Transaction")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyAccountsUsedOnDisplayedTransactions(D3User user) {
        Dashboard dashboard = login(user);
        TransactionsPage transactions = dashboard.getHeader().clickTransactionsButton();
        Assert.assertTrue(transactions.areCorrectTransactionsDisplayed(DatabaseUtils.getMaskedUserAccounts(user)));
    }

    @Flaky
    @Story("Consumer Categories Available")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_CONSUMER_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyConsumerCategoriesDisplayForConsumerAccounts(D3User user) {
        String accountName = DatabaseUtils.getRandomAccountWithPostedTransaction(user, D3UserProfile.ProfileType.CONSUMER);
        List<D3Account> account = user.getAccounts().stream().filter(accounts -> accounts.getName().equals(accountName)).collect(Collectors.toList());

        Dashboard dashboard = login(user);
        TransactionsPage transactions =
                dashboard.getHeader().clickTransactionsButton().filterByAccounts(account).clickFirstPostedTransaction().clickCategoryName();
        Assert.assertTrue(transactions.areCorrectCategoriesDisplayed(account.get(0)));
    }


    @Story("Business Categories Available")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBusinessCategoriesAreAvailableForBusinessAccounts(D3User user) {
        String accountName = DatabaseUtils.getRandomAccountWithPostedTransaction(user, D3UserProfile.ProfileType.BUSINESS);
        List<D3Account> account = user.getAccounts().stream().filter(accounts -> accounts.getName().equals(accountName)).collect(Collectors.toList());

        Dashboard dashboard = login(user);
        TransactionsPage transactions =
                dashboard.getHeader().clickTransactionsButton().filterByAccounts(account).clickFirstPostedTransaction().clickCategoryName();
        Assert.assertTrue(transactions.areCorrectCategoriesDisplayed(account.get(0)));
    }

}
