package com.d3.tests.consumer.core.planning;



import com.d3.tests.consumer.ConsumerTestBase;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;


public class PlanningTestBase extends ConsumerTestBase {

    @DataProvider(name = "Basic User With Budget")
    public Object[][] getBasicUserWithBudget(Method method) {
        return getDataFromSerializedFile(method);
    }
    
    @DataProvider(name = "Specific User with Goal")
    public Object[][] getSpecificUsersWithGoal(Method method) {
        return getDataFromSerializedFile(method);
    }
}
