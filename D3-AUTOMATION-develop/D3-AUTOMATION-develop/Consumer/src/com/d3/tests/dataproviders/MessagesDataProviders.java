package com.d3.tests.dataproviders;

import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;

import com.d3.api.helpers.banking.MessageAlertApiHelper;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.messages.SecureMessage;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.DataHelper;
import com.d3.helpers.RandomHelper;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertFrequency;
import com.d3.tests.annotations.D3DataProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessagesDataProviders extends DataProviderBase {

    private static final String SECURE_ERROR_MESSAGE = "Issue Creating average User with secure Message";

    @D3DataProvider(name = "Basic User With Secure Message")
    public Object[][] getBasicUserWithSecureMessage(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        SecureMessage secureMessage = SecureMessage.createRandomMessage(user);
        if (user == null) {
            return nullData;
        }

        try {
            MessageAlertApiHelper api = new MessageAlertApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
            api.login(user);
            api.createSecureMessage(user, secureMessage);
        } catch (D3ApiException e) {
            log.error(SECURE_ERROR_MESSAGE, e);
            return nullData;
        }

        return new Object[][] {{user, secureMessage}};
    }


    @D3DataProvider(name = "Basic User With Secure Message Replies")
    public Object[][] getBasicUserWithSecureMessageReplies(Method method) {
        Object[][] secureMessage = getBasicUserWithSecureMessage(method);
        Object[][] nullData = new Object[][] {{null, null}};

        D3User user = (D3User) secureMessage[0][0];
        SecureMessage message = (SecureMessage) secureMessage[0][1];
        if (user == null) {
            return nullData;
        }

        try {
            MessageAlertApiHelper api = new MessageAlertApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
            api.login(user);
            // Create multiple replies to the secure message
            for (int x = 0; x < RandomHelper.getRandomNumberInt(2, 4); x++) {
                api.createSecureMessageReply(user, message);
            }
        } catch (D3ApiException e) {
            log.error(SECURE_ERROR_MESSAGE, e);
            return nullData;
        }

        return new Object[][] {{user, message}};
    }


    /**
     * This data provider will trigger consumer alert for user. The method using this data provider needs to use
     * the @RunWithSpecificAlert annotation. Although that annotation can take multiple ConsumerAlerts, the data provider
     * should be used for one type of alert and will only return the first one found if multiple listed on the method
     *
     * @param method Test method
     * @return {D3Alert}
     */
    @D3DataProvider(name = "Basic User With Alert Triggered")
    public Object[][] getBasicUserWithAlertTriggered(Method method) {
        Object[][] nullData = new Object[][] {{null}};
        List<D3User> users = new ArrayList<>();

        List<Class<D3Alert>> specificAlerts = getD3AlertsFromAnnotation(method);
        for (int i = 0; i < specificAlerts.size(); ++i) {
            users.add(createAverageUser(method));
        }

        List<D3Alert> alertsToTrigger = D3Alert.createAllConsumerAlerts(users, specificAlerts);
        if (alertsToTrigger == null) {
            return nullData;
        }

        //Set frequency to DAYS in order for alert to be triggered
        alertsToTrigger.forEach(alert -> {
            if (alert.hasFrequency()) {
                alert.setFrequency(AlertFrequency.DAYS);
            }
        });

        for (D3Alert alert : alertsToTrigger) {
            D3User user = alert.getUser();
            String baseUrl = getConsumerApiURLFromProperties(user.getCuID());
            MessageAlertApiHelper api = new MessageAlertApiHelper(baseUrl);
            try {
                api.login(user);
                api.addAlert(user, alert);
                alert.triggerAlert();
            } catch (D3ApiException | ConduitException e) {
                log.error("Issue triggering user alert");
                return nullData;
            }
        }

        // If @RunForSpecificAlerts boolean runForEachTest is set to true return each alert individually, otherwise choose random alert to run test for
        return DataHelper.convert1DListTo2DArray(alertsToTrigger);
    }

    @D3DataProvider(name = "Basic User With Multiple Secure Messages")
    public Object[][] getBasicUserWithMultipleSecureMessages(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        if (user == null) {
            log.error("Error creating user (user object was null)");
            return nullData;
        }

        List<SecureMessage> secureMessages = SecureMessage.createMultipleSecureMessages(user, RandomHelper.getRandomNumberInt(3, 6));
        MessageAlertApiHelper api = new MessageAlertApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
        try {
            api.login(user);
            for (SecureMessage message : secureMessages) {
                api.createSecureMessage(user, message);
            }
        } catch (D3ApiException e) {
            log.error(SECURE_ERROR_MESSAGE, e);
            return nullData;
        }

        return new Object[][] {{user, secureMessages}};
    }
}
