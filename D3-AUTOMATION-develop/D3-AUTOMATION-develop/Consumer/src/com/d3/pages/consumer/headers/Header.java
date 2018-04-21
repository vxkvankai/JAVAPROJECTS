package com.d3.pages.consumer.headers;

import static com.d3.helpers.WebdriverHelper.isMobile;
import static com.d3.helpers.WebdriverHelper.isMobileApp;

import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.login.LoginPage;
import com.d3.pages.consumer.messages.Notices;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.planning.Budget;
import com.d3.pages.consumer.selfservice.FAQs;
import com.d3.pages.consumer.settings.profile.Profile;
import com.d3.pages.consumer.transactions.TransactionsPage;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class Header extends HeaderBase {

    public Header(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Header me() {
        return this;
    }

    @FindBy(css = "div.nav-icon.dashboard")
    private Element dashboardButton;

    @FindBy(css = "div.nav-icon.messages")
    private Element messagesButton;

    @FindBy(css = "div.nav-icon.accounts")
    private Element accountsButton;

    @FindBy(css = "div.nav-icon.transactions")
    private Element transactionsButton;

    @FindBy(css = "div.nav-icon.money-movement")
    private Element moneyMvntButton;

    @FindBy(css = "div.nav-icon.planning")
    private Element planningButton;

    @FindBy(css = "div.nav-icon.self-service")
    private Element selfServiceButton;

    @FindBy(css = "div.nav-icon.settings")
    private Element settingsButton;

    @FindBy(className = "logout")
    private Element logoutLink;

    @FindBy(css = "i.glyphicon-menu-hamburger")
    private Element hamburgerMenu;

    @FindBy(css = "button.user-select-button")
    private Element toggleDropdown;

    @FindBy(xpath = "//li//a[@href='#accounts']")
    private Element footerAccountsLink;


    public boolean isFooterLinkAccountsDisplayed() {
        try {
            return footerAccountsLink.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Footer Accounts Link not Found", e);
            return false;
        }
    }

    private D3Element personalToggle(D3User user) {
        log.info("Attempting to find user name {} {} to toggle to",
            user.getProfile().getFirstName(),
            user.getProfile().getLastName());
        By by = By.linkText(String.format("%s %s", user.getProfile().getFirstName(), user.getProfile().getLastName()));
        return new D3Element(driver.findElement(by));
    }

    private D3Element businessToggle(String businessName) {
        log.info("Attempting to find business name {} to toggle to", businessName);
        return new D3Element(driver.findElement(By.linkText(businessName)));
    }

    @Step("Click the dashboard tab")
    public Dashboard clickDashboardButton() {
        dashboardButton.click();
        return Dashboard.initialize(driver, Dashboard.class);
    }

    @Step("Click the messages tab")
    public Notices clickMessagesButton() {
        messagesButton.click();
        return Notices.initialize(driver, Notices.class);
    }

    @Step("Click on the Accounts tab")
    public MyAccountsSection clickAccountsButton() {
        if (isMobile()) {
            hamburgerMenu.click();
        }
        accountsButton.click();
        return MyAccountsSection.initialize(driver, MyAccountsSection.class);
    }

    @Step("Click the Transactions tab")
    public TransactionsPage clickTransactionsButton() {
        if (isMobile()) {
            hamburgerMenu.click();
        }
        transactionsButton.click();
        return TransactionsPage.initialize(driver, TransactionsPage.class);
    }

    @Step("Click the Money Movement tab")
    public Schedule clickMoneyMovementButton() {
        if (isMobile()) {
            hamburgerMenu.click();
        }
        moneyMvntButton.click();
        return Schedule.initialize(driver, Schedule.class);
    }

    @Step("Click the planning tab")
    public Budget clickPlanningButton() {
        if (isMobile()) {
            hamburgerMenu.click();
        }
        planningButton.click();
        return Budget.initialize(driver, Budget.class);
    }

    @Step("Click the self service tab")
    public FAQs clickSelfServiceButton() {
        if (isMobile()) {
            hamburgerMenu.click();
        }
        selfServiceButton.click();
        return FAQs.initialize(driver, FAQs.class);
    }

    @Step("Click the settings tab")
    public Profile clickSettingsButton() {
        if (isMobile()) {
            hamburgerMenu.click();
        }
        settingsButton.click();
        return Profile.initialize(driver, Profile.class);
    }

    @Step("Click the Header logout button")
    public LoginPage clickLogout() {
        if (isMobile()) {
            hamburgerMenu.click();
        }
        logoutLink.click();
        if (isMobileApp()) {
            switchToNativeView();
        }
        return LoginPage.initialize(driver, LoginPage.class);
    }

    @Step("Change toggle mode for user {user}")
    public Dashboard changeToggleMode(D3User user) {
        toggleDropdown.click();
        D3Element toggleUser =
            user.getToggleMode().equals(ToggleMode.CONSUMER) ? businessToggle(user.getFirstCompany().getName()) : personalToggle(user);
        toggleUser.click();
        return Dashboard.initialize(driver, Dashboard.class);
    }
}
