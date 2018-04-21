package com.d3.api.helpers.banking;

import com.d3.api.mappings.alerts.AddAlert;
import com.d3.api.mappings.messages.AddSecureMessage;
import com.d3.api.services.BankingService;
import com.d3.database.AlertDatabaseHelper;
import com.d3.database.MessageDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.messages.SecureMessage;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.exceptions.D3DatabaseException;
import com.d3.helpers.RandomHelper;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Slf4j
public class MessageAlertApiHelper extends D3BankingApi {
    private static final String ERROR_CONST = "Error";

    public MessageAlertApiHelper(String baseUrl) {
        super(baseUrl);
    }

    public D3BankingApi addAlert(D3User user, D3Alert alert) throws D3ApiException {
        log.info("Attempting to add consumer alert for user {}", user.getLogin());
        Integer alertId = AlertDatabaseHelper.getConsumerAlertId(alert.getAlert());
        Integer destinationId = UserDatabaseHelper.getDestinationId(user, "Inbox");

        if (alertId == null || destinationId == null) {
            throw new D3ApiException(
                String.format("Error retrieving consumer alert id or destination id from the database for alert name: %s",
                    alert.getAlert().name()));
        }

        if (log.isInfoEnabled()) {
            log.info("destinationId: {}, alertName: {}", destinationId, alert.getAlert().name());
        }

        AddAlert addAlert = new AddAlert(user, alert, destinationId, alertId);
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<Void> response = service.addAlert(addAlert).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_CONST, e);
            throw new D3ApiException(String.format("Adding alert failed for alert name: %s, with id %i", alert.getAlert().name(), alertId));
        }

        log.info("Adding alert {} was successful", alert);
        return this;
    }

    public D3BankingApi createSecureMessage(D3User user, SecureMessage message) throws D3ApiException {
        log.info("Attempting to add secure message for {} ", user.getLogin());
        BankingService service = retrofit.create(BankingService.class);
        AddSecureMessage secureMessage = new AddSecureMessage(message);

        Call<Void> addSecureMessage = service.addSecureMessage(secureMessage);
        try {
            Response<Void> response = addSecureMessage.execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_CONST, e);
            throw new D3ApiException("Adding secure message failed");
        }

        log.info("Created Secure Message with Topic: {}, Issue: {}, Subject: {}, Message Details: {}", message.getTopic(),
            message.getIssue(), message.getSubject(), message.getMessageBody());
        return this;
    }


    public D3BankingApi createSecureMessageReply(D3User user, SecureMessage message) throws D3ApiException {
        log.info("Attempting to reply to secure message for {} ", user.getLogin());
        BankingService service = retrofit.create(BankingService.class);
        try {

            Integer secureMessageId = MessageDatabaseHelper.getSecureMessageId(message);
            if (secureMessageId == null) {
                throw new D3DatabaseException("Secure Message ID was null");
            }
            Response<Void> response = service.secureMessageReply(secureMessageId,
                String.format("Test reply %s", RandomHelper.getRandomString(10))).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException | D3DatabaseException e) {
            log.error(ERROR_CONST, e);
            throw new D3ApiException("Replying to secure message failed");
        }
        return this;
    }
}
