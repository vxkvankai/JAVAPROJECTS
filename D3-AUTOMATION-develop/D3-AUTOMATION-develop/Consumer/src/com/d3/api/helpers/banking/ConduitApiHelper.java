package com.d3.api.helpers.banking;

import com.d3.api.services.ConduitService;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

@Slf4j
public class ConduitApiHelper extends D3BankingApi {

    private String baseUrl;

    public ConduitApiHelper(String baseUrl, String apiVersion) {
        super(baseUrl + apiVersion);
        this.baseUrl = baseUrl;
    }

    /**
     * Send conduit information to the server via the api.
     *
     * @param conduitInfo Conduit information in xml format
     * @throws ConduitException Thrown on failure to send
     */
    public ConduitApiHelper sendConduitFileToApi(String conduitInfo) throws ConduitException {
        // Conduit doesn't use api version
        retrofit = retrofit.newBuilder().baseUrl(baseUrl).build();

        ConduitService conduitService = retrofit.create(ConduitService.class);

        RequestBody file = RequestBody.create(MediaType.parse("application/xml"), conduitInfo);
        Response<ResponseBody> response;
        try {
            response = conduitService.sendConduitFile(file).execute();
            checkForSuccess(response);
        } catch (IOException | D3ApiException e) {
            log.error("Error Sending the conduit file", e);
            throw new ConduitException("Issue sending conduit file through api");
        }
        return this;
    }

}
