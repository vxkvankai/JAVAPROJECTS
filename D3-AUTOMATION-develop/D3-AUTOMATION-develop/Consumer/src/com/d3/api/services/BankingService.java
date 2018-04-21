package com.d3.api.services;

import com.d3.api.mappings.accounts.Account;
import com.d3.api.mappings.accounts.EStatementEnrollment;
import com.d3.api.mappings.alerts.AddAlert;
import com.d3.api.mappings.authentication.Authentication;
import com.d3.api.mappings.billpay.BillPayEnrollment;
import com.d3.api.mappings.ebills.ActivationForm;
import com.d3.api.mappings.ebills.AutoPay;
import com.d3.api.mappings.messages.AddSecureMessage;
import com.d3.api.mappings.planning.Budget;
import com.d3.api.mappings.planning.BudgetCategory;
import com.d3.api.mappings.planning.Goals;
import com.d3.api.mappings.recipients.ApiRecipient;
import com.d3.api.mappings.recipients.SearchResults;
import com.d3.api.mappings.session.Session;
import com.d3.api.mappings.simple.Fingerprint;
import com.d3.api.mappings.transactions.AddCategory;
import com.d3.api.mappings.transactions.Transaction;
import com.d3.api.mappings.transfer.onetime.ApiOneTimeTransfer;
import com.d3.api.mappings.transfer.recurring.ApiRecurringTransfer;
import com.d3.api.mappings.transfer.worksheets.Worksheet;
import com.d3.api.mappings.users.AccountServiceGroups;
import com.d3.api.mappings.users.SecondaryUsers;
import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface BankingService {

    @GET("auth/challenge")
    Call<Authentication> getChallenge();

    @POST("auth/challenge")
    Call<Authentication> postChallenge(@Body Authentication auth);

    @GET("auth/session")
    Call<Session> getSession();

    @POST("auth/session/mode")
    Call<Void> changeToggleMode(@Body Session body);

    @POST("users/current/enrollment/tos/acceptance/APPLICATION")
    Call<Void> acceptApplicationTOS(@Body Fingerprint body);

    @POST("users/current/enrollment/tos/acceptance/BUSINESS_APPLICATION")
    Call<Void> acceptBusinessTOS(@Body Fingerprint body);

    @POST("users/current/enrollment/tos/acceptance/MOBILE_APPLICATION")
    Call<Void> acceptMobileTOS(@Body Fingerprint body);

    @POST("mm/enrollment")
    Call<Void> billPayEnrollment(@Body BillPayEnrollment body);

    @POST("accounts/documents/estatements/enrollment")
    Call<Void> estatementEnrollment(@Body List<EStatementEnrollment> body);

    @POST("mm/endpoints")
    Call<Void> addRecipient(@Body ApiRecipient body);

    @POST("mm/worksheets")
    Call<Void> addWorksheetTemplate(@Body Worksheet body);

    @GET("mm/endpoints/merchants")
    Call<List<SearchResults>> getMMSearchResults(@Query("name") String searchTerm);

    @POST("mm/transfers?scope=ALL")
    Call<Void> addRecurringTransfer(@Body ApiRecurringTransfer body);

    @POST("mm/transfers?scope=ALL")
    Call<Void> addOneTimeTransfer(@Body ApiOneTimeTransfer body);

    @GET("transactions/{transaction_id}")
    Call<Transaction> getTransactionInfo(@Path("transaction_id") int transactionId);

    @PUT("transactions/{transaction_id}")
    Call<Void> addCategoryRule(@Path("transaction_id") int transactionId, @Body Transaction body);

    @POST("transactions")
    Call<Void> addOfflineTransaction(@Body Transaction body);

    @POST("categories")
    Call<Void> addCategory(@Body AddCategory body);

    @POST("alerts")
    Call<Void> addAlert(@Body AddAlert body);

    @POST("budgets")
    Call<Void> createBudget(@Body JsonObject emptyBody);

    @POST("messages/secure/")
    Call<Void> addSecureMessage(@Body AddSecureMessage body);

    @GET("/")
    Call<ResponseBody> getApiVersion();

    @POST("goals")
    Call<Void> addGoal(@Body Goals body);

    @GET("accounts")
    Call<List<Account>> getAccounts();

    @PUT("accounts/{account_id}")
    Call<Account> updateAccount(@Path("account_id") int accountId, @Body Account account);

    @POST("accounts")
    Call<Void> addOfflineAccount(@Body Account body);

    @PUT("messages/secure/{message_id}")
    Call<Void> secureMessageReply(@Path("message_id") int messageId, @Body String reply);

    @GET("mm/ebills/providers/{provider_id}/activation-form")
    Call<ResponseBody> getActivationForm(@Path("provider_id") int providerId);

    @PUT("mm/ebills/providers/{provider_id}/activation-form")
    Call<Void> activateEbiller(@Path("provider_id") int providerId, @Body ActivationForm body);

    @POST("mm/ebills/providers/{provider_id}/autopay")
    Call<Void> addAutoPay(@Path("provider_id") int providerId, @Body AutoPay body);

    @GET("usermgmt/users/new")
    Call<AccountServiceGroups> getAccountsAndServicesForUser();

    @POST("usermgmt/users")
    Call<Void> addSecondaryUser(@Body SecondaryUsers body);

    @GET("budgets")
    Call<List<Budget>> getBudgets();

    @GET("budgets/periods/current")
    Call<List<Budget>> getCurrentBudgetPeriods();

    @PUT("budgets/{budgetId}/periods/current/categories/{budgetCategoryId}")
    Call<BudgetCategory> updateCurrentPeriodBudgetCategory(@Path("budgetId") int budgetId, @Path("budgetCategoryId") int budgetCategoryId, @Body BudgetCategory body);
}
