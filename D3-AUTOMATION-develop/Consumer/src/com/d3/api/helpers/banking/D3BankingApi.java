package com.d3.api.helpers.banking;

import com.d3.api.helpers.D3BaseApi;
import com.d3.api.mappings.authentication.Authentication;
import com.d3.api.mappings.session.Session;
import com.d3.api.mappings.simple.Fingerprint;
import com.d3.api.services.BankingService;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import okhttp3.ResponseBody;
import org.apache.commons.lang.StringUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class D3BankingApi extends D3BaseApi {

    private static final String ERROR_MSG = "Error";

    public D3BankingApi(String baseUrl) {
        retrofit = getNewRetrofit(baseUrl);
    }

    public D3BankingApi(String baseUrl, D3User user) throws D3ApiException {
        this(baseUrl);
        this.login(user);
    }

    public D3BankingApi(D3BankingApi existingApi) {
        retrofit = existingApi.getRetrofit();
    }

    public Map<String, String> login(D3User user) throws D3ApiException {
        return login(user.getLogin(), user.getPassword(), user.getSecretQuestion());
    }

    /**
     * Login via api calls and returns the sid and JSessionId values
     *
     * @param username Username to login as
     * @param password Password of the User
     * @param challengeAnswer Answer to the Secret Question asked during login
     * @return A hashmap of cookies
     */
    public Map<String, String> login(String username, String password, String challengeAnswer) throws D3ApiException {
        logger.info("Attempting to login via the api for user: {}", username);

        BankingService service = retrofit.create(BankingService.class);
        Authentication auth;

        try {
            auth = service.getChallenge().execute().body();
        } catch (IOException e) {
            logger.error(ERROR_MSG, e);
            throw new D3ApiException("Error getting the challenge questions");
        }

        auth.getChallenge().setUsernamePassword(username, password);

        Call<Authentication> challengeQuestionsCall = service.postChallenge(auth);
        Response<Authentication> challengeQuestionsResp;

        try {
            challengeQuestionsResp = challengeQuestionsCall.execute();
        } catch (IOException e) {
            logger.error(ERROR_MSG, e);
            throw new D3ApiException("Error sending the login response");
        }

        Authentication challengeQuestions = challengeQuestionsResp.body();
        // extract and use cfid cookie
        String cfid = getCookieFromResponse(challengeQuestionsResp, "cfid");
        setCookie("cfid", cfid);
        service = retrofit.create(BankingService.class);

        // set answers to the returned challenge questions
        challengeQuestions.getChallenge().setChallengeAnswers(challengeAnswer);

        // send challenge answers and extract sid token
        Response<Authentication> authResp;
        try {
            authResp = service.postChallenge(challengeQuestions).execute();
        } catch (IOException e) {
            logger.error(ERROR_MSG, e);
            throw new D3ApiException("Error sending the challenge question response");
        }

        String sid = getCookieFromResponse(authResp, "sid");
        setCookie("sid", sid);
        service = retrofit.create(BankingService.class);

        // Get the JSESSIONID and csrfToken
        Response<Session> sessionResponse;
        try {
            sessionResponse = service.getSession().execute();
            checkFor200(sessionResponse);
        } catch (IOException e) {
            logger.error(ERROR_MSG, e);
            throw new D3ApiException("Error getting the session information");
        }

        String csrfToken = sessionResponse.body().getCsrfToken();
        setHeader("X-D3-Csrf", csrfToken);

        // accept terms of service(s)
        service = retrofit.create(BankingService.class);
        logger.info("Accepting terms of service for user: {}", username);

        // Set a dummy fingerprint value (just needs something here to pass the api)
        String fingerprint = "abc1234";
        try {
            service.acceptApplicationTOS(new Fingerprint(fingerprint)).execute();
            service.acceptBusinessTOS(new Fingerprint(fingerprint)).execute();
            service.acceptMobileTOS(new Fingerprint(fingerprint)).execute();
        } catch (IOException e) {
            logger.error(ERROR_MSG, e);
            throw new D3ApiException("Error accpeting the TOS");
        }

        logger.info("Login was successful for user: {}", username);

        // build a hashmap of the needed cookies
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sid", sid);
        hashMap.put("X-D3-Csrf", csrfToken);

        return hashMap;
    }

    public D3BankingApi switchToggleToBusinessMode() throws D3ApiException {
        logger.info("Attempting to change toggle to business mode");
        BankingService service = retrofit.create(BankingService.class);

        // TODO switch toggle based on business name
        Session sessionMode = new Session();
        Call<Void> toggle = service.changeToggleMode(sessionMode);
        try {
            Response<Void> response = toggle.execute();
            checkForStatusCode(response, 204);
        } catch (IOException | D3ApiException e) {
            logger.error(ERROR_MSG, e);
            throw new D3ApiException("Changing Toggle Mode failed");
        }

        logger.info("Changing toggle to business mode was successful");
        return this;
    }

    public String getApiVersion() throws D3ApiException {
        logger.info("Attempting to get current API version");
        BankingService service = retrofit.create(BankingService.class);
        Call<ResponseBody> getVersion = service.getApiVersion();
        try {
            Response<ResponseBody> response = getVersion.execute();
            checkFor200(response);
            return StringUtils.substringBetween(new String(response.body().bytes()), "version:", "}").replaceAll("[']", "").trim();
        } catch (IOException | D3ApiException e) {
            logger.error(ERROR_MSG, e);
            throw new D3ApiException("Unable to get API Version, site might be down");
        }
    }
}