package com.d3.api.mappings.transactions;

import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCategory {

    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("profileType")
    @Expose
    private String profileType;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddCategory(D3User user, String categoryType) {
        String typeCat = categoryType.toLowerCase().contains("expense") ? "EXPENSE_DISCRETIONARY" : "INCOME";
        this.group = typeCat + " TEST CATEGORY";
        this.name = typeCat + " TEST CATEGORY";
        this.owner = "USER";
        this.profileType = user.getUserType().getProfileType();
        setType(typeCat);
    }
}