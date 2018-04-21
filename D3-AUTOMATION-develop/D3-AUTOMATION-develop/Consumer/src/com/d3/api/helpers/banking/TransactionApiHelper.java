package com.d3.api.helpers.banking;

import com.d3.api.mappings.transactions.AddCategory;
import com.d3.api.mappings.transactions.Transaction;
import com.d3.api.services.BankingService;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

@Slf4j
public class TransactionApiHelper extends D3BankingApi {

    public TransactionApiHelper(String baseUrl) {
        super(baseUrl);
    }

    public void addCategory(D3User user, String... categoryType) throws D3ApiException {
        String type;

        if (categoryType.length == 0) {
            String[] categoryTypes = new String[] {"income", "expense"};
            Random random = new Random();
            type = categoryTypes[random.nextInt(categoryTypes.length)];
        } else {
            type = Arrays.toString(categoryType);
        }

        log.info("Attempting to add {} category for user: {} ", type, user);
        AddCategory category = new AddCategory(user, type);

        BankingService service = retrofit.create(BankingService.class);
        try {
            Response<Void> response = service.addCategory(category).execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error("Error: ", e);
            throw new D3ApiException(String.format("Adding a %s category failed", type));
        }
        log.info("Adding {} category {} was successful for user: {}", type, category.getName(), user.getLogin());
    }

    public void addCategoryRule(int transactionId, int categoryId) throws D3ApiException {
        log.info("Attempting to add category rule with id {} to transaction with id {} ", categoryId, transactionId);
        BankingService service = retrofit.create(BankingService.class);

        try {
            Transaction transactionInfo = service.getTransactionInfo(transactionId).execute().body();
            transactionInfo.setCategoryId(categoryId);

            Call<Void> addCategoryRuleCall = service.addCategoryRule(transactionId, transactionInfo);
            Response<Void> response = addCategoryRuleCall.execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error("Error:", e);
            throw new D3ApiException("Adding a category rule failed");
        }

        log.info("Adding category rule {} was successful", categoryId);
    }

    public void addRenamingRule(int transactionId) throws D3ApiException {
        log.info("Attempting to add renaming rule to transaction with id {} ", transactionId);
        BankingService service = retrofit.create(BankingService.class);
        try {
            Transaction transactionInfo = service.getTransactionInfo(transactionId).execute().body();
            transactionInfo.setDescription(transactionInfo.getDescription() + " Test");

            Call<Void> addCategoryRuleCall = service.addCategoryRule(transactionId, transactionInfo);
            Response<Void> response = addCategoryRuleCall.execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            log.error("Error: ", e);
            throw new D3ApiException("Adding a transaction renaming rule failed");
        }

        log.info("Adding renaming rule to transaction with id {} was successful", transactionId);
    }
}
