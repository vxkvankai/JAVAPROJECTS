package com.d3.api.mappings.planning;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAccount {

    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static UserAccount getAccountId(D3User user, D3Account account) {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(DatabaseUtils.waitForUserAccountId(user, account));
        return userAccount;
    }

}
