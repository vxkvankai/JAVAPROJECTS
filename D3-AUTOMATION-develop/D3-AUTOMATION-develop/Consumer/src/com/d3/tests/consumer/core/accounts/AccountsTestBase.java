package com.d3.tests.consumer.core.accounts;


import com.d3.tests.consumer.ConsumerTestBase;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;


public class AccountsTestBase extends ConsumerTestBase {

    static final String ENROLLMENT_CODE = "springboard";

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - different types}
     */
    @DataProvider(name = "Basic User with existing accounts")
    public Object[][] getBasicUserWithAccount(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User, D3Account - asset account, StopPaymentReason} - An "Average User" with a StopPaymentReason to choose for Single or Check Range
     * Stop Payments
     */
    @DataProvider(name = "User with Stop Payment Reasons")
    public Object[][] createStopPaymentWithReasons(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Create a basic user and hides one of their internal accounts Will return 3 users, and each have a different account type hidden (ex: Checking,
     * Savings, Credit Card)
     *
     * @return {D3User, D3Account - hidden account}
     */
    @DataProvider(name = "Basic User with hidden existing accounts")
    public Object[][] getBasicUserWithHiddenAccounts(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Create a basic user that has all their associated internal accounts hidden.
     *
     * @return {D3User}
     */
    @DataProvider(name = "Basic User with all internal accounts hidden")
    public Object[][] getBasicUserWithAllAccountsHidden(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - one of each AccountingClass type {@link com.d3.datawrappers.account.D3Account.AccountingClass}}
     */
    @DataProvider(name = "Basic User with Asset and Liability account")
    public Object[][] getBasicUserWithAssetAndLiabilityAccount(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - each asset account}
     */
    @DataProvider(name = "Basic User with existing Asset accounts")
    public Object[][] getBasicUserWithAssetAccounts(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - each liability account}
     */
    @DataProvider(name = "Basic User with existing Liability accounts")
    public Object[][] getBasicUserWithLiabilityAccounts(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new average user that has 1 internal transfer between a liability and asset account
     *
     * @return {D3User, D3Transfer}
     */
    @DataProvider(name = "Basic User with one time internal transfer")
    public Object[][] getBasicUserWithOneTimeInternalTransfer(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" enrolled in Estatements to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - an asset account}
     */
    @DataProvider(name = "Basic User Enrolled in Estatments")
    public Object[][] getBasicUserEnrolledInEstatements(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new average user that has an offline asset account
     *
     * @return {D3User, D3Account (Offline Account)}
     */
    @DataProvider(name = "Basic User with Offline Asset Account")
    public Object[][] getBasicUserWithOfflineAssetAccount(Method method) {
        return getDataFromSerializedFile(method);
    }
}
