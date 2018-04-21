package com.d3.pages.consumer.messages.base;

import com.d3.pages.consumer.messages.MessagesBasePage;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public abstract class MessageSection<T extends MessageSection> extends MessagesBasePage {

    @FindBy(css = "input.select-all")
    private Element selectAllCheckbox;

    @FindBy(css = "button[id$='message-delete-btn']")
    private Element deleteSelectedButton;

    @FindBy(css = "button.submit-one")
    private Element continueButton;

    @FindBy(css = "button.cancel")
    private Element cancelButton;

    @FindBy(css = "input.select-item")
    private List<Element> messageCheckboxes;

    @FindBy(id = "date-range-btn")
    private Element dateRangeButton;

    @FindBy(id = "startDate")
    private Element startDateField;

    @FindBy(id = "endDate")
    private Element endDateField;

    @FindBy(css = "button.searchButton")
    private Element searchDateButton;

    @FindBy(id = "searchTerm")
    private Element searchField;

    @FindBy(css = "button.clear-search-date-range")
    private Element clearDatesButton;
    
    @FindBy(css = "button.searchButton")
    private Element dateRangeSearchButton;
    
    @FindBy(id = "searchTerm")
    private Element searchTermInput;

    @FindBy(id = "input-search-btn")
    private Element searchButton;

    @FindBy(css = "span[data-role='remove']")
    private List<Element> removeTerm;

    @FindBy(css = "li.entity.message")
    private List<Element> messages;

    @FindBy(css = "a.dropdown-toggle")
    private Element messageTypeDropdown;

    @FindBy(css = "span.title")
    private Element currentFilter;

    public MessageSection(WebDriver driver) {
        super(driver);
    }

    private D3Element messageCheckbox(String messageSubject) {
        By by = By.cssSelector(String.format("input.select-item[aria-label='%s']", messageSubject));
        return new D3Element(driver.findElement(by));
    }

    public T clickDateRangeSearchButton() {
        dateRangeSearchButton.click();
        waitForSpinner();
        return me();
    }

    public T searchForTerm(String messageSubject) {
        searchTermInput.sendKeys(messageSubject);
        return me();
    }

    public T searchButtonClick() {
        searchButton.click();
        waitForSpinner();
        return me();
    }

    @Override
    protected abstract T me();

    public T clickDeleteSelectedButton() {
        deleteSelectedButton.click();
        return me();
    }

    public T clickDeleteMessageContinueButton() {
        continueButton.click();
        waitForSpinner();
        return me();
    }

    public T clickCancelButton() {
        cancelButton.click();
        waitForSpinner();
        return me();
    }

    public T clickSelectAllCheckbox() {
        selectAllCheckbox.click();
        return me();
    }

    public T deleteAllMessages() {
        clickSelectAllCheckbox().
            clickDeleteSelectedButton().
            clickDeleteMessageContinueButton();
        return me();
    }

    public T deleteMessageWithSubject(String messageSubject) {
        messageCheckbox(messageSubject).click();
        clickDeleteSelectedButton().
            clickDeleteMessageContinueButton();
        return me();
    }

    public T selectMultipleCheckboxes() {
        messageCheckboxes.forEach(Element::click);
        return me();
    }
    
    public T clickDateRangeButton() {
        dateRangeButton.click();
        waitForSpinner();
        return me();
    }
    
    public T clickClearDatesButton() {
        clearDatesButton.click();
        waitForSpinner();
        return me();
    }
    
    public boolean areDateFieldsDisplayed(){
        return startDateField.isDisplayed() && endDateField.isDisplayed();
    }
    
    public String getStartDateValue(){
        return startDateField.getValueAttribute();
    }
    
    public String getEndDateValue(){
        return endDateField.getValueAttribute();
    }

    public T clearSearch() {
        removeTerm.forEach(Element::click);
        waitForSpinner();
        return me();
    }

    public int getMessageCount() {
        return messages.size();
    }

    public T clickOnDateRange() {
        dateRangeButton.click();
        return me();
    }

    public T enterStartDate(String startDate) {
        startDateField.sendKeys(startDate);
        return me();
    }

    public T enterEndDate(String endDate) {
        endDateField.sendKeys(endDate);
        return me();
    }

    public T clickOnSearchDateButton() {
        searchDateButton.click();
        waitForSpinner();
        return me();
    }

    public T enterSearchCriteria(String criteria) {
        searchField.sendKeys(criteria);
        return me();
    }

    public T clickOnSearchButton() {
        searchButton.click();
        waitForSpinner();
        return me();
    }

    public T clearDateRange() {
        clearDatesButton.click();
        waitForSpinner();
        return me();
    }

    public T clickMessageTypeDropdown() {
        messageTypeDropdown.click();
        return me();
    }

    public boolean deleteButtonIsEnabled() {
        return deleteSelectedButton.isEnabled();
    }

    public String getCurrentFilter() {
        return currentFilter.getText();
    }

}

