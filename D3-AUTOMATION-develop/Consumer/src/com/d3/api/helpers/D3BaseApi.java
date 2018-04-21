package com.d3.api.helpers;

import com.d3.exceptions.D3ApiException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class D3BaseApi {

    protected Retrofit retrofit;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected String cookieString = "";
    protected Map<String, String> headers = new HashMap<>();

    protected static Retrofit getNewRetrofit(String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
    }

    protected Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * Checks if the response has a 200 response code
     *
     * @param response The retrofit response object to check
     */
    protected void checkFor200(Response response) throws IOException, D3ApiException {
        checkForStatusCode(response, 200);
    }

    /**
     * Checks if the response has the given response code
     *
     * @param response The retofit response object to check
     * @param code status code to verify
     */
    protected void checkForStatusCode(Response response, int code) throws IOException, D3ApiException {
        if (response.code() != code) {
            if (logger.isErrorEnabled()) {
                logger.error("{}", response);
                logger.error("{}", response.errorBody().string());
            }
            throw new D3ApiException(String.format("Api call did not have a %d response code", code));
        }
    }

    /**
     * Check if the response was sucessful
     *
     * @param response The retrofit response object to check
     */
    protected void checkForSuccess(Response response) throws D3ApiException, IOException {
        if (!response.isSuccessful()) {
            if (logger.isErrorEnabled()) {
                logger.error("{}", response);
                logger.error("{}", response.errorBody().string());
            }
            throw new D3ApiException("Api call was not successful");
        }
    }

    /**
     * Sets the given cookie name and value pair into the headers of the retrofit object
     *
     * @param cookieName Name of the cookie to add
     * @param cookieValue Value of the cookie to add
     */
    protected void setCookie(String cookieName, String cookieValue) {
        if (cookieString.isEmpty()) {
            cookieString = String.format("%s=%s;", cookieName, cookieValue);
        } else {
            cookieString = String.format("%s%s=%s;", cookieString, cookieName, cookieValue);
        }
        setHeaders();
    }

    protected void setHeaders() {
        setHeader("", "");
    }

    /**
     * Sets the given header name and value pair for the retrofit object
     *
     * @param headerName Name of the header
     * @param headerValue Value of the Header
     */
    protected void setHeader(String headerName, String headerValue) {
        if (!headerName.isEmpty() && !headerValue.isEmpty()) {
            headers.put(headerName, headerValue);
            logger.info("Setting header: '{}' value: '{}'", headerName, headerValue);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                    builder.addHeader("Cookie", cookieString);
                    return chain.proceed(builder.build());
                }).build();
        retrofit = retrofit.newBuilder().client(client).build();
    }

    /**
     * Pulls the requested cookie name out of the Set-Cookie header of the given response
     *
     * @param resp Response to pull set-cookie from
     * @param cookieName Name of the cookie to extract
     * @return The cookie value
     * @throws D3ApiException Thrown if no Set-Cookie header is present or the given cookieName isn't found in the set-cookie header
     */
    protected String getCookieFromResponse(Response resp, String cookieName) throws D3ApiException {
        logger.info("Attempting to find the cookie: '{}' in the Set-Cookie header", cookieName);
        String setCookies = resp.headers().get("Set-Cookie");
        if (setCookies == null) {
            if (logger.isErrorEnabled()) {
                logger.error("Searching for cookie: {}", cookieName);
                logger.error("{}", resp.body());
                try {
                    logger.error("{}", resp.errorBody().string());
                } catch (IOException e) {
                    logger.error("Error getting body of error", e);
                }
            }
            throw new D3ApiException("No Set-Cookie header was found");
        }

        Matcher cookieMatch = Pattern.compile(cookieName + "=(.+?); .*").matcher(setCookies);
        if (cookieMatch.matches()) {
            String cookie = cookieMatch.group(1);
            logger.info("Found the cookie: {}", cookie);
            return cookie;
        } else {
            logger.error("SetCookes: {}, cookeMatch: {}", setCookies, cookieMatch);
            throw new D3ApiException(String.format("No %s value was in the response", cookieName));
        }
    }
}
