package com.d3.datawrappers.goals;

import static com.d3.helpers.RandomHelper.getRandomFutureDate;
import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberInt;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.planning.goals.RetirementGoalForm;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.openqa.selenium.WebDriver;

public class RetirementGoal extends D3Goal {

    public int getRetirementAge() {
        return retirementAge;
    }

    public DateTime getBirthDate() {
        return birthDate;
    }

    public String getBirthDateFormatted() {
        return DateTimeFormat.forPattern("MM/dd/yyyy").print(birthDate);
    }

    private int retirementAge;
    private DateTime birthDate;

    public RetirementGoal(String name, DateTime startDate, D3Account fundingAccount, int rateOfReturn, double startAmount,
            double targetAmount, DateTime targetDate, int retirementAge, DateTime birthDate) {
        super(name, startDate, fundingAccount, rateOfReturn, startAmount, targetAmount, targetDate);
        this.retirementAge = retirementAge;
        this.birthDate = birthDate;
        this.type = GoalType.RETIREMENT;
    }

    /**
     * Creates a random goal with funding from the given account
     *
     * @param user User to add the goal to
     * @param fundingAccount Account to associate the goal to
     */
    public static RetirementGoal createRandomGoal(D3User user, D3Account fundingAccount) {
        String name = String.format("Retirement Goal for %s %s", user.getLogin(), getRandomString(5));
        DateTime startDate = new DateTime();
        int rateOfReturn = getRandomNumberInt(1, 5);
        int retirementAge = getRandomNumberInt(65, 73);
        DateTime birthdate = user.getUserDateOfBirth(true);
        double startAmount = getRandomNumber(0, 500);
        double targetAmount = getRandomNumber(600, 1000);
        DateTime targetDate = getRandomFutureDate();

        return new RetirementGoal(name, startDate, fundingAccount, rateOfReturn, startAmount, targetAmount, targetDate, retirementAge,
                birthdate);
    }

    /**
     * Creates a random goal with funding from the given account and invalid data for the following fields:
     * Goal Name (includes not allowed characters)
     * Rate of Return (negative int)
     * Start Amount (double not within allowed range) //NOTE (jmarshall): Used in API call, field not on UI
     * Target Amount (double not within allowed range)
     *
     * @param user User to add the goal to
     * @param fundingAccount Account to associate the goal to
     */
    public static RetirementGoal createRandomGoalWithInvalidData(D3User user, D3Account fundingAccount) {
        String name = "Invalid Goal Name ~ ` < > ^ | /";
        DateTime startDate = new DateTime();
        int rateOfReturn = Math.abs(getRandomNumberInt(50, 75)) * -1;
        int retirementAge = getRandomNumberInt(200, 300);
        DateTime birthdate = user.getUserDateOfBirth(true);
        double startAmount = getRandomNumber(1000000000, 1999999999);
        double targetAmount = getRandomNumber(2000000000, 2111111111);
        DateTime targetDate = getRandomFutureDate();

        return new RetirementGoal(name, startDate, fundingAccount, rateOfReturn, startAmount, targetAmount, targetDate, retirementAge,
                birthdate);
    }

    /**
     * Creates a random goal using using a checking account from the user
     */
    public static RetirementGoal createRandomGoal(D3User user) {
        D3Account fundingAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        return createRandomGoal(user, fundingAccount);
    }

    /**
     * Creates a goal with invalid values using using a checking account from the user
     */
    public static RetirementGoal createRandomGoalWithInvalidData(D3User user) {
        D3Account fundingAccount = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        return createRandomGoalWithInvalidData(user, fundingAccount);
    }

    @Override
    public GoalForm getGoalForm(WebDriver driver) {
        return RetirementGoalForm.initialize(driver, RetirementGoalForm.class);
    }
    
    
}
