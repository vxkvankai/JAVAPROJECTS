package com.d3.datawrappers.user;

import static com.d3.helpers.AccountHelper.generatePassword;
import static com.d3.helpers.RandomHelper.getRandomNumberString;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.enums.AccessLevel;
import com.d3.datawrappers.user.enums.AccountPermissions;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.datawrappers.user.enums.UserLevel;
import com.d3.datawrappers.user.enums.UserServices;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.helpers.RandomHelper;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.javatuples.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class D3SecondaryUser implements Serializable, Loginable {

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public D3Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public D3Contact getHomePhone() {
        return homePhone;
    }

    public D3Contact getMobilePhone() {
        return mobilePhone;
    }

    public D3Contact getWorkPhone() {
        return workPhone;
    }

    public String getPassword() {
        return password;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public D3UserProfile.ProfileType getProfileType() {
        return profileType;
    }

    public D3User getPrimaryUser() {
        return primaryUser;
    }

    public String getLoginPsswd() {
        return loginPsswd;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public ToggleMode getToggleMode() {
        return toggleMode;
    }

    public String getFullName() {
        if (middleName == null || middleName.isEmpty()) {
            return String.format("%s %s", firstName, lastName);
        } else {
            return String.format("%s %s %s", firstName, middleName, lastName);
        }
    }

    private String firstName;
    private String middleName;
    private String lastName;
    private D3Address address;
    private String email;
    private D3Contact homePhone;
    private D3Contact mobilePhone;
    private D3Contact workPhone;
    private String loginId;
    private String loginPsswd = "password"; //NOTE: this will always be the login password for secondary users, no matter what was entered when they were created
    private String password;
    private String secretQuestion = "denver";
    private UserLevel userLevel;
    private D3UserProfile.ProfileType profileType;
    private List<Pair<D3Account, AccountPermissions>> userAccounts = new ArrayList<>();
    private List<Pair<D3Account, D3AccountLimits>> userAccountLimits = new ArrayList<>();
    private List<Pair<UserServices, AccessLevel>> userServices = new ArrayList<>();
    private UserType userType;
    private D3User primaryUser;
    private ToggleMode toggleMode;

    private D3SecondaryUser(D3User user) {
        Person person = Fairy.create().person();
        this.firstName = person.getFirstName();
        this.middleName = person.getMiddleName();
        this.lastName = person.getLastName();
        this.address = new D3Address(person.getAddress());
        this.email = person.getEmail();
        this.homePhone = new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.HOME, getRandomNumberString(10, true));
        this.mobilePhone = new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.MOBILE, getRandomNumberString(10, true));
        this.workPhone = new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.WORK, getRandomNumberString(10, true));
        this.loginId = String.format("%s%s", person.getUsername(), RandomHelper.getRandomString(3));
        this.password = generatePassword();
        this.userType = user.getUserType();
        this.userLevel = UserLevel.SECONDARY;
        this.profileType = user.getProfile().getType();
        this.primaryUser = user;
        this.toggleMode = user.getToggleMode();
    }

    /**
     * Creates new Secondary User with random account permissions and limits for the given accounts
     * Also gives Secondary user random Banking Services access
     *
     * @param user Primary User creating the secondary user for
     * @param accounts List of (Primary User) Accounts the secondary user should have access to
     * @return D3SecondaryUser
     */
    public static D3SecondaryUser createUserWithRandomServicesAndPermissions(D3User user, List<D3Account> accounts) {
        return new D3SecondaryUser(user)
            .withRandomAccountPermissions(accounts, AccountPermissions.listsOfAccountPermissions())
            .withServices(UserServices.getRandomListOfUserServices());
    }

    /**
     * Will assign a random account permissions for a list of accounts
     *
     * @param accounts List of Accounts Secondary User will have access to
     * @param accountPermissions List of Lists to select random account permissions from
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withRandomAccountPermissions(@Nonnull List<D3Account> accounts, List<List<AccountPermissions>> accountPermissions) {
        for (D3Account account : accounts) {
            List<AccountPermissions> permissions = AccountPermissions.getRandomAccountPermissions(accountPermissions);
            permissions.forEach(permission -> {
                withAccountPermission(Pair.with(account, permission));
                if (permission.hasLimits()) {
                    withAccountLimits(Pair.with(account, permission));
                }
            });
        }
        return this;
    }

    /**
     * Will assign list of account permissions for a list of accounts
     * NOTE: This will assign the same permissions to each account
     *
     * @param accounts List of Accounts Secondary User will have access to
     * @param accountPermissions List of Account Permissions to assign each account
     * @return D3SecondaryUser
     */
    public D3SecondaryUser withAccountPermissions(@Nonnull List<D3Account> accounts, List<AccountPermissions> accountPermissions) {
        for (D3Account account : accounts) {
            //If user ONLY has READ access, remove ALL other Account Permissions
            if (accountPermissions.equals(AccountPermissions.readOnly())) {
                withoutAccountPermissions(account, AccountPermissions.allPermissions());
            }
            withAccountPermissions(account, accountPermissions);
        }
        return this;
    }

    /**
     * Will assign list of account permissions for a specific account
     *
     * @param account Individual account of a Secondary User to set permissions for
     * @param accountPermissions List of Account Permissions to assign to the account
     * @return D3SecondaryUser
     */
    public D3SecondaryUser withAccountPermissions(D3Account account, List<AccountPermissions> accountPermissions) {
        accountPermissions.forEach(permission -> {
            withAccountPermission(Pair.with(account, permission));
            if (permission.hasLimits()) {
                withAccountLimits(Pair.with(account, permission));
            }
        });
        return this;
    }

    /**
     * Pairs a D3Account with a given Account Permission
     *
     * @param account Pair of D3Account and AccountPermissions to associate
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withAccountPermission(@Nonnull Pair<D3Account, AccountPermissions> account) {
        userAccounts.add(account);
        return this;
    }


    /**
     * Removes account permissions for a list of accounts
     *
     * @param accounts List of D3Accounts to remove account permissions from
     * @param accountPermissions List of Account Permissions to remove from each given account
     * @return D3SecondaryUser
     */
    public D3SecondaryUser withoutAccountPermissions(@Nonnull List<D3Account> accounts, List<AccountPermissions> accountPermissions) {
        for (D3Account account : accounts) {
            withoutAccountPermissions(account, accountPermissions);
        }
        return this;
    }

    /**
     * Removes account permissions and limits (if permission has them) for a specific account.
     *
     * @param account D3Account to remove account permissions from
     * @param accountPermissions List of Account Permissions to remove from account
     * @return D3SecondaryUser
     */
    public D3SecondaryUser withoutAccountPermissions(D3Account account, List<AccountPermissions> accountPermissions) {
        List<AccountPermissions> permissions = accountPermissions;
        //If user does not have READ access, remove ALL Account Permissions
        if (accountPermissions.contains(AccountPermissions.READ)) {
            permissions = AccountPermissions.allPermissions();
        }
        permissions.forEach(permission -> {
            withoutAccountPermissions(Pair.with(account, permission));
            if (permission.hasLimits()) {
                withoutAccountLimits(Pair.with(account, permission));
            }
        });
        return this;
    }

    /**
     * Removes a Pair<D3Account, AccountPermissions> from userAccounts
     *
     * @param account Pair of D3Account and AccountPermission to remove
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withoutAccountPermissions(@Nonnull Pair<D3Account, AccountPermissions> account) {
        List<Pair<D3Account, AccountPermissions>> permissionsToRemove = userAccounts.stream()
            .filter(pair -> pair.getValue0() == account.getValue0() && pair.getValue1() == account.getValue1())
            .collect(Collectors.toList());
        userAccounts.removeAll(permissionsToRemove);
        return this;
    }

    /**
     * Will assign List of UserServices Secondary User should have access to
     * NOTE: If they have access, will always set AccessLevel to maxAccess (Read Only or Full Access depending on the UserService)
     *
     * @param services List of UserServices
     * @return D3SecondaryUser
     */
    public D3SecondaryUser withServices(List<UserServices> services) {
        for (UserServices service : services) {
            withService(Pair.with(service, service.getMaxAccess()));
        }
        return this;
    }

    /**
     * List of UserServices Secondary User should not have access to. Will remove any services from userServices
     * NOTE: Intended to be used with @WithSpecificServiceAndPermissions annotation
     *
     * @param services List of UserServices
     * @return D3SecondaryUser
     */
    public D3SecondaryUser withoutServices(List<UserServices> services) {
        for (UserServices service : services) {
            withoutService(Pair.with(service, service.getMaxAccess()));
        }
        return this;
    }

    /**
     * Pairs a UserService with a given AccessLevel
     *
     * @param service Pair of UserService and AccessLevel to associate
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withService(@Nonnull Pair<UserServices, AccessLevel> service) {
        userServices.add(service);
        return this;
    }

    /**
     * Removes a Pair<UserService, AccessLevel> from userServices
     *
     * @param service Pair of UserService and AccessLevel to remove
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withoutService(@Nonnull Pair<UserServices, AccessLevel> service) {
        List<Pair<UserServices, AccessLevel>> servicesToRemove = userServices.stream().filter(pair -> pair.getValue0() == service.getValue0()).collect(Collectors.toList());
        userServices.removeAll(servicesToRemove);
        return this;
    }

    /**
     * For each Secondary User account, if the account permissions has limits (ex: ACH, Bill Pay, Internal Transfers, Wire)
     * method will create random D3AccountLimit data and associate it with the given account
     *
     * @param account Pair of <D3Account, AccountPermissions> to create D3AccountLimit for
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withAccountLimits(@Nonnull Pair<D3Account, AccountPermissions> account) {
        D3AccountLimits limit = D3AccountLimits.createRandomAccountPermissionLimit(account.getValue0(), account.getValue1());
        withAccountLimit(Pair.with(account.getValue0(), limit));
        return this;
    }

    /**
     * Removes a Pair<D3Account, D3AccountLimits> from userAccountLimits
     *
     * @param account Pair of D3Account and AccountPermission with limit to remove
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withoutAccountLimits(@Nonnull Pair<D3Account, AccountPermissions> account) {
        List<Pair<D3Account, D3AccountLimits>> limitsToRemove = userAccountLimits.stream()
            .filter(pair -> pair.getValue0() == account.getValue0() && pair.getValue1().getPermission() == account.getValue1())
            .collect(Collectors.toList());
        userAccountLimits.removeAll(limitsToRemove);
        return this;
    }

    /**
     * Pairs a given D3Account with a D3AccountLimit
     *
     * @param accountLimit Pair of D3Account and D3AccountLimit to associate
     * @return D3SecondaryUser
     */
    private D3SecondaryUser withAccountLimit(@Nonnull Pair<D3Account, D3AccountLimits> accountLimit) {
        userAccountLimits.add(accountLimit);
        return this;
    }

    /**
     * @return List of D3Accounts Secondary User has access to
     */
    public List<D3Account> getAccounts() {
        return userAccounts.stream().map(Pair::getValue0).distinct().collect(Collectors.toList());
    }

    /**
     * @return List of UserServices Secondary User has access to
     */
    public List<UserServices> getServices() {
        return userServices.stream().map(Pair::getValue0).distinct().collect(Collectors.toList());
    }

    /**
     * @param service UserService to get AccessLevel for
     * @return AccessLevel of the specified UserService, null if not found
     */
    public AccessLevel getAccessLevelForService(UserServices service) {
        return userServices.stream().filter(pair -> pair.getValue0() == service).findFirst().map(Pair::getValue1).orElse(null);
    }

    /**
     * @param account D3Account Secondary User has access to
     * @return List of AccountPermissions for the given D3Account
     */
    public List<AccountPermissions> getAccountPermissions(D3Account account) {
        return userAccounts.stream().filter(pair -> pair.getValue0().getName().equals(account.getName())).map(Pair::getValue1).collect(Collectors.toList());
    }

    /**
     * @param account D3Account Secondary User has access to
     * @return List of distinct D3AccountLimits filtered by the type of AccountPermission for the given D3Account
     */
    public List<D3AccountLimits> getAccountLimitsForAccount(D3Account account) {
        return userAccountLimits.stream().filter(pair -> pair.getValue0().getName().equals(account.getName())).map(Pair::getValue1).filter(distinctByKey(D3AccountLimits::getPermission)).collect(Collectors.toList());

    }

    /**
     * @param account D3Account to check AccountPermissions for
     * @return true if Secondary User account has STATEMENTS & TRANSACTIONS AccountPermissions
     */
    public boolean hasStatementAndTransactionAccess(D3Account account) {
        return getAccountPermissions(account).stream().filter(permission -> permission == AccountPermissions.STATEMENTS || permission == AccountPermissions.TRANSACTIONS).collect(Collectors.toList()).size() == 2;
    }

    /**
     * @param account D3Account to check AccountPermissions for
     * @return true if Secondary User AccountPermissions contains at least 1 Permission that hasLimits (ex: ACH, BILL_PAY, TRANSFER, WIRE)
     */
    public boolean hasMoneyMovementAccess(D3Account account) {
        return !getAccountPermissions(account).stream().filter(AccountPermissions::hasLimits).collect(Collectors.toList()).isEmpty();
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public String getLogin() {
        return loginId;
    }

    /**
     * Stateful filter for filtering distinct by object property/attribute directly
     *
     * @param objectProperty type of object property/attribute to filter by
     * @param <T> T is type of stream element
     * @return Predicate instance that maintains state about what is seen previously using ConcurrentHashMap.
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> objectProperty) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(objectProperty.apply(t));
    }
}
