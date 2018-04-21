package com.d3.api.mappings.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("value")
    @Expose
    private String value;

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Item usernameItem(String username) {
        Item user = new Item();
        user.setType("USER_NAME");
        user.setResponse(username);
        return user;
    }

    public static Item passwordItem(String password) {
        Item user = new Item();
        user.setType("PASSWORD");
        user.setResponse(password);
        return user;
    }
}
