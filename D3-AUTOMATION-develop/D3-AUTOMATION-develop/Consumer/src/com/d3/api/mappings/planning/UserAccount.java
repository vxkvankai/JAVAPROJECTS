package com.d3.api.mappings.planning;

import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccount {
    private Integer id;

    public static UserAccount getAccountId(D3User user, D3Account account) {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(UserDatabaseHelper.waitForUserAccountId(user, account));
        return userAccount;
    }
}
