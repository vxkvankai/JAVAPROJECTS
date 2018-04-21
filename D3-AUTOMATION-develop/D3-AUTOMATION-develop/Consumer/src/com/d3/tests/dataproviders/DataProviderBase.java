package com.d3.tests.dataproviders;

import static com.d3.datawrappers.user.UserFactory.averageBusinessUser;
import static com.d3.datawrappers.user.UserFactory.averageToggleUser;
import static com.d3.datawrappers.user.UserFactory.averageUser;
import static com.d3.helpers.DataHelper.convertListTo2DArray;
import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.api.helpers.banking.AccountApiHelper;
import com.d3.api.helpers.banking.BudgetApiHelper;
import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.common.D3ScheduledJobs;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.GoalType;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.datawrappers.goals.SavingsGoal;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.UserFactory;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.exceptions.D3DatabaseException;
import com.d3.helpers.ConduitHelper;
import com.d3.helpers.UserDataCreator;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.RunForSpecificAlerts;
import com.d3.tests.annotations.RunForUserTypes;
import com.d3.tests.annotations.UseCompany;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@Slf4j
public abstract class DataProviderBase {

    /**
     * Creates an average consumer user and does some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    static D3User createAverageUser(Method method) {
        return createAverageUser(method, false, false);
    }

    /**
     * Get D3Alerts from the RunForSpecificAlerts annotation. If annotation isn't present, return all alert types
     * @param method to check annotation for
     * @return Array of Classes
     */
    @SuppressWarnings("unchecked")
    public static List<Class<D3Alert>> getD3AlertsFromAnnotation(Method method) {
        if (method.isAnnotationPresent(RunForSpecificAlerts.class) && method.getAnnotation(RunForSpecificAlerts.class).d3AlertType().length != 0) {
            return Arrays.asList(method.getAnnotation(RunForSpecificAlerts.class).d3AlertType());
        } else {
            return new ArrayList(getAllAlerts());
        }
    }

    /**
     * Get D3ScheduledJobs types to filter alerts by from the RunForSpecificAlerts annotation. If annotation isn't present, return empty list
     * @param method to check annotation for
     * @return Array of Classes
     */
    @SuppressWarnings("unchecked")
    static List<D3ScheduledJobs> getD3AlertsTriggerJobFromAnnotation(Method method) {
        if (method.isAnnotationPresent(RunForSpecificAlerts.class)) {
            return Arrays.asList(method.getAnnotation(RunForSpecificAlerts.class).d3AlertTrigger());
        }
        return Collections.emptyList();
    }

    /**
     * Get all the alert types
     *
     * @return A Set of all the classes that inherit from D3Alert.class
     */
    public static Set<Class<? extends D3Alert>> getAllAlerts() {
        return new Reflections(D3Alert.class).getSubTypesOf(D3Alert.class);
    }

    /**
     * Creates an average user and does some basic error checking
     *
     * @param method TestNG method
     * @param isBusiness set to true to create a Business user, false to create a Consumer
     */
    @CheckForNull
    static D3User createAverageUser(Method method, boolean isBusiness, boolean isToggle) {
        String fi = getFi(method, isToggle);
        String consumerBaseUrl = getConsumerBaseUrl(fi);
        String apiVersion = getConsumerApiVersion();

        try {
            if (isToggle) {
                if (isBusiness) {
                    return averageToggleUser(fi, ToggleMode.BUSINESS, consumerBaseUrl, apiVersion);
                } else {
                    return averageToggleUser(fi, ToggleMode.CONSUMER, consumerBaseUrl, apiVersion);
                }
            } else if (isBusiness) {
                return averageBusinessUser(fi, consumerBaseUrl, apiVersion);
            } else {
                return averageUser(fi, consumerBaseUrl, apiVersion);
            }
        } catch (Exception e) {
            log.error("Error creating a basic user", e);
            return null;
        }
    }

    static String getFi(Method method, boolean isToggle) {
        if (isToggle) {
            return method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).toggleFi() : "fi2";
        } else {
            return method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";
        }
    }

    /**
     * Creates an Object[][] array where the given object is attached to each element in the list The most common example of using this is having the
     * same user be used in all of the tests, but a different data element (like a recipient) that is iterated on. Make sure that user and list are
     * NOT null before passing them into this method
     */
    static Object[][] addSingleObjectForEachElementInList(@Nonnull Object object, @Nonnull List list) {
        Object[][] data = new Object[list.size()][];
        for (int i = 0; i < list.size(); ++i) {
            data[i] = new Object[] {object, list.get(i)};
        }
        return data;
    }

    /**
     * Creates an Object[][] array where the given object is attached to each element in the list The most common example of using this is having the
     * same user be used in all of the tests, but a different data element (like a recipient) that is iterated on. Make sure that user and list are
     * NOT null before passing them into this method
     *
     * @param object Object to add to each element in the list
     * @param list array to add each object to
     * @return 2d object array with first element always being object
     */
    static Object[][] addSingleObjectForEachElementInList(@Nonnull Object object, @Nonnull Object[] list) {
        return addSingleObjectForEachElementInList(object, Arrays.asList(list));
    }

    private static Pair<UserType, D3User> pairUserType(UserType type, String fi, String toggleFi) throws ConduitException {
        D3User user = null;

        String nonToggleBaseApiUrl = getConsumerBaseUrl(fi);
        String toggleBaseApiUrl = getConsumerBaseUrl(toggleFi);
        String apiVersion = getConsumerApiVersion();

        switch (type) {
            case PRIMARY_CONSUMER_USER:
                user = averageUser(fi, nonToggleBaseApiUrl, apiVersion);
                break;
            case PRIMARY_BUSINESS_USER:
                user = averageBusinessUser(fi, nonToggleBaseApiUrl, apiVersion);
                break;
            case PRIMARY_CONSUMER_TOGGLE:
                user = averageToggleUser(toggleFi, ToggleMode.CONSUMER, toggleBaseApiUrl, apiVersion);
                break;
            case PRIMARY_BUSINESS_TOGGLE:
                user = averageToggleUser(toggleFi, ToggleMode.BUSINESS, toggleBaseApiUrl, apiVersion);
                break;
            case COMMINGLED:
                user = averageToggleUser(fi, ToggleMode.NONE, nonToggleBaseApiUrl, apiVersion);
                break;
        }

        if (user == null) {
            log.error("Issue generating user for type: {}", type);
            throw new ConduitException("Error generating user");
        }

        user.setUserType(type);
        return Pair.with(type, user);
    }

    /**
     * Creates an average consumer user with an invalid address and does some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    D3User createAverageUserWithInvalidAddress(Method method) {
        String fi = getFi(method, false);
        D3User user = UserFactory.generateAverageUser(fi, false);
        user.getProfile().getPhysicalAdd().setPostalCode("00000");
        return UserFactory.modifiedUser(user, getConsumerBaseUrl(fi), getConsumerApiVersion());
    }

    /**
     * Creates an average consumer user without an address and does some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    D3User createAverageUserWithNoAddress(Method method) {
        String fi = getFi(method, false);
        D3User user = UserFactory.generateAverageUser(fi, false);
        user.getProfile().setPhysicalAdd(null);
        user.getProfile().setMailingAdd(null);
        return UserFactory.modifiedUser(user, getConsumerBaseUrl(fi), getConsumerApiVersion());
    }

    /**
     * Creates an average consumer user without a US address and does some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    D3User createAverageUserWithoutUSAddress(Method method) {
        String fi = getFi(method, false);
        D3User user = UserFactory.generateAverageUser(fi, false);
        user.getProfile().getPhysicalAdd().setState("LL");
        return UserFactory.modifiedUser(user, getConsumerBaseUrl(fi), getConsumerApiVersion());
    }

    /**
     * Creates an average co mingled user and does some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    D3User createCoMingledUser(Method method) {
        String fi = getFi(method, false);

        try {
            return averageToggleUser(fi, ToggleMode.NONE, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            log.error("Error creating a co-mingled user", e);
            return null;
        }
    }

    /**
     * Creates an average user with bill pay and does some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    D3User createBillPayUser(Method method) {
        String fi = getFi(method, false);

        D3User user = UserFactory.generateAverageUser(fi, false);
        if (!ConduitHelper.sendUserToServerAndWait(user, getConsumerBaseUrl(fi), getConsumerApiVersion())) {
            return null;
        }

        try {
            new UserDataCreator(user, getConsumerBaseUrl(user.getCuID()), getConsumerApiVersion())
                .enrollInBillPay(user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING));
        } catch (D3ApiException | D3DatabaseException e) {
            log.error("Error creating a bill pay user", e);
            return null;
        }

        return user;
    }

    /**
     * Creates an average user with a budget with some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    D3User createUserWithBudget(Method method) {
        String fi = getFi(method, false);

        D3User user = UserFactory.generateAverageUser(fi, false);
        if (!ConduitHelper.sendUserToServerAndWait(user, getConsumerBaseUrl(fi), getConsumerApiVersion())) {
            return null;
        }

        try {
            new UserDataCreator(user, getConsumerBaseUrl(user.getCuID()), getConsumerApiVersion())
                .createBudget();
        } catch (D3ApiException e) {
            log.error("Error creating a user with a budget", e);
            return null;
        }

        return user;
    }

    /**
     * Creates an average user with bill pay and a budget with some basic error checking
     *
     * @param method TestNG method
     */
    @CheckForNull
    D3User createUserWithBillPayAndBudget(Method method) {
        String fi = getFi(method, false);

        D3User user = UserFactory.generateAverageUser(fi, false);
        if (!ConduitHelper.sendUserToServerAndWait(user, getConsumerBaseUrl(fi), getConsumerApiVersion())) {
            return null;
        }

        try {
            new UserDataCreator(user, getConsumerBaseUrl(user.getCuID()), getConsumerApiVersion())
                .enrollInBillPay(user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING))
                .createBudget();
        } catch (D3ApiException | D3DatabaseException e) {
            log.error("Error creating a user enrolled in bill pay with a budget", e);
            return null;
        }

        return user;
    }

    /**
     * Helper method to get user with added recipients, but have a choice to get
     *
     * @param method TestNG Method
     * @param includeSeeded Set to true to return recipient list with a seeded bill pay recipient, false otherwise
     * @return {{D3User, Recipient}}
     */
    @CheckForNull
    Object[][] getUserWithAddedRecipients(Method method, boolean includeSeeded, boolean includeExpedited) {
        D3User user = createBillPayUser(method);
        if (user == null) {
            log.error("Error generating user for test data (user object was null): {}", method);
            return new Object[][] {{null, null}};
        }

        List<Recipient> recipients = Recipient.createAllRecipients(includeSeeded, includeExpedited);
        String apiUrl = getConsumerApiURLFromProperties(getFi(method, false));

        try {
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(apiUrl);
            api.login(user);
            api.addRecipientsToUser(user, recipients);

        } catch (D3ApiException e) {
            log.error("Error adding recipients to user: ", e);
            return new Object[][] {{null, null}};
        }

        return addSingleObjectForEachElementInList(user, recipients);

    }

    private Object[][] specificUserTypes(String fi, String toggleFi, UserType[] users) {
        Object[][] userTypes = new Object[users.length][];
        int index = 0;
        ExecutorService service = Executors.newCachedThreadPool();
        ArrayList<Future<Pair<UserType, D3User>>> userThreaded = new ArrayList<>();

        for (UserType type : users) {
            userThreaded.add(service.submit(() -> pairUserType(type, fi, toggleFi)));
        }

        for (Future<Pair<UserType, D3User>> userThread : userThreaded) {
            try {
                userTypes[index++] = new Object[] {userThread.get().getValue1()};
            } catch (InterruptedException e) {
                log.error("Thread was interrupted while making specific user types", e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                log.error("Execution error making specific users", e);
            }
        }
        return userTypes;
    }

    Object[][] getSpecificUsers(Method method) {
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";
        String toggleFi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).toggleFi() : "fi2";
        UserType[] types =
            method.isAnnotationPresent(RunForUserTypes.class) ? method.getAnnotation(RunForUserTypes.class).userTypes()
                : new UserType[] {};
        return specificUserTypes(fi, toggleFi, types);
    }

    /**
     * Generates a new "average user" to be used in tests
     *
     * @return {D3User - An "Average User", D3Account - different types}
     */
    @D3DataProvider(name = "Basic User with existing accounts")
    public Object[][] getBasicUserWithAccount(Method method) {
        D3User user = createAverageUser(method);
        if (user == null) {
            return new Object[][] {{null, null}};
        }
        return addSingleObjectForEachElementInList(user, user.getAccounts());
    }

    /**
     * Create a basic user that has all their associated internal accounts excluded. (Also Creates a budget)
     *
     * @return {D3User, D3Account}
     */
    @D3DataProvider(name = "Basic User with excluded existing accounts")
    public Object[][] getBasicUserWithExcludedAccounts(Method method) {
        Object[][] data = getBasicUserWithAccount(method);
        Object[][] errorData = new Object[][] {{null, null}};
        if (data[0][0] == null) {
            log.error("Error creating user with accounts: {}", method);
            return data;
        }

        // This assumes only one user is given
        D3User user = (D3User) data[0][0];
        String url = getConsumerApiURLFromProperties(getFi(method, user.isToggleUser()));

        for (Object[] aData : data) {

            AccountApiHelper api = new AccountApiHelper(url);
            try {
                api.login(user);
                api.excludeAccount(((D3Account) aData[1]).getName());
            } catch (D3ApiException e) {
                log.error("Error excluding account", e);
                return errorData;
            }

        }

        BudgetApiHelper budgetApi = new BudgetApiHelper(url);
        try {
            budgetApi.login(user);
            budgetApi.createBudget();
        } catch (D3ApiException e) {
            log.warn("Error creating budget. Not stopping tests as budget may already have been created for this user", e);
        }
        return data;
    }


    /**
     * Returns a basic user with accounts and goals associated with the asset accounts (Adds one goal of each type: Retirement, Savings)
     *
     * @return {{D3User, D3Account, D3Goal}}
     */
    @D3DataProvider(name = "Basic User with Goals")
    public Object[][] getBasicUserWithGoals(Method method) {
        Object[][] data = getBasicUserWithAccount(method);
        if (data == null || data[0][0] == null) {
            log.error("Wasn't able to create the user or add goals: {}", method);
            return new Object[][] {{null, null, null}};
        }

        D3User user = (D3User) data[0][0];

        BudgetApiHelper api = new BudgetApiHelper(getConsumerApiURLFromProperties(getFi(method, false)));

        // login to api
        try {
            api.login(user);
        } catch (D3ApiException e) {
            log.error("Issue logging into the api", e);
            return new Object[][] {{null, null, null}};
        }

        List<GoalType> types = new ArrayList<>(Arrays.asList(GoalType.values()));
        List<List<Object>> newData = new ArrayList<>();
        GoalType goalType;
        // Add a new goal for each asset account
        for (Object[] aData : data) {
            D3Account fundingAccount = (D3Account) aData[1];
            if (fundingAccount.isAsset()) {
                D3Goal savingsGoal = SavingsGoal.createRandomGoal(user, fundingAccount);
                D3Goal retirementGoal = RetirementGoal.createRandomGoal(user, fundingAccount);

                goalType = types.get(0);
                types.remove(types.get(0));

                D3Goal testGoal = goalType == GoalType.SAVINGS ? savingsGoal : retirementGoal;
                log.info("Adding test Goal: {}", testGoal);
                try {
                    api.addGoal(user, testGoal);
                } catch (D3ApiException e) {
                    log.error("Issue adding goal", e);
                    return new Object[][] {{null, null, null}};
                }
                List<Object> innerData = new ArrayList<>();
                innerData.add(user);
                innerData.add(fundingAccount);
                innerData.add(testGoal);

                newData.add(innerData);
            }
        }
        Object[][] finalData = convertListTo2DArray(newData);
        if (finalData == null) {
            return new Object[][] {{null, null, null}};
        }
        return finalData;
    }
}
