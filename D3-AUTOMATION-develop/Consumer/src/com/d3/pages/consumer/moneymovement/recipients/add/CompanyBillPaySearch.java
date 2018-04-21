package com.d3.pages.consumer.moneymovement.recipients.add;

import com.d3.support.D3Element;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class CompanyBillPaySearch extends PageObjectBase {

    @FindBy(css = "input.global-search")
    private Element searchFieldInput;

    @FindBy(css = "button.global-search-button")
    private Element searchButton;

    @FindBy(css = "button.enter-information")
    private Element manualEntryButton;

    private D3Element recipientButton(String name) {
        By by = By.xpath(String.format("//div[contains(text(),'%s')]", name));
        return new D3Element(driver.findElement(by));
    }

    public AddBillPayManualForm clickManualButton() {
        manualEntryButton.click();
        return AddBillPayManualForm.initialize(driver, AddBillPayManualForm.class);
    }

    public AddExistingBillPayCompanyForm clickRecipientButton(String name) {
        recipientButton(name).click();
        return AddExistingBillPayCompanyForm.initialize(driver, AddExistingBillPayCompanyForm.class);
    }

    public CompanyBillPaySearch enterSearchTerm(String searchTerm) {
        searchFieldInput.sendKeys(searchTerm);
        return this;
    }

    public CompanyBillPaySearch clickSearchButton() {
        searchButton.click();
        waitForSpinner();
        return this;
    }

    public CompanyBillPaySearch(WebDriver driver) {
        super(driver);
    }

    @Override
    protected CompanyBillPaySearch me() {
        return this;
    }
}
