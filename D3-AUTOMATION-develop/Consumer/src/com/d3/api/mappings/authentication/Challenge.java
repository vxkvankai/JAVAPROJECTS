package com.d3.api.mappings.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Challenge {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setUsernamePassword(String username, String password) {
        items.forEach(item -> {
            if (item.getType().equals("USER_NAME")) {
                item.setResponse(username);
            } else if (item.getType().equals("PASSWORD")) {
                item.setResponse(password);
            }
        });
    }

    public void setChallengeAnswers(String answer) {
        items.forEach(item -> item.setResponse(answer));
    }
}
