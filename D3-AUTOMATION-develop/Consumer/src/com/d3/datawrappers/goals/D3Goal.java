package com.d3.datawrappers.goals;

import com.d3.datawrappers.account.D3Account;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.openqa.selenium.WebDriver;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public abstract class D3Goal implements Serializable {

    /**
     * {Funding Account, percentage}
     */
    private Map<D3Account, Integer> fundingAccounts;
    private String name;
    GoalType type;
    private int rateOfReturn;
    private DateTime startDate;
    private double startAmount;
    private double targetAmount;
    private DateTime targetDate;

    public DateTime getStartDate() {
        return startDate;
    }

    public String getStartDateFormatted() {
        return DateTimeFormat.forPattern("MM/dd/yyyy").print(startDate);
    }

    public String getTargetDateFormatted() {
        return DateTimeFormat.forPattern("MM/dd/yyyy").print(targetDate);
    }

    public Map<D3Account, Integer> getFundingAccounts() {
        return fundingAccounts;
    }

    public String getName() {
        return name;
    }

    public String getTargetAmountStr() {
        return new DecimalFormat(".00").format(targetAmount);
    }

    public int getRateOfReturn() {
        return rateOfReturn;
    }

    public GoalType getType() {
        return type;
    }

    public D3Goal(String name, DateTime startDate, D3Account fundingAccount, int rateOfReturn, double startAmount, double targetAmount,
            DateTime targetDate) {
        this.name = name;
        this.startDate = startDate;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
        this.fundingAccounts = new HashMap<>();
        this.fundingAccounts.put(fundingAccount, 100);
        this.rateOfReturn = rateOfReturn;
        this.startAmount = startAmount;
    }

    public abstract GoalForm getGoalForm(WebDriver driver);

    public double getStartAmount() {
        return startAmount;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public DateTime getTargetDate() {
        return targetDate;
    }
}
