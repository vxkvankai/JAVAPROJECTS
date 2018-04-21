package com.d3.api.mappings.transactions;

import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddCategory {
    private String owner;
    private String profileType;
    private String group;
    private String type;
    private String name;

    public AddCategory(D3User user, String categoryType) {
        String typeCat = categoryType.toLowerCase().contains("expense") ? "EXPENSE_DISCRETIONARY" : "INCOME";
        this.group = typeCat + " TEST CATEGORY";
        this.name = typeCat + " TEST CATEGORY";
        this.owner = "USER";
        this.profileType = user.getUserType().getProfileType();
        setType(typeCat);
    }
}