package com.d3.api.mappings.accounts;

import com.d3.datawrappers.account.D3Account;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Account {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("associated")
    @Expose
    private Boolean associated;
    @SerializedName("excluded")
    @Expose
    private Boolean excluded;
    @SerializedName("hidden")
    @Expose
    private Boolean hidden;
    @SerializedName("preferredAccount")
    @Expose
    private Boolean preferredAccount;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("routingTransitNumber")
    @Expose
    private String routingTransitNumber;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("balanceType")
    @Expose
    private String balanceType;
    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;
    @SerializedName("interestRate")
    @Expose
    private Double interestRate;
    @SerializedName("statementPreferenceType")
    @Expose
    private String statementPreferenceType;
    @SerializedName("openDate")
    @Expose
    private String openDate;
    @SerializedName("profileType")
    @Expose
    private String profileType;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("lastSyncTime")
    @Expose
    private String lastSyncTime;
    @SerializedName("updatedTimestamp")
    @Expose
    private String updatedTimestamp;
    @SerializedName("attributes")
    @Expose
    private List<Attribute> attributes = null;
    @SerializedName("accountId")
    @Expose
    private Integer accountId;
    @SerializedName("hasTransactionsAccess")
    @Expose
    private Boolean hasTransactionsAccess;
    @SerializedName("hasStatementsAccess")
    @Expose
    private Boolean hasStatementsAccess;
    @SerializedName("hasPaymentAccess")
    @Expose
    private Boolean hasPaymentAccess;
    @SerializedName("hasTransferAccess")
    @Expose
    private Boolean hasTransferAccess;
    @SerializedName("hasWireAccess")
    @Expose
    private Boolean hasWireAccess;
    @SerializedName("overdraftEnrolled")
    @Expose
    private Boolean overdraftEnrolled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getAssociated() {
        return associated;
    }

    public void setAssociated(Boolean associated) {
        this.associated = associated;
    }

    public Boolean getExcluded() {
        return excluded;
    }

    public void setExcluded(Boolean excluded) {
        this.excluded = excluded;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getPreferredAccount() {
        return preferredAccount;
    }

    public void setPreferredAccount(Boolean preferredAccount) {
        this.preferredAccount = preferredAccount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoutingTransitNumber() {
        return routingTransitNumber;
    }

    public void setRoutingTransitNumber(String routingTransitNumber) {
        this.routingTransitNumber = routingTransitNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getStatementPreferenceType() {
        return statementPreferenceType;
    }

    public void setStatementPreferenceType(String statementPreferenceType) {
        this.statementPreferenceType = statementPreferenceType;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Boolean getHasTransactionsAccess() {
        return hasTransactionsAccess;
    }

    public void setHasTransactionsAccess(Boolean hasTransactionsAccess) {
        this.hasTransactionsAccess = hasTransactionsAccess;
    }

    public Boolean getHasStatementsAccess() {
        return hasStatementsAccess;
    }

    public void setHasStatementsAccess(Boolean hasStatementsAccess) {
        this.hasStatementsAccess = hasStatementsAccess;
    }

    public Boolean getHasPaymentAccess() {
        return hasPaymentAccess;
    }

    public void setHasPaymentAccess(Boolean hasPaymentAccess) {
        this.hasPaymentAccess = hasPaymentAccess;
    }

    public Boolean getHasTransferAccess() {
        return hasTransferAccess;
    }

    public void setHasTransferAccess(Boolean hasTransferAccess) {
        this.hasTransferAccess = hasTransferAccess;
    }

    public Boolean getHasWireAccess() {
        return hasWireAccess;
    }

    public void setHasWireAccess(Boolean hasWireAccess) {
        this.hasWireAccess = hasWireAccess;
    }

    public Boolean getOverdraftEnrolled() {
        return overdraftEnrolled;
    }

    public void setOverdraftEnrolled(Boolean overdraftEnrolled) {
        this.overdraftEnrolled = overdraftEnrolled;
    }

    public Account (D3Account offlineAccount) {
        this.associated = true;
        this.balance = offlineAccount.getBalance();
        this.excluded = false;
        this.hidden = false;
        this.name = offlineAccount.getName();
        this.nickname = offlineAccount.getName();
        this.product = new Product();
        this.setProduct(product.offlineAccount(offlineAccount.getProductType()));
        this.source = "OFFLINE";
        this.status = "OPEN";
    }
}
