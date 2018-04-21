package com.d3.datawrappers.account;


import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberInt;
import static com.d3.helpers.RandomHelper.getRandomNumberString;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.common.D3Attribute;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.accounts.AccountsL10N;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@Slf4j
public class D3Account implements TransferableAccount, Serializable {

    private String uid; // Required
    private String cuid; // Required
    private Boolean delete = false; // Requried
    private ProductType productUniqId; // Required
    private String name; // Optional
    private String routingNumber = "082900872"; // Required
    private String number; // Required
    private Double balance; // Optional
    private AccountStatus status = AccountStatus.OPEN; // Optional
    private String currencyCode = "USD"; // Optional
    private Double availableBalance; // Optional
    private Boolean restricted = false; // Optional
    private Estatement estatement; // Optional
    private Date openDate; // Optional
    private Date closedDate; // Optional
    private List<D3Transaction> transactionList;
    private List<D3Attribute> attributes;

    public D3Account(String name, String cuid, Double balance, ProductType productType, List<D3Transaction> transactions,
        List<D3AccountAttribute> attributes) {
        this.name = name;
        this.cuid = cuid;
        this.productUniqId = productType;
        this.uid = String.format("autogen-%s-%s", name.replaceAll(" ", "-"), getRandomString(5));
        this.number = getRandomNumberString(10, true);
        this.balance = balance;
        this.availableBalance = balance;
        this.transactionList = new ArrayList<>();
        if (transactions != null) {
            this.transactionList.addAll(transactions);
        }
        this.attributes = new ArrayList<>();
        if (attributes != null) {
            this.attributes.addAll(attributes);
        }
    }

    public D3Account(String name, String cuid, Double balance, ProductType productType) {
        this(name, cuid, balance, productType, null, null);
    }

    public static D3Account generateRandomAccount(String name, String cuid, ProductType productType) {
        return generateRandomAccount(name, cuid, productType, false);
    }

    public static D3Account generateRandomAccount(String name, String cuid, ProductType productType, boolean largeNumOfTransactions) {
        Double balance = Double.valueOf(getRandomNumberInt(1000, 10000));

        int numOfTransactions;
        if (largeNumOfTransactions) {
            numOfTransactions = getRandomNumber(30, 50).intValue();
        } else {
            numOfTransactions = getRandomNumber(5, 20).intValue();
        }
        List<D3Transaction> transactions = D3Transaction.generateRandomTransactions(numOfTransactions, productType);
        return new D3Account(name, cuid, balance, productType, transactions, null);
    }

    /**
     * Generates random offline account for a user. Offline accounts cannot be added through conduit
     *
     * @param user D3User to create offline account for
     * @param productType Type of offline account to create (ASSET or LIABILITY)
     * @return D3Account with the offline account info
     */
    public static D3Account generateOfflineAccount(D3User user, ProductType productType) {
        String name = String.format("Offline Account - %s", RandomHelper.getRandomString(5));
        String cuid = user.getCuID();
        Double balance = Double.valueOf(getRandomNumberInt(1000, 10000));
        return new D3Account(name, cuid, balance, productType);
    }

    public String getUid() {
        return uid;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getDeleteValue() {
        if (delete == null) {
            return "";
        }
        return delete ? "1" : "0";
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public String getProductUniqIdValue() {
        return productUniqId.toString();
    }

    public ProductType getProductType() {
        return productUniqId;
    }

    public void setProductUniqId(ProductType productUniqId) {
        this.productUniqId = productUniqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatusValue() {
        if (status == null) {
            return "";
        }
        return status.getConduitCode();
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getAvailableBalanceStr() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(availableBalance);
    }

    public String getBalanceStr() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(balance);
    }

    public String getRestricted() {
        if (restricted == null) {
            return "";
        }
        return restricted ? "1" : "0";
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public String getEstatementValue() {
        if (estatement == null) {
            return "";
        }
        return estatement.getConduitCode();
    }

    public void setEstatement(Estatement estatement) {
        this.estatement = estatement;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public List<D3Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<D3Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    /**
     * Return the total of the account transaction amounts for this current month
     */
    public BigDecimal getCurrentMonthTotal(boolean forBudget) {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(getCurrentMonthAssets(forBudget));
        total = total.subtract(getCurrentMonthLiabilities());
        return total;
    }

    /**
     * Returns the total amount of money in assets (Credit transactions) that the account has for the current month (for asset accounts, otherwise
     * returns 0)
     */
    public BigDecimal getCurrentMonthAssets(boolean forBudget) {
        return isAsset() ? getCurrentMonthNumber(D3Transaction.TransactionType.CREDIT, forBudget) : BigDecimal.ZERO;
    }

    /**
     * Returns the total amount of money in liabilities (debit transactions) that the account has for the current month (will return the value for
     * asset accounts or if forBudget is true, otherwise returns 0)
     *
     * @param forBudget Set to true to force return the current Month Number, false will still check if the account is an asset
     * @return A positive money amount of liability transactions
     */
    public BigDecimal getCurrentMonthLiabilities(boolean forBudget) {
        return forBudget || isAsset() ? getCurrentMonthNumber(D3Transaction.TransactionType.DEBIT, forBudget) : BigDecimal.ZERO;
    }

    /**
     * Returns the total amount of money in liabilities (debit transactions) that the account has for the current month (for asset accounts, otherwise
     * returns 0)
     *
     * @return A positive money amount of liability transactions
     */
    public BigDecimal getCurrentMonthLiabilities() {
        return getCurrentMonthLiabilities(false);
    }

    /**
     * Return the total of the account transaction amounts
     */
    public BigDecimal getNetWorthTotal() {
        return isAsset() ? getNetWorthAssets() : getNetWorthLiabilities().negate();
    }

    /**
     * Returns the balance of the account if it the account is an asset, 0 otherwise
     */
    public BigDecimal getNetWorthAssets() {
        return isAsset() ? BigDecimal.valueOf(getBalance()) : BigDecimal.ZERO;
    }

    /**
     * Returns the balance of the account if it the account is a liability, 0 otherwise
     *
     * @return A positive money amount of liability transactions
     */
    public BigDecimal getNetWorthLiabilities() {
        return !isAsset() ? BigDecimal.valueOf(getBalance()) : BigDecimal.ZERO;
    }

    /**
     * Returns a list of the net worth Info for the account
     *
     * @return a BigDecimal list of order {NetWorthAssets, NetWorthLiabilities, netWorthTotal}
     */
    public List<BigDecimal> getNetWorthInfo() {
        List<BigDecimal> values = new ArrayList<>();
        values.add(getNetWorthAssets());
        values.add(getNetWorthLiabilities());
        values.add(getNetWorthTotal());
        return values;
    }

    /**
     * Returns a list of the current month info for the account
     *
     * @return a BigDecimal list of order {CurrentMonthAssets, CurrentMonthLiabilities, CurrentMonthTotal}
     */
    public List<BigDecimal> getCurrentMonthInfo(boolean forBudget) {
        List<BigDecimal> values = new ArrayList<>();
        values.add(getCurrentMonthAssets(forBudget));
        values.add(getCurrentMonthLiabilities());
        values.add(getCurrentMonthTotal(forBudget));
        return values;
    }

    /**
     * Returns the total (positive) amount of money a type of transaction has in the current month
     *
     * @param type Type of transaction to check
     * @return Positive double of the total amount of money in the month for that transaction type
     */
    private BigDecimal getCurrentMonthNumber(D3Transaction.TransactionType type, boolean forBudget) {
        BigDecimal total = BigDecimal.ZERO;
        DateTime now = DateTime.now();
        for (D3Transaction transaction : transactionList) {
            DateTime dateTime = new DateTime(transaction.getPostDate());
            if (dateTime.getMonthOfYear() == now.getMonthOfYear()
                && dateTime.getYear() == now.getYear()
                && transaction.getType() == type) {
                if (forBudget) {
                    if (!transaction.isPending()) {
                        total = total.add(BigDecimal.valueOf(Double.valueOf(transaction.getAmountString())));
                    }
                } else {
                    total = total.add(BigDecimal.valueOf(Double.valueOf(transaction.getAmountString())));
                }
            }
        }
        return total;
    }

    public String getStatus() {
        return status.toString();
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public boolean isAsset() {
        return productUniqId.getAccountingClass() == AccountingClass.ASSET;
    }

    public void addAttributes(@Nonnull List<D3AccountAttribute> attributes) {
        this.attributes.addAll(attributes);
    }

    public void addAttribute(D3AccountAttribute attribute) {
        this.attributes.add(attribute);
    }

    public List<D3Attribute> getAttributes() {
        return attributes;
    }

    public void addTransaction(D3Transaction transaction) {
        transactionList.add(transaction);
    }

    public void appendTransactionList(List<D3Transaction> transactions) {
        transactionList.addAll(transactions);
    }

    @Override
    public String getInternalExternalId(D3User user) {
        Integer id = UserDatabaseHelper.waitForUserAccountId(user, this);
        String idStr = id == null ? "" : id.toString();
        return String.format("INTERNAL:%s", idStr);
    }

    @Override
    public ProviderOption getProviderOption() {
        return ProviderOption.INTERNAL_TRANSFER;
    }

    @Override
    public String getTransferableName() {
        return getName();
    }

    @Override
    public boolean isBillPay() {
        return false;
    }

    public String toString() {
        return name;
    }

    public List<D3Transaction> getTransactionsWithCheckNumbers() {
        return getTransactionList().stream().filter(d3Transaction -> d3Transaction.getCheckNumber() != null).collect(Collectors.toList());
    }

    public D3Transaction getRandomTransaction() {
        log.info("Getting random transaction from account {}", this);
        if (getTransactionList().isEmpty()) {
            log.warn("No transactions to get for {}", this);
            return null;
        }
        D3Transaction transaction = getTransactionList().get(ThreadLocalRandom.current().nextInt(getTransactionList().size()));
        log.info("{} was retrieved from account: {}", transaction, this);
        return transaction;
    }

    /**
     * Returns the expected confirmation text for excluding the account
     */
    public String getExcludeAccountConfirmationText() {
        return this.getProductType().getAccountingClass().equals(AccountingClass.LIABILITY) ? AccountsL10N.Localization.EXCLUDE_LIABILITY_CONFIRM
            .getValue() : AccountsL10N.Localization.EXCLUDE_ASSET_CONFIRM.getValue();
    }

    /**
     * Returns the expected confirmation text for including the account
     */
    public String getIncludeAccountConfirmationText() {
        return this.getProductType().getAccountingClass().equals(AccountingClass.LIABILITY) ? AccountsL10N.Localization.INCLUDE_LIABILITY_CONFIRM
            .getValue() : AccountsL10N.Localization.INCLUDE_ASSET_CONFIRM.getValue();
    }

    @Override
    public boolean hasNoteField() {
        return true;
    }

    @Override
    public boolean eligibleForExpedite() {
        return false;
    }

    public enum AccountStatus {
        OPEN("o"),
        CLOSED("c"),
        FROZEN("f");
        private final String conduitCode;

        AccountStatus(String conduitCode) {
            this.conduitCode = conduitCode;
        }

        public String getConduitCode() {
            return this.conduitCode;
        }

    }

    public enum Estatement {
        ELECTRONIC("e"),
        PAPER("p"),
        BOTH("b");
        private final String conduitCode;

        Estatement(String conduitCode) {
            this.conduitCode = conduitCode;
        }

        public String getConduitCode() {
            return this.conduitCode;
        }
    }

    public enum AccountingClass {
        ASSET,
        LIABILITY
    }
}
