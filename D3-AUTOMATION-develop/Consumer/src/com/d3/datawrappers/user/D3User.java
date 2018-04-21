

package com.d3.datawrappers.user;

import static com.d3.helpers.RandomHelper.getRandomNumber;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.common.D3Attribute;
import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientType;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.user.enums.AccountAction;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.datawrappers.user.enums.UserLevel;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.helpers.RandomHelper;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
import org.javatuples.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class D3User implements Serializable, Loginable {

    private static final String INBOX_STR = "Inbox";

    private String uid; // Required
    private String cuID; // Required
    private Boolean delete; // Optional
    private String login; // Required if Consumer
    private UserLevel level; // Required
    private Boolean enrolled; // Optional (default false)
    private D3UserProfile profile; // Required
    private List<Pair<D3Account, AccountAction>> userAccounts;
    private List<JFairyCompany> associatedCompanies;
    private List<Recipient> recipients;
    private List<D3Contact> userContacts;
    private String password;
    private String secretQuestion;
    private List<D3Attribute> userAttributes;
    private boolean toggleUser;
    private ToggleMode toggleMode;
    private UserType userType;

    public static class Builder {

        private String login;
        private final String cuId;
        private final D3UserProfile profile;

        private String uid = "";
        private Boolean delete = false;
        private UserLevel level = UserLevel.PRIMARY;
        private Boolean enrolled = true;
        private List<Pair<D3Account, AccountAction>> userAccounts = new ArrayList<>();
        private List<JFairyCompany> associatedCompanies = new ArrayList<>();
        private List<Recipient> recipients = new ArrayList<>();
        private List<D3Contact> userContacts = new ArrayList<>();
        private String secretQuestion = "denver";
        private String psswd = "password";
        private List<D3Attribute> userAttributes = new ArrayList<>();
        private boolean toggleUser = false;
        private ToggleMode toggleMode = ToggleMode.NONE;
        private UserType userType = UserType.PRIMARY_CONSUMER_USER;

        private boolean forceLogin = false;

        public Builder(@Nonnull String login, @Nonnull String cuId, @Nonnull D3UserProfile profile) {
            this.login = login;
            this.cuId = cuId;
            this.profile = profile;
            this.uid = "host-" + login + RandomHelper.getRandomString(3);
        }

        public Builder(@Nonnull Person person, @Nonnull String cuId) {
            this(person.getUsername(), cuId, new D3UserProfile(person));
        }

        public Builder(@Nonnull JFairyCompany company, @Nonnull String cuId) {
            this(company.getLogin(), cuId, new D3UserProfile(company));
        }

        public Builder forceLogin() {
            forceLogin = true;
            return this;
        }

        public Builder withPassword(@Nonnull String password) {
            this.psswd = password;
            return this;
        }

        public Builder withDelete(boolean delete) {
            this.delete = delete;
            return this;
        }

        public Builder withEnrolled(boolean enrolled) {
            this.enrolled = enrolled;
            return this;
        }

        public Builder withUserLevel(@Nonnull UserLevel level) {
            this.level = level;
            return this;
        }

        public Builder withAccount(@Nonnull Pair<D3Account, AccountAction> account) {
            userAccounts.add(account);
            return this;
        }

        public Builder withAccounts(@Nonnull List<D3Account> accounts) {
            for (D3Account account : accounts) {
                withAccount(Pair.with(account, AccountAction.ASSOCIATE));
            }
            return this;
        }

        public Builder withCompany(@Nonnull JFairyCompany company) {
            associatedCompanies.add(company);
            return this;
        }

        public Builder withCompanies(@Nonnull List<JFairyCompany> companies) {
            associatedCompanies.addAll(companies);
            return this;
        }

        public Builder withRecipient(@Nonnull Recipient recipient) {
            recipients.add(recipient);
            return this;
        }

        public Builder withContact(@Nonnull D3Contact contact) {
            return withContact(contact, false);
        }

        public Builder withContact(@Nonnull D3Contact contact, boolean forcePrimary) {
            if (!forcePrimary && userContacts.stream()
                    .anyMatch(contactTest -> contactTest.isPrimary() && contactTest.getType() == contact.getType())) {
                LoggerFactory.getLogger(getClass()).warn("{} type already has a primary contact set for this user", contact.getType());
                LoggerFactory.getLogger(getClass()).warn("Setting {} to not be primary", contact.getValue());
                contact.setPrimary(false);
            }
            userContacts.add(contact);
            return this;
        }

        public Builder withSecretAnswer(@Nonnull String answer) {
            secretQuestion = answer;
            return this;
        }

        public Builder withAttribute(@Nonnull D3Attribute attribute) {
            userAttributes.add(attribute);
            return this;
        }

        public Builder setToggleUser(@Nonnull ToggleMode mode) {
            this.toggleUser = true;
            this.toggleMode = mode;
            return this;
        }

        public Builder withUserType(@Nonnull UserType type) {
            this.userType = type;
            return this;
        }

        public Builder withCommonContacts(@Nonnull Person person) {
            return withContact(
                    new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.PRIMARY, person.getTelephoneNumber().replace("-", "")))
                    .withContact(
                            new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.HOME, person.getTelephoneNumber().replace("-", "")))
                    .withContact(
                            new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.WORK, person.getTelephoneNumber().replace("-", "")))
                    .withContact(
                            new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.MOBILE, person.getTelephoneNumber().replace("-", "")))
                    .withContact(new D3Contact(D3Contact.ContactType.EMAIL, D3Contact.ContactLabel.PRIMARY, person.getEmail()))
                    .withContact(new D3Contact(D3Contact.ContactType.INBOX, D3Contact.ContactLabel.INBOX, INBOX_STR));
        }

        public Builder withCommonContacts(@Nonnull JFairyCompany company) {
            withContact(new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.WORK,
                    Fairy.create().person(PersonProperties.telephoneFormat("###-###-####")).getTelephoneNumber().replace("-", "")))

                    .withContact(new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.MOBILE,
                            Fairy.create().person(PersonProperties.telephoneFormat("###-###-####")).getTelephoneNumber().replace("-", "")))
                    .withContact(new D3Contact(D3Contact.ContactType.EMAIL, D3Contact.ContactLabel.PRIMARY, company.getEmail()))
                    .withContact(new D3Contact(D3Contact.ContactType.INBOX, D3Contact.ContactLabel.INBOX, INBOX_STR));
            return this;
        }

        public D3User build() {
            if (!forceLogin) {
                login += RandomHelper.getRandomString(3);
            }
            return new D3User(this);
        }

    }

    D3User(Builder builder) {
        this.login = builder.login;
        this.password = builder.psswd;
        this.cuID = builder.cuId;
        this.profile = builder.profile;
        this.uid = builder.uid;
        this.delete = builder.delete;
        this.level = builder.level;
        this.enrolled = builder.enrolled;
        this.userAccounts = builder.userAccounts;
        this.associatedCompanies = builder.associatedCompanies;
        this.recipients = builder.recipients;
        this.userContacts = builder.userContacts;
        this.secretQuestion = builder.secretQuestion;
        this.userAttributes = builder.userAttributes;
        this.toggleUser = builder.toggleUser;
        this.toggleMode = builder.toggleMode;
        this.userType = builder.userType;
    }

    public List<D3Contact> getUserContacts() {
        return userContacts;
    }

    public String getPassword() {
        return password;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    public ToggleMode getToggleMode() {
        return toggleMode;
    }

    public void setToggleMode(ToggleMode toggleMode) {
        this.toggleMode = toggleMode;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<D3Attribute> getUserAttributes() {
        return userAttributes;
    }

    public String getLogin() {
        return login;
    }

    public String getCuID() {
        return cuID;
    }

    public boolean getEnrolledValue() {
        return enrolled;
    }

    public String getLevelValue() {
        return this.level != null ? level.getConduitCode() : "";
    }

    public String getUid() {
        return uid;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isToggleUser() {
        return toggleUser;
    }

    public void setToggleUser(boolean toggleUser) {
        this.toggleUser = toggleUser;
    }

    public D3UserProfile getProfile() {
        return profile;
    }

    public void setProfile(D3UserProfile profile) {
        this.profile = profile;
    }

    public List<Pair<D3Account, AccountAction>> getAccountPairs() {
        return userAccounts;
    }

    public List<JFairyCompany> getCompanies() {
        return associatedCompanies;
    }

    public JFairyCompany getFirstCompany() {
        return getCompanies().get(0);
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public List<Recipient> getRecipients() {
        List<Recipient> list = new ArrayList<>();
        list.addAll(recipients);
        return list;
    }

    @CheckForNull
    public Recipient getFirstRecipientByType(RecipientWho endpointType) {
        return getRecipients().stream().filter(recipient -> recipient.getWho() == endpointType).findFirst().orElse(null);
    }

    @CheckForNull
    public Recipient getFirstBillPayRecipientByType(RecipientWho endpointType) {
        return getRecipients().stream()
                .filter(recipient -> recipient.getType() == RecipientType.BILL_AND_ADDRESS && recipient.getWho() == endpointType).findFirst()
                .orElse(null);
    }

    public List<D3Account> getAccounts() {
        return userAccounts.stream().map(Pair::getValue0).collect(Collectors.toList());
    }

    public D3Account getFirstAccount() {
        return getAccounts().get(0);
    }

    @Nullable
    public D3Account getFirstAccountByType(ProductType type) {
        return getAccounts().stream().filter(account -> account.getProductType() == type).findFirst().orElse(null);
    }

    @Nullable
    public D3Account getFirstAccountByAccountingClass(D3Account.AccountingClass accountingClass) {
        return getAccounts().stream().filter(account -> account.getProductType().getAccountingClass() == accountingClass).findFirst().orElse(null);
    }


    @CheckForNull
    public D3Contact getAnEmailContact() {
        return userContacts.stream().filter(d3Contact -> d3Contact.getType() == D3Contact.ContactType.EMAIL).findAny().orElse(null);
    }

    @CheckForNull
    public D3Contact getAPhoneNumberContact() {
        return userContacts.stream().filter(d3Contact -> d3Contact.getType() == D3Contact.ContactType.PHONE).findAny().orElse(null);
    }

    /**
     * Get primary D3Contact for specified type.
     *
     * @param type D3Contact ContactType {@link com.d3.datawrappers.user.D3Contact.ContactType}
     * @return Primary D3Contact for specific contact type. Will return null if no primary contacts found
     */
    @CheckForNull
    public D3Contact getPrimaryContactForType(D3Contact.ContactType type) {
        return userContacts.stream().filter(d3Contact -> d3Contact.getType() == type && d3Contact.isPrimary()).findFirst().orElse(null);
    }

    public String toString() {
        return String.format("User: login: %s, User Type: %s", getLogin(), getUserType());
    }

    public D3Account getRandomAccount() {
        getLogger().info("Getting random account from user");
        if (getAccounts().isEmpty()) {
            getLogger().warn("No account to get");
            return null;
        }
        return getAccounts().get(ThreadLocalRandom.current().nextInt(getAccounts().size()));
    }

    @Nullable
    public List<D3Account> getAssetAccounts() {
        getLogger().info("Getting asset accounts from user");
        if (getAccounts().isEmpty()) {
            getLogger().warn("No accounts to get");
            return null;
        }

        return userAccounts.stream().filter(pair -> pair.getValue0().getProductType().getAccountingClass().equals(D3Account.AccountingClass.ASSET))
                .map(Pair::getValue0).collect(Collectors.toList());
    }

    @Nullable
    public List<D3Account> getLiabilityAccounts() {
        getLogger().info("Getting liability accounts from user");
        if (getAccounts().isEmpty()) {
            getLogger().warn("No accounts to get");
            return null;
        }
        return userAccounts.stream()
                .filter(pair -> pair.getValue0().getProductType().getAccountingClass().equals(D3Account.AccountingClass.LIABILITY))
                .map(Pair::getValue0).collect(Collectors.toList());
    }

    @CheckForNull
    public D3Transaction getRandomTransactionFromChecking() {
        D3Account checking = null;
        for (D3Account account : getAccounts()) {
            if (account.getProductType() == ProductType.DEPOSIT_CHECKING || account.getProductType() == ProductType.BUSINESS_DEPOSIT_CHECKING) {
                checking = account;
            }
        }
        getLogger().info("Getting transaction from {} account", checking);
        if (checking != null) {
            List<D3Transaction> transactions = checking.getTransactionList();
            return transactions.isEmpty() ? null : transactions.get(ThreadLocalRandom.current().nextInt(transactions.size()));
        } else {
            return null;
        }
    }

    @Nullable
    public D3Transaction getRandomTransaction() {
        D3Account randomAccount = getRandomAccount();
        getLogger().info("Getting random transaction from account: {}", randomAccount);
        for (int i = 0; i < 5; ++i) {
            if (randomAccount == null || randomAccount.getTransactionList().isEmpty()) {
                getLogger().warn("No transaction to get from account: {}", randomAccount);
                randomAccount = getRandomAccount();
            } else {
                break;
            }
        }
        if (randomAccount != null) {
            List<D3Transaction> transactions = randomAccount.getTransactionList();
            return transactions.get(ThreadLocalRandom.current().nextInt(transactions.size()));
        } else {
            return null;
        }
    }


    @Nullable
    public D3Transaction getRandomPostedTransaction() {
        D3Account randomAccount = getRandomAccount();
        List<D3Transaction> postedTransactions = new ArrayList<>();
        getLogger().info("Getting random posted transaction from account: {}", randomAccount);
        for (int i = 0; i < 5; ++i) {
            if (randomAccount == null || randomAccount.getTransactionList().stream().filter(transaction -> !transaction.isPending())
                    .collect(Collectors.toList()).isEmpty()) {
                getLogger().warn("No posted transaction to get from account: {}", randomAccount);
                randomAccount = getRandomAccount();
            } else {
                postedTransactions =
                        randomAccount.getTransactionList().stream().filter(transaction -> !transaction.isPending()).collect(Collectors.toList());
                break;
            }
        }
        if (randomAccount != null) {
            return postedTransactions.get(ThreadLocalRandom.current().nextInt(postedTransactions.size()));
        } else {
            return null;
        }
    }

    public DateTime getUserDateOfBirth(boolean setRandomYear) {
        return setRandomYear ? this.getProfile().getDateOfBirth().withYear(getRandomNumber(1960, 2000).intValue())
                : this.getProfile().getDateOfBirth();
    }
}