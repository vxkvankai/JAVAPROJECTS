package com.d3.pages.consumer.settings.users;


import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.settings.UsersL10N;
import com.d3.pages.consumer.settings.SettingsBasePage;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;

public class UsersPage extends SettingsBasePage {

    @FindBy(css = "button.new-user")
    private Element addUserButton;

    @FindBy(xpath = "//div[@class='entity-attribute physical-address']")
    private Element physicalAddress;

    @FindBy(xpath = "//li[@class='entity user-entity active']//div[@class='user-email']")
    private Element secondaryUserEmail;

    @FindBy(xpath = "//li[@class='entity user-entity active']//div[@class='user-name']")
    private Element secondaryUserName;

    @FindBy(xpath = "//li[@class='entity user-entity active']//em[@class='user-login']")
    private Element displayedLoginId;

    @FindBy(xpath = "//div[@class='row entity-attribute work-phone']")
    private Element workPhoneField;

    @FindBy(xpath = "//div[@class='row entity-attribute mobile-phone']")
    private Element mobilePhoneField;

    @FindBy(xpath = "//div[@class='user-access-section user-account-access-section row']")
    private Element accountAccessInfo;

    @FindBy(xpath = "//li[starts-with(@class, 'entity-attribute row')]")
    private List<Element> bankingServiceInfo;

    @FindBy(xpath = "//div[@class='user-access-section user-banking-services-section row']")
    private Element bankingServiceSection;


    private D3Element secondaryUserLoginId(String loginId) {
        By by = By.xpath(String.format("//em[@class='user-login'][.='%s']", loginId));
        return new D3Element(driver.findElement(by));
    }

    public UsersPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected UsersPage me() {
        return this;
    }

    public UserInformation clickAddUserButton() {
        addUserButton.click();
        return UserInformation.initialize(driver, UserInformation.class);
    }

    public UsersPage expandUserDetails(String username) {
        secondaryUserLoginId(username).click();
        return this;
    }

    public boolean areSecUserDetailsCorrect(D3SecondaryUser user) {
        String errorMsg = "{}: '{}' on the Dom does not equal what was entered for the secondary user: '{}'";
        String address = String.format("%s %s %s, %s %s", user.getAddress().getAddress1(), user.getAddress().getAddress2(),
                user.getAddress().getCity(), user.getAddress().getState(), user.getAddress().getPostalCode());

        String addressOnScreen = getPhysicalAddress().trim();
        if (!addressOnScreen.equals(address)) {
            logger.warn(errorMsg, "Physical Address", address, addressOnScreen);
            logger.info("Attempting to check if addresses are the same with no whitespace");
            if (!address.replaceAll("\\s+", "").equals(addressOnScreen.replaceAll("\\s+", ""))) {
                logger.warn("Address comparison with no spaces failed");
                return false;
            }
        }

        try {
            checkIfTextEquals(getEmailAddress(), user.getEmail());
            checkIfTextEquals(getUserName(), user.getFullName());
            checkIfTextEquals(getLoginId(), user.getLoginId());
            checkIfTextEquals(getMobilePhoneField(), user.getMobilePhone().getValue());
            checkIfTextEquals(getWorkPhoneField(), user.getWorkPhone().getValue());
        } catch (TextNotDisplayedException e) {
            logger.warn("Sec User details are not validated", e);
            return false;
        }

        return true;
    }

    public boolean areAccountDetailsCorrect(String checkingAccount, String savingsAccount) {
        String errorMsg = "{}: '{}' on the Dom does not equal what was entered for the secondary user: '{}'";
        String checkingAccountInternalTransferLimits = "Internal Transfers $25.00 4 Daily";
        if (!StringUtils.substringBetween(getAccountAccess(), checkingAccount, savingsAccount).contains(checkingAccountInternalTransferLimits)) {
            logger.warn(errorMsg, "Internal Transfer Limits for Checking Account", checkingAccountInternalTransferLimits, getAccountAccess());
            return false;
        }

        String checkingAccountPaymentLimits = "Bill Pay $250.00 2 Weekly";
        if (!StringUtils.substringBetween(getAccountAccess(), checkingAccount, savingsAccount).contains(checkingAccountPaymentLimits)) {
            logger.warn(errorMsg, "Payment Limits for Checking Account", checkingAccountPaymentLimits, getAccountAccess());
            return false;
        }

        String checkingAccountWireLimits = "Wire $100.00 3 Monthly";
        if (!StringUtils.substringBetween(getAccountAccess(), checkingAccount, savingsAccount).contains(checkingAccountWireLimits)) {
            logger.warn(errorMsg, "Wire Limits for Checking Account", checkingAccountWireLimits, getAccountAccess());
            return false;
        }

        String savingsAccountAccess = "This user does not have any money movement access for this account";
        if (!StringUtils.substringAfter(getAccountAccess(), savingsAccount).contains(savingsAccountAccess)) {
            logger.warn(errorMsg, "Money Movement Access for Savings Account", savingsAccountAccess, getAccountAccess());
            return false;
        }

        return true;
    }

    public boolean areServicesCorrectAccess(Map<String, String> enabledServices) {
        String errorMsg2 = "{}: '{}' on the Dom does has the wrong access level displayed: {}. Should have the access level: {}";
        for (Element service : bankingServiceInfo) {
            String serviceName = StringUtils.substringBefore(service.getText(), "\n").trim();
            String accessLevel = StringUtils.substringAfter(service.getText(), "\n").trim().toUpperCase();
            if (enabledServices.containsKey(serviceName)) {
                if (!accessLevel.equalsIgnoreCase(enabledServices.get(serviceName))) {
                    logger.warn(errorMsg2, "Banking Service", serviceName, accessLevel, enabledServices.get(serviceName));
                    return false;
                }
            } else {
                if (!accessLevel.equals("No Access")) {
                    logger.warn("User should not have access to service: {}, but access is listed as {}", serviceName, accessLevel);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean areUserDetailsCorrect(D3SecondaryUser user, String checkingAccount, String savingsAccount) {
        if (!areSecUserDetailsCorrect(user)) {
            return false;
        }

        if (!areAccountDetailsCorrect(checkingAccount, savingsAccount)) {
            return false;
        }

        // TODO clean this up
        if (!bankingServiceInfo.isEmpty()) {
            if (!areServicesCorrectAccess(user.getEnabledServices())) {
                return false;
            }
        } else {
            String accessLevel = UsersL10N.Localization.NO_SERVICES.getValue();
            String actualAccess = bankingServiceSection.getText();
            if (!accessLevel.equals(actualAccess)) {
                logger.warn("'{}' message was not shown on the screen for the users who did not have access to anything, {} was shown instead",
                        accessLevel, actualAccess);
                return false;
            }
            if (!user.getEnabledServices().isEmpty()) {
                logger.warn("Banking Service Info had no access information, but user should have access to: {}", user.getEnabledServices());
                return false;
            }
        }

        return true;
    }

    public String getPhysicalAddress() {
        return StringUtils.substringAfter(physicalAddress.getText().replace("\n", " "), ": ");
    }

    public String getEmailAddress() {
        return secondaryUserEmail.getText();
    }

    public String getUserName() {
        return secondaryUserName.getText();
    }

    public String getLoginId() {
        return displayedLoginId.getText();
    }

    public String getMobilePhoneField() {
        return StringUtils.deleteWhitespace(StringUtils.substringAfter(mobilePhoneField.getText().replaceAll("[()\\s-]+", ""), ":"));
    }

    public String getWorkPhoneField() {
        return StringUtils.deleteWhitespace(StringUtils.substringAfter(workPhoneField.getText().replaceAll("[()\\s-]+", ""), ":"));
    }

    public String getAccountAccess() {
        return accountAccessInfo.getText().replace("\n", " ");
    }
}
