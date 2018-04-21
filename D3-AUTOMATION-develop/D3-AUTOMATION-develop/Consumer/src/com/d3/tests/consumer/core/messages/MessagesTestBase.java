package com.d3.tests.consumer.core.messages;

import com.d3.tests.consumer.ConsumerTestBase;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class MessagesTestBase extends ConsumerTestBase {

    @DataProvider(name = "Basic User With Secure Message")
    public Object[][] getBasicUserWithSecureMessage(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Basic User With Alert Triggered")
    public Object[][] getBasicUserWithAlertTriggered(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Basic User With Multiple Secure Messages")
    public Object[][] getBasicUserWithMultipleSecureMessages(Method method) {
        return getDataFromSerializedFile(method);
    }

    @DataProvider(name = "Basic User With Secure Message Replies")
    public Object[][] getBasicUserWithSecureMessageReplies(Method method) {
        return getDataFromSerializedFile(method);
    }
}

