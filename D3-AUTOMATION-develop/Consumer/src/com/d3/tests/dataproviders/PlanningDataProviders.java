package com.d3.tests.dataproviders;

import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.api.helpers.banking.BudgetApiHelper;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.GoalType;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.datawrappers.goals.SavingsGoal;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.UserFactory;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3ApiException;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.UseCompany;

import java.lang.reflect.Method;

public class PlanningDataProviders extends DataProviderBase {

    @D3DataProvider(name = "Basic User With Budget")
    public Object[][] getBasicUserWithBudget(Method method) {
        D3User user = null;
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";

        try {
            user = UserFactory.averageUserWithBudget(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            logger.error("Issue creating an Average User with a budget");
        }

        return new Object[][] {{user}};
    }

    @D3DataProvider(name = "Specific User with Goal")
    public Object[][] getSpecificUsersWithGoal(Method method) {
        Object[][] specificUsers = getSpecificUsers(method);
        Object[][] userWithGoal = new Object[specificUsers.length][];
        for (int i = 0; i < specificUsers.length; ++i) {
            D3User user = (D3User) specificUsers[i][0];

            BudgetApiHelper api = new BudgetApiHelper(getConsumerApiURLFromProperties(getFi(method, user.isToggleUser())));
            GoalType goalType = user.getUserType().isBusinessType() ? GoalType.SAVINGS : GoalType.RETIREMENT;

            D3Goal goal = goalType == GoalType.SAVINGS ? SavingsGoal.createRandomGoal(user) : RetirementGoal.createRandomGoal(user);

            try {
                api.login(user);
                if (user.getToggleMode().equals(ToggleMode.BUSINESS)) {
                    api.switchToggleToBusinessMode();
                }
                api.addGoal(user, goal);
            } catch (D3ApiException e) {
                logger.error("Error creating user: {} with goals", user);
                logger.error("Error:", e);
                return new Object[][] {{null, null}};
            }
            userWithGoal[i] = new Object[] {user, goal};

        }
        return userWithGoal;
    }
}
