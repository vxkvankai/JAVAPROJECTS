package com.d3.pages.consumer.settings.users;


import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3AccountLimits;
import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.datawrappers.user.enums.UserServices;
import com.d3.exceptions.TextNotContainedException;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.settings.UsersL10N;
import com.d3.pages.consumer.settings.SettingsBasePage;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class UsersPage extends SettingsBasePage {

    @FindBy(css = "button.new-user")
    private Element addUserButton;

    @FindBy(css = "li.user-entity.active div.physical-address div.value")
    private Element physicalAddress;

    @FindBy(css = "li.user-entity.active div.user-email")
    private Element secondaryUserEmail;

    @FindBy(css = "li.user-entity.active div.user-name")
    private Element secondaryUserName;

    @FindBy(css = "li.user-entity.active em.user-login")
    private Element displayedLoginId;

    @FindBy(css = "li.user-entity.active div.work-phone div.value")
    private Element workPhoneField;

    @FindBy(css = "li.user-entity.active div.mobile-phone div.value")
    private Element mobilePhoneField;

    @FindBy(css = "div.user-access-section p.user-account")
    private List<Element> userAccountsAvailable;

    @FindBy(css = "div.user-access-section div.access")
    private List<Element> userAccountAccess;

    @FindBy(css = "li.entity-attribute.row label.key")
    private List<Element> availableBankingServices;

    @FindBy(css = "li.entity-attribute.row div.value")
    private List<Element> bankingServiceAccessLevel;

    @FindBy(css = "li.user-entity")
    private List<Element> createdSecondaryUsers;

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

    /**
     * Expands secondary user the contains the given user name
     * NOTE: Username will display as all lowercase once they're created and you've navigated away from the Settings > User page and come back
     *
     * @param username LoginId that was given to the created secondary user
     * @return UserPage
     */
    public UsersPage expandUserDetails(String username) {
        createdSecondaryUsers.stream()
            .filter(element -> element.getText().contains(username) || element.getText().contains(username.toLowerCase()))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("No created secondary user found with text containing username %s", username))).click();
        return this;
    }

    /**
     * Checks if displayed information for given expanded secondary user matches what was entered on User Information form
     *
     * @param secondaryUser Secondary User to check User Information
     * @return true if user information displayed is correct for secondary user
     */
    public boolean areSecUserDetailsCorrect(D3SecondaryUser secondaryUser) {
        String address = String.format("%s %s %s, %s %s", secondaryUser.getAddress().getAddress1(), secondaryUser.getAddress().getAddress2(),
            secondaryUser.getAddress().getCity(), secondaryUser.getAddress().getState(), secondaryUser.getAddress().getPostalCode());
        String displayedPhysicalAddress = getPhysicalAddress().trim();

        try {
            checkIfTextEquals(displayedPhysicalAddress, address);
            checkIfTextEquals(secondaryUserEmail.getText(), secondaryUser.getEmail());
            checkIfTextEquals(secondaryUserName.getText(), secondaryUser.getFullName());
            checkIfTextEquals(displayedLoginId.getText().toLowerCase(), secondaryUser.getLogin().toLowerCase()); //NOTE: Username will display as all lowercase once they're created and you've navigated away from the Settings > User page and come back
            checkIfTextEquals(mobilePhoneField.getText(), secondaryUser.getMobilePhone().getFormattedPhoneNumber());
            checkIfTextEquals(workPhoneField.getText(), secondaryUser.getWorkPhone().getFormattedPhoneNumber());
        } catch (TextNotDisplayedException e) {
            log.warn("Secondary User details are not validated", e);
            return false;
        }

        return true;
    }


    /**
     * Checks if displayed account permissions for given expanded secondary user match what was entered on Account Access form
     *
     * @param secondaryUser Secondary User to check account permissions for
     * @return true if displayed accounts, permissions, and money movement limits are correct for secondary user
     */
    public boolean areAccountPermissionsCorrect(D3SecondaryUser secondaryUser) {
        String limitsErrorMsg = "Displayed account access %s did not contain the correct limit access that was expected %s";
        for (D3Account account : secondaryUser.getAccounts()) {
            log.info("Checking account access for available account {}:", account.getName());
            String accountAccess = getDisplayedAccountAccess(account);

            if (secondaryUser.hasStatementAndTransactionAccess(account)) {
                try {
                    checkIfTextContains(accountAccess, limitsErrorMsg, UsersL10N.Localization.STATEMENTS_AND_TRANSACTION_ACCESS.getValue());
                } catch (TextNotContainedException e) {
                    log.error("Secondary User should have Statements & Transactions for account: {}", account.getName());
                    return false;
                }
            }
            if (!secondaryUser.hasMoneyMovementAccess(account)) {
                try {
                    checkIfTextContains(accountAccess, "No displayed", UsersL10N.Localization.NO_MONEY_MOVEMENT_ACCESS.getValue());
                } catch (TextNotContainedException e) {
                    log.error("Secondary user should not have any money movement access for account: {}", account.getName());
                    return false;
                }
            } else {
                return areMoneyMovementLimitsCorrect(secondaryUser.getAccountLimitsForAccount(account));

            }
        }

        return true;
    }


    /**
     * Checks if displayed money movement limits for given secondary user account match what was entered on Account Access form
     *
     * @param limits D3AccountLimits to check for secondary user
     * @return true if each limit (permission, amount limit, count limit, period limit) matches what the secondary user was created with
     */
    private boolean areMoneyMovementLimitsCorrect(List<D3AccountLimits> limits) {
        String limitsErrorMsg = "Displayed account access %s did not contain the correct limit access that was expected %s";
        for (D3AccountLimits limit : limits) {
            String accountAccess = getDisplayedAccountAccess(limit.getAccount());
            try {
                checkIfTextContains(accountAccess, limitsErrorMsg, String.format("%s $%s %s %s", limit.getPermission().getFormatted(), limit.getAmountLimitStr(), limit.getCountLimitStr(), limit.getPeriodLimit().getFormatted()));
            } catch (TextNotContainedException e) {
                log.error("Money Movement permission limit was not correct");
                return false;
            }
        }

        return true;
    }

    /**
     * Checks displayed Banking Services and Access Levels of an expanded Secondary User match what was entered on Banking Services form
     *
     * @param secondaryUser Secondary User to check Banking Service access for
     * @return true displayed Banking Services and Access Levels match what Secondary User was created with
     */
    public boolean areBankingServicesAndAccessLevelsCorrect(D3SecondaryUser secondaryUser) {
        if (secondaryUser.getServices().isEmpty()) {
            try {
                checkIfTextDisplayed(UsersL10N.Localization.NO_SERVICES.getValue(), "No Services L10N text not displayed");
            } catch (TextNotDisplayedException e) {
                log.error("Secondary User should not have access to any banking services");
                return false;
            }
        } else {
            for (int x = 0; x < availableBankingServices.size(); x++) {
                String service = availableBankingServices.get(x).getText().trim().replaceAll("-", "").toUpperCase();
                if (!secondaryUser.getServices().contains(UserServices.valueOf(service))) {
                    log.error("Secondary User should not have access to Banking Service {}", service);
                    return false;
                }

                try {
                    checkIfTextEquals(bankingServiceAccessLevel.get(x).getText(), secondaryUser.getAccessLevelForService(UserServices.valueOf(service)).toString());
                } catch (TextNotDisplayedException e) {
                    log.error("User access level for banking service {} was not correct", service);
                    return false;
                }
            }

        }

        return true;
    }

    public boolean areUserDetailsCorrect(D3SecondaryUser user) {
        return areSecUserDetailsCorrect(user) && areAccountPermissionsCorrect(user) && areBankingServicesAndAccessLevelsCorrect(user);

    }

    public String getPhysicalAddress() {
        return physicalAddress.getText().replace("\n", " ");
    }

    private String getDisplayedAccountAccess(D3Account account) {
        return userAccountAccess.get(accountIndex(account.getName())).getText().replaceAll("\n", " ");
    }

    private Integer accountIndex(String accountName) {
        int index = -1;
        for (int x = 0; x < userAccountsAvailable.size(); x++) {
            if (userAccountsAvailable.get(x).getText().contains(accountName)) {
                index = x;
                break;
            }
        }
        return index;
    }
}
