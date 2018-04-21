package com.d3.api.mappings.accounts;

import com.d3.api.mappings.users.AchLimit;
import com.d3.api.mappings.users.BillPayLimit;
import com.d3.api.mappings.users.TransferLimit;
import com.d3.api.mappings.users.WireLimit;
import com.d3.datawrappers.account.D3Account;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private Integer id;
    private String nickname;
    private Boolean associated;
    private Boolean excluded;
    private Boolean hidden;
    private Boolean preferredAccount;
    private String number;
    private String source;
    private String status;
    private String name;
    private String routingTransitNumber;
    private Double balance;
    private String balanceType;
    private String currencyCode;
    private Double interestRate;
    private String statementPreferenceType;
    private String openDate;
    private String profileType;
    private Product product;
    private String lastSyncTime;
    private String updatedTimestamp;
    private List<Attribute> attributes = null;
    private Integer accountId;
    private Boolean hasTransactionsAccess;
    private Boolean hasStatementsAccess;
    private Boolean hasPaymentAccess;
    private Boolean hasTransferAccess;
    private Boolean hasWireAccess;
    private Boolean overdraftEnrolled;
    private String type;
    private AchLimit achLimit;
    private TransferLimit transferLimit;
    private BillPayLimit billPayLimit;
    private WireLimit wireLimit;
    private String accountNumber;
    private List<String> access = null;
    private Boolean hasBillPayAccess;
    private Boolean hasAchAccess;
    private Boolean hasPayment;

    public Account(D3Account offlineAccount) {
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
