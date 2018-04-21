package com.d3.tests.dataproviders;

import static com.d3.helpers.DataHelper.convertListTo2DArray;
import static com.d3.tests.TestBase.getConsumerApiURLFromProperties;
import static com.d3.tests.TestBase.getConsumerApiVersion;
import static com.d3.tests.TestBase.getConsumerBaseUrl;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.mappings.transfer.recurring.ApiRecurringTransfer;
import com.d3.database.RecipientMMDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.ebills.D3EBill;
import com.d3.datawrappers.ebills.enums.AutoPay;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.transfers.BillPayRecurringTransfer;
import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.datawrappers.transfers.RecurringTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.UserFactory;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.RandomHelper;
import com.d3.tests.annotations.D3DataProvider;
import com.d3.tests.annotations.UseCompany;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MoneyMovementDataProviders extends DataProviderBase {

    @D3DataProvider(name = "User with bill pay and recipients")
    public Object[][] getUserWithRecipients(Method method) {
        D3User user = null;
        List<Recipient> recipients = Recipient.createAllRecipients();

        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";

        try {
            user = UserFactory.averageUserWithBillPay(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            log.error("Issue Creating an average user with Bill Pay", e);
        }
        if (user == null) {
            return new Object[][] {{null, null}};
        }

        return addSingleObjectForEachElementInList(user, recipients);
    }

    /**
     * Generates a new "average user" enrolled in Bill Pay and has Pay Multiple Transfers to be used in tests Creates random transfer data to a
     * Company and Person Recipient
     *
     * @return {D3User - An "Average User", List<PayMultipleTransfers> - 1 Company Recipient, 1 Person Recipient}
     */
    @D3DataProvider(name = "User with pay multiple transfers")
    public Object[][] getUserWithPayMultipleTransfers(Method method) {
        D3User user = null;
        String fi = getFi(method, false);
        List<Recipient> recipients = null;
        List<PayMultipleTransfer> transfers = new ArrayList<>();

        Object[][] errorObject = new Object[][] {{null, null}};

        try {
            user = UserFactory.averageUserWithBillPay(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());

            if (user == null) {
                log.error("Error generating average user with bill pay");
                return errorObject;
            }
            recipients = Recipient.createAllRecipients();
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(getConsumerApiURLFromProperties(fi));
            api.login(user);
            api.addRecipientsToUser(user, recipients, true);

        } catch (D3ApiException e) {
            log.error("Issue creating Average User with bill pay and recipients", e);
            return errorObject;
        }

        PayMultipleTransfer firstPayment =
            PayMultipleTransfer.createRandomTransfer(user.getFirstBillPayRecipientByType(RecipientWho.COMPANY), user.getFirstAccountByType(
                ProductType.DEPOSIT_CHECKING));
        PayMultipleTransfer secondPayment =
            PayMultipleTransfer.createRandomTransfer(user.getFirstBillPayRecipientByType(RecipientWho.PERSON),
                user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS));
        transfers.addAll(Arrays.asList(firstPayment, secondPayment));

        if (recipients == null || transfers.isEmpty()) {
            return errorObject;
        }

        return new Object[][] {{user, transfers}};
    }


    /**
     * Generates a new "average user" enrolled in Bill Pay and has a Pay Multiple template created Template consists 1 Company recipient and 1 Person
     * recipient
     *
     * @return {D3User - An "Average User", List<PayMultipleTransfers> - 1 Company Recipient, 1 Person Recipient}
     */
    @D3DataProvider(name = "User with pay multiple template created")
    public Object[][] getUserWithPayMultipleTemplate(Method method) {
        D3User user = null;
        String fi = getFi(method, false);
        List<Recipient> recipients = null;
        List<PayMultipleTransfer> transfers = new ArrayList<>();

        Object[][] errorObject = new Object[][] {{null, null}};

        try {
            user = UserFactory.averageUserWithBillPay(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());

            if (user == null) {
                log.error("Error generating average user with bill pay");
                return errorObject;
            }
            recipients = Recipient.createAllRecipients();
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(getConsumerApiURLFromProperties(fi));
            api.login(user);
            api.addRecipientsToUser(user, recipients, true);
            PayMultipleTransfer firstPayment =
                PayMultipleTransfer.createRandomTransfer(user.getFirstBillPayRecipientByType(RecipientWho.COMPANY), user.getFirstAccountByType(
                    ProductType.DEPOSIT_CHECKING));
            PayMultipleTransfer secondPayment =
                PayMultipleTransfer.createRandomTransfer(user.getFirstBillPayRecipientByType(RecipientWho.PERSON),
                    user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS));
            transfers.addAll(Arrays.asList(firstPayment, secondPayment));
            api.addPayMultipleTemplate(user, transfers);

        } catch (D3ApiException e) {
            log.error("Issue creating Average User with Worksheet Template", e);
            return errorObject;
        }

        if (recipients == null || transfers.isEmpty()) {
            log.error("No Recipients or Pay Multiple Transfers were found");
            return errorObject;
        }

        return new Object[][] {{user, transfers}};
    }

    @D3DataProvider(name = "Bill Pay User with Bad new Recipients and no seeded")
    public Object[][] getBadRecipientData(Method method) {
        D3User user = null;
        String fi = method.isAnnotationPresent(UseCompany.class) ? method.getAnnotation(UseCompany.class).companyId() : "fi1";
        try {
            user = UserFactory.averageUserWithBillPay(fi, getConsumerBaseUrl(fi), getConsumerApiVersion());
        } catch (Exception e) {
            log.error("Issue creating Average User with Bill Pay", e);
        }
        if (user == null) {
            return new Object[][] {{null, null}};
        }
        List<PersonBillAddressRecipient> recipients = PersonBillAddressRecipient.createListOfBadDataRecipients(": ~ ` < > ^ |\\  .");
        return new Object[][] {{user, recipients}};
    }

    @D3DataProvider(name = "Basic User with Recurring Transfers")
    public Object[][] getRecurringTransfers(Method method) {
        Object[][] data = getUserWithAddedRecipients(method, true, false);
        Object[][] errorObject = new Object[][] {{null, null}};

        if (data == null || data[0][0] == null) {
            return errorObject;
        }

        D3User user = (D3User) data[0][0];
        RecipientMMDatabaseHelper.setAllEndpointsActiveForUser(user);
        D3Account checkingAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        List<RecurringTransfer> transfers = new ArrayList<>();

        // create internal transfer
        transfers.add(
            StandardRecurringTransfer.createRandomTransferDetails(user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS), checkingAccount));

        // create other transfers
        for (Object[] aData : data) {
            Recipient recipient = (Recipient) aData[1];
            if (recipient.isBillPay()) {
                transfers.add(BillPayRecurringTransfer.createRandomTransferDetails(recipient, checkingAccount));
            } else {
                transfers.add(StandardRecurringTransfer.createRandomTransferDetails(recipient, checkingAccount));
            }
        }

        for (RecurringTransfer transfer : transfers) {
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(getConsumerApiURLFromProperties(user.getCuID()));
            try {
                api.login(user);
                api.addRecurringTransfer(new ApiRecurringTransfer(transfer, user));
            } catch (D3ApiException e) {
                log.error("Error adding recurring transfer: {}", transfer, e);
                return errorObject;
            }
        }

        return addSingleObjectForEachElementInList(user, transfers);
    }

    /**
     * This dataprovider will activate multiple ebill recipients for a user
     *
     * @param method test method using dataprovider
     * @return {D3User, CompanyBillPaySeedRecipient (Random recipient of multiple created)}
     */
    @D3DataProvider(name = "User with active e-billers")
    public Object[][] getUserWithActiveEbillers(Method method) {
        D3User user = null;
        //NOTE: (jmarshall) E-Bill Recipients are already added once enrolled in Bill Pay
        List<CompanyBillPaySeedRecipient> ebillRecipients = CompanyBillPaySeedRecipient.createEBillCapableRecipients();
        List<Recipient> recipientsToAdd = Recipient.createAllRecipients();

        try {
            user = createBillPayUser(method);
        } catch (Exception e) {
            log.error("Issue Creating an average user with Bill Pay", e);
        }
        if (user == null) {
            return new Object[][] {{null, null}};
        }

        String apiUrl = getConsumerApiURLFromProperties(getFi(method, false));
        try {
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(apiUrl);
            api.login(user);
            api.addRecipientsToUser(user, recipientsToAdd, true);
            for (Recipient recipient : ebillRecipients) {
                api.activateEbiller(user, recipient);
            }

            // Delete from the user_sync_operation table so added e-billers will be active on login
            UserDatabaseHelper.deleteFromUserSyncOperation(user);

        } catch (D3ApiException e) {
            log.error("Error adding recipients to user: ", e);
            return new Object[][] {{null, null}};
        }
        return new Object[][] {{user, RandomHelper.getRandomElementFromList(ebillRecipients)}};
    }


    /**
     * Returns a new user with an active ebiller and D3Ebill auto pay data (one for each Pay On type)
     *
     * @return {{D3User, Recipient, D3EBill}}
     */
    @D3DataProvider(name = "User with active e-billers and auto pay data")
    public Object[][] getUserWithActiveEbillersAndAutoPayData(Method method) {
        List<List<Object>> newData = new ArrayList<>();
        List<AutoPay.PayOn> payOnType = new ArrayList<>(Arrays.asList(AutoPay.PayOn.values()));

        for (AutoPay.PayOn type : payOnType) {
            Object[][] data = getUserWithActiveEbillers(method);
            if (data == null || data[0][0] == null) {
                log.error("Wasn't able to create the user or add active ebiller");
                return new Object[][] {{null, null, null}};
            }

            D3User user = (D3User) data[0][0];
            Recipient recipient = (Recipient) data[0][1];
            D3EBill autoPay = D3EBill.createRandomAutoPay(user);
            autoPay.setPayOn(type);

            List<Object> innerData = new ArrayList<>();
            innerData.add(user);
            innerData.add(recipient);
            innerData.add(autoPay);
            newData.add(innerData);
        }

        Object[][] finalData = convertListTo2DArray(newData);
        if (finalData == null) {
            return new Object[][] {{null, null, null}};
        }
        return finalData;
    }


    @D3DataProvider(name = "User with active e-billers enrolled in Auto Pay")
    public Object[][] getUserWithActiveEbillersEnrolledInAutoPay(Method method) {
        Object[][] data = getUserWithActiveEbillers(method);
        if (data == null || data[0][0] == null) {
            log.error("Wasn't able to create the user or add active ebiller");
            return new Object[][] {{null, null, null}};
        }

        D3User user = (D3User) data[0][0];
        Recipient activeEbiller = (Recipient) data[0][1];

        String apiUrl = getConsumerApiURLFromProperties(getFi(method, false));
        try {
            MoneyMovementApiHelper api = new MoneyMovementApiHelper(apiUrl);
            api.login(user);
            api.addAutoPayToEbiller(user, activeEbiller);

        } catch (D3ApiException e) {
            log.error("Error adding recipients to user: ", e);
            return new Object[][] {{null, null}};
        }
        return new Object[][] {{user, activeEbiller}};
    }
}
