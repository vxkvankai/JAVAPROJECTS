package com.d3.datawrappers.user;

import static com.d3.datawrappers.account.ProductType.BUSINESS_CREDIT_CARD;
import static com.d3.datawrappers.account.ProductType.BUSINESS_DEPOSIT_CHECKING;
import static com.d3.datawrappers.account.ProductType.BUSINESS_DEPOSIT_SAVINGS;
import static com.d3.datawrappers.account.ProductType.CREDIT_CARD;
import static com.d3.datawrappers.account.ProductType.DEPOSIT_CHECKING;
import static com.d3.datawrappers.account.ProductType.DEPOSIT_SAVINGS;
import static com.d3.helpers.AccountHelper.getCompanyName;
import static com.d3.helpers.ConduitHelper.sendUserToServerAndWait;
import static com.d3.helpers.RandomHelper.getRandomNumberString;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.api.helpers.banking.TransactionApiHelper;
import com.d3.database.TransactionDatabaseHelper;
import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.company.JFairyCompany;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.exceptions.D3ApiException;
import com.d3.helpers.UserDataCreator;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.CompanyProperties;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@Slf4j
public class UserFactory {

    private static final String PHONE_FORMAT = "###-###-####";

    private UserFactory() {
    }

    /**
     * Generate an average user with bill pay enabled
     *
     * @param fi Fi to add the user to
     * @param baseApiUrl base API URL to use
     * @param restVersion the version of the api to use (and any additional api url, e.i. d3rest/3.2)
     * @deprecated use {@link UserDataCreator#enrollInBillPay(D3Account)}
     */
    @CheckForNull
    @Deprecated
    public static D3User averageUserWithBillPay(String fi, String baseApiUrl, String restVersion) throws D3ApiException {
        D3User user = generateAverageUser(fi, false);

        sendUserToServerAndWait(user, baseApiUrl, restVersion);

        D3Account checkingAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);

        Integer accountId = UserDatabaseHelper.waitForUserAccountId(user, checkingAccount);
        if (accountId == null) {
            return null;
        }
        MoneyMovementApiHelper api = new MoneyMovementApiHelper(baseApiUrl + restVersion, user);
        api.enrollInBillPay(user, accountId);
        return user;
    }

    /**
     * Generate an average user with a category
     *
     * @param fi Fi to add the user to
     * @param baseApiUrl base API URL to use
     * @param restVersion the version of the api to use (and any additional api url, e.i. d3rest/3.2)
     */
    public static D3User averageUserWithCategory(String fi, String baseApiUrl, String restVersion) throws D3ApiException {
        D3User user = generateAverageUser(fi, false);

        sendUserToServerAndWait(user, baseApiUrl, restVersion);
        TransactionApiHelper api = new TransactionApiHelper(baseApiUrl + restVersion);
        api.login(user);
        api.addCategory(user);
        return user;
    }

    /**
     * Generate an average user with a category rule
     *
     * @param fi Fi to add the user to
     * @param baseApiUrl base API URL to use
     * @param restVersion the version of the api to use (and any additional api url, e.i. d3rest/3.2)
     */
    public static D3User averageUserWithCategoryRule(String fi, String baseApiUrl, String restVersion) throws D3ApiException {
        D3User user = generateAverageUser(fi, false);
        sendUserToServerAndWait(user, baseApiUrl, restVersion);
        String categoryName = TransactionDatabaseHelper.getRandomCategoryName("CONSUMER");

        if (categoryName == null) {
            log.error("Error getting the category name from the database");
            throw new D3ApiException("Error creating user with category rule");
        }
        Integer categoryId = TransactionDatabaseHelper.getCategoryId(categoryName, "CONSUMER");
        Integer transactionId = TransactionDatabaseHelper.getRandomTransactionId(user);

        if (categoryId == null || transactionId == null) {
            log.error("Error getting category id or transactionid from the database");
            throw new D3ApiException("Error creating user with category rule");
        }

        TransactionApiHelper api = new TransactionApiHelper(baseApiUrl + restVersion);
        api.login(user);
        api.addCategoryRule(transactionId, categoryId);
        return user;
    }


    /**
     * Generates an average user with a renaming rule
     *
     * @param fi Fi to add the user to
     * @param baseApiUrl base API URL to use
     * @param restVersion the version of the api to use (and any additional api url, e.i. d3rest/3.2)
     */
    public static D3User averageUserWithRenamingRule(String fi, String baseApiUrl, String restVersion) throws D3ApiException {
        D3User user = generateAverageUser(fi, false);
        sendUserToServerAndWait(user, baseApiUrl, restVersion);
        Integer transactionId = TransactionDatabaseHelper.getRandomTransactionId(user);
        if (transactionId == null) {
            log.error("Error getting the transaction id from the database");
            throw new D3ApiException("Error generating a user with a renaming rule");
        }

        TransactionApiHelper api = new TransactionApiHelper(baseApiUrl + restVersion);
        api.login(user);
        api.addRenamingRule(transactionId);

        return user;
    }

    /**
     * Generates an average user
     *
     * @param fi Fi to add the user to
     * @param baseApiUrl base API URL to use
     */
    @CheckForNull
    public static D3User averageUser(String fi, String baseApiUrl, String apiVersion) {
        D3User user = generateAverageUser(fi, false);
        if (!sendUserToServerAndWait(user, baseApiUrl, apiVersion)) {
            return null;
        }
        return user;
    }

    /**
     * Sends an average user with updated information to the server
     *
     * @param user Modified user to send to server
     * @param baseApiUrl base API URL to use
     * @param apiVersion API version to use
     */
    @CheckForNull
    public static D3User modifiedUser(D3User user, String baseApiUrl, String apiVersion) {
        return sendUserToServerAndWait(user, baseApiUrl, apiVersion) ? user : null;
    }

    /**
     * Generates an average business user
     *
     * @param fi Fi to add the user to
     * @param baseApiUrl base API URL to use
     * @param apiVersion api version to use
     */
    @CheckForNull
    public static D3User averageBusinessUser(String fi, String baseApiUrl, String apiVersion) {
        D3User user = generateAverageBusinessUser(fi, baseApiUrl, apiVersion);
        if (user == null) {
            return null;
        }
        sendUserToServerAndWait(user, baseApiUrl, apiVersion);
        user.setUserType(UserType.PRIMARY_BUSINESS_USER);
        return user;
    }

    /**
     * Generates an average toggle user
     *
     * @param fi Fi to add the user to
     * @param toggleMode Toggle mode of the user
     * @param baseApiUrl base Api Url to use
     */
    @CheckForNull
    public static D3User averageToggleUser(String fi, ToggleMode toggleMode, String baseApiUrl, String apiVersion) {
        D3User user = generateAverageToggleUser(fi, toggleMode, baseApiUrl, apiVersion);
        if (user == null) {
            return null;
        }
        sendUserToServerAndWait(user, baseApiUrl, apiVersion);

        // Note (JMoravec): This info doesn't need to be sent to the server
        if (toggleMode.equals(ToggleMode.NONE)) {
            user.setUserType(UserType.COMMINGLED);
            user.setToggleUser(false);
        } else {
            user.setUserType(toggleMode.equals(ToggleMode.CONSUMER) ?
                    UserType.PRIMARY_CONSUMER_TOGGLE : UserType.PRIMARY_BUSINESS_TOGGLE);
        }

        return user;
    }


    public static D3User generateAverageUser(String fi, boolean largeNumOfTransactions) {
        Person newPerson = Fairy.create().person(PersonProperties.telephoneFormat(PHONE_FORMAT),
                PersonProperties.withNationalIdentificationNumber(getRandomNumberString(9, true)));

        List<D3Account> accountList = createCommonAccounts(newPerson.getFullName(), fi, false, largeNumOfTransactions);

        return new D3User.Builder(newPerson, fi)
                .withAccounts(accountList)
                .withCommonContacts(newPerson)
                .build();
    }

    @CheckForNull
    private static D3User generateAverageBusinessUser(String fi, String baseApiUrl, String apiVersion) {
        Person newPerson = Fairy.create().person(PersonProperties.telephoneFormat(PHONE_FORMAT),
                PersonProperties.withNationalIdentificationNumber(getRandomNumberString(9, true)));

        JFairyCompany company = JFairyCompany.createNewCompany(CompanyProperties.CompanyProperty.withName(getCompanyName()));
        List<JFairyCompany> companies = new ArrayList<>();
        companies.add(company);

        List<D3Account> businessAccounts = createCommonAccounts(company.getName(), fi, true, false);

        D3User business = new D3User.Builder(company, fi)
                .forceLogin()
                .withAccounts(businessAccounts)
                .withCommonContacts(company)
                .build();
        if (!sendUserToServerAndWait(business, baseApiUrl, apiVersion)) {
            return null;
        }

        return new D3User.Builder(newPerson, fi)
                .withAccounts(businessAccounts)
                .withCompanies(companies)
                .withCommonContacts(newPerson)
                .build();
    }


    private static D3User generateAverageToggleUser(String fi, ToggleMode toggleMode, String baseApiUrl, String apiVersion) {
        Person newPerson = Fairy.create().person(PersonProperties.telephoneFormat(PHONE_FORMAT),
                PersonProperties.withNationalIdentificationNumber(getRandomNumberString(9, true)));

        JFairyCompany company = JFairyCompany.createNewCompany(CompanyProperties.CompanyProperty.withName(getCompanyName()));
        List<JFairyCompany> companies = new ArrayList<>();
        companies.add(company);
        List<D3Account> businessAccounts = createCommonAccounts(company.getName(), fi, true, false);

        D3User business = new D3User.Builder(company, fi)
                .forceLogin()
                .withAccounts(businessAccounts)
                .withCommonContacts(company)
                .build();
        if (!sendUserToServerAndWait(business, baseApiUrl, apiVersion)) {
            return null;
        }

        List<D3Account> userAccounts = createCommonAccounts(newPerson.getFullName(), fi, false, false);
        userAccounts.addAll(businessAccounts);

        return new D3User.Builder(newPerson, fi)
                .withAccounts(userAccounts)
                .withCompanies(companies)
                .withCommonContacts(newPerson)
                .setToggleUser(toggleMode)
                .build();
    }

    private static List<D3Account> createCommonAccounts(@Nonnull String commonName, String fi, boolean isBusiness, boolean largeNumOfTransactions) {
        List<ProductType> types = !isBusiness ? new ArrayList<>(Arrays.asList(DEPOSIT_SAVINGS, DEPOSIT_CHECKING, CREDIT_CARD))
                : new ArrayList<>(Arrays.asList(BUSINESS_DEPOSIT_SAVINGS, BUSINESS_DEPOSIT_CHECKING, BUSINESS_CREDIT_CARD));
        List<D3Account> accounts = new ArrayList<>();

        for (ProductType type : types) {
            accounts.add(D3Account.generateRandomAccount(String.format("%s %s", commonName, type.getName()), fi, type, largeNumOfTransactions));
        }
        return accounts;
    }
}
