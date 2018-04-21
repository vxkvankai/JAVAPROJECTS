package com.d3.pages.consumer.headers;

import com.d3.pages.consumer.moneymovement.BillPayPage;
import com.d3.pages.consumer.moneymovement.PayMultiple;
import com.d3.pages.consumer.moneymovement.PopMoneyPage;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.ebills.EBillsPage;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
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

    @FindBy(xpath = "//a[@href='money-movement/external-p2p']")
    private Element popMoneyLink ;

    public MoneyMovementTabs(WebDriver driver) {
        super(driver);
    }

    @Override
    protected MoneyMovementTabs me() {
        return this;
    }

    public boolean isPayMultipleButtonDisplayed() {
        try {
            return payMultipleLink.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error finding Pay Multiple tab", e);
            return false;
        }
    }


    public boolean isEbillsButtonDisplayed() {
        try {
            return ebillsLink.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error finding Ebills tab", e);
            return false;
        }

    }

    public PopMoneyPage clickPopMoneyLink() {
        popMoneyLink.click();
        return PopMoneyPage.initialize(driver, PopMoneyPage.class);
    }

    public boolean isRecipientTabDisplayed() {
        try {
            return recipientsLink.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error finding Recipient tab", e);
            return false;
        }
    }

    @Step("Click the bill pay enrollment link")
    public BillPayPage clickBillPayEnrollmentLink() {
        billPayEnrollmentLink.click();
        return BillPayPage.initialize(driver, BillPayPage.class);
    }

    @Step("Click the recipients link")
    public RecipientsPage clickRecipientsLink() {
        recipientsLink.click();
        return RecipientsPage.initialize(driver, RecipientsPage.class);
    }

    @Step("Click the pay multiple link")
    public PayMultiple clickPayMultipleLink() {
        payMultipleLink.click();
        return PayMultiple.initialize(driver, PayMultiple.class);
    }

    @Step("Click the ebills link")
    public EBillsPage clickEbillsLink() {
        ebillsLink.click();
        return EBillsPage.initialize(driver, EBillsPage.class);
    }

    @Step("Click the schedule link")
    public Schedule clickScheduleLink() {
        scheduleLink.click();
        return Schedule.initialize(driver, Schedule.class);
    }

}
