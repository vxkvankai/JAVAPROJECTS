package com.d3.pages.consumer.headers;

import com.d3.pages.consumer.settings.accounts.Accounts;
import com.d3.pages.consumer.settings.alerts.Alerts;
import com.d3.pages.consumer.settings.businessprofile.BusinessProfile;
import com.d3.pages.consumer.settings.categories.Categories;
import com.d3.pages.consumer.settings.profile.Profile;
import com.d3.pages.consumer.settings.rules.Rules;
import com.d3.pages.consumer.settings.security.Security;
import com.d3.pages.consumer.settings.users.UsersPage;
import com.d3.pages.consumer.settings.dashboard.Dashboard;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class SettingsTabs extends HeaderBase {

    @FindBy(xpath = "//a[@href='#settings/profile']")
    private Element profileLink;

    @FindBy(xpath = "//a[@href='#settings/business-profile']")
    private Element businessProfileLink;

    @FindBy(xpath = "//a[@href='#settings/accounts']")
    private Element accountsLink;

    @FindBy(xpath = "//a[@href='#settings/alerts']")
    private Element alertsLink;

    @FindBy(xpath = "//a[@href='#settings/categories']")
    private Element categoriesLink;

    @FindBy(xpath = "//a[@href='#settings/rules']")
    private Element rulesLink;

    @FindBy(xpath = "//a[@href='#settings/security']")
    private Element securityLink;

    @FindBy(xpath = "//a[@href='#settings/usermanagement']")
    private Element usersLink;

    @FindBy(xpath = "//a[@href='#settings/dashboard']")
    private Element dashboardLink;

    public SettingsTabs(WebDriver driver) {
        super(driver);
    }

    @Override
    protected SettingsTabs me() {
        return this;
    }

    @Step("Click the profile link")
    public Profile clickProfileLink() {
        profileLink.click();
        return Profile.initialize(driver, Profile.class);
    }

    public boolean isAlertTabDisplayed() {

        try {
            return alertsLink.isDisplayed();

        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error finding Alerts tab", e);
            return false;
        }

    }


    public boolean isAccountsTabDisplayed() {

        try {
            return accountsLink.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error finding Accounts tab", e);
            return false;
        }

    }

    public boolean isDashboardTabDisplayed() {

        try {
            return dashboardLink.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error finding Dashboard tab", e);
            return false;
        }

    }

    public boolean isUsersTabDisplayed() {

        try {
            return usersLink.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("Error finding Users tab", e);
            return false;
        }

    }

    @Step("Click the profile link")
    public Dashboard clickDashboardLink() {
        dashboardLink.click();
        return Dashboard.initialize(driver, Dashboard.class);
    }


    @Step("Click the business profile link")
    public BusinessProfile clickBusinessProfileLink() {
        businessProfileLink.click();
        return BusinessProfile.initialize(driver, BusinessProfile.class);
    }

    @Step("Click the accounts link")
    public Accounts clickAccountsLink() {
        accountsLink.click();
        return Accounts.initialize(driver, Accounts.class);
    }

    @Step("Click the alerts link")
    public Alerts clickAlertsLink() {
        alertsLink.click();
        return Alerts.initialize(driver, Alerts.class);
    }

    @Step("Click the categories link")
    public Categories clickCategoriesLink() {
        categoriesLink.click();
        return Categories.initialize(driver, Categories.class);
    }

    @Step("Click the rules link")
    public Rules clickRulesLink() {
        rulesLink.click();
        return Rules.initialize(driver, Rules.class);
    }

    @Step("Click the security link")
    public Security clickSecurityLink() {
        securityLink.click();
        return Security.initialize(driver, Security.class);
    }

    @Step("Click the users link")
    public UsersPage clickUsersLink() {
        usersLink.click();
        return UsersPage.initialize(driver, UsersPage.class);
    }
}