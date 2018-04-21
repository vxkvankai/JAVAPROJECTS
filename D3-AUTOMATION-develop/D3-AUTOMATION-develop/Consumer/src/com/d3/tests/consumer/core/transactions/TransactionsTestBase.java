package com.d3.tests.consumer.core.transactions;

import com.d3.tests.consumer.ConsumerTestBase;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;


public class TransactionsTestBase extends ConsumerTestBase {

    @DataProvider(name = "Basic User With Many Transactions")
    public Object[][] getBasicUserWithLotsOfTransactions(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Basic User With Posted Transaction")
    public Object[][] getBasicUserWithPostedTransaction(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Basic User with Image Transaction")
    public Object[][] getBasicUserWithAnImageTransaction(Method method) {
        return getDataFromSerializedFile(method);
    }

}
