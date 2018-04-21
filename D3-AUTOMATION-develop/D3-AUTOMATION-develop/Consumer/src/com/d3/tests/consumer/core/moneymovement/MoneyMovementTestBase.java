package com.d3.tests.consumer.core.moneymovement;

import com.d3.tests.consumer.ConsumerTestBase;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class MoneyMovementTestBase extends ConsumerTestBase {

    @DataProvider(name = "User with bill pay and recipients")
    public Object[][] getUserWithRecipients(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Bill Pay User with Bad new Recipients and no seeded")
    public Object[][] getBadRecipientData(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Basic User with Recurring Transfers")
    public Object[][] getRecurringTransfers(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with pay multiple transfers")
    public Object[][] getUserWithPayMultipleTransfers(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with pay multiple template created")
    public Object[][] getUserWithPayMultipleTemplate(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with pending single transfers")
    public Object[][] getUserWithPendingSingleTransfers(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with active e-billers")
    public Object[][] getUserWithActiveEbillers(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with active e-billers and auto pay data")
    public Object[][] getUserWithActiveEbillersAndAutoPayData(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with active e-billers enrolled in Auto Pay")
    public Object[][] getUserWithActiveEbillersEnrolledInAutoPay(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with not submitted Bill Pay transfer for current date")
    public Object[][] getUserWithNotSubmittedBillPayTransferForCurrentDate(Method method) {
        return getDataFromSerializedFile(method);
    }

}
