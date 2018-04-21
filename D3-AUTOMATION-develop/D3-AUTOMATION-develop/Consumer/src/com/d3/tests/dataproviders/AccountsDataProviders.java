package com.d3.tests.dataproviders;

import static com.d3.helpers.RandomHelper.getRandomElementFromList;
import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;

import com.d3.api.helpers.banking.AccountApiHelper;
import com.d3.database.AttributeDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.account.stoppayment.StopPaymentReason;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.tests.annotations.D3DataProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AccountsDataProviders extends DataProviderBase {

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User, D3Account - asset account, StopPaymentReason} - An "Average User" with a StopPaymentReason to choose for Single or Check Range
     * Stop Payments
     */
    @D3DataProvider(name = "User with Stop Payment Reasons")
    public Object[][] createStopPaymentWithReasons(Method method) {
        D3User user = createAverageUser(method);

        if (user == null || user.getAssetAccounts() == null) {
            return new Object[][] {{null, null}};
        }

        Object[][] data = new Object[StopPaymentReason.values().length][];
        for (int j = 0; j < StopPaymentReason.values().length; ++j) {
            data[j] = new Object[] {user, user.getAssetAccounts().get(0), StopPaymentReason.values()[j]};
        }
        return data;
    }

    /**
     * Create a basic user and hides one of their internal accounts Will return 3 users, and each have a different account type hidden (ex: Checking,
     * Savings, Credit Card)
     *
     * @return {D3User, D3Account - hidden account}
     */
    @D3DataProvider(name = "Basic User with hidden existing accounts")
    public Object[][] getBasicUserWithHiddenAccounts(Method method) {
        List<D3User> users = new ArrayList<>();
        users.add(createAverageUser(method));
        users.add(createAverageUser(method));
        users.add(createAverageUser(method));

        if (users.get(0) == null) {
            return new Object[][] {{null, null}};
        }

        Object[][] data = new Object[users.size()][];
        for (D3User user : users) {
            AccountApiHelper api = new AccountApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
            try {
                api.login(user);
                api.hideAccount(user.getAccounts().get(users.indexOf(user)).getName());
            } catch (D3ApiException e) {
                log.error("Error hiding account", e);
            }
            data[users.indexOf(user)] = new Object[] {user, user.getAccounts().get(users.indexOf(user))};
        }

        return data;
    }

    /**
     * Create a basic user that has all their associated internal accounts hidden.
     *
     * @return {D3User}
     */
    @D3DataProvider(name = "Basic User with all internal accounts hidden")
    public Object[][] getBasicUserWithAllAccountsHidden(Method method) {
        Object[][] data = getBasicUserWithAccount(method);
        if (data[0][0] == null) {
            log.error("Error creating user with accounts");
            return data;
        }

        D3User user = (D3User) data[0][0];

        for (Object[] aData : data) {
            AccountApiHelper api = new AccountApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
            try {
                api.login((D3User) aData[0]);
                api.hideAccount(((D3Account) aData[1]).getName());
            } catch (D3ApiException e) {
                log.error("Error hiding account", e);
            }
        }

        return new Object[][] {{user}};
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - one of each AccountingClass type {@link com.d3.datawrappers.account.D3Account.AccountingClass}}
     */
    @D3DataProvider(name = "Basic User with Asset and Liability account")
    public Object[][] getBasicUserWithAssetAndLiabilityAccount(Method method) {
        D3User user = createAverageUser(method);

        return user != null ? new Object[][] {
            {user, user.getFirstAccountByAccountingClass(D3Account.AccountingClass.ASSET)},
            {user, user.getFirstAccountByAccountingClass(D3Account.AccountingClass.LIABILITY)}}
            : new Object[][] {{null, null}};
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - each asset account}
     */
    @D3DataProvider(name = "Basic User with existing Asset accounts")
    public Object[][] getBasicUserWithAssetAccounts(Method method) {
        D3User user = createAverageUser(method);
        return user != null ? addSingleObjectForEachElementInList(user, user.getAssetAccounts()) : new Object[][] {{null, null}};
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - each liability account}
     */
    @D3DataProvider(name = "Basic User with existing Liability accounts")
    public Object[][] getBasicUserWithLiabilityAccounts(Method method) {
        D3User user = createAverageUser(method);
        return user != null ? addSingleObjectForEachElementInList(user, user.getLiabilityAccounts()) : new Object[][] {{null, null}};
    }

    /**
     * Generates a new average user that has 1 internal transfer between a liability and asset account
     *
     * @return {D3User, D3Transfer}
     */
    @D3DataProvider(name = "Basic User with one time internal transfer")
    public Object[][] getBasicUserWithOneTimeInternalTransfer(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        if (user == null) {
            return nullData;
        }

        SingleTransfer transfer = SingleTransfer.createRandomTransfer(user.getFirstAccountByType(ProductType.CREDIT_CARD),
            user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING));
        try {
            transfer.addToApi(user, getConsumerApiURLFromProperties(user.getCuID()));
        } catch (D3ApiException e) {
            log.error("Error adding transfer to the api", e);
            return nullData;
        }

        return new Object[][] {{user, transfer}};
    }

    /**
     * Generates a new "average user" enrolled in Estatements to be used in tests
     *
     * @return {D3User - An "Average User", D3Accounts - Random asset accounts}
     */
    @D3DataProvider(name = "Basic User Enrolled in Estatments")
    public Object[][] getBasicUserEnrolledInEstatements(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        if (user == null) {
            return nullData;
        }

        List<D3Account> accounts = user.getAssetAccounts();
        if (accounts == null) {
            return nullData;
        }

        try {
            AccountApiHelper api = new AccountApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
            api.login(user);
            for (D3Account account : accounts) {
                Integer accountId = UserDatabaseHelper.waitForUserAccountId(user, account);
                api.enrollInEstatements(user, accountId);
            }

        } catch (D3ApiException e) {
            log.error("Error enrolling in Estatements through the api", e);
            return nullData;
        }

        return new Object[][] {{user, getRandomElementFromList(accounts)}};
    }

    /**
     * Generates a new average user that has an offline liability account
     *
     * @return {D3User, D3Account (Offline Account)}
     */
    @D3DataProvider(name = "Basic User with Offline Liability Account")
    public Object[][] getBasicUserWithOfflineLiabilityAccount(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        if (user == null) {
            return nullData;
        }
        D3Account offlineAccount = D3Account.generateOfflineAccount(user, ProductType.LIABILITY);
        AccountApiHelper api = new AccountApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
        try {
            api.login(user);
            api.addOfflineAccount(offlineAccount);
        } catch (D3ApiException e) {
            log.error("Error adding offline liability account through the api", e);
            return nullData;
        }

        return new Object[][] {{user, offlineAccount}};
    }

    /**
     * Generates a new average user that has an offline asset account
     *
     * @return {D3User, D3Account (Offline Account)}
     */
    @D3DataProvider(name = "Basic User with Offline Asset Account")
    public Object[][] getBasicUserWithOfflineAssetAccount(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        if (user == null) {
            return nullData;
        }
        D3Account offlineAccount = D3Account.generateOfflineAccount(user, ProductType.ASSET);
        AccountApiHelper api = new AccountApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
        try {
            api.login(user);
            api.addOfflineAccount(offlineAccount);
        } catch (D3ApiException e) {
            log.error("Error adding offline asset account through the api", e);
            return nullData;
        }

        return new Object[][] {{user, offlineAccount}};
    }
}
