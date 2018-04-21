package com.d3.api.helpers.banking;

import com.d3.api.mappings.planning.Goals;
import com.d3.api.services.BankingService;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class BudgetApiHelper extends D3BankingApi {

    public BudgetApiHelper(String baseUrl) {
        super(baseUrl);
    }

    public D3BankingApi addGoal(D3User user, D3Goal goal) throws D3ApiException {
        logger.info("Attempting to add {} goal for user {}", goal, user.getLogin());
        BankingService service = retrofit.create(BankingService.class);
        Goals newGoal = new Goals(user, goal);
        Call<Void> addGoal = service.addGoal(newGoal);
        try {
            Response<Void> response = addGoal.execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            logger.error("Error", e);
            throw new D3ApiException(String.format("Creating %s goal for %s failed", goal, user.getLogin()));
        }

        logger.info("Creating {} goal for {} was successful", goal, user.getLogin());
        return this;
    }

    public D3BankingApi createBudget(D3User user) throws D3ApiException {
        logger.info("Attempting to create budget for user {}", user.getLogin());
        BankingService service = retrofit.create(BankingService.class);
        Call<Void> createBudget = service.createBudget(new JsonObject());
        try {
            Response<Void> response = createBudget.execute();
            checkFor200(response);
        } catch (IOException | D3ApiException e) {
            logger.error("Error: ", e);
            throw new D3ApiException(String.format("Creating budget failed for user: %s", user.getLogin()));
        }

        logger.info("Creating budget for {} was successful", user.getLogin());
        return this;
    }
}
