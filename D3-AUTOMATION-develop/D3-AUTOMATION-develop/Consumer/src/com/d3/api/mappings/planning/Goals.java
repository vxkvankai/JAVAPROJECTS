package com.d3.api.mappings.planning;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.goals.D3Goal;
import com.d3.datawrappers.goals.GoalType;
import com.d3.datawrappers.goals.RetirementGoal;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Goals {

    private List<Account> accounts;
    private Integer rateOfReturn;
    private String type;
    private String startDate;
    private Double startAmount;
    private Integer retirementAge;
    private String name;
    private String dateOfBirth;
    private String targetDate;
    private Double targetAmount;

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