package com.d3.api.mappings.transactions;

import com.d3.database.TransactionDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private Integer id;
    private String postDate;
    private Integer userAccountId;
    private Boolean pending;
    private Boolean split;
    private String type;
    private String description;
    private Integer categoryId;
    private Double amount;
    private String originalName;
    private Integer accountId;
    private String accountName;
    private String accountNumber;
    private String checkNum;
    private String memo;

    public Transaction(D3User user, D3Transaction transaction, D3Account offlineAccount, String category) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.amount = Double.parseDouble(transaction.getAmountString());
        this.categoryId = TransactionDatabaseHelper.getCategoryId(category, user.getProfile().getType().toString());
        this.checkNum = transaction.getCheckNumber();
        this.description = transaction.getName();
        this.memo = transaction.getMemo();
        this.postDate = formatter.print(new DateTime(transaction.getPostDate()));
        this.type = transaction.getType().name();
        this.userAccountId = UserDatabaseHelper.waitForUserAccountId(user, offlineAccount);
    }
}
