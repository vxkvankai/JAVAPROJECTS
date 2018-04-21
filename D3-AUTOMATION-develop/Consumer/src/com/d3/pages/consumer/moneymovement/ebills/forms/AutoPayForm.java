package com.d3.pages.consumer.moneymovement.ebills.forms;


import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.ebills.D3EBill;
import com.d3.datawrappers.ebills.enums.AutoPay;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.moneymovement.EBillsL10N;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.ebills.EBillsPage;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class AutoPayForm extends MoneyMovementBasePage {

    @FindBy(id = "sourceId")
    private Select fromAccount;

    @FindBy(id = "amountType")
    private Select amountType;

    @FindBy(id = "trigger")
    private Select payOn;

    @FindBy(id = "leadDays")
    private Select daysBefore;

    @FindBy(id = "fixedAmount")
    private Element fixedAmount;

    @FindBy(id = "maxAuthorizedAmount")
    private Element maximumPayAmount;

    @FindBy(css = "button.cancel-autoPay")
    private Element cancelAutoPay;

    @FindBy(css = "button.save-autoPay")
    private Element saveAutoPay;

    @FindBy(id = "alertWhenScheduled")
    private CheckBox alertWhenScheduled;

    @FindBy(id = "alertWhenSent")
    private CheckBox alertWhenSent;

    @FindBy(css = "button.delete")
    private Element deleteAutoPay;

    @FindBy(css = "button.ok")
    private Element confirmDeleteAutoPay;


    public AutoPayForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PageObjectBase me() {
        return this;
    }

    public AutoPayForm selectFromAccount(D3Account account) {
        fromAccount.selectByTextContains(account.getName());
        return this;
    }

    public AutoPayForm selectAmountType(AutoPay.AmountType type) {
        amountType.selectByValue(type.name());
        return this;
    }

    public AutoPayForm selectPaymentDate(AutoPay.PayOn payment) {
        payOn.selectByValue(payment.name());
        return this;
    }

    public AutoPayForm selectDaysBefore(AutoPay.DaysBefore days) {
        daysBefore.selectByValue(days.name());
        return this;
    }

    public AutoPayForm enterMaximumPayAmount(String amount) {
        maximumPayAmount.sendKeys(amount);
        return this;
    }

    public AutoPayForm enterFixedAmount(String amount) {
        fixedAmount.sendKeys(amount);
        return this;
    }

    public AutoPayForm enterAmount(D3EBill autoPay) {
        return autoPay.getAmountType() == AutoPay.AmountType.AMOUNT_DUE ? enterMaximumPayAmount(autoPay.getAmountStr()) : enterFixedAmount(autoPay.getAmountStr());
    }

    public AutoPayForm selectAlerts(D3EBill autoPay) {
        if (autoPay.getAlertWhenSent()) {
            checkAlertWhenSent();
        }
        if (autoPay.getAlertWhenScheduled()) {
            checkAlertWhenScheduled();
        }
        return this;
    }

    public AutoPayForm checkAlertWhenScheduled() {
        alertWhenScheduled.check();
        return this;
    }

    public AutoPayForm checkAlertWhenSent() {
        alertWhenSent.check();
        return this;
    }

    public EBillsPage clickAutoPaySaveButton() {
        saveAutoPay.click();
        waitForSpinner();
        return EBillsPage.initialize(driver, EBillsPage.class);
    }

    public AutoPayForm clickAutoPayDeleteButton() {
        deleteAutoPay.click();
        waitForSpinner();
        return this;
    }

    public EBillsPage clickAutoPayDeleteConfirmButton() {
        confirmDeleteAutoPay.click();
        waitForSpinner();
        return EBillsPage.initialize(driver, EBillsPage.class);
    }

    public AutoPayForm fillOutForm(D3EBill autoPay) {
        selectFromAccount(autoPay.getAutoPayAccount())
            .selectAmountType(autoPay.getAmountType())
            .selectPaymentDate(autoPay.getPayOn())
            .enterAmount(autoPay)
            .selectAlerts(autoPay);
        if (autoPay.getPayOn() == AutoPay.PayOn.LEAD_DAYS) {
            selectDaysBefore(autoPay.getDaysBefore());
        }
        return this;
    }

    /**
     * This method will verify the passed in EBill L10 values are displayed NOTE: Pay On and Pay Amount dropdown are default required fields. Fixed
     * Amount input and Lead Days dropdown are required as well, but you will only see those depending on what is selected for the default dropdowns
     *
     * @param localization List of EBills L10 values
     * @return true if localized values are displayed correctly, false otherwise
     */
    public boolean verifyRequiredFields(List<EBillsL10N.Localization> localization) {
        String errMsg = "The following required field messaging was not displayed on the AutoPay Form: %s";

        for (EBillsL10N.Localization eBillsL10N : localization) {
            try {
                checkIfTextDisplayed(eBillsL10N.getValue(), errMsg);
            } catch (TextNotDisplayedException e) {
                logger.error("Auto Pay required field messages were not displayed", e);
                return false;
            }
        }
        return true;
    }
}
