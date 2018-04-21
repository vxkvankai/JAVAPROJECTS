package com.d3.api.helpers.banking;

import com.d3.api.mappings.users.AccountServiceGroups;
import com.d3.api.mappings.users.SecondaryUsers;
import com.d3.api.services.BankingService;
import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.exceptions.D3ApiException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

import java.io.IOException;

@Slf4j
public class SettingsApiHelper extends D3BankingApi {

    private static final String ERROR_MSG = "Error";

    public SettingsApiHelper(String baseUrl) {
        super(baseUrl);
    }

    public SettingsApiHelper(String baseUrl, Gson converterFactory) {
        super(baseUrl, converterFactory);
    }

    /**
     * Will get the available accounts and banking service groups that will be available to a secondary user
     * NOTE: Access for all accounts and services will not be set by default. (ex: access:[])
     *
     * @return AccountServiceGroups
     * @throws D3ApiException if there is an error getting Account and Service Groups for the user
     */
    private AccountServiceGroups getAccountServiceGroups() throws D3ApiException {
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<AccountServiceGroups> accountGroupResponse = service.getAccountsAndServicesForUser().execute();
            checkFor200(accountGroupResponse);
            return accountGroupResponse.body();
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error getting account and service groups available for secondary user");
        }
    }

    /**
     * Creates a secondary user through the api
     *
     * @param secondaryUser Secondary User to create
     * @return D3BankingApi
     * @throws D3ApiException if there is an error adding secondary user
     */
    public D3BankingApi addSecondaryUser(D3SecondaryUser secondaryUser) throws D3ApiException {
        log.info("Attempting to add secondary user for: {}", secondaryUser.getPrimaryUser().getLogin());
        SecondaryUsers secondaryUsers = new SecondaryUsers(secondaryUser, getAccountServiceGroups());
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<Void> response = service.addSecondaryUser(secondaryUsers).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error adding secondary user");
        }
        log.info("Adding secondary user: {} was successful for user: {}", secondaryUser.getLogin(), secondaryUser.getPrimaryUser().getLogin());
        return this;
    }

}

