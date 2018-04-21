package com.d3.pages.consumer.settings.users;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3AccountLimits;
import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import com.d3.support.wrappers.base.Select;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class AccountServices extends PageObjectBase {

    private static final String LIMITS_NOT_ALLOWED_FOR_PERMISSION = "Account Permission: %s does not have limits associated with it";

    @FindBy(css = "button.btn-next")
    private Element nextButton;

    @FindBy(css = "div.slider-wrap")
    private List<Element> availableAccounts;

    @FindBy(css = "div.slider-wrap input")
    private List<CheckBox> sliderStatus;

    @FindBy(css = "div.slider")
    private List<CheckBox> availableAccountSlider;

    @FindBy(name = "hasStatementsTransactions")
    private List<CheckBox> statementAndTransactionsCheckbox;

    @FindBy(name = "hasTransfer")
    private List<CheckBox> internalTransfersCheckbox;

    @FindBy(name = "hasBillPay")
    private List<CheckBox> billPayCheckbox;

    @FindBy(name = "hasWire")
    private List<CheckBox> wireCheckbox;

    @FindBy(name = "hasAch")
    private List<CheckBox> achCheckbox;

    @FindBy(css = "div.row.ach-limits input[name='amount']")
    private List<Element> achAmountLimit;

    @FindBy(css = "div.row.ach-limits input[name='count']")
    private List<Element> achCountLimit;

    @FindBy(css = "div.row.ach-limits select[name='period']")
    private List<Select> achPeriodLimit;

    @FindBy(css = "div.row.billPay-limits input[name='amount']")
    private List<Element> billPayAmountLimit;

    @FindBy(css = "div.row.billPay-limits input[name='count']")
    private List<Element> billPayCountLimit;

    @FindBy(css = "div.row.billPay-limits select[name='period']")
    private List<Select> billPayPeriodLimit;

    @FindBy(css = "div.row.transfer-limits input[name='amount']")
    private List<Element> transferAmountLimit;

    @FindBy(css = "div.row.transfer-limits input[name='count']")
    private List<Element> transferCountLimit;

    @FindBy(css = "div.row.transfer-limits select[name='period']")
    private List<Select> transferPeriodLimit;

    @FindBy(css = "div.row.wire-limits input[name='amount']")
    private List<Element> wireAmountLimit;

    @FindBy(css = "div.row.wire-limits input[name='count']")
    private List<Element> wireCountLimit;

    @FindBy(css = "div.row.wire-limits select[name='period']")
    private List<Select> wirePeriodLimit;

    public AccountServices(WebDriver driver) {
        super(driver);
    }

    @Override
    protected AccountServices me() {
        return this;
    }

    private Integer accountIndex(String accountName) {
        int index = -1;
        for (int x = 0; x < availableAccounts.size(); x++) {
            if (availableAccounts.get(x).getText().contains(accountName)) {
                index = x;
                break;
            }
        }
        return index;
    }

    private boolean accountEnabled(String accountName) {
        return sliderStatus.get(accountIndex(accountName)).isSelected();
    }

    public AccountServices enableAccount(String accountName) {
        if (!accountEnabled(accountName)) {
            availableAccountSlider.get(accountIndex(accountName)).check();
        }
        return this;
    }

    public AccountServices disableAccount(String accountName) {
        if (accountEnabled(accountName)) {
            availableAccountSlider.get(accountIndex(accountName)).uncheck();
        }
        return this;
    }

    public BankingServices continueToBankingServicesForm() {
        nextButton.click();
        return BankingServices.initialize(driver, BankingServices.class);
    }


    /**
     * Enables accounts for secondary user that they should have access to, and will also
     * enable account permissions and limits
     *
     * @param secondaryUser Secondary User that is being created/added for Primary User
     * @return AccountServices page
     */
    public AccountServices setAccountPermissionAndLimits(D3SecondaryUser secondaryUser) {
        for (D3Account account : secondaryUser.getAccounts()) {
            enableAccount(account.getName())
                .setStatementsAndTransactionsAccess(secondaryUser, account);
            for (D3AccountLimits limit : secondaryUser.getAccountLimitsForAccount(account)) {
                enableLimitPermissions(limit)
                    .enterAmountLimit(limit)
                    .enterCountLimit(limit)
                    .selectPeriodLimit(limit);
            }
        }
        return this;

    }

    /**
     * Enables Statement And Transactions checkbox for the given account of a secondary user
     *
     * @param secondaryUser Secondary User to enable Statement & Transactions for
     * @param account Account to enable Statement & Transactions for
     * @return AccountServices page
     */
    public AccountServices setStatementsAndTransactionsAccess(D3SecondaryUser secondaryUser, D3Account account) {
        int accountIndex = accountIndex(account.getName());
        if (secondaryUser.hasStatementAndTransactionAccess(account)) {
            statementAndTransactionsCheckbox.get(accountIndex).check();
        } else {
            statementAndTransactionsCheckbox.get(accountIndex).uncheck();

        }

        return this;
    }

    /**
     * Will enable money movement permissions checkbox (ACH, Bill Pay, Internal Transfers, Wire) for the given D3AccountLimit
     *
     * @param accountLimit D3AccountLimit data that determines which permission to enable checkbox for
     * @return AccountServices Page
     */
    public AccountServices enableLimitPermissions(D3AccountLimits accountLimit) {
        int accountIndex = accountIndex(accountLimit.getAccount().getName());
        switch (accountLimit.getPermission()) {
            case ACH:
                achCheckbox.get(accountIndex).check();
                break;
            case BILL_PAY:
                billPayCheckbox.get(accountIndex).check();
                break;
            case TRANSFER:
                internalTransfersCheckbox.get(accountIndex).check();
                break;
            case WIRE:
                wireCheckbox.get(accountIndex).check();
                break;
            default:
                log.info(LIMITS_NOT_ALLOWED_FOR_PERMISSION);

        }
        return this;
    }

    /**
     * Will enter the amount limit for D3AccountLimit permission (ACH, Bill Pay, Internal Transfers, Wire) for the given D3AccountLimit
     * and D3Account it is associated with
     *
     * @param accountLimit D3AccountLimit data that determines which permission to set Amount Limit for
     * @return AccountServices Page
     */
    public AccountServices enterAmountLimit(D3AccountLimits accountLimit) {
        int accountIndex = accountIndex(accountLimit.getAccount().getName());
        String amountLimit = accountLimit.getAmountLimitStr();

        switch (accountLimit.getPermission()) {
            case ACH:
                achAmountLimit.get(accountIndex).sendKeys(amountLimit);
                return this;
            case BILL_PAY:
                billPayAmountLimit.get(accountIndex).sendKeys(amountLimit);
                return this;
            case TRANSFER:
                transferAmountLimit.get(accountIndex).sendKeys(amountLimit);
                return this;
            case WIRE:
                wireAmountLimit.get(accountIndex).sendKeys(amountLimit);
                return this;
            default:
                throw new NotImplementedException(String.format(LIMITS_NOT_ALLOWED_FOR_PERMISSION, accountLimit.getPermission().name()));
        }

    }

    /**
     * Will enter the count limit for D3AccountLimit permission (ACH, Bill Pay, Internal Transfers, Wire) for the given D3AccountLimit
     * and D3Account it is associated with
     *
     * @param accountLimit D3AccountLimit data that determines which permission to set Count Limit for
     * @return AccountServices Page
     */
    public AccountServices enterCountLimit(D3AccountLimits accountLimit) {
        int accountIndex = accountIndex(accountLimit.getAccount().getName());
        String countLimit = accountLimit.getCountLimitStr();
        switch (accountLimit.getPermission()) {
            case ACH:
                achCountLimit.get(accountIndex).sendKeys(countLimit);
                return this;
            case BILL_PAY:
                billPayCountLimit.get(accountIndex).sendKeys(countLimit);
                return this;
            case TRANSFER:
                transferCountLimit.get(accountIndex).sendKeys(countLimit);
                return this;
            case WIRE:
                wireCountLimit.get(accountIndex).sendKeys(countLimit);
                return this;
            default:
                throw new NotImplementedException(String.format(LIMITS_NOT_ALLOWED_FOR_PERMISSION, accountLimit.getPermission().name()));

        }
    }

    /**
     * Will enter the period limit for D3AccountLimit permission (ACH, Bill Pay, Internal Transfers, Wire) for the given D3AccountLimit
     * and D3Account it is associated with
     *
     * @param accountLimit D3AccountLimit data that determines which permission to set Period Limit for
     * @return AccountServices Page
     */
    public AccountServices selectPeriodLimit(D3AccountLimits accountLimit) {
        int accountIndex = accountIndex(accountLimit.getAccount().getName());
        String periodLimit = accountLimit.getPeriodLimit().name();
        switch (accountLimit.getPermission()) {
            case ACH:
                achPeriodLimit.get(accountIndex).selectByValue(periodLimit);
                return this;
            case BILL_PAY:
                billPayPeriodLimit.get(accountIndex).selectByValue(periodLimit);
                return this;
            case TRANSFER:
                transferPeriodLimit.get(accountIndex).selectByValue(periodLimit);
                return this;
            case WIRE:
                wirePeriodLimit.get(accountIndex).selectByValue(periodLimit);
                return this;
            default:
                throw new NotImplementedException(String.format(LIMITS_NOT_ALLOWED_FOR_PERMISSION, accountLimit.getPermission().name()));

        }
    }

}
