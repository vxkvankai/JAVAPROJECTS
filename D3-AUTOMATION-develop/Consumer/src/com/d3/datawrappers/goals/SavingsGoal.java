package com.d3.datawrappers.goals;

import static com.d3.helpers.RandomHelper.getRandomFutureDate;
import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberInt;
import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.user.D3User;
import com.d3.pages.consumer.planning.goals.SavingsGoalForm;
import com.d3.pages.consumer.planning.goals.base.GoalForm;
import org.joda.time.DateTime;
import org.openqa.selenium.WebDriver;

public class SavingsGoal extends D3Goal {

    public SavingsGoal(String name, DateTime startDate, D3Account fundingAccount,
            int rateOfReturn, double startAmount, double targetAmount, DateTime targetDate) {
        super(name, startDate, fundingAccount, rateOfReturn, startAmount, targetAmount, targetDate);
        this.type = GoalType.SAVINGS;
    }

    /**
     * Creates a random goal with funding from the given account and invalid data for the following fields:
     * Goal Name (includes not allowed characters)
     * Rate of Return (negative int)
     * Start Amount (double not within allowed range) //NOTE (jmarshall): Used in API call, field not on UI
     * Target Amount (double not within allowed range)
     *
     * @param fundingAccount Account to associate the goal to
     */
    public static SavingsGoal createRandomGoalWithInvalidData(D3Account fundingAccount) {
        String name = "Invalid Goal Name ~ ` < > ^ | /";
        DateTime startDate = new DateTime();
        int rateOfReturn = Math.abs(getRandomNumberInt(50, 75)) * -1;
        DateTime targetDate = getRandomFutureDate();
        double startAmount = getRandomNumber(1000000000, 1999999999); //NOTE (jmarshall): Used in API call, field not on UI
        double targetAmount = getRandomNumber(1000000000, 1999999999);

        return new SavingsGoal(name, startDate, fundingAccount, rateOfReturn, startAmount, targetAmount, targetDate);
    }

    /**
     * Creates a random goal with funding from the given account
     *
     * @param user User to add the goal to
     * @param fundingAccount Account to associate the goal to
     */
    public static SavingsGoal createRandomGoal(D3User user, D3Account fundingAccount) {
        String name = String.format("Savings Goal for %s %s", user.getLogin(), getRandomString(5));
        DateTime startDate = new DateTime();
        int rateOfReturn = getRandomNumberInt(1, 5);
        DateTime targetDate = getRandomFutureDate();
        double startAmount = getRandomNumber(0, 500); //NOTE (jmarshall): Used in API call, field not on UI
        double targetAmount = getRandomNumber(500000, 1000000);
        return new SavingsGoal(name, startDate, fundingAccount, rateOfReturn, startAmount, targetAmount, targetDate);
    }

    /**
     * Creates a random goal using using a checking account from the user
     */
    public static SavingsGoal createRandomGoal(D3User user) {
        D3Account fundingAccount = user.getUserType().isBusinessType() ?
                user.getFirstAccountByType(ProductType.BUSINESS_DEPOSIT_CHECKING)
                : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        return createRandomGoal(user, fundingAccount);
    }
    
    /**
     * Creates a goal with invalid values using using a checking account from the user
     */
    public static SavingsGoal createRandomGoalWithInvalidData(D3User user) {
        D3Account fundingAccount = user.getUserType().isBusinessType() ?
                user.getFirstAccountByType(ProductType.BUSINESS_DEPOSIT_CHECKING)
                : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        return createRandomGoalWithInvalidData(fundingAccount);
    }

    @Override
    public GoalForm getGoalForm(WebDriver driver) {
        return SavingsGoalForm.initialize(driver, SavingsGoalForm.class);
    }
}
