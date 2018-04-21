package com.d3.datawrappers.account;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberString;
import static com.d3.helpers.RandomHelper.getRandomPastDate;
import static com.d3.helpers.RandomHelper.getRandomString;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.joda.time.DateTime;
import org.testng.Assert;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class D3Transaction implements Serializable {

    public String getUid() {
        return uid;
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

    public String getTypeValue() {
        return type == null ? "" : type.getConduitCode();
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getAmountString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(amount);
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Double getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(Double otherAmount) {
        this.otherAmount = otherAmount;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getPendingValue() {
        if (pending == null) {
            return "";
        }
        return pending ? "1" : "0";
    }

    public boolean isPending() {
        return pending == null ? false : pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }

    public Date getOriginationDate() {
        return originationDate;
    }

    public void setOriginationDate(Date originationDate) {
        this.originationDate = originationDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(String depositNumber) {
        this.depositNumber = depositNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPostingSequence() {
        return postingSequence;
    }

    public void setPostingSequence(Integer postingSequence) {
        this.postingSequence = postingSequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSuppressAlertValue() {
        if (suppressAlert == null) {
            return "";
        }
        return suppressAlert ? "1" : "0";
    }

    public void setSuppressAlert(Boolean suppressAlert) {
        this.suppressAlert = suppressAlert;
    }

    public String getFitId() {
        return fitId;
    }

    public void setFitId(String fitId) {
        this.fitId = fitId;
    }


    public enum TransactionType {
        CREDIT("c"),
        DEBIT("d");
        private String conduitCode;

        TransactionType(String conduitCode) {
            this.conduitCode = conduitCode;
        }

        public String getConduitCode() {
            return conduitCode;
        }

        public String getDbCode() {
            return "c".equals(conduitCode) ? "0" : "1";
        }

        public static TransactionType getRandom() {
            return values()[(ThreadLocalRandom.current().nextInt(values().length))];
        }
    }

    public enum TransactionStatus {
        CANCELLED,
        FAILED,
        PENDING,
        PROCESSED
    }

    private String uid; // Required
    private Boolean delete; // Optional
    private TransactionType type; // Required
    private Double amount; // Required
    private Double principalAmount; // Optional
    private Double interestAmount; // Optional
    private Double otherAmount; //Optional
    private Date postDate; // Required
    private Boolean pending; // Required if creating
    private String confirmationNumber; // Optional
    private String merchantCategoryCode; // Required
    private Double runningBalance; // Optional
    private Date originationDate; // Optional
    private String code; // Optional
    private String checkNumber; // Optional
    private String depositNumber; // Optional
    private String image; //Optional
    private Integer postingSequence = 1; // Required
    private String name; // Required
    private String memo; // Optional
    private Boolean suppressAlert; // Optional
    private String fitId; // Optional


    public D3Transaction(String name, TransactionType type, Double amount, Date postDate, Boolean pending,
            String merchantCategoryCode) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.postDate = postDate;
        this.pending = pending;
        this.merchantCategoryCode = merchantCategoryCode;
        this.uid = name.replaceAll(" ", "-") + "-" + getRandomString(10);
        this.image = "checkimg-1001.jpg";

        validateUIDLength();
    }

    public D3Transaction(String name, String checkNumber, TransactionType type, Double amount, Date postDate, Boolean pending) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.postDate = postDate;
        this.pending = pending;
        this.checkNumber = checkNumber;
        this.uid = name.replaceAll(" ", "-") + "-" + getRandomString(10);
        this.image = "checkimg-1001.jpg";
        validateUIDLength();
    }

    public D3Transaction(String name, TransactionType type, Double amount, Date postDate, Boolean pending) {
        this(name, type, amount, postDate, pending, null);
    }

    public static D3Transaction generateRandomTransaction() {
        Person person = Fairy.create().person();
        TransactionType type = TransactionType.getRandom();
        Double amount = getRandomNumber(1, 500);
        DateTime date = getRandomPastDate();
        boolean pending = ThreadLocalRandom.current().nextBoolean();

        return new D3Transaction(person.getFullName(), type, amount, date.toDate(), pending);
    }


    /**
     * Create a copy of a transaction
     *
     * @param changeAmountAndDate Set to true if you want to have a new random amount (1-500) and post date
     * @return a new transaction
     */
    private D3Transaction createCopy(boolean changeAmountAndDate) {
        D3Transaction copy = new D3Transaction(name, type, amount, postDate, pending);
        if (changeAmountAndDate) {
            copy.setAmount(getRandomNumber(1, 500));
            copy.setPostDate(new DateTime().toDate());
        }
        return copy;
    }


    public static List<D3Transaction> generateRandomTransactions(int numOfTransactions, ProductType type) {
        List<D3Transaction> transactions = new ArrayList<>();
        Assert.assertTrue(numOfTransactions > 0);
        for (int i = 0; i < numOfTransactions / 2; ++i) {
            D3Transaction transaction = generateRandomTransaction();

            if (type.equals(ProductType.DEPOSIT_CHECKING) || type.equals(ProductType.DEPOSIT_SAVINGS)) {
                transaction.setCheckNumber(i + getRandomNumberString(3, true));
                transaction.setPending(false);
            }

            D3Transaction transactionCopy = transaction.createCopy(true);
            transactions.add(transaction);
            transactions.add(transactionCopy);
        }

        D3Transaction debitTransaction = generateRandomTransaction();
        debitTransaction.setType(TransactionType.DEBIT);
        debitTransaction.setPostDate(new DateTime().toDate());
        debitTransaction.setAmount(getRandomNumber(1, 500));

        D3Transaction transactionCopy = debitTransaction.createCopy(true);
        transactions.add(debitTransaction);
        transactions.add(transactionCopy);

        return transactions;
    }


    public String toString() {
        return String.format("Transaction: %s $%s %s Pending: %s %s", name, getAmountString(), type, pending, postDate);
    }

    private void validateUIDLength() {
        // UID has a max length of 31 chars
        if (this.uid != null && this.uid.length() > 30) {
            this.uid = this.uid.substring(0, 30);
        }
    }

}
