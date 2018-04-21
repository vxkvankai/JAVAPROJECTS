package com.d3.api.mappings.authentication;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Challenge {
    private String type;
    private List<Item> items = null;

    public void setUsernamePassword(String username, String password) {
        items.forEach(item -> {
            if ("USER_NAME".equals(item.getType())) {
                item.setResponse(username);
            } else if ("PASSWORD".equals(item.getType())) {
                item.setResponse(password);
            }
        });
    }

    public void setChallengeAnswers(String answer) {
        items.forEach(item -> item.setResponse(answer));
    }
}
