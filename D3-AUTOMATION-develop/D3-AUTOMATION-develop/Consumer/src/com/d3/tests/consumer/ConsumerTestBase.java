package com.d3.tests.consumer;

import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.d3.tests.TestBase;
import com.d3.tests.annotations.DataRequiresCompanyAttributes;
import com.d3.tests.annotations.RunForSpecificAlerts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.javatuples.Pair;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
public abstract class ConsumerTestBase extends TestBase {

    private ConcurrentHashMap<String, Pair<Object[][], String>> testData;

    @CheckForNull
    protected Object[][] getDataFromSerializedFile(Method method) {
        if (testData == null) {
            setTestData();
        }

        // check if still null
        if (testData == null) {
            throw new NullPointerException("testDataObject is still null");
        }

        String methodName = method.getClass().getName() + "." + method.getName();

        // Allow the test itself to rerun the data provider (can be used if company attributes need to be set for conduit processing of the file
        if (method.isAnnotationPresent(DataRequiresCompanyAttributes.class)) {
            log.info("Running dataprovider and company attributes due to @DataRequiresCompanyAttributes, {}", method);
            addCompanyAttribute(method);
            return rerunDataProvider(testData.get(methodName).getValue1(), method);
        }

        Pair<Object[][], String> data = testData.get(methodName);
        if (data == null) {
            log.warn("No data at all for {}, can't try the dataprovider again", methodName);
            return null;
        }

        Object[][] firstTryData = data.getValue0();
        // If some of the data is null, rerun the data provider
        if (firstTryData == null || firstTryData[0][0] == null) {
            try {
                log.warn("The original data was sent as null: {}. Trying again", (Object) firstTryData);
                return rerunDataProvider(testData.get(methodName).getValue1(), method);
            } catch (Exception e) {
                log.error("Error calling data provider second time", e);
            }
        }

        return firstTryData;
    }

    private void setTestData() {
        if (testDataFilename == null || testDataFilename.isEmpty()) {
            testDataFilename = System.getProperty("testDataFilename");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(testDataFilename))) {
            //noinspection unchecked
            testData = (ConcurrentHashMap<String, Pair<Object[][], String>>) ois.readObject();
        } catch (FileNotFoundException e) {
            log.error("{} was not found when reading test data", testDataFilename, e);
        } catch (IOException e) {
            log.error("Error initializing stream for file: {}", testDataFilename, e);
        } catch (ClassNotFoundException e) {
            log.error("Error reading test data file: {}", testDataFilename, e);
        }
    }

    /**
     * Rerun a data provider given its name
     *
     * @param classMethodName name of the class + method in format "className#methodName"
     * @param testMethod Method to run the data provider for
     */
    @Nullable
    private Object[][] rerunDataProvider(@Nonnull String classMethodName, Method testMethod) {
        try {
            Class dataProviderClass = Class.forName(StringUtils.substringBefore(classMethodName, "#"));
            //noinspection unchecked
            Method dataProviderMethod = dataProviderClass.getMethod(StringUtils.substringAfter(classMethodName, "#"), Method.class);
            log.warn("Retrying data provider: {} for test method: {}", classMethodName, testMethod.getName());
            return (Object[][]) dataProviderMethod.invoke(dataProviderClass.newInstance(), testMethod);
        } catch (InstantiationException | InvocationTargetException e) {
            log.error("Error trying to run data provider again", e);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            log.error("Couldn't find data provider method: {}", classMethodName, e);
        } catch (IllegalAccessException e) {
            log.error("Couldn't access dataProvider", e);
        }
        return null;
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return D3User - An "Average User"
     */
    @DataProvider(name = "Basic User")
    public Object[][] getBasicUser(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" with invalid address to be used in tests
     *
     * @return D3User - An "Average User" with invalid address
     */
    @DataProvider(name = "Basic User with Invalid Address")
    public Object[][] getBasicUserWithInvalidAddress(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" with no address to be used in tests
     *
     * @return D3User - An "Average User" with invalid address
     */
    @DataProvider(name = "Basic User with No Address")
    public Object[][] getBasicUserWithNoAddress(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "average user" without US address to be used in tests
     *
     * @return D3User - An "Average User" with invalid address
     */
    @DataProvider(name = "Basic User without US Address")
    public Object[][] getBasicUserWithoutUSAddress(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "Average Business User" to be used in tests User is only associated to business account from one company
     *
     * @return D3User - An "Average Business User"
     */
    @DataProvider(name = "Basic Business User")
    public Object[][] getBusinessUser(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "Average Toggle User" to be used in tests User has both business and consumer accounts and is created on an FI that has toggle
     * enabled
     *
     * @return D3User - A "Toggle User" in Consumer Mode
     */
    @DataProvider(name = "Toggle User in Consumer Mode")
    public Object[][] getToggleUserInConsumerMode(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new "Average Toggle User" to be used in tests User has both business and consumer accounts and is created on an FI that has toggle
     * enabled
     *
     * @return D3User - A "Toggle User" in Business Mode
     */
    @DataProvider(name = "Toggle User in Business Mode")
    public Object[][] getToggleUserInBusinessMode(Method method) {
        return getDataFromSerializedFile(method);
    }


    /**
     * Generates a new "Average commingled user" to be used in tests User has both business and consumer accounts and is created on an FI that has
     * toggle disabled
     *
     * @return D3User - An "Average Commingled User"
     */
    @DataProvider(name = "Basic Commingled User")
    public Object[][] getCommingledUser(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new basic user that has bill pay activated
     *
     * @return D3User
     */
    @DataProvider(name = "Basic User With Bill Pay")
    public Object[][] getBasicUserWithBillPay(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new user with bill pay activated and recipients (of all types) already added to the user
     *
     * @return D3User, Recipient
     */
    @DataProvider(name = "User with bill pay and already added recipients and no seeded")
    public Object[][] getUserWithBPAndRecipients(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates all user types for test to be run against
     *
     * @return D3User - A Primary Consumer User, Primary Business User, Toggle User in Consumer Mode, Toggle User in Business Mode, and a Commingled
     * User
     */
    @DataProvider(name = "Get All User Types")
    public Object[][] getAllUserTypes(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates users listed with the @RunForUserType annotation on the test method
     *
     * @return user types defined by the method
     */
    @DataProvider(name = "Get Specific User Types")
    public Object[][] getSpecificUsers(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Specific User with Bill Pay")
    public Object[][] getSpecificUsersWithBillPay(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Specific User with Bill Pay And Recipients")
    public Object[][] getSpecificUsersWithBillPayAndRecipients(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generates a new average user that has an offline liability account
     *
     * @return {D3User, D3Account (Offline Account)}
     */
    @DataProvider(name = "Basic User with Offline Liability Account")
    public Object[][] getBasicUserWithOfflineLiabilityAccount(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Generate a user with recurring transfers for each account
     *
     * @param method Test method
     * @return {D3User, RecurringTransfer}
     */
    @DataProvider(name = "Basic User with Recurring Transfers By Accounts")
    public Object[][] getUserWithRecurringTransfers(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Will add {@link ConsumerAlerts} through the API a created user. Checks to see if the method has {@link RunForSpecificAlerts} annotation and
     * will add any specified Consumer Alerts used with the annotation. If {@link RunForSpecificAlerts} annotation is not found, it will add all
     * {@link ConsumerAlerts} through the API for the user
     *
     * @param method Test method
     * @return {D3User, D3Alert}
     */
    @DataProvider(name = "User with Specific Consumer Alerts Added")
    public Object[][] getUserWithConsumerAlertsAdded(Method method) {
        return getDataFromSerializedFile(method);
    }


    /**
     * Creates a user with all recipient types, then returns a transfer associated with each of those recipients. This does not send the transfer to
     * the server.
     *
     * @return {{D3User, SingleTransfer}}
     */
    @DataProvider(name = "User with not submitted transfers")
    public Object[][] getUserWithNotCreatedTransfers(Method method) {
        return getDataFromSerializedFile(method);
    }


    @DataProvider(name = "Basic User With Offline Account Transaction")
    public Object[][] getBasicUserWithOfflineAccountTransaction(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Create a basic user that has all their associated internal accounts excluded. (Also Creates a budget)
     *
     * @return {D3User, D3Account}
     */
    @DataProvider(name = "Basic User with excluded existing accounts")
    public Object[][] getBasicUserWithExcludedAccounts(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Creates a user with all recipient types, then returns a transfer associated with each of those recipients. This does not send the transfer to
     * the server.
     *
     * @return {{D3User, SingleTransfer}}
     */
    @DataProvider(name = "User with not submitted transfers current date")
    public Object[][] getUserWithNotCreatedTransfersCurrentDate(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Creates a user with all recipient types, then returns a recurring transfer associated with each of those recipients. This does not send the
     * transfer to the server.
     *
     * @return {{D3User, SingleTransfer}}
     */
    @DataProvider(name = "User with not submitted recurring transfers")
    public Object[][] getUserWithNotCreatedRecurringTransfers(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Returns a basic user with accounts and goals associated with the asset accounts (Adds 1 Savings and 1 Retirement goal)
     *
     * @return {{D3User, D3Account, D3Goal}}
     */
    @DataProvider(name = "Basic User with Goals")
    public Object[][] getBasicUserWithGoals(Method method) {
        return getDataFromSerializedFile(method);
    }


    /**
     * Creates a user with recipients eligible for expedited payments, then returns a single transfer for each of those recipients.
     * This does not send the transfers to the server.
     *
     * @return {{D3User, SingleTransfer}}
     */
    @DataProvider(name = "User with not submitted expedited single transfers")
    public Object[][] getUserWithExpeditedSingleTransfers(Method method) {
        return getDataFromSerializedFile(method);
    }

}
