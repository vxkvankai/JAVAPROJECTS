package com.d3.tests.dataproviders;

import static com.d3.datawrappers.user.UserFactory.averageBusinessUser;
import static com.d3.datawrappers.user.UserFactory.averageToggleUser;
import static com.d3.datawrappers.user.UserFactory.averageUser;
import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.api.helpers.banking.AccountApiHelper;
import com.d3.api.helpers.banking.MessageAlertApiHelper;
import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.database.RecipientMMDatabaseHelper;
import com.d3.database.TransactionDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.transfers.BillPayRecurringTransfer;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.DataHelper;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.RunForSpecificAlerts;
import com.d3.tests.annotations.UseCompany;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
public class CommonDataProviders extends DataProviderBase {
    private static final String ERROR_MSG = "Error getting the user data";
    private static final String NULL_RECIPIENT_MSG = "One of the recipients was null";

    private static Callable<D3User> getAverageUserThreaded(String fi, String nonToggleUrl, String apiVersion) {
        return () -> averageUser(fi, nonToggleUrl, apiVersion);
    }

    private static Callable<D3User> getAverageBusinessUserThreaded(String fi, String nonToggleUrl, String apiVersion) {
        return () -> averageBusinessUser(fi, nonToggleUrl, apiVersion);
    }

    private static Callable<D3User> getAverageToggleUserThreaded(String fi, ToggleMode toggleMode, String toggleUrl, String apiVersion) {
        return () -> averageToggleUser(fi, toggleMode, toggleUrl, apiVersion);
    }

    private static Object[][] allUserTypes(String fi, String toggleFi) {
        String nonToggleUrl = getConsumerBaseUrl(fi);
        String toggleUrl = getConsumerBaseUrl(toggleFi);
        String apiVersion = getConsumerApiVersion();

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<D3User> averageUser = executor.submit(getAverageUserThreaded(fi, nonToggleUrl, apiVersion));
        Future<D3User> businessUser = executor.submit(getAverageBusinessUserThreaded(fi, nonToggleUrl, apiVersion));
        Future<D3User> consumerToggle = executor.submit(getAverageToggleUserThreaded(toggleFi, ToggleMode.CONSUMER, toggleUrl, apiVersion));
        Future<D3User> businessToggle = executor.submit(getAverageToggleUserThreaded(toggleFi, ToggleMode.BUSINESS, toggleUrl, apiVersion));
        Future<D3User> noneToggle = executor.submit(getAverageToggleUserThreaded(fi, ToggleMode.NONE, nonToggleUrl, apiVersion));

        try {
            Object[][] allUsers = new Object[][] {
                {averageUser.get()},
                {businessUser.get()},
                {consumerToggle.get()},
                {businessToggle.get()},
                {noneToggle.get()}};

            for (Object[] allUser : allUsers) {
                if (allUser[0] == null) {
                    log.error("One of the users for toggle data provider was null");
                    return new Object[][] {{null}};
                }
            }
            return allUsers;
        } catch (InterruptedException e) {
            log.error("Thread was interrupted", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("Execution Error", e);
        }
        return new Object[][] {{null}};
    }

    /**
     * Generates a basic user
     */
    @D3DataProvider(name = "Basic User")
    public Object[][] basicUserDataProvider(Method method) {
        D3User user = createAverageUser(method);
        return user == null ? new Object[][] {{null}} : new Object[][] {{user}};
    }

    /**
     * Generates a new "average user" with invalid address to be used in tests
     *
     * @return D3User - An "Average User" with invalid address
     */
    @D3DataProvider(name = "Basic User with Invalid Address")
    public Object[][] getBasicUserWithInvalidAddress(Method method) {
        return new Object[][] {{createAverageUserWithInvalidAddress(method)}};
    }

    /**
     * Generates a new "average user" with no address to be used in tests
     *
     * @return D3User - An "Average User" with invalid address
     */
    @D3DataProvider(name = "Basic User with No Address")
    public Object[][] getBasicUserWithNoAddress(Method method) {
        return new Object[][] {{createAverageUserWithNoAddress(method)}};
    }

    /**
     * Generates a new "average user" without US address to be used in tests
     *
     * @return D3User - An "Average User" with invalid address
     */
    @D3DataProvider(name = "Basic User without US Address")
    public Object[][] getBasicUserWithoutUSAddress(Method method) {
        return new Object[][] {{createAverageUserWithoutUSAddress(method)}};
    }

    /**
     * Generates a new "Average Business User" to be used in tests User is only associated to business account from one company
     *
     * @return D3User - An "Average Business User"
     */
    @D3DataProvider(name = "Basic Business User")
    public Object[][] getBusinessUser(Method method) {
        return new Object[][] {{createAverageUser(method, true, false)}};
    }

    /**
     * Generates a new "Average Toggle User" to be used in tests User has both business and consumer accounts and is created on an FI that has toggle
     * enabled
     *
     * @return D3User - A "Toggle User" in Consumer Mode
     */
    @D3DataProvider(name = "Toggle User in Consumer Mode")
    public Object[][] getToggleUserInConsumerMode(Method method) {
        return new Object[][] {{createAverageUser(method, false, true)}};
    }

    /**
     * Generates a new "Average Toggle User" to be used in tests User has both business and consumer accounts and is created on an FI that has toggle
     * enabled
     *
     * @return D3User - A "Toggle User" in Business Mode
     */
    @D3DataProvider(name = "Toggle User in Business Mode")
    public Object[][] getToggleUserInBusinessMode(Method method) {
        return new Object[][] {{createAverageUser(method, true, true)}};
    }

    /**
     * Generates a new "Average commingled user" to be used in tests User has both business and consumer accounts and is created on an FI that has
     * toggle disabled
     *
     * @return D3User - An "Average Commingled User"
     */
    @D3DataProvider(name = "Basic Commingled User")
    public Object[][] getCommingledUser(Method method) {

        return new Object[][] {{createCoMingledUser(method)}};
    }

    /**
     * Generates a new basic user that has bill pay activated
     *
     * @return D3User
     */
    @D3DataProvider(name = "Basic User With Bill Pay")
    public Object[][] getBasicUserWithBillPay(Method method) {
        return new Object[][] {{createBillPayUser(method)}};
    }

    /**
     * Generates a new user with bill pay activated and recipients (of all types) already added to the user
     *
     * @return D3User, Recipient
     */
    @D3DataProvider(name = "User with bill pay and already added recipients and no seeded")
    public Object[][] getUserWithBPAndRecipients(Method method) {
        return getUserWithAddedRecipients(method, false, false);
    }

    /**
     * Generates all user types for test to be run against
     *
     * @return D3User - A Primary Consumer User, Primary Business User, Toggle User in Consumer Mode, Toggle User in Business Mode, and a Commingled
     * User
     */
    @D3DataProvider(name = "Get All User Types")
    public Object[][] getAllUserTypes(Method method) {
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";
        String toggleFi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).toggleFi() : "fi2";
        return allUserTypes(fi, toggleFi);
    }

    /**
     * Generates users listed with the @RunForUserType annotation on the test method
     *
     * @return user types defined by the method
     */
    @D3DataProvider(name = "Get Specific User Types")
    public Object[][] getSpecificUsersDataProvider(Method method) {
        return getSpecificUsers(method);
    }

    @D3DataProvider(name = "Specific User with Bill Pay")
    public Object[][] getSpecificUsersWithBillPay(Method method) {
        Object[][] specificUsers = getSpecificUsers(method);
        Object[][] userWithBillPay = new Object[specificUsers.length][];
        for (int i = 0; i < specificUsers.length; ++i) {
            D3User user = (D3User) specificUsers[i][0];
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(getConsumerApiURLFromProperties(user.getCuID()));

            D3Account checkingAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
                ProductType.BUSINESS_DEPOSIT_CHECKING) : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
            Integer accountId = UserDatabaseHelper.waitForUserAccountId(user, checkingAccount);
            if (accountId == null) {
                return new Object[][] {{null, null}};
            }

            try {
                api.login(user);
                if (user.getToggleMode().equals(ToggleMode.BUSINESS)) {
                    api.switchToggleToBusinessMode();
                }
                api.enrollInBillPay(user, accountId);
            } catch (D3ApiException e) {
                log.error("Error enrolling in bill pay", e);
                return new Object[][] {{null}};
            }

            userWithBillPay[i] = new Object[] {user};
        }
        return userWithBillPay;
    }

    @D3DataProvider(name = "Specific User with Bill Pay And Recipients")
    public Object[][] getSpecificUsersWithBillPayAndRecipients(Method method) {
        Object[][] specificUsers = getSpecificUsers(method);
        Object[][] userWithBillPayAndRecipients = new Object[specificUsers.length][];
        List<Recipient> recipients;

        for (int i = 0; i < specificUsers.length; ++i) {
            D3User user = (D3User) specificUsers[i][0];

            D3Account checkingAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(ProductType.BUSINESS_DEPOSIT_CHECKING)
                : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
            Integer accountId = UserDatabaseHelper.waitForUserAccountId(user, checkingAccount);

            if (accountId == null) {
                return new Object[][] {{null}};
            }

            MoneyMovementApiHelper api = new MoneyMovementApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
            try {
                api.login(user);
                if (user.getToggleMode().equals(ToggleMode.BUSINESS)) {
                    api.switchToggleToBusinessMode();
                }
                api.enrollInBillPay(user, accountId);
            } catch (D3ApiException e) {
                log.warn("User might already be enrolled in bill pay", e);
            }

            try {
                recipients = Recipient.createAllRecipients();
                api.addRecipientsToUser(user, recipients, true);
            } catch (D3ApiException e) {
                log.error("Error generating the data", e);
                return new Object[][] {{null}};
            }

            userWithBillPayAndRecipients[i] = new Object[] {user};
        }
        return userWithBillPayAndRecipients;
    }

    /**
     * Generate a user with recurring transfers for each account
     *
     * @param method Test method
     * @return {D3User, RecurringTransfer}
     */
    @D3DataProvider(name = "Basic User with Recurring Transfers By Accounts")
    public Object[][] getUserWithRecurringTransfers(Method method) {
        D3User user = createBillPayUser(method);
        if (user == null) {
            log.error("Error creating user (user object was null)");
            return new Object[][] {{null, null}};
        }
        List<Recipient> recipients;
        List<RecurringTransfer> transfers = new ArrayList<>();
        List<D3Account> accounts;

        try {
            recipients = Recipient.createAllRecipients();
            if (recipients == null) {
                log.error("Error creating recipients (recipients object was null)");
                return new Object[][] {{null, null}};
            }
            String baseUrl = getConsumerApiURLFromProperties(user.getCuID());
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(baseUrl);

            api.login(user);
            api.addRecipientsToUser(user, recipients, true);
            accounts = user.getAccounts();

            for (D3Account account : accounts) {
                Recipient recipient = recipients.get(accounts.indexOf(account));
                RecurringTransfer transfer;
                if (account.isAsset()) {
                    transfer = recipient.isBillPay() ? BillPayRecurringTransfer.createRandomTransferDetails(recipient, account)
                        : StandardRecurringTransfer.createRandomTransferDetails(recipient, account);
                    log.info("Creating transfer: {}", transfer.toString()); //NOSONAR
                    transfer.addToApi(user, api);
                    transfers.add(transfer);
                }
            }
        } catch (D3ApiException e) {
            log.error("Issue creating test data: user with Recurring Transfers", e);
            return new Object[][] {{null, null}};
        }

        if (transfers.isEmpty()) {
            log.error("Error generating test data, transfers object was empty");
            return new Object[][] {{null, null}};
        }

        return addSingleObjectForEachElementInList(user, transfers);
    }

    /**
     * Will add {@link ConsumerAlerts} through the API a created user. Checks to see if the method has {@link RunForSpecificAlerts} annotation and
     * will add any specified Consumer Alerts used with the annotation. If {@link RunForSpecificAlerts} annotation is not found, it will add all
     * {@link ConsumerAlerts} through the API for the user
     *
     * @param method Test method
     * @return {D3Alert}
     */
    @D3DataProvider(name = "User with Specific Consumer Alerts Added")
    public Object[][] getUserWithConsumerAlertsAdded(Method method) {
        Object[][] nullData = new Object[][] {{null, null}};
        List<Class<D3Alert>> alertList = getD3AlertsFromAnnotation(method);
        List<D3ScheduledJobs> specificAlertTrigger = getD3AlertsTriggerJobFromAnnotation(method);
        if (alertList.isEmpty()) {
            log.error("Error getting alert types to add (empty array)"); //NOSONAR
            return nullData;
        }

        List<D3User> users = new ArrayList<>();

        for (int i = 0; i < alertList.size(); ++i) {
            users.add(createUserWithBillPayAndBudget(method));
        }

        List<D3Alert> alertsToAdd = D3Alert.createAllConsumerAlerts(users, alertList);
        if (alertsToAdd == null) {
            return nullData;
        }

        //Filter alerts by specific trigger job if listed in @RunForSpecificAlerts method annotation
        if(!specificAlertTrigger.isEmpty()) {
            alertsToAdd = alertsToAdd.stream().filter(alert -> specificAlertTrigger.contains(alert.jobTrigger())).collect(Collectors.toList());
        }

        List<D3Alert> alertsToUpdate = new ArrayList<>();

        for (D3Alert alert : alertsToAdd) {
            alertsToUpdate.add(alert.copy());
        }

        //Set frequency to DAYS in order for alert to be triggered
        alertsToUpdate.forEach(alert -> {
            if (alert.hasFrequency()) {
                alert.setFrequency(AlertFrequency.DAYS);
            }
        });

        for (D3Alert alert : alertsToAdd) {
            D3User user = alert.getUser();
            String baseUrl = getConsumerApiURLFromProperties(user.getCuID());
            MessageAlertApiHelper api = new MessageAlertApiHelper(baseUrl);
            try {
                api.login(user);
                api.addAlert(user, alert);
            } catch (D3ApiException e) {
                log.error("Issue adding user alert", e);
                return nullData;
            }
        }

        return DataHelper.convert1DListTo2DArray(alertsToUpdate);
    }

    /**
     * Creates a user with all recipient types, then returns a transfer associated with each of those recipients. This does not send the transfer to
     * the server.
     *
     * @return {{D3User, SingleTransfer}}
     */
    @D3DataProvider(name = "User with not submitted transfers")
    public Object[][] getUserWithNotCreatedTransfers(Method method) {
        Object[][] data = getUserWithAddedRecipients(method, true, false);
        Object[][] nullData = new Object[][] {{null, null}};

        if (data == null || data[0][0] == null) {
            log.error(ERROR_MSG);
            return nullData;
        }

        List<SingleTransfer> transfers = new ArrayList<>();
        D3User user = (D3User) data[0][0];

        RecipientMMDatabaseHelper.setAllEndpointsActiveForUser(user);

        D3Account fromAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        if (fromAccount == null) {
            log.error("User did not have a deposit checking account");
            return nullData;
        }

        // create internal transfer
        transfers.add(SingleTransfer.createRandomTransfer(user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS), fromAccount));

        for (Object[] aData : data) {
            Recipient recipient = (Recipient) aData[1];
            if (recipient == null) {
                log.error(NULL_RECIPIENT_MSG);
                return nullData;
            }

            transfers.add(SingleTransfer.createRandomTransfer(recipient, fromAccount));
        }

        return addSingleObjectForEachElementInList(user, transfers);
    }

    @D3DataProvider(name = "User with pending single transfers")
    public Object[][] getUserWithCreatedPendingSingleTransfers(Method method) {
        Object[][] data = getUserWithNotCreatedTransfers(method);

        Object[][] nullData = new Object[][] {{null, null}};
        if (data == null || data[0][0] == null) {
            log.error(ERROR_MSG);
            return nullData;
        }

        for (Object[] aData : data) {
            SingleTransfer transfer = (SingleTransfer) aData[1];
            transfer.setScheduledDate(RandomHelper.getRandomFutureDate());
            D3User user = (D3User) aData[0];
            try {
                transfer.addToApi(user, getConsumerApiURLFromProperties(user.getCuID()));
            } catch (D3ApiException e) {
                log.error("Error adding transfer to api", e);
                return nullData;
            }
        }

        return data;
    }

    /**
     * Creates a user with an offline account and adds a transaction for that account through the api
     *
     * @return {D3User, D3Account (offline), D3Transaction (offline account transaction)}
     */
    @D3DataProvider(name = "Basic User With Offline Account Transaction")
    public Object[][] getBasicUserWithOfflineAccountTransaction(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        if (user == null) {
            return nullData;
        }
        D3Account offlineAccount = D3Account.generateOfflineAccount(user, ProductType.LIABILITY);

        D3Transaction offlineTxn = D3Transaction.generateRandomTransaction();
        offlineTxn.setMemo(RandomHelper.getRandomString(10));
        offlineTxn.setCheckNumber(RandomHelper.getRandomNumberString(4, false));

        String category = TransactionDatabaseHelper.getRandomCategoryName(user.getProfile().getType().toString());

        AccountApiHelper api = new AccountApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
        try {
            api.login(user);
            api.addOfflineAccount(offlineAccount);
            api.addOfflineAccountTransaction(user, offlineTxn, offlineAccount, category);
        } catch (D3ApiException e) {
            log.error("Error adding offline account transaction through the api", e);
            return nullData;
        }

        return new Object[][] {{user, offlineAccount, offlineTxn}};
    }

    /**
     * Creates a user with all recipient types, then returns a transfer scheduled for the current date associated with each of those recipients.
     * This does not send the transfer to the server.
     *
     * @param method TestNG passed reflected method
     * @return {{D3User, SingleTransfer}}
     */
    @D3DataProvider(name = "User with not submitted transfers current date")
    public Object[][] getUserWithNotCreatedTransfersCurrentDate(Method method) {
        Object[][] data = getUserWithNotCreatedTransfers(method);
        Object[][] nullData = new Object[][] {{null, null}};
        D3User user = (D3User) data[0][0];
        List<SingleTransfer> transfers = new ArrayList<>();
        for (Object[] aData : data) {
            D3Transfer transfer = (D3Transfer) aData[1];
            if (transfer == null) {
                log.error(NULL_RECIPIENT_MSG);
                return nullData;
            }
            if (!transfer.getToAccount().isBillPay()) {
                transfer.setScheduledDate(DateTime.now());
                transfers.add((SingleTransfer) transfer);
            }
        }
        return addSingleObjectForEachElementInList(user, transfers);
    }

    /**
     * Creates a user with all recipient types, then returns a recurring transfer associated with each of those recipients. This does not send the
     * transfer to the server.
     *
     * @return {{D3User, SingleTransfer}}
     */
    @D3DataProvider(name = "User with not submitted recurring transfers")
    public Object[][] getUserWithNotCreatedRecurringTransfers(Method method) {
        Object[][] data = getUserWithAddedRecipients(method, true, false);
        Object[][] nullData = new Object[][] {{null, null}};

        if (data == null || data[0][0] == null) {
            log.error(ERROR_MSG);
            return nullData;
        }

        List<RecurringTransfer> transfers = new ArrayList<>();
        D3User user = (D3User) data[0][0];

        RecipientMMDatabaseHelper.setAllEndpointsActiveForUser(user);

        D3Account fromAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        if (fromAccount == null) {
            log.error("User did not have a deposit checking account");
            return nullData;
        }

        // create internal recurring transfer
        transfers.add(StandardRecurringTransfer.createRandomTransferDetails(user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS), fromAccount));

        for (Object[] aData : data) {
            Recipient recipient = (Recipient) aData[1];
            if (recipient == null) {
                log.error(NULL_RECIPIENT_MSG);
                return nullData;
            }
            transfers.add(recipient.isBillPay() ? BillPayRecurringTransfer.createRandomTransferDetails(recipient, fromAccount)
                : StandardRecurringTransfer.createRandomTransferDetails(recipient, fromAccount));
        }

        return addSingleObjectForEachElementInList(user, transfers);
    }


    /**
     * Creates a user with recipients eligible for expedited payments, then returns a single transfer for each of those recipients.
     * This does not send the transfers to the server.
     *
     * @return {{D3User, SingleTransfer}}
     */
    @D3DataProvider(name = "User with not submitted expedited single transfers")
    public Object[][] getUserWithExpeditedSingleTransfers(Method method) {
        D3User user = createBillPayUser(method);
        if (user == null) {
            log.error("Error generating user for test data (user object was null)");
            return new Object[][] {{null, null}};
        }

        List<Recipient> recipients = Recipient.createAllRecipients(false, true)
            .stream()
            .filter(Recipient::eligibleForExpedite)
            .collect(Collectors.toList());
        List<SingleTransfer> transfers = new ArrayList<>();
        String apiUrl = getConsumerApiURLFromProperties(getFi(method, false));

        for (Recipient recipient : recipients) {
            SingleTransfer transfer = SingleTransfer.createRandomTransfer(recipient,
                Objects.requireNonNull(user.getAssetAccounts()).get(ThreadLocalRandom.current().nextInt(user.getAssetAccounts().size())));
            transfer.setProviderOption(recipient.getWho() == RecipientWho.PERSON ? ProviderOption.OVERNIGHT_CHECK : ProviderOption.EXPEDITED_PAYMENT);
            transfers.add(transfer);
        }

        try {
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(apiUrl);
            api.login(user);
            api.addRecipientsToUser(user, recipients);

        } catch (D3ApiException e) {
            log.error("Error adding recipients to user: ", e);
            return new Object[][] {{null, null}};
        }

        return addSingleObjectForEachElementInList(user, transfers);
    }

    /**
     * Creates a user with bill pay, then returns a transfer scheduled for the current date for a bill pay recipient
     * This does not send the transfer to the server.
     *
     * @param method TestNG passed reflected method
     * @return {{D3User, D3Transfer}}
     */
    @D3DataProvider(name = "User with not submitted Bill Pay transfer for current date")
    public Object[][] getUserWithCurrentDateBillPayTransfer(Method method) {
        Object[][] data = getUserWithNotCreatedTransfers(method);
        Object[][] nullData = new Object[][] {{null, null}};
        D3User user = (D3User) data[0][0];
        D3Transfer transfer = null;
        for (Object[] aData : data) {
            D3Transfer testTransfer = (D3Transfer) aData[1];
            if (testTransfer == null) {
                log.error(NULL_RECIPIENT_MSG);
                return nullData;
            }
            if (testTransfer.getToAccount().isBillPay()) {
                testTransfer.setScheduledDate(DateTime.now());
                transfer = testTransfer;
                break;
            }
        }

        if (transfer == null) {
            return nullData;
        }
        return new Object[][] {{user, transfer}};
    }
}
