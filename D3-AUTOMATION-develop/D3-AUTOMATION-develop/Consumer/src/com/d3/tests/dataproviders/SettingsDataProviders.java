package com.d3.tests.dataproviders;

import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.api.helpers.banking.BudgetApiHelper;
import com.d3.api.helpers.banking.SettingsApiHelper;
import com.d3.api.helpers.banking.TransactionApiHelper;
import com.d3.api.mappings.users.AccountServiceGroups;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.alerts.AlertType;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.UserFactory;
import com.d3.datawrappers.user.enums.AccountPermissions;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.datawrappers.user.enums.UserServices;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.DataHelper;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.RunForSpecificAlerts;
import com.d3.tests.annotations.UseCompany;
import com.d3.tests.annotations.WithSpecificServiceAndPermissions;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
/*
 * The common data providers for Settings tests
 */
@Slf4j
public class SettingsDataProviders extends DataProviderBase {

    private static final String WITH_ACCESS = "WITH_ACCESS";
    private static final String WITHOUT_ACCESS = "WITHOUT_ACCESS";
    private static final String ERROR_ADDING_SEC_USER_MSG = "Error adding secondary user: %s for primary user: %s";

    /**
     * Get Map of UserServices from the WithSpecificServiceAndPermissions annotation that a Secondary User should and should not have access to If
     * annotation isn't present, return empty map
     *
     * @param method to check annotation for
     * @return Map of UserServices
     */
    private static Map<String, List<UserServices>> getUserServicesFromAnnotation(Method method) {
        Map<String, List<UserServices>> userServices = new HashMap<>();
        if (method.isAnnotationPresent(WithSpecificServiceAndPermissions.class)) {
            userServices.put(WITH_ACCESS, Arrays.asList(method.getAnnotation(WithSpecificServiceAndPermissions.class).withUserServices()));
            userServices.put(WITHOUT_ACCESS, Arrays.asList(method.getAnnotation(WithSpecificServiceAndPermissions.class).withoutUserServices()));
        }
        return userServices;
    }

    /**
     * Get Map of AccountPermissions from the WithSpecificServiceAndPermissions annotation that a Secondary User should and should not have access to
     * If annotation isn't present, return empty map
     *
     * @param method to check annotation for
     * @return Map of AccountPermissions
     */
    private static Map<String, List<AccountPermissions>> getUserAccountPermissionsFromAnnotation(Method method) {
        Map<String, List<AccountPermissions>> accountPermissions = new HashMap<>();
        if (method.isAnnotationPresent(WithSpecificServiceAndPermissions.class)) {
            accountPermissions.put(WITH_ACCESS, Arrays.asList(method.getAnnotation(WithSpecificServiceAndPermissions.class).withAccountPermissions()));
            accountPermissions.put(WITHOUT_ACCESS, Arrays.asList(method.getAnnotation(WithSpecificServiceAndPermissions.class).withoutAccountPermissions()));
        }
        return accountPermissions;
    }

    @D3DataProvider(name = "User with Category Added")
    public Object[][] getBasicUserWithCategory(Method method) {
        D3User user = null;
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";

        try {
            user = UserFactory.averageUserWithCategory(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            log.error("Error creating an Average user with Categories", e);
        }

        return new Object[][] {{user}};
    }

    @D3DataProvider(name = "User with Category Rule")
    public Object[][] getBasicUserWithCategoryRule(Method method) {
        D3User user = null;
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";

        try {
            user = UserFactory.averageUserWithCategoryRule(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            log.error("Error creating an Average User with Category Rules", e);
        }

        return new Object[][] {{user}};
    }

    @D3DataProvider(name = "User with Renaming Rule")
    public Object[][] getBasicUserWithRenamingRule(Method method) {
        D3User user = null;
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";

        try {
            user = UserFactory.averageUserWithRenamingRule(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            log.error("Error creating a user with renaming rules", e);
        }

        return new Object[][] {{user}};
    }

    /**
     * Checks to see if the method has {@link RunForSpecificAlerts} annotation and will run test method once for any specified Consumer Alerts used
     * with the annotation. If {@link RunForSpecificAlerts} annotation is not found, it will run test method for ALL {@link ConsumerAlerts}
     *
     * @param method Test method
     * @return {D3Alert}
     */
    @D3DataProvider(name = "User with No Consumer Alerts")
    public Object[][] addSpecificConsumerAlertsForUser(Method method) {
        List<D3User> users = new ArrayList<>();
        Object[][] nullData = new Object[][] {{null, null}};
        List<Class<D3Alert>> specificAlerts = getD3AlertsFromAnnotation(method);
        for (int i = 0; i < specificAlerts.size(); ++i) {
            users.add(createUserWithBillPayAndBudget(method));
        }

        List<D3Alert> consumerAlerts = D3Alert.createAllConsumerAlerts(users, specificAlerts);

        if (consumerAlerts == null || consumerAlerts.isEmpty()) {
            return nullData;
        }

        return DataHelper.convert1DListTo2DArray(consumerAlerts);
    }

    @D3DataProvider(name = "Add Categories For Specific Users")
    public Object[][] getSpecificUsersWithCategories(Method method) {
        Object[][] specificUsers = getSpecificUsers(method);
        Object[][] usersWithCategories = new Object[specificUsers.length][];
        for (int i = 0; i < specificUsers.length; ++i) {
            D3User user = (D3User) specificUsers[i][0];

            TransactionApiHelper api = new TransactionApiHelper(getConsumerApiURLFromProperties(getFi(method, true)));
            try {
                api.login(user);
                if (user.getToggleMode().equals(ToggleMode.BUSINESS)) {
                    api.switchToggleToBusinessMode();
                }
                api.addCategory(user);
            } catch (D3ApiException e) {
                log.error("Error generating users with categories", e);
                return new Object[][] {{null}};
            }
            usersWithCategories[i] = new Object[] {user};
        }
        return usersWithCategories;
    }

    @D3DataProvider(name = "Basic User with Alert Types")
    public Object[][] getUserWithAlertTypes(Method method) {
        D3User user = createAverageUser(method);

        return user == null ? new Object[][] {{null}} : addSingleObjectForEachElementInList(user, AlertType.values());
    }

    /**
     * Creates a Secondary User through the api with random Banking Service and Account Permissions
     *
     * @param method Test Method
     * @return {D3SecondaryUser secondaryUser}
     */
    @D3DataProvider(name = "Secondary User with Random Account and Service Access")
    public Object[][] getBasicSecondaryUserWithRandomAccess(Method method) {
        D3User user = createBillPayUser(method);
        Object[][] nullData = new Object[][] {{null, null}};
        if (user == null) {
            log.error("Error generating user for test data (user object was null): {}", method);
            return nullData;
        }

        D3SecondaryUser secondaryUser = D3SecondaryUser.createUserWithRandomServicesAndPermissions(user, user.getRandomAccounts());
        String apiUrl = getConsumerApiURLFromProperties(getFi(method, false));
        try {
            SettingsApiHelper api = new SettingsApiHelper(apiUrl, AccountServiceGroups.deserializer);
            api.login(user);
            api.addSecondaryUser(secondaryUser);

        } catch (D3ApiException e) {
            log.error(String.format(ERROR_ADDING_SEC_USER_MSG, secondaryUser.getLogin(), secondaryUser.getPrimaryUser().getLogin()));
            return nullData;
        }
        return new Object[][] {{secondaryUser}};
    }


    /**
     * Creates a Secondary User through the api with specific Banking Service and Account Permissions that the user should have Will set specific
     * account permissions the same for ALL accounts of secondary user NOTE: Using this dataprovider requires @WithSpecificServiceAndPermissions
     * annotation to be on test method
     *
     * @param method Test Method
     * @return {D3SecondaryUser secondaryUser}
     */
    @D3DataProvider(name = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public Object[][] getBasicSecondaryUserWithSpecificAccessAndPermissionsSetSameForAllAccounts(Method method) {
        Object[][] secondaryUserData = createSecondaryUserData(method);
        Object[][] nullData = new Object[][] {{null, null}};
        if (secondaryUserData == nullData) {
            log.error("Error generating secondary user for test data (user object was null), {}", method);
            return nullData;
        }
        D3SecondaryUser secondaryUser = (D3SecondaryUser) secondaryUserData[0][0];

        secondaryUser.withServices(getUserServicesFromAnnotation(method).get(WITH_ACCESS))
            .withoutServices(getUserServicesFromAnnotation(method).get(WITHOUT_ACCESS))
            .withAccountPermissions(secondaryUser.getAccounts(), getUserAccountPermissionsFromAnnotation(method).get(WITH_ACCESS))
            .withoutAccountPermissions(secondaryUser.getAccounts(), getUserAccountPermissionsFromAnnotation(method).get(WITHOUT_ACCESS));

        String apiUrl = getConsumerApiURLFromProperties(getFi(method, false));
        try {
            SettingsApiHelper api = new SettingsApiHelper(apiUrl, AccountServiceGroups.deserializer);
            api.login(secondaryUser.getPrimaryUser());
            api.addSecondaryUser(secondaryUser);
            if (secondaryUser.getServices().contains(UserServices.BUDGET)) {
                new BudgetApiHelper(api).createBudget();
            }

        } catch (D3ApiException e) {
            log.error(String.format(ERROR_ADDING_SEC_USER_MSG, secondaryUser.getLogin(), secondaryUser.getPrimaryUser().getLogin()));
            return nullData;
        }
        return new Object[][] {{secondaryUser}};
    }


    /**
     * Creates a Secondary User through the api with specific Banking Service and Account Permissions that the user should have Will set specific
     * account permissions for only 1 account of secondary user Ex: @WithSpecificServiceAndPermissions annotation has withoutAccountPermissions =
     * {AccountPermissions.STATEMENTS, AccountPermissions.TRANSACTIONS} This would set one account without Statements & Transactions, and the other
     * account with Statements & Transactions NOTE: Using this dataprovider requires @WithSpecificServiceAndPermissions annotation to be on test
     * method
     *
     * @param method Test Method
     * @return {D3SecondaryUser secondaryUser, D3Account account (the account with & without specific account permissions)}
     */
    @D3DataProvider(name = "Secondary User with Specific Service Access and Account Permissions set for single account")
    public Object[][] getBasicSecondaryUserWithSpecificAccessAndPermissionsSetForSingleAccount(Method method) {
        Object[][] secondaryUserData = createSecondaryUserData(method);
        Object[][] nullData = new Object[][] {{null, null}};
        if (secondaryUserData == nullData) {
            log.error("Error generating secondary user for test data (user object was null): {}", method);
            return nullData;
        }
        D3SecondaryUser secondaryUser = (D3SecondaryUser) secondaryUserData[0][0];
        D3Account firstAccount = secondaryUser.getAccounts().get(0);
        D3Account secondAccount = secondaryUser.getAccounts().get(1);

        secondaryUser.withServices(getUserServicesFromAnnotation(method).get(WITH_ACCESS))
            .withoutServices(getUserServicesFromAnnotation(method).get(WITHOUT_ACCESS))
            .withAccountPermissions(firstAccount, getUserAccountPermissionsFromAnnotation(method).get(WITH_ACCESS))
            .withoutAccountPermissions(firstAccount, getUserAccountPermissionsFromAnnotation(method).get(WITHOUT_ACCESS))
            //Set second account to the opposite of the first account
            .withAccountPermissions(secondAccount, getUserAccountPermissionsFromAnnotation(method).get(WITHOUT_ACCESS))
            .withoutAccountPermissions(secondAccount, getUserAccountPermissionsFromAnnotation(method).get(WITH_ACCESS));

        String apiUrl = getConsumerApiURLFromProperties(getFi(method, false));
        try {
            SettingsApiHelper api = new SettingsApiHelper(apiUrl, AccountServiceGroups.deserializer);
            api.login(secondaryUser.getPrimaryUser());
            api.addSecondaryUser(secondaryUser);

        } catch (D3ApiException e) {
            log.error(String.format(ERROR_ADDING_SEC_USER_MSG, secondaryUser.getLogin(), secondaryUser.getPrimaryUser().getLogin()));
            return nullData;
        }
        return new Object[][] {{secondaryUser, firstAccount}};
    }


    /**
     * Creates D3SecondaryUser data with random account permissions and service access if the test method has @WithSpecificServiceAndPermissions
     * annotation and values set NOTE: Permissions and Services will be modified in the dataproviders calling this method, based on values set in
     * annotation This does not create the secondary user, just the data
     *
     * @param method Test method
     * @return D3SecondaryUser data with random access if @WithSpecificServiceAndPermissions annotation is set on method and has values set, otherwise
     * return null object
     */
    private Object[][] createSecondaryUserData(Method method) {
        Object[][] nullData = new Object[][] {{null, null}};
        //If @WithSpecificServiceAndPermissions not on test method, return null
        if (getUserServicesFromAnnotation(method).isEmpty()) {
            log.error("This @Dataprovider needs @WithSpecificServiceAndPermissions annotation "
                + "to be able to set Banking Service and Account Permissions: {}", method);
            return nullData;
        }

        boolean noServices = getUserServicesFromAnnotation(method).entrySet().stream().allMatch(map -> map.getValue().isEmpty());
        boolean noPermissions = getUserAccountPermissionsFromAnnotation(method).entrySet().stream().allMatch(map -> map.getValue().isEmpty());

        //If @WithSpecificServiceAndPermissions is on test method but no Banking Services or Account Permission are specified, return null
        if (noServices && noPermissions) {
            log.error("@WithSpecificServiceAndPermissions annotation "
                + "is used on test method but no services or permissions listed to assign to user: {}", method);
            return nullData;
        }
        D3User user = createBillPayUser(method);

        if (user == null) {
            log.error("Error generating user for test data (user object was null): {}", method);
            return nullData;
        }

        return new Object[][] {{D3SecondaryUser.createUserWithRandomServicesAndPermissions(user, user.getRandomAccounts())}};

    }

}
