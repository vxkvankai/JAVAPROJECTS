package com.d3.conduit;

import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.api.helpers.banking.ConduitApiHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.datawrappers.common.D3Attribute;
import com.d3.datawrappers.company.D3Company;
import com.d3.datawrappers.user.D3Address;
import com.d3.datawrappers.user.D3Contact;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.D3UserProfile;
import com.d3.datawrappers.user.enums.AccountAction;
import com.d3.exceptions.ConduitException;
import com.d3.exceptions.D3ApiException;
import com.google.common.base.Strings;
import com.jamesmurty.utils.XMLBuilder2;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.joda.time.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.CheckForNull;
import javax.xml.transform.OutputKeys;

@Slf4j
public class ConduitBuilder {

    private static final String TRANSACTION_BEGIN_DATE_DEFAULT = "2014-01-01";
    private static DecimalFormat df = new DecimalFormat(".##");
    private XMLBuilder2 builder;
    private boolean accountLstStarted = false;
    private boolean accountLstCompleted = false;
    private boolean companyLstStarted = false;
    private boolean companyLstCompleted = false;
    private boolean userLstStarted = false;
    private boolean userLstCompleted = false;
    private ConduitVersion conduitVersion;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String transactionBeginDate;
    private String transactionEndDate;
    private String baseFileName;

    public ConduitBuilder(ConduitVersion version, Date transactionBeginDate, Date transactionEndDate) {
        df.setMinimumFractionDigits(2);
        conduitVersion = version;
        this.transactionBeginDate =
            transactionBeginDate != null ? dateFormat.format(transactionBeginDate) : TRANSACTION_BEGIN_DATE_DEFAULT;
        Date transactionEndDateDefault = new Date();
        this.transactionEndDate =
            transactionEndDate != null ? dateFormat.format(transactionEndDate) : dateFormat.format(transactionEndDateDefault);
        builder = XMLBuilder2.create("conduit");
        createHeader();
        this.baseFileName = "conduit-" + dateFormat.format(transactionEndDateDefault) + "-";
    }

    public ConduitBuilder() {
        this(ConduitVersion.THREE, null, null);
    }

    private ConduitBuilder createHeader() {
        // start hdr and ver tags
        builder = builder.e("hdr");
        addSingleElement("ver", conduitVersion.getConduitCode())
            .addSingleElement("rqid", getRandomString(20));
        builder = builder.e("authn").a("p", "password").a("u", "user").up();
        builder = builder.e("txndtrng").a("bdt", transactionBeginDate).a("edt", transactionEndDate).up();
        // end on main conduit tag
        builder = builder.up();

        return this;
    }

    private Properties getOutputProperties() {
        Properties properties = new Properties();

        properties.put(OutputKeys.INDENT, "yes");
        properties.put(OutputKeys.VERSION, "1.0");
        properties.put(OutputKeys.ENCODING, "UTF-8");
        properties.put(OutputKeys.STANDALONE, "no");
        return properties;
    }

    public String getDocument() {
        return builder.asString(getOutputProperties());
    }

    /**
     * Saves a copy of the generated conduit file to the conduit/ folder with the naming format of conduit-{currentDate}-{profileType}-{loginId}.xml
     *
     * @return String - path of the saved conduit file, null if error
     */
    @CheckForNull
    public String saveConduitFileLocally() {
        File temp = new File(String.format("conduit/%s%s-%s.xml",
            baseFileName,
            builder.xpathFind("//pt").getElement().getTextContent(),
            builder.xpathFind("//login").getElement().getTextContent()));
        log.info("Attempting to save file: {}", temp.getPath());

        try (FileWriter fileWriter = new FileWriter(temp)) {
            BufferedWriter writer = new BufferedWriter(fileWriter);
            builder.toWriter(writer, getOutputProperties());
            return temp.getPath();
        } catch (IOException e) {
            log.error("There was a problem trying to save the conduit file {}", e);
            return null;
        }
    }

    /**
     * Upload the conduit file to a server via the api
     *
     * @param fi Which api to use (used for loging into the api)
     * @param baseApiUrl URL of the api - NOTE (JMoravec 9/25/2017): This requires base url, up to .com, not the api version endpoint
     * @param apiVersion Version of the api
     * @return The existing conduit builder object
     */
    public ConduitBuilder uploadFileToApi(String fi, String baseApiUrl, String apiVersion) throws ConduitException {
        log.info("Logging into access conduit API: {}, {}, {}", fi, baseApiUrl, apiVersion);
        int maxTries = 2;
        for (int i = 0; i < maxTries; ++i) {
            try {
                ConduitApiHelper api = new ConduitApiHelper(baseApiUrl, apiVersion);
                api.login(UserDatabaseHelper.getRandomUserName(fi), "password", "denver");
                log.info("Sending Conduit file through API to fi: {}", fi);
                api.sendConduitFileToApi(getDocument());
                return this;
            } catch (D3ApiException e) {
                log.error("Conduit API call failed", e);
            }
            log.error("Try nubmer: {}, trying again if below {}", i, maxTries);
        }
        throw new ConduitException("Error connecting to conduit");
    }

    public ConduitBuilder startCompanyLstNode() throws ConduitException {
        if (companyLstCompleted || companyLstStarted) {
            throw new ConduitException("complst node already completed or started");
        }

        builder = builder.e("complst");
        companyLstStarted = true;
        return this;
    }

    public ConduitBuilder closeCompanyLstNode() throws ConduitException {
        if (companyLstCompleted || !companyLstStarted) {
            throw new ConduitException("complst node was already completed or not started");
        }

        builder = builder.up();
        companyLstCompleted = true;
        companyLstStarted = false;
        return this;
    }

    public ConduitBuilder startAttributeLstNode() {
        builder = builder.e("alst");
        return this;
    }

    public ConduitBuilder closeAttributeLstNode() {
        builder = builder.up();
        return this;
    }

    public ConduitBuilder addCompanies(List<D3Company> companies) throws ConduitException {
        startCompanyLstNode();
        for (D3Company company : companies) {
            addCompany(company);
        }
        closeCompanyLstNode();
        return this;
    }

    public ConduitBuilder addAttributes(List<D3Attribute> attributes) {
        startAttributeLstNode();
        for (D3Attribute attribute : attributes) {
            builder = builder.e("a");
            addSingleAttribute("n", attribute.getName())
                .addSingleAttribute("type", attribute.getAttributeType()).builder.t(attribute.getValue()).up();
            builder = builder.up();
        }
        closeAttributeLstNode();
        return this;
    }

    private ConduitBuilder addCompany(D3Company company) {
        builder = builder.e("comp");
        addSingleAttribute("uid", company.getUid())
            .addSingleAttribute("puid", company.getPuid())
            .addSingleElement("tp", company.getType());
        if (!company.getAttributes().isEmpty()) {
            addAttributes(company.getAttributes());
        }
        builder = builder.up();
        return this;
    }

    public ConduitBuilder startAccountListNode() throws ConduitException {
        if (accountLstCompleted || accountLstStarted) {
            throw new ConduitException("acctlst node already completed or started");
        }

        builder = builder.e("acctlst");
        accountLstStarted = true;
        return this;
    }

    public ConduitBuilder closeAccountLstNode() throws ConduitException {
        if (accountLstCompleted || !accountLstStarted) {
            throw new ConduitException("acctlst node was already completed or not started");
        }

        builder = builder.up();
        accountLstCompleted = true;
        accountLstStarted = false;
        return this;
    }

    public ConduitBuilder addAccounts(List<D3Account> accounts) throws ConduitException {
        startAccountListNode();
        accounts.forEach(this::addAccount);
        closeAccountLstNode();
        return this;
    }

    public ConduitBuilder addAccounts(D3Account account) throws ConduitException {
        startAccountListNode();
        addAccount(account);
        closeAccountLstNode();
        return this;
    }

    private ConduitBuilder addAccount(D3Account account) {
        builder = builder.e("acct");
        addSingleAttribute("uid", account.getUid())
            .addSingleAttribute("cuid", account.getCuid())
            .addSingleAttribute("del", account.getDeleteValue())
            .addSingleElement("produid", account.getProductUniqIdValue(), true)
            .addSingleElement("nm", account.getName())
            .addSingleElement("rttn", account.getRoutingNumber(), true)
            .addSingleElement("nbr", account.getNumber(), true)
            .addSingleElement("bal", df.format(account.getBalance()))
            .addSingleElement("avbal", df.format(account.getAvailableBalance()))
            .addSingleElement("sts", account.getStatusValue())
            .addSingleElement("cc", account.getCurrencyCode())
            .addSingleElement("rstr", account.getRestricted())
            .addSingleElement("estmt", account.getEstatementValue())
            .addSingleElement("opndt", account.getOpenDate())
            .addSingleElement("clsdt", account.getClosedDate())
            .addTransactionList(account.getTransactionList());
        if (!account.getAttributes().isEmpty()) {
            addAttributes(account.getAttributes());
        }
        builder = builder.up();
        return this;
    }

    private ConduitBuilder addTransactionList(List<D3Transaction> transactionList) {
        if (transactionList != null) {
            builder = builder.e("txnlst");
            transactionList.forEach(this::addTransaction);
            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addTransaction(D3Transaction transaction) {
        builder = builder.e("txn");
        addSingleAttribute("uid", transaction.getUid(), true)
            .addSingleAttribute("del", transaction.getDeleteValue())
            .addSingleAttribute("tp", transaction.getTypeValue(), true)
            .addSingleAttribute("am", transaction.getAmountString(), true)
            .addSingleAttribute("pa", transaction.getPrincipalAmount())
            .addSingleAttribute("ia", transaction.getInterestAmount())
            .addSingleAttribute("oa", transaction.getOtherAmount())
            .addSingleAttribute("pd", transaction.getPostDate(), true)
            .addSingleAttribute("pn", transaction.getPendingValue())
            .addSingleAttribute("cf", transaction.getConfirmationNumber())
            .addSingleAttribute("mc", transaction.getMerchantCategoryCode())
            .addSingleAttribute("rb", transaction.getRunningBalance())
            .addSingleAttribute("od", transaction.getOriginationDate())
            .addSingleAttribute("tc", transaction.getCode())
            .addSingleAttribute("cn", transaction.getCheckNumber())
            .addSingleAttribute("dn", transaction.getDepositNumber())
            .addSingleAttribute("im", transaction.getImage())
            .addSingleAttribute("ps", transaction.getPostingSequence(), true)
            .addSingleAttribute("sa", transaction.getSuppressAlertValue())
            .addSingleAttribute("fid", transaction.getFitId())
            .addSingleElement("nm", transaction.getName(), true)
            .addSingleAttribute("mm", transaction.getMemo());
        builder = builder.up();
        return this;
    }

    public ConduitBuilder startUserListNode() throws ConduitException {
        if (userLstCompleted || userLstStarted) {
            throw new ConduitException("UserLst tag already completed or started");
        }

        builder = builder.e("usrlst");
        userLstStarted = true;
        return this;
    }

    public ConduitBuilder closeUserLstNode() throws ConduitException {
        if (userLstCompleted || !userLstStarted) {
            throw new ConduitException("User list node was already completed or not started");
        }

        builder = builder.up();
        userLstCompleted = true;
        userLstStarted = false;
        return this;
    }

    public ConduitBuilder addUsers(List<D3User> users) throws ConduitException {
        startUserListNode();
        for (D3User user : users) {
            addUser(user);
        }
        closeUserLstNode();
        return this;
    }

    public ConduitBuilder addUsers(D3User user) throws ConduitException {
        startUserListNode();
        addUser(user);
        closeUserLstNode();
        return this;
    }

    private ConduitBuilder addUser(D3User user) {
        builder = builder.e("usr");
        addSingleAttribute("uid", user.getUid())
            .addSingleAttribute("cuid", user.getCuID())

            .addSingleAttribute("del", String.valueOf(user.getDelete()))
            .addSingleElement("login", user.getLogin(), true)
            .addSingleElement("ut", user.getLevelValue(), true)
            .addSingleElement("enrl", String.valueOf(user.getEnrolled()))
            .addUserProfile(user)
            .addUserAccountList(user.getUserAccounts())

            .addUserContactList(user.getUserContacts())
            .addAttributeList(user.getUserAttributes());
        builder = builder.up();

        return this;
    }

    private ConduitBuilder addUserContactList(List<D3Contact> contacts) {
        if (contacts != null && !contacts.isEmpty()) {
            builder = builder.e("usrcnctlst");

            for (D3Contact contact : contacts) {
                addUserContact(contact);
            }

            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addUserContact(D3Contact contact) {
        if (contact != null) {
            builder = builder.e("usrcnct");

            addSingleAttribute("uid", contact.getUid(), true)
                .addSingleElement("ctp", contact.getTypeValue(), true)
                .addSingleElement("lbl", contact.getLabelValue(), true)
                .addSingleElement("val", contact.getValue(), true)
                .addSingleElement("oob", String.valueOf(contact.getOutOfBandValue()))
                .addSingleElement("pri", String.valueOf(contact.isPrimary()))
                .addSingleElement("sms", String.valueOf(contact.getSms()));

            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addUserAccountList(List<Pair<D3Account, AccountAction>> accounts) {
        if (accounts != null && !accounts.isEmpty()) {
            builder = builder.e("usracctlst");

            for (Pair<D3Account, AccountAction> account : accounts) {
                addUserAccount(account);
            }

            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addUserAccount(Pair<D3Account, AccountAction> account) {
        if (account != null && account.getSize() != 0) {
            builder = builder.e("usracct");
            addSingleAttribute("auid", account.getValue0().getUid())
                .addSingleAttribute("a", account.getValue1().getConduitCode());
            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addUserProfile(D3User user) {
        return addUserProfile(user.getProfile());
    }

    private ConduitBuilder addUserProfile(D3UserProfile user) {
        builder = builder.e("usrpfl");

        addSingleElement("pt", user.getType().getConduitCode(), true)
            .addSingleElement("fn", user.getFirstName())
            .addSingleElement("mn", user.getMiddleName())
            .addSingleElement("ln", user.getLastName())
            .addSingleElement("bn", user.getBusinessName())
            .addSingleElement("emp", user.getEmployeeValue())
            .addSingleElement("txid", user.getTaxId())
            .addSingleElement("txtp", user.getTaxIdType())
            .addSingleElement("m", user.getMobileValue())
            .addSingleElement("emlopt", user.getEmailOptOutValue())
            .addSingleElement("dob", user.getDateOfBirth())
            .addSingleElement("g", user.getGender())
            .addSingleElement("crsc", user.getCreditScore())
            .addSingleElement("crdt", user.getCreditScoreDate())
            .addSingleElement("cls", user.getUserClass())
            .addAddress("paddr", user.getPhysicalAdd())
            .addAddress("maddr", user.getMailingAdd());

        // end usrpfl
        builder = builder.up();
        return this;
    }

    private ConduitBuilder addAddress(String elementName, D3Address address) {
        if (address != null) {
            builder = builder.e(elementName);

            addSingleElement("a1", address.getAddress1())
                .addSingleElement("a2", address.getAddress2())
                .addSingleElement("a3", address.getAddress3())
                .addSingleElement("a4", address.getAddress4())
                .addSingleElement("ct", address.getCity())
                .addSingleElement("st", address.getState())
                .addSingleElement("cc", address.getCountryCode())
                .addSingleElement("pc", address.getPostalCode())
                .addSingleElement("lt", address.getLatitude())
                .addSingleElement("Ln", address.getLongitude());

            // end addr element node
            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addConduitAttribute(D3Attribute attribute) {
        if (attribute != null) {
            builder = builder.e("a").t(attribute.getValue());
            addSingleAttribute("n", attribute.getName())
                .addSingleAttribute("t", attribute.getAttributeType())
                .addSingleAttribute("v", attribute.isVisible())
                .addSingleAttribute("d", attribute.getDisplayOrder());
            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addAttributeList(List<D3Attribute> attributes) {
        if (attributes != null) {
            builder = builder.e("alst");
            for (D3Attribute attribute : attributes) {
                addConduitAttribute(attribute);
            }
            builder = builder.up();
        }
        return this;
    }

    private ConduitBuilder addSingleElement(String elementName, String textValue) {
        return addSingleElement(elementName, textValue, false);
    }

    private ConduitBuilder addSingleElement(String elementName, Integer value) {
        String valueStr = value != null ? String.valueOf(value) : "";
        return addSingleElement(elementName, valueStr);
    }

    private ConduitBuilder addSingleElement(String elementName, Double value) {
        String valueStr = value != null ? String.valueOf(value) : "";
        return addSingleElement(elementName, valueStr);
    }

    private ConduitBuilder addSingleElement(String elementName, Date date) {
        String value = date != null ? dateFormat.format(date) : "";
        return addSingleElement(elementName, value);
    }

    private ConduitBuilder addSingleElement(String elementName, DateTime date) {
        String value = date != null ? dateFormat.format(date.toDate()) : "";
        return addSingleElement(elementName, value);
    }

    private ConduitBuilder addSingleElement(String elementName, String textValue, boolean required) {
        return addSingleElementOrAttribute(elementName, textValue, required, ElementType.ELEMENT);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, Integer value, boolean required) {
        String txtValue = value != null ? String.valueOf(value) : "";
        return addSingleAttribute(attributeName, txtValue, required);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, Integer value) {
        return addSingleAttribute(attributeName, value, false);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, Boolean bool) {
        if (bool == null) {
            return addSingleAttribute(attributeName, "");
        }
        String txtValue = bool ? "1" : "0";
        return addSingleAttribute(attributeName, txtValue);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, Date date, boolean required) {
        String value = date != null ? dateFormat.format(date) : "";
        return addSingleAttribute(attributeName, value, required);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, Date date) {
        return addSingleAttribute(attributeName, date, false);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, Double value, boolean required) {
        String txtValue = value != null ? String.valueOf(value) : "";
        return addSingleAttribute(attributeName, txtValue, required);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, Double value) {
        return addSingleAttribute(attributeName, value, false);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, String textValue) {
        return addSingleAttribute(attributeName, textValue, false);
    }

    private ConduitBuilder addSingleAttribute(String attributeName, String textValue, boolean required) {
        return addSingleElementOrAttribute(attributeName, textValue, required, ElementType.ATTRIBUTE);
    }

    private ConduitBuilder addSingleElementOrAttribute(String elementAttributeName, String textValue, boolean required,
        ElementType type) {
        if (required && Strings.isNullOrEmpty(textValue)) {
            throw new NullPointerException(textValue);
        } else if (!Strings.isNullOrEmpty(textValue)) {
            if (type == ElementType.ELEMENT) {
                builder = builder.e(elementAttributeName).t(textValue).up();
            } else {
                builder = builder.a(elementAttributeName, textValue);
            }
        }
        return this;
    }

    private enum ElementType {
        ELEMENT,
        ATTRIBUTE
    }

    public enum ConduitVersion {
        THREE("3.0");

        private final String conduitCode;

        ConduitVersion(String conduitCode) {
            this.conduitCode = conduitCode;
        }

        public String getConduitCode() {
            return conduitCode;
        }
    }
}
