package com.d3.api.helpers.banking;

import com.d3.api.mappings.billpay.BillPayEnrollment;
import com.d3.api.mappings.ebills.ActivationForm;
import com.d3.api.mappings.ebills.AutoPay;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.api.mappings.recipients.SearchResults;
import com.d3.api.mappings.transfer.onetime.ApiOneTimeTransfer;
import com.d3.api.mappings.transfer.recurring.ApiRecurringTransfer;
import com.d3.api.mappings.worksheets.Worksheet;
import com.d3.api.services.BankingService;
import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.ebills.D3EBill;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientType;
import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3ApiException;
import okhttp3.ResponseBody;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MoneyMovementApiHelper extends D3BankingApi {

    public MoneyMovementApiHelper(String baseUrl) {
        super(baseUrl);
    }

    public MoneyMovementApiHelper(String baseUrl, D3User user) throws D3ApiException {
        super(baseUrl, user);
    }

    public MoneyMovementApiHelper(D3BankingApi existingApi) {
        super(existingApi);
    }

    public D3BankingApi enrollInBillPay(D3User user, int accountId) throws D3ApiException {
        logger.info("Attempting to enroll in bill pay for user: {} with accountId of {}", user, accountId);
        BillPayEnrollment enrollment = new BillPayEnrollment(user, accountId);
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<Void> response = service.billPayEnrollment(enrollment).execute();
            checkFor200(response);
        } catch (D3ApiException | IOException e) {
            logger.error("Error ", e);
            throw new D3ApiException("Bill pay enrollment failed");
        }

        logger.info("Bill pay enrollment was successful for user: {}", user);
        return this;
    }

    public D3BankingApi addRecipient(ApiRecipient recipient) throws D3ApiException {
        logger.info("Attempting to add recipient: {}", recipient);
        try {
            Response<Void> response = retrofit.create(BankingService.class).addRecipient(recipient).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            logger.error("Error", e);
            throw new D3ApiException("Adding a recipient failed");
        }
        logger.info("Recipient adding was successful");
        return this;
    }

    public D3BankingApi addRecurringTransfer(ApiRecurringTransfer transfer) throws D3ApiException {
        logger.info("Attempting to add transfer: {}", transfer);
        try {
            Response<Void> response = retrofit.create(BankingService.class).addRecurringTransfer(transfer).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            logger.error("Error: ", e);
            throw new D3ApiException("Adding a recurring transfer failed");
        }
        logger.info("Transfer adding was successful");
        return this;
    }

    public D3BankingApi addOneTimeTransfer(ApiOneTimeTransfer transfer) throws D3ApiException {
        logger.info("Attempting to add transfer: {}", transfer);
        try {
            Response<Void> response = retrofit.create(BankingService.class).addOneTimeTransfer(transfer).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            logger.error("Error: ", e);
            throw new D3ApiException("Adding a one time transfer failed");
        }
        logger.info("Transfer adding was successful");
        return this;
    }

    /**
     * Add a list of recipients to the user via the api, all of which are set as non-active, also sets the D3User's recipients to the same list
     *
     * @param user User object to add recipients to
     * @param recipients List of new recipients to add
     * @return the MoneyMovementApi object, already logged in as the user
     */
    public MoneyMovementApiHelper addRecipientsToUser(D3User user, List<Recipient> recipients) throws D3ApiException {
        return addRecipientsToUser(user, recipients, false);
    }

    /**
     * Add a list of recipients to the user via the api, also sets the D3User's recipients to the same list
     *
     * @param user User object to add recipients to
     * @param recipients List of new recipients to add
     * @param setAllAsActive Set to true to set recipients as active, false otherwise
     * @return the MoneyMovementApi object, already logged in as the user
     */
    public MoneyMovementApiHelper addRecipientsToUser(D3User user, List<Recipient> recipients, boolean setAllAsActive)
        throws D3ApiException {
        if (user.getUserType().isBusinessType()) {
            recipients.removeAll(recipients.stream().filter(
                recipient -> recipient.getType().equals(RecipientType.ACCOUNT_I_OWN))
                .collect(Collectors.toList()));
        }

        if (user.getToggleMode().equals(ToggleMode.BUSINESS)) {
            switchToggleToBusinessMode();
        }

        for (Recipient recipient : recipients) {
            LoggerFactory.getLogger(MoneyMovementApiHelper.class).info("Creating recipient: {}", recipient);
            recipient.addApiRecipient(this);
        }

        user.setRecipients(recipients);

        if (setAllAsActive) {
            DatabaseUtils.setAllEndpointsActiveForUser(user);
        }
        return this;
    }

    public SearchResults getSearchResultsForBillPay(String searchTerm) throws D3ApiException {
        logger.info("Attempting to get search results for bill pay provider, {}", searchTerm);
        BankingService service = retrofit.create(BankingService.class);
        Call<List<SearchResults>> getSearchResults = service.getMMSearchResults(searchTerm);
        try {
            Response<List<SearchResults>> response = getSearchResults.execute();
            checkFor200(response);
            if (response.body().isEmpty()) {
                throw new D3ApiException("Response body was empty for search results");
            }

            return response.body().get(0);
        } catch (IOException | D3ApiException e) {
            logger.error("Error", e);
            throw new D3ApiException("Error getting search results");
        }
    }

    /**
     * Add a Money Movement Worksheet template to the user via the api
     *
     * @param user D3User being used in the test
     * @param payments List of Pay Multiple Transfers to be used for creating the worksheet template
     * @return D3BankingApi object
     */
    public D3BankingApi addPayMultipleTemplate(D3User user, List<PayMultipleTransfer> payments) throws D3ApiException {
        logger.info("Attempting to add pay multiple worksheet template alert for user {}", user.getLogin());
        Worksheet worksheet = new Worksheet(user, payments);
        BankingService service = retrofit.create(BankingService.class);

        try {
            Response<Void> response = service.addWorksheetTemplate(worksheet).execute();
            checkFor200(response);
        } catch (D3ApiException | IOException e) {
            logger.error("Error ", e);
            throw new D3ApiException("Creating Worksheet Template failed");
        }

        logger.info("Creating Worksheet Template was successful for user: {}", user);
        return this;
    }

    /**
     * Active an Ebiller for the user via the api
     *
     * @param user D3User being used in the test
     * @param recipient E-Bill Capable recipient to activate
     * @return D3BankingApi object
     */
    public D3BankingApi activateEbiller(D3User user, Recipient recipient) throws D3ApiException {
        logger.info("Attempting active E-Biller for user {}", user.getLogin());
        Integer providerId = DatabaseUtils.getExternalEndpointProviderId(user, recipient.getName());
        BankingService service = retrofit.create(BankingService.class);
        Call<ResponseBody> getActivationForm = service.getActivationForm(providerId);

        try {
            ActivationForm activationForm = new ActivationForm(user, recipient, getActivationForm.execute());
            Response<Void> response = service.activateEbiller(providerId, activationForm).execute();
            checkFor200(response);
        } catch (D3ApiException | IOException e) {
            logger.error("Error ", e);
            throw new D3ApiException("Adding and Activating E-Biller failed");
        }

        logger.info("Activating E-Biller {} for user: {} was successful", recipient.getName(), user);
        return this;
    }


    /**
     * Adds Auto Pay to an active ebiller
     *
     * @param user D3User being used in the test
     * @param recipient E-Bill Capable recipient to add Auto Pay to
     * @return D3BankingApi object
     */
    public D3BankingApi addAutoPayToEbiller(D3User user, Recipient recipient) throws D3ApiException {
        logger.info("Attempting to add Auto Pay to E-Biller for user {}", user.getLogin());
        Integer providerId = DatabaseUtils.getExternalEndpointProviderId(user, recipient.getName());
        D3EBill autoPayDetails = D3EBill.createRandomAutoPay(user);
        autoPayDetails.setAmountType(com.d3.datawrappers.ebills.enums.AutoPay.AmountType.FIXED_AMOUNT);
        BankingService service = retrofit.create(BankingService.class);

        try {
            AutoPay autoPay = new AutoPay(user, autoPayDetails);
            Response<Void> response = service.addAutoPay(providerId, autoPay).execute();
            checkFor200(response);
        } catch (D3ApiException | IOException e) {
            logger.error("Error ", e);
            throw new D3ApiException("Adding AutoPay for Ebiller failed");
        }

        logger.info("Adding AutoPay for E-Biller {} for user: {} was successful", recipient.getName(), user);
        return this;
    }

}
