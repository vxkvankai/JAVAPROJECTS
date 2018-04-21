package com.d3.tests.consumer.core.settings;

import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.d3.tests.annotations.RunForSpecificAlerts;
import com.d3.tests.consumer.ConsumerTestBase;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class SettingsTestBase extends ConsumerTestBase {

    @DataProvider(name = "User with Category Added")
    public Object[][] getBasicUserWithCategory(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with Category Rule")
    public Object[][] getBasicUserWithCategoryRule(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "User with Renaming Rule")
    public Object[][] getBasicUserWithRenamingRule(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * Checks to see if the method has {@link RunForSpecificAlerts} annotation and will run test method once for any specified Consumer Alerts used
     * with the annotation. If {@link RunForSpecificAlerts} annotation is not found, it will run test method for ALL {@link ConsumerAlerts}
     *
     * @param method Test method
     * @return {D3User, D3Alert}
     */
    @DataProvider(name = "User with No Consumer Alerts")
    public Object[][] addSpecificConsumerAlertsForUser(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Add Categories For Specific Users")
    public Object[][] getSpecificUsersWithCategories(Method method) {
        return getDataFromSerializedFile(method);
    }

    /**
     * @return {D3User, AlertType}
     */
    @DataProvider(name = "Basic User with Alert Types")
    public Object[][] getUserWithAlertTypes(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Secondary User with Random Account and Service Access")
    public Object[][] getBasicSecondaryUserWithRandomAccess(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Secondary User with Specific Service Access and Account Permissions set the same for all accounts")
    public Object[][] getBasicSecondaryUserWithSpecificAccessAndPermissionsSetSameForAllAccounts(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Secondary User with Specific Service Access and Account Permissions set for single account")
    public Object[][] getBasicSecondaryUserWithSpecificAccessAndPermissionsSetForSingleAccount(Method method) {
        return getDataFromSerializedFile(method);
    }
}
