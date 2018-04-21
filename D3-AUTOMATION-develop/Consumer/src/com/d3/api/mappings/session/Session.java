
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Session {

    @SerializedName("profiles")
    @Expose
    private List<Profile> profiles = null;
    @SerializedName("csrfToken")
    @Expose
    private String csrfToken;
    @SerializedName("tos")
    @Expose
    private List<Object> tos = null;
    @SerializedName("rdcAllowed")
    @Expose
    private Boolean rdcAllowed;
    @SerializedName("startupKey")
    @Expose
    private Integer startupKey;
    @SerializedName("activeProfileType")
    @Expose
    private String activeProfileType;
    @SerializedName("selectedProfileIndex")
    @Expose
    private Integer selectedProfileIndex;
    @SerializedName("validProfileTypes")
    @Expose
    private List<String> validProfileTypes = null;
    @SerializedName("serverTime")
    @Expose
    private String serverTime;
    @SerializedName("syncOps")
    @Expose
    private List<Object> syncOps = null;
    @SerializedName("profileIndex")
    @Expose
    private Integer profileIndex;

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public List<Object> getTos() {
        return tos;
    }

    public void setTos(List<Object> tos) {
        this.tos = tos;
    }

    public Boolean getRdcAllowed() {
        return rdcAllowed;
    }

    public void setRdcAllowed(Boolean rdcAllowed) {
        this.rdcAllowed = rdcAllowed;
    }

    public Integer getStartupKey() {
        return startupKey;
    }

    public void setStartupKey(Integer startupKey) {
        this.startupKey = startupKey;
    }

    public String getActiveProfileType() {
        return activeProfileType;
    }

    public void setActiveProfileType(String activeProfileType) {
        this.activeProfileType = activeProfileType;
    }

    public Integer getSelectedProfileIndex() {
        return selectedProfileIndex;
    }

    public void setSelectedProfileIndex(Integer selectedProfileIndex) {
        this.selectedProfileIndex = selectedProfileIndex;
    }

    public List<String> getValidProfileTypes() {
        return validProfileTypes;
    }

    public void setValidProfileTypes(List<String> validProfileTypes) {
        this.validProfileTypes = validProfileTypes;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public List<Object> getSyncOps() {
        return syncOps;
    }

    public void setSyncOps(List<Object> syncOps) {
        this.syncOps = syncOps;
    }

    public Integer getProfileIndex() {
        return profileIndex;
    }

    public void setProfileIndex(Integer profileIndex) {
        this.profileIndex = profileIndex;
    }

    public Session () {
        this.profileIndex = 1;
    }


}
