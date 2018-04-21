package com.d3.api.mappings.planning;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.GoalType;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Goals {

    @SerializedName("accounts")
    @Expose
    private List<Account> accounts = null;
    @SerializedName("rateOfReturn")
    @Expose
    private Integer rateOfReturn;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("startAmount")
    @Expose
    private Double startAmount;
    @SerializedName("retirementAge")
    @Expose
    private Integer retirementAge;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("targetDate")
    @Expose
    private String targetDate;
    @SerializedName("targetAmount")
    @Expose
    private Double targetAmount;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Integer getRateOfReturn() {
        return rateOfReturn;
    }

    public void setRateOfReturn(Integer rateOfReturn) {
        this.rateOfReturn = rateOfReturn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Double getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(Double startAmount) {
        this.startAmount = startAmount;
    }

    public Integer getRetirementAge() {
        return retirementAge;
    }

    public void setRetirementAge(Integer retirementAge) {
        this.retirementAge = retirementAge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Goals(D3User user, D3Goal goal) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.accounts = new ArrayList<>();
        for (Map.Entry<D3Account, Integer> accountInfo : goal.getFundingAccounts().entrySet()) {
            this.accounts.add(new Account(user, accountInfo.getKey(), accountInfo.getValue()));
        }

        this.name = goal.getName();
        this.rateOfReturn = goal.getRateOfReturn();
        this.startAmount = goal.getStartAmount();
        this.startDate = formatter.print(goal.getStartDate());
        this.targetAmount = goal.getTargetAmount();
        this.targetDate = formatter.print(goal.getTargetDate());

        if (goal.getType() == GoalType.RETIREMENT) {
            this.type = GoalType.RETIREMENT.name();
            RetirementGoal retirementGoal = (RetirementGoal) goal;
            this.dateOfBirth = formatter.print(retirementGoal.getBirthDate());
            this.retirementAge = retirementGoal.getRetirementAge();
        } else if (goal.getType() == GoalType.SAVINGS) {
            this.type = "OTHER";
        }

    }
}