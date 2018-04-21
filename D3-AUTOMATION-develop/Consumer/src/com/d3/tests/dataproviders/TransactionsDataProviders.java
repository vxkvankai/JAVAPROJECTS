package com.d3.tests.dataproviders;

import static com.d3.helpers.ConduitHelper.sendUserToServerAndWait;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.UserFactory;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.UseCompany;

import java.lang.reflect.Method;

public class TransactionsDataProviders extends DataProviderBase {

    @D3DataProvider(name = "Basic User With Many Transactions")
    public Object[][] getBasicUserWithLotsOfTransactionns(Method method) {
        String fi = getFi(method, false);
        D3User user = UserFactory.generateAverageUser(fi, true);
        if(!sendUserToServerAndWait(user, getConsumerBaseUrl(fi), getConsumerApiVersion())) {
            return new Object[][] {{null}};
        }

        return new Object[][] {{user}};
    }

    @D3DataProvider(name = "Basic User With Posted Transaction")
    public Object[][] getBasicUserWithPostedTransaction(Method method) {
        D3User user = createAverageUser(method);
        Object[][] nullData = new Object[][] {{null, null}};

        if (user == null) {
            return nullData;
        }

        D3Transaction postedTransaction = user.getRandomPostedTransaction();

        return postedTransaction == null ? nullData : new Object[][] {{user, postedTransaction}};
    }

    @D3DataProvider(name = "Basic User with Image Transaction")
    public Object[][] getBasicUserWithAnImageTransaction(Method method) {
        D3User user = null;
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";

        try {
            user = UserFactory.averageUser(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            logger.error("Issue creating a user with lots of transactions", e);
        }

        if (user == null) {
            logger.error("Issue getting user");
            return new Object[][] {{null, null}};
        }

        D3Transaction transaction = user.getRandomTransactionFromChecking();

        for (int i=0; i<30; ++i) {
            if (transaction != null && !transaction.isPending()) {
                logger.info("Using transaction: {} for testing", transaction);
                break;
            } else {
                logger.info("Issue with transaction: {}, getting new one", transaction);
            }
            transaction = user.getRandomTransactionFromChecking();
        }

        if (transaction == null) {
            logger.error("Couldn't get a good transaction");
            return new Object[][] {{null, null}};
        }

        return new Object[][] {{user, transaction}};
    }

}
