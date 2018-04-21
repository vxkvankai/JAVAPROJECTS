package com.d3.pages.consumer.headers;

import com.d3.pages.consumer.moneymovement.BillPayPage;
import com.d3.pages.consumer.moneymovement.PayMultiple;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.ebills.EBillsPage;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class MoneyMovementTabs extends HeaderBase {

    @FindBy(xpath = "//div[@class='nav-text'][.='Bill Pay Enrollment']")
    private Element billPayEnrollmentLink;

    @FindBy(xpath = "//div[@class='nav-text'][.='Recipients']")
    private Element recipientsLink;

    @FindBy(xpath = "//a[@href='#money-movement/multiple']")
    private Element payMultipleLink;

    @FindBy(xpath = "//a[@href='#money-movement/ebills']")
    private Element ebillsLink;

    @FindBy(xpath = "//a[@href='#money-movement/schedule']")
    private Element scheduleLink;

    public MoneyMovementTabs(WebDriver driver) {
        super(driver);
    }

    @Override
    protected MoneyMovementTabs me() {
        return this;
    }

    @Step("Click the bill pay enrollment link")
    public BillPayPage clickBillPayEnrollmentLink() {
        billPayEnrollmentLink.click();
        waitForSpinner();
        return BillPayPage.initialize(driver, BillPayPage.class);
    }

    @Step("Click the recipients link")
    public RecipientsPage clickRecipientsLink() {
        recipientsLink.click();
        waitForSpinner();
        return RecipientsPage.initialize(driver, RecipientsPage.class);
    }

    @Step("Click the pay multiple link")
    public PayMultiple clickPayMultipleLink() {
        payMultipleLink.click();
        waitForSpinner();
        return PayMultiple.initialize(driver, PayMultiple.class);
    }

    @Step("Click the ebills link")
    public EBillsPage clickEbillsLink() {
        ebillsLink.click();
        waitForSpinner();
        return EBillsPage.initialize(driver, EBillsPage.class);
    }

    @Step("Click the schedule link")
    public Schedule clickScheduleLink() {
        scheduleLink.click();
        waitForSpinner();
        return Schedule.initialize(driver, Schedule.class);
    }
}
