package com.d3.api.mappings.planning;


import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    private UserAccount userAccount;
    private Integer percentage;

    public Account(UserAccount account, int percentage) {
        this.userAccount = account;
        this.percentage = percentage;
    }

    public Account(D3User user, D3Account account, int percentage) {
        this(UserAccount.getAccountId(user, account), percentage);
    }
}
