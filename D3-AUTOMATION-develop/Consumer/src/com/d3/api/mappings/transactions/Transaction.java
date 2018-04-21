package com.d3.api.mappings.transactions;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Transaction {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("postDate")
    @Expose
    private String postDate;
    @SerializedName("userAccountId")
    @Expose
    private Integer userAccountId;
    @SerializedName("pending")
    @Expose
    private Boolean pending;
    @SerializedName("split")
    @Expose
    private Boolean split;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("originalName")
    @Expose
    private String originalName;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("accountName")
    @Expose
    private String accountName;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("checkNum")
    @Expose
    private String checkNum;
    @SerializedName("memo")
    @Expose
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Boolean getSplit() {
        return split;
    }

    public void setSplit(Boolean split) {
        this.split = split;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Transaction(D3User user, D3Transaction transaction, D3Account offlineAccount, String category) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.amount = Double.parseDouble(transaction.getAmountString());
        this.categoryId = DatabaseUtils.getCategoryId(category, user.getProfile().getType().toString());
        this.checkNum = transaction.getCheckNumber();
        this.description = transaction.getName();
        this.memo = transaction.getMemo();
        this.postDate = formatter.print(new DateTime(transaction.getPostDate()));
        this.type = transaction.getType().name();
        this.userAccountId = DatabaseUtils.waitForUserAccountId(user, offlineAccount);
    }

}
