package com.d3.pages.consumer.headers;

import com.d3.pages.consumer.planning.Budget;
import com.d3.pages.consumer.planning.GoalsPage;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class PlanningTabs extends HeaderBase {

    @FindBy(xpath = "//a[@href='#planning/budget']")
    private Element budgetLink;

    @FindBy(xpath = "//a[@href='#planning/goals']")
    private Element goalsLink;


    public PlanningTabs(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PlanningTabs me() {
        return this;
    }

    public Budget clickBudgetLink() {
        budgetLink.click();
        return Budget.initialize(driver, Budget.class);
    }

    public GoalsPage clickGoalsLink() {
        goalsLink.click();
        waitForSpinner();
        return GoalsPage.initialize(driver, GoalsPage.class);
    }

}
