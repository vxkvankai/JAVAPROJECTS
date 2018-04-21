package com.d3.pages.consumer.moneymovement.ebills;

import com.d3.datawrappers.ebills.D3EBill;
import com.d3.datawrappers.ebills.enums.AutoPay;
import com.d3.datawrappers.ebills.enums.FileReason;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.helpers.DateAndCurrencyHelper;
import com.d3.l10n.moneymovement.EBillsL10N;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.ebills.forms.AutoPayForm;
import com.d3.pages.consumer.moneymovement.ebills.forms.FileForm;
import com.d3.pages.consumer.moneymovement.ebills.forms.PayNowForm;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class EBillsPage extends MoneyMovementBasePage {

    @FindBy(css = "section.active-ebills div.entity-summary")
    private List<Element> activeEBillers;

    @FindBy(css = "section.received-ebills div.entity-summary")
    private List<Element> recievedEBills;

    @FindBy(css = "li.entity.active button.fileEbill")
    private Element fileEbill;

    @FindBy(css = "li.entity.active button.addAutopay")
    private Element addAutoPay;

    @FindBy(css = "li.entity.active button.editAutopay")
    private Element editAutoPay;

    @FindBy(css = "li.entity.active button.payNow")
    private Element payNow;

    @FindBy(css = "button.dropdown-toggle")
    private Element addEBillerDropdown;

    @FindBy(css = "button.stopEbills")
    private Element stopEBills;

    @FindBy(css = "span.more-details")
    private Element moreDetails;

    @FindBy(css = "button.save-paper")
    private Element goPaperlessSaveButton;

    @FindBy(css = "button.ok")
    private Element removeEBillerConfirm;

    @FindBy(css = "a.dropDownItemSelect")
    private List<Element> availableRecipients;

    @FindBy(id = "searchTerm")
    private Element searchInput;

    @FindBy(id = "input-search-btn")
    private Element searchButton;

    @FindBy(id = "startDate")
    private Element startDate;

    @FindBy(id = "endDate")
    private Element endDate;

    @FindBy(css = "button.searchButton")
    private Element dateRangeSearchButton;

    @FindBy(css = "button.clear-search-date-range")
    private Element clearDateRangeButton;

    @FindBy(id = "date-range-btn")
    private Element dateRangeButton;

    @FindBy(xpath = "//span[@data-role='remove']")
    private List<Element> searchFilters;

    public EBillsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PageObjectBase me() {
        return this;
    }

    public EBillsPage expandActiveEbiller(String ebillerName) {
        getElementInListByTextContains(activeEBillers, ebillerName).click();
        return this;
    }

    public EBillsPage stopReceivingPaperBills() {
        moreDetails.click();
        return this;
    }

    public EBillsPage clickGoPaperLessSaveButton() {
        goPaperlessSaveButton.click();
        return this;
    }

    public EBillsPage clickStopEbillsButton() {
        stopEBills.click();
        return this;
    }

    public EBillsPage confirmRemoveEBiller() {
        removeEBillerConfirm.click();
        return this;
    }

    public EBillsPage expandAddEbillerDropdown() {
        addEBillerDropdown.click();
        return this;
    }

    public boolean isRecipientAvailableToAddAsEBiller(Recipient recipient) {
        try {
            return getElementInListByTextContains(availableRecipients, recipient.getName()).isDisplayed();
        } catch (NoSuchElementException e) {
            log.error("Recipient not available to add as E-Biller", e);
            return false;
        }
    }

    public AutoPayForm clickAddAutoPayButton() {
        addAutoPay.click();
        return AutoPayForm.initialize(driver, AutoPayForm.class);
    }

    public AutoPayForm clickEditAutoPayButton() {
        editAutoPay.click();
        return AutoPayForm.initialize(driver, AutoPayForm.class);
    }

    public FileForm clickFileButton() {
        fileEbill.click();
        return FileForm.initialize(driver, FileForm.class);
    }

    public PayNowForm clickPayNowButton() {
        payNow.click();
        return PayNowForm.initialize(driver, PayNowForm.class);
    }

    public EBillsPage expandEbillWithText(String firstValue, String secondValue) {
        getElementInListByMultipleTextContains(recievedEBills, firstValue, secondValue).click();
        return this;
    }

    public EBillsPage enterSearchTerm(String searchTerm) {
        searchInput.sendKeys(searchTerm);
        return this;
    }

    public EBillsPage clickSearchButton() {
        searchButton.click();
        return this;
    }

    public EBillsPage clearSearchFilters() {
        searchFilters.forEach(Element::click);
        return this;
    }

    public EBillsPage clickDateRangeButton() {
        dateRangeButton.click();
        return this;
    }

    public EBillsPage enterStartDate(DateTime date) {
        startDate.sendKeys(DateAndCurrencyHelper.formatDateTime("MM/dd/yyy", date));
        return this;
    }

    public EBillsPage enterEndDate(DateTime date) {
        endDate.sendKeys(DateAndCurrencyHelper.formatDateTime("MM/dd/yyy", date));
        return this;
    }

    public EBillsPage clickDateRangeSearchButton() {
        dateRangeSearchButton.click();
        return this;
    }

    public EBillsPage clickClearDateRangeButton() {
        clearDateRangeButton.click();
        return this;
    }

    private boolean isPayNowButtonDisplayed() {
        try {
            return payNow.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Error finding pay now button", e);
            return false;
        }
    }

    private boolean isFileButtonDisplayed() {
        try {
            return fileEbill.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Error finding file Ebill", e);
            return false;
        }
    }

    public boolean isAddAutoPayButtonDisplayed() {
        try {
            return addAutoPay.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Error finding add auto pay", e);
            return false;
        }
    }

    /**
     * This method will verify if the received E-Bill is displaying the correct information based on it's status
     *
     * @param eBill D3EBill datawrapper info to pass to method
     * @return true if information is correct, false otherwise
     */
    public boolean isEBillFormCorrect(D3EBill eBill) {
        String errMsg = "The following text was not displayed on the E-Bill Form: %s";
        switch (eBill.getStatus()) {
            case FILED:
                try {
                    checkIfTextDisplayed(EBillsL10N.Localization.MINIMUM_DUE.getValue(), errMsg);
                    checkIfTextDisplayed(EBillsL10N.Localization.BALANCE.getValue(), errMsg);
                    checkIfTextDisplayed(EBillsL10N.Localization.FILED_NOTE.getValue(), errMsg);
                    checkIfTextDisplayed(eBill.getNote(), errMsg);
                    if (eBill.getFileReason() != FileReason.NONE_SPECIFIED) {
                        checkIfTextDisplayed(EBillsL10N.Localization.FILED_REASON.getValue(), errMsg);
                        checkIfTextDisplayed(eBill.getFileReason().getFormatted(), errMsg);
                    }
                    return (!isFileButtonDisplayed() && !isPayNowButtonDisplayed());
                } catch (TextNotDisplayedException e) {
                    log.error("EBills L10 was not displayed correctly", e);
                    return false;
                }
            case PAID:
                try {
                    checkIfTextDisplayed(EBillsL10N.Localization.MINIMUM_DUE.getValue(), errMsg);
                    checkIfTextDisplayed(EBillsL10N.Localization.BALANCE.getValue(), errMsg);
                    return (!isFileButtonDisplayed() && !isPayNowButtonDisplayed());
                } catch (TextNotDisplayedException e) {
                    log.error("EBills L10 was not displayed correctly", e);
                    return false;
                }
            case UNPAID:
                try {
                    checkIfTextDisplayed(EBillsL10N.Localization.MINIMUM_DUE.getValue(), errMsg);
                    checkIfTextDisplayed(EBillsL10N.Localization.BALANCE.getValue(), errMsg);
                    return (isFileButtonDisplayed() && isPayNowButtonDisplayed());
                } catch (TextNotDisplayedException | NoSuchElementException e) {
                    log.error("EBills L10 was not displayed correctly, or Pay Now and File buttons were not displayed", e);
                    return false;
                }

            default:
                throw new IllegalArgumentException(String.format("The following is not a valid E-Bill Status : %s", eBill.getStatus().toString()));
        }
    }

    /**
     * This method will verify if submitted Auto Pay for an active E-Biller is displaying the correct information
     *
     * @param autoPay D3EBill datawrapper info to pass to method
     * @return true if information is correct, false otherwise
     */
    public boolean areAutoPayDetailsCorrect(D3EBill autoPay) {
        String errMsg = "The following text was not displayed on the AutoPay Form: %s";
        try {
            checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_ACTIVE.getValue(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_ACCOUNT.getValue(), errMsg);
            checkIfTextDisplayed(autoPay.getAutoPayAccount().getName(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_AMOUNT.getValue(), errMsg);
            checkIfTextDisplayed(autoPay.getAmountType().toString(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_PAY_ON.getValue(), errMsg);
            checkIfTextDisplayed(autoPay.getPayOn().toString(), errMsg);
            if (autoPay.getPayOn() == AutoPay.PayOn.LEAD_DAYS) {
                checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_LEAD_DAYS.getValue(), errMsg);
                checkIfTextDisplayed(autoPay.getDaysBefore().toString(), errMsg);
            }

            if (autoPay.getAmountType() == AutoPay.AmountType.AMOUNT_DUE) {
                checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_MAX_AMOUNT.getValue(), errMsg);

            } else if (autoPay.getAmountType() == AutoPay.AmountType.FIXED_AMOUNT) {
                checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_FIXED_AMOUNT.getValue(), errMsg);

            }

            if (autoPay.getAlertWhenSent()) {
                checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_ALERT_SENT.getValue(), errMsg);

            }
            if (autoPay.getAlertWhenScheduled()) {
                checkIfTextDisplayed(EBillsL10N.Localization.AUTOPAY_STATUS_ALERT_SCHEDULED.getValue(), errMsg);

            }
            checkIfTextDisplayed(autoPay.getAmountStr(), errMsg);
        } catch (TextNotDisplayedException e) {
            log.error("EBills Auto Pay Information was not displayed correctly", e);
            return false;
        }

        return true;

    }

    /**
     * This method will verify if correct received ebills are displayed after a search filter has been applied
     *
     * @param text text to verify is contained in each displayed ebill after a search filter has been applied
     * @return true if displayed ebill contains the specified text, false otherwise
     */
    public boolean filteredEbillsContainText(String text) {
        return recievedEBills.stream().allMatch(element -> element.getText().contains(text));
    }
}