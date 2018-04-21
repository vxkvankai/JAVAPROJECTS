package com.d3.api.helpers.banking;

import com.d3.api.mappings.accounts.Account;
import com.d3.api.mappings.accounts.EStatementEnrollment;
import com.d3.api.mappings.transactions.Transaction;
import com.d3.api.services.BankingService;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Slf4j
public class AccountApiHelper extends D3BankingApi {

    private static final String ERROR_MSG = "Error";

    public AccountApiHelper(String baseUrl) {
        super(baseUrl);
    }

    public List<Account> getAccounts() throws D3ApiException {
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<List<Account>> accountsResponse = service.getAccounts().execute();
            checkFor200(accountsResponse);
            return accountsResponse.body();
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error getting accounts");
        }
    }

    public D3BankingApi excludeAccount(String accountName) throws D3ApiException {
        log.info("Attempting to exclude the account named: {}", accountName);
        List<Account> accounts = getAccounts();
        if (accounts == null || accounts.isEmpty()) {
            throw new D3ApiException("No accounts to exclude");
        }

        Account accountToEdit = null;
        for (Account account : accounts) {
            if (account.getName().equalsIgnoreCase(accountName)) {
                accountToEdit = account;
                break;
            }
        }

        if (accountToEdit == null) {
            throw new D3ApiException(String.format("No Account matches name: %s in list: %s", accountName, accounts));
        }

        log.info("Found Account to edit");

        accountToEdit.setExcluded(true);
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<Account> response = service.updateAccount(accountToEdit.getId(), accountToEdit).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error excluding the account");
        }

        log.info("Account: {} updated successfully (excluded)", accountName);
        return this;
    }

    public D3BankingApi hideAccount(String accountName) throws D3ApiException {
        log.info("Attempting to hide the account named: {}", accountName);
        List<Account> accounts = getAccounts();
        if (accounts == null || accounts.isEmpty()) {
            throw new D3ApiException("No accounts to hide");
        }

        Account accountToEdit = null;
        for (Account account : accounts) {
            if (account.getName().equalsIgnoreCase(accountName)) {
                accountToEdit = account;
                break;
            }
        }

        if (accountToEdit == null) {
            throw new D3ApiException(String.format("No Account matches name: %s in list: %s", accountName, accounts));
        }

        log.info("Found Account to edit");

        accountToEdit.setHidden(true);
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<Account> response = service.updateAccount(accountToEdit.getId(), accountToEdit).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error hiding the account");
        }
        log.info("Account: {} updated successfully (hidden)", accountName);
        return this;
    }

    public D3BankingApi enrollInEstatements(D3User user, Integer accountId) throws D3ApiException {
        log.info("Attempting to enroll in E-Statements for user: {} using accountId {}", user, accountId);
        List<EStatementEnrollment> enrollment = EStatementEnrollment.enrollment(user, accountId);
        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<Void> response = service.estatementEnrollment(enrollment).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error(ERROR_MSG, e);
            throw new D3ApiException("Error enrolling in EStatements for the account");
        }
        log.info("EStatement enrollment was successful for user: {}", user);
        return this;
    }

    public D3BankingApi addOfflineAccount(D3Account offlineAccount) throws D3ApiException {
        log.info("Attempting to add offline account");
        BankingService service = retrofit.create(BankingService.class);
        Account accountToAdd = new Account(offlineAccount);
        try {
            Response<Void> response = service.addOfflineAccount(accountToAdd).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error("Error adding offline account of type {}", offlineAccount.getProductType().toString(), e);
            throw new D3ApiException("Error adding offline account");
        }
        log.info("Offline account: {} was added successfully", offlineAccount.getName());
        return this;
    }

    public D3BankingApi addOfflineAccountTransaction(D3User user, D3Transaction transaction, D3Account offlineAccount, String category) throws D3ApiException {
        log.info("Attempting to add offline transaction for {}'s account {} ", user.getLogin(), offlineAccount.getName());
        BankingService service = retrofit.create(BankingService.class);
        Transaction offlineTxn = new Transaction(user, transaction, offlineAccount, category);
        try {
            Response<Void> response = service.addOfflineTransaction(offlineTxn).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error("Error:", e);
            throw new D3ApiException(String.format("Adding offline transaction for %s's account %s failed", user.getLogin(), offlineAccount.getName()));
        }

        log.info("Adding offline transaction for {}'s account {} was successful", user.getLogin(), offlineAccount.getName());
        return this;
    }

}
