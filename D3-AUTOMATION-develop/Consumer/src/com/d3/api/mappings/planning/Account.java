package com.d3.api.mappings.planning;


import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("userAccount")
    @Expose
    private UserAccount userAccount;
    @SerializedName("percentage")
    @Expose
    private Integer percentage;

    public Account(UserAccount account, int percentage) {
        this.userAccount = account;
        this.percentage = percentage;
    }

    public Account(D3User user, D3Account account, int percentage) {
        this(UserAccount.getAccountId(user, account), percentage);
    }

    public static Account createAccount(D3User user) {
        D3Account account = user.getUserType().isBusinessType() ? user.getFirstAccountByType(ProductType.BUSINESS_DEPOSIT_CHECKING)
                : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        return new Account(user, account, 100);
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
