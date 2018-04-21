package com.d3.datawrappers.user;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.enums.AccountPermissions;
import com.d3.datawrappers.user.enums.PeriodLimits;
import com.d3.helpers.RandomHelper;

import java.io.Serializable;
import java.text.DecimalFormat;

public class D3AccountLimits implements Serializable {

    private D3Account d3Account;
    private double amountLimit;
    private AccountPermissions permission;
    private int countLimit;
    private PeriodLimits periodLimit;

    private D3AccountLimits(D3Account d3Account, AccountPermissions permission, double amountLimit, int countLimit, PeriodLimits periodLimit) {
        this.d3Account = d3Account;
        this.permission = permission;
        this.amountLimit = amountLimit;
        this.countLimit = countLimit;
        this.periodLimit = periodLimit;
    }

    /**
     * Creates random Account, Count, and Period limits for the given account and permission
     *
     * @param account D3Account to set limits for
     * @param permission AccountPermissions type to set limits for (ex: ACH, BILL_PAY, TRANSFER, WIRE)
     * @return D3AccountLimits
     */
    public static D3AccountLimits createRandomAccountPermissionLimit(D3Account account, AccountPermissions permission) {
        double accountLimit = RandomHelper.getRandomNumber(25, 500);
        int countLimit = RandomHelper.getRandomNumberInt(1, 10);
        PeriodLimits periodLimit = PeriodLimits.getRandom();
        return new D3AccountLimits(account, permission, accountLimit, countLimit, periodLimit);
    }

    public D3Account getAccount() {
        return d3Account;
    }

    public double getAmountLimit() {
        return amountLimit;
    }

    public String getAmountLimitStr() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(amountLimit);
    }

    public Integer getCountLimit() {
        return countLimit;
    }

    public String getCountLimitStr() {
        return String.valueOf(countLimit);
    }

    public PeriodLimits getPeriodLimit() {
        return periodLimit;
    }

    public AccountPermissions getPermission() {
        return permission;
    }
}
