package com.d3.datawrappers.user.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public enum AccountPermissions {
    ACH("ACH", true),
    BILL_PAY("Bill Pay", true),
    READ("Read", false),
    STATEMENTS("Statements", false),
    TRANSACTIONS("Transactions", false),
    TRANSFER("Internal Transfers", true),
    WIRE("Wire", true);

    private boolean hasLimits;
    private String formatted;

    AccountPermissions(String formatted, boolean hasLimits) {
        this.formatted = formatted;
        this.hasLimits = hasLimits;
    }

    /**
     * Gets List of AccountPermissions that would give Secondary User Read-Only Access for an account
     *
     * @return AccountPermissions.READ as a list
     */
    public static List<AccountPermissions> readOnly() {
        return Collections.singletonList(AccountPermissions.READ);
    }

    /**
     * Gets List of AccountPermissions that would give Secondary User Statements & Transactions Access for an account
     *
     * @return List of AccountPermissions
     */
    public static List<AccountPermissions> statementAndTransactions() {
        return Arrays.asList(AccountPermissions.READ, AccountPermissions.STATEMENTS, AccountPermissions.TRANSACTIONS);
    }

    /**
     * Gets List of AccountPermissions that would give Secondary User money movement access for an account
     * Gets all of the AccountPermissions where hasLimits = true, shuffles the list and returns 2 random permissions
     *
     * @return List of AccountPermissions
     */
    public static List<AccountPermissions> moneyMovementAccess() {
        List<AccountPermissions> moneyMovementPermissions = Arrays.stream(AccountPermissions.values()).filter(AccountPermissions::hasLimits).collect(Collectors.toList());
        Collections.shuffle(moneyMovementPermissions);
        List<AccountPermissions> access = moneyMovementPermissions.stream().limit(2).collect(Collectors.toList());
        access.add(AccountPermissions.READ);
        return access;
    }

    /**
     * Gets List of AccountPermissions that would give Secondary User full access for an account
     *
     * @return List of all AccountPermissions
     */
    public static List<AccountPermissions> allPermissions() {
        return Arrays.asList(AccountPermissions.values());
    }

    /**
     * List of Lists containing different account permissions
     *
     * @return List of Lists of different AccountPermissions
     */
    public static List<List<AccountPermissions>> listsOfAccountPermissions() {
        return Arrays.asList(readOnly(), statementAndTransactions(), moneyMovementAccess(), allPermissions());
    }

    /**
     * Get a random List of AccountPermission to assign to an account for a secondary user (Read-Only, Statements & Transactions, Money Movement, Full Access)
     *
     * @param accountPermissions List of Lists for different AccountPermissions
     * @return Random list of AccountPermissions
     */
    public static List<AccountPermissions> getRandomAccountPermissions(List<List<AccountPermissions>> accountPermissions) {
        return accountPermissions.get(ThreadLocalRandom.current().nextInt(accountPermissions.size()));
    }

    public String getFormatted() {
        return formatted;
    }

    public boolean hasLimits() {
        return hasLimits;
    }

}
