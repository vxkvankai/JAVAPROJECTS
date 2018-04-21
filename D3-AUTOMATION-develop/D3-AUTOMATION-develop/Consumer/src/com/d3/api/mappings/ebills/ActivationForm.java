package com.d3.api.mappings.ebills;

import com.d3.database.RecipientMMDatabaseHelper;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivationForm {

    private Tokens tokens;
    private Integer endpointProviderId;

    public ActivationForm(D3User user, Recipient recipient, Response<ResponseBody> response) throws IOException {
        this.endpointProviderId = RecipientMMDatabaseHelper.getExternalEndpointProviderId(user, recipient.getName());
        this.tokens = Tokens.getActivationTokens(user, recipient, response);
    }
}
