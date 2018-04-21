package com.d3.api.mappings.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Authentication {

    @SerializedName("challenge")
    @Expose
    private Challenge challenge;
    @SerializedName("previousItems")
    @Expose
    private List<Object> previousItems = null;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("passwordResetEnabled")
    @Expose
    private Boolean passwordResetEnabled;
    @SerializedName("authenticated")
    @Expose
    private Boolean authenticated;
    @SerializedName("isAuthenticated")
    @Expose
    private Boolean isAuthenticated;

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public List<Object> getPreviousItems() {
        return previousItems;
    }

    public void setPreviousItems(List<Object> previousItems) {
        this.previousItems = previousItems;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getPasswordResetEnabled() {
        return passwordResetEnabled;
    }

    public void setPasswordResetEnabled(Boolean passwordResetEnabled) {
        this.passwordResetEnabled = passwordResetEnabled;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

}
