package com.d3.api.mappings.ebills;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

public class ActivationForm {

    @SerializedName("tokens")
    @Expose
    private Tokens tokens;
    @SerializedName("endpointProviderId")
    @Expose
    private Integer endpointProviderId;

    public Tokens getTokens() {
        return tokens;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    public Integer getEndpointProviderId() {
        return endpointProviderId;
    }

    public void setEndpointProviderId(Integer endpointProviderId) {
        this.endpointProviderId = endpointProviderId;
    }

    public ActivationForm(D3User user, Recipient recipient, Response<ResponseBody> response) throws IOException {
        this.endpointProviderId = DatabaseUtils.getExternalEndpointProviderId(user, recipient.getName());
        this.tokens = Tokens.getActivationTokens(user, recipient, response);
    }

}
