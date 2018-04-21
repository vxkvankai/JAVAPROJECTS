package com.d3.tests.dataproviders;

import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.api.helpers.banking.TransactionApiHelper;
import com.d3.datawrappers.alerts.AlertType;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.UserFactory;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.DataHelper;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.RunForSpecificAlerts;
import com.d3.tests.annotations.UseCompany;
import com.d3.tests.consumer.core.settings.SettingsTestBase;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SettingsDataProviders extends DataProviderBase {

    @D3DataProvider(name = "User with Category Added")
    public Object[][] getBasicUserWithCategory(Method method) {
        D3User user = null;
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";

        try {
            user = UserFactory.averageUserWithCategory(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            logger.error("Error creating an Average user with Categories", e);
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
            logger.error("Error creating an Average User with Category Rules", e);
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
            logger.error("Error creating a user with renaming rules", e);
        }

        return new Object[][] {{user}};
    }

    /**
     * Checks to see if the method has {@link RunForSpecificAlerts} annotation and will run test method once
     * for any specified Consumer Alerts used with the annotation.
     * If {@link RunForSpecificAlerts} annotation is not found, it will run test method for ALL {@link ConsumerAlerts}
     * @param method Test method
     * @return {D3Alert}
     *
     */
    @D3DataProvider(name = "User with No Consumer Alerts")
    public Object[][] addSpecificConsumerAlertsForUser(Method method) {
        List<D3User> users = new ArrayList<>();
        List<Class<D3Alert>> specificAlerts = getD3AlertsFromAnnotation(method);
        for (int i = 0; i < specificAlerts.size(); ++i) {
            users.add(createAverageUser(method));
        }

        List<D3Alert> consumerAlerts = D3Alert.createAllConsumerAlerts(users, specificAlerts);

        if (consumerAlerts == null || consumerAlerts.isEmpty()) {
            return new Object[][] {{null, null}};
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
                LoggerFactory.getLogger(SettingsTestBase.class).error("Error generating users with categories");
                return new Object[][] {{null}};
            }
            usersWithCategories[i] = new Object[] {user};
        }
        return usersWithCategories;
    }

    @D3DataProvider(name = "Basic User with Alert Types")
    public Object[][] getUserWithAlertTypes(Method method) {
        D3User user = createAverageUser(method);
        return addSingleObjectForEachElementInList(user, AlertType.values());
    }

}
