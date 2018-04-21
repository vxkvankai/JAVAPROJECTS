package com.d3.helpers;

import com.d3.api.helpers.banking.D3BankingApi;
import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.exceptions.D3DatabaseException;

public class UserDataCreator {

    private final D3User user;
    private D3BankingApi api;

    public UserDataCreator(D3User user, String baseUrl, String apiVersion) throws D3ApiException {
        this.user = user;
        api = new MoneyMovementApiHelper(baseUrl + apiVersion, user);
    }

    /**
     * Enrolls the user in bill pay with the given account
     *
     * @param account Account to use for the sign up process
     * @return this
     * @throws D3ApiException Thrown on Api Errors
     * @throws D3DatabaseException Thrown when the user account id isn't found in the db
     */
    public UserDataCreator enrollInBillPay(D3Account account) throws D3ApiException, D3DatabaseException {
        Integer accountId = DatabaseUtils.waitForUserAccountId(user, account);
        if (accountId == null) {
            throw new D3DatabaseException("Error getting the account id");
        }
        api = new MoneyMovementApiHelper(api).enrollInBillPay(user, accountId);
        return this;
    }

}
