package com.d3.pages.consumer.moneymovement;

import static com.d3.helpers.WebdriverHelper.waitUntilClickableBy;

import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.exceptions.TextNotContainedException;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.moneymovement.PayMultipleL10N;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class PayMultiple extends MoneyMovementBasePage {

    @FindBy(css = "li.entity")
    private List<Element> recipients;
    @FindBy(css = "button.dropdown-toggle span.title")
    private Element payMultipleDropdownTitle;
    @FindBy(css = "button.dropdown-toggle")
    private Element payMultipleDropdown;
    @FindBy(xpath = "//button[starts-with(@id, 'worksheet-action')]")
    private Element paySelectedDropdown;
    @FindBy(linkText = "Save As New Template")
    private Element saveAsTemplateLink;
    @FindBy(linkText = "Delete Template")
    private Element deleteTemplateLink;
    @FindBy(css = "li.entity.active select[name='source']")
    private Select fromAccount;
    @FindBy(css = "li.entity.active input[name='date']")
    private Element paymentDate;
    @FindBy(css = "li.entity.active input[name='amount']")
    private Element paymentAmount;
    @FindBy(css = "li.entity.active input[id='memo']")
    private Element paymentMemo;
    @FindBy(id = "d3-modal-prompt-input")
    private Element templateName;
    @FindBy(xpath = "//button[.='Save'][not(@disabled)]")
    private Element saveTemplateButton;
    @FindBy(xpath = "//button[.='Close']")
    private Element closeButton;
    @FindBy(xpath = "//button[contains(text(), 'Pay Selected')]")
    private Element paySelectedButton;
    @FindBy(xpath = "//button[.='Confirm']")
    private Element confirmPaymentButton;
    @FindBy(css = "span.total-amount")
    private List<Element> paymentTotal;
    @FindBy(css = "button.cancel")
    private Element leavePageCancel;
    @FindBy(css = "button.submit-one")
    private Element leavePageContinue;
    @FindBy(css = "button.submit-one")
    private Element confirmDeleteTemplate;
    @FindBy(css = "div.modal-body")
    private List<Element> deleteTemplateModal;
    @FindBy(css = "button.expand-details")
    private List<Element> expandDetails;
    @FindBy(css = "div.worksheet-confirmation-item-view")
    private List<Element> worksheetItemDetails;
    @FindBy(css = "div.worksheet-confirmation-view")
    private Element submittedWorksheetSummary;

    public PayMultiple(WebDriver driver) {
        super(driver);
    }

    private D3Element selectRecipient(String recipientNickname) {
        By by = By.xpath(String.format("//div[@class='nickname'][.='%s']", recipientNickname));
        return waitUntilClickableBy(driver, by);
    }

    private D3Element selectWorksheet(String worksheetName) {
        By by = By.xpath(String.format("//a[contains(text(), '%s')]", worksheetName));
        return waitUntilClickableBy(driver, by);
    }

    @Override
    protected PayMultiple me() {
        return this;
    }

    @Step("Select the recipient with nickname: {recipientNickname}")
    public PayMultiple selectRecipientWithNickname(String recipientNickname) {
        selectRecipient(recipientNickname).click();
        return this;
    }

    @Step("Click the first recipient")
    public PayMultiple clickFirstRecipient() {
        recipients.get(0).click();
        return this;
    }

    @Step("Select the account: {accountName}")
    public PayMultiple selectFromAccount(String accountName) {
        fromAccount.selectByTextContains(accountName);
        return this;
    }

    @Step("Enter the payment date: {date}")
    public PayMultiple enterPaymentDate(DateTime date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        String scheduledDate = formatter.print(date);
        paymentDate.sendKeys(scheduledDate);
        return this;
    }

    @Step("Enter {amount} as the payment amount")
    public PayMultiple enterPaymentAmount(String amount) {
        paymentAmount.sendKeys(amount);
        return this;
    }

    @Step("Enter {memo} as the payment memo")
    public PayMultiple enterPaymentMemo(String memo) {
        paymentMemo.sendKeys(memo);
        return this;
    }

    @Step("Click the pay selected button")
    public PayMultiple clickPaySelectedButton() {
        paySelectedButton.click();
        return this;
    }

    @Step("Click the confirm payment button")
    public PayMultiple clickConfirmPaymentButton() {
        confirmPaymentButton.click();
        waitUntilTextPresent(PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_TITLE.getValue());
        return this;
    }

    @Step("Click the pay selected dropdown")
    public PayMultiple clickPaySelectedDropdown() {
        paySelectedDropdown.click();
        return this;
    }

    @Step("Click the save as template link")
    public PayMultiple clickSaveAsTemplateLink() {
        saveAsTemplateLink.click();
        return this;
    }

    @Step("Click the delete template link")
    public PayMultiple clickDeleteTemplateLink() {
        deleteTemplateLink.click();
        return this;
    }

    @Step("Enter {name} as the template name")
    public PayMultiple enterTemplateName(String name) {
        templateName.sendKeys(name);
        return this;
    }

    @Step("Click the save template button")
    public PayMultiple clickSaveTemplateButton() {
        saveTemplateButton.click();
        return this;
    }

    @Step("Click the close payments scheduled modal")
    public PayMultiple clickClosePaymentsScheduledModal() {
        closeButton.click();
        return this;
    }

    @Step("Create a new template with name {name}")
    public PayMultiple createNewTemplate(String name) {
        clickPaySelectedDropdown()
            .clickSaveAsTemplateLink()
            .enterTemplateName(name)
            .clickSaveTemplateButton();
        return this;
    }

    @Step("Delete the template")
    public PayMultiple deleteTemplate() {
        return clickPaySelectedDropdown()
            .clickDeleteTemplateLink();
    }

    @Step("Fill the schedule payment info")
    public PayMultiple fillSchedulePaymentInfo(List<PayMultipleTransfer> payMultipleTransfers) {
        payMultipleTransfers.forEach(transfer ->
            selectRecipientWithNickname(transfer.getToAccount().getTransferableName())
                .selectFromAccount(transfer.getFromAccount().getName())
                .enterPaymentDate(transfer.getScheduledDate())
                .enterPaymentAmount(transfer.getAmountStr())
                .enterPaymentMemo(transfer.getNote()));
        return this;
    }

    @Step("Check if {template} is selected")
    public boolean isCorrectTemplateSelected(String template) {
        log.info("Checking if Pay Multiple dropdown is set to {}", template);
        try {
            checkIfTextEquals(getCurrentTemplateTitle(), template);
        } catch (TextNotDisplayedException e) {
            log.warn("Correct template was not selected", e);
            return false;
        }

        return true;
    }

    @Step("Check if the payment total is {paymentAmount}")
    public boolean isPaymentTotalCorrect(String paymentAmount) {
        log.info("Checking if Pay Multiple Payment Total is equal to {}", paymentAmount);
        try {
            Optional<Element> value = paymentTotal.stream().filter(Element::isDisplayed).findFirst();
            if (value.isPresent()) {
                checkIfTextEquals(value.get().getText(), paymentAmount);
            } else {
                log.warn("Value/element was not present");
                return false;
            }
        } catch (TextNotDisplayedException e) {
            log.warn("Payment total was not correct", e);
            return false;
        }

        return true;
    }

    @Step("Click the Leave Page Cancel button")
    public PayMultiple clickLeavePageCancel() {
        leavePageCancel.click();
        return this;
    }

    @Step("Click the leave Page continue button")
    public PayMultiple clickLeavePageContinue() {
        leavePageContinue.click();
        return this;
    }

    @Step("Select the worksheet named {worksheet}")
    public PayMultiple selectWorksheetByName(String worksheet) {
        payMultipleDropdown.click();
        selectWorksheet(worksheet).click();
        return this;
    }

    @Step("Click the confirm delete template button")
    public PayMultiple clickConfirmDeleteTemplate() {
        confirmDeleteTemplate.click();
        return this;
    }

    @Step("Check if the delete worksheet confirmation text is displayed")
    public boolean isDeleteWorksheetConfirmTextDisplayed() {
        try {
            checkIfTextEquals(getDisplayedElement(deleteTemplateModal).getText(),
                String.format(PayMultipleL10N.Localization.DELETE_TEMPLATE_CONFIRM_TEXT.getValue(), "will not be deleted",
                    "Money Movement Schedule"));
        } catch (TextNotDisplayedException e) {
            log.warn("Delete Worksheet Confirm message not displayed", e);
            return false;
        }

        return true;
    }

    @Step("Check if the pay selected button is enabled")
    public boolean isPaySelectedButtonEnabled() {
        return paySelectedButton.isEnabled();
    }

    @Step("Check if the save as template link is enabled")
    public boolean isSaveAsTemplateLinkEnabled() {
        return saveAsTemplateLink.isEnabled();
    }

    @Step("Get the current template title")
    public String getCurrentTemplateTitle() {
        return payMultipleDropdownTitle.getText();
    }

    @Step("Check if the account named {accountName} is displayed")
    public boolean isAccountDisplayed(String accountName) {
        return fromAccount.optionContainsText(accountName);
    }

    @Step("Expand the submitted worksheet item details")
    public PayMultiple expandSubmittedWorksheetItemDetails() {
        expandDetails.forEach(Element::click);
        return this;
    }

    @Step("Check if the submitted worksheet confimation details are correct")
    public boolean areSubmittedWorksheetConfirmationDetailsCorrect(List<PayMultipleTransfer> transfers) {
        String errorMsg = "Submitted Worksheet Summary %s did not contain the following information %s";
        String expectedPaymentTotal = String.format("$%,.2f", (Double) transfers.stream().mapToDouble(D3Transfer::getAmount).sum());

        //Checking entire summary for correct labels and payment total
        try {
            String submittedWorksheet = submittedWorksheetSummary.getText().replaceAll("\n", " ");
            checkIfTextContains(submittedWorksheet, errorMsg,
                String.format("%s %s", PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_PAYMENT_TOTAL.getValue().toUpperCase(),
                    expectedPaymentTotal));
            checkIfTextContains(submittedWorksheet, errorMsg, PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_AMOUNT_LABEL.getValue().toUpperCase());
            checkIfTextContains(submittedWorksheet, errorMsg, PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_DATE_LABEL.getValue().toUpperCase());
            checkIfTextContains(submittedWorksheet, errorMsg, PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_FROM_LABEL.getValue().toUpperCase());
            checkIfTextContains(submittedWorksheet, errorMsg, PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_RECIPIENT_LABEL.getValue().toUpperCase());
            transfers.forEach(transfer -> Assert.assertTrue(isIndividualWorksheetItemInfoCorrect(transfer)));
        } catch (TextNotContainedException e) {
            log.error("Worksheet summary did not display label or Payment total correctly", e);
            return false;
        }
        return true;
    }

    @Step("Check if the individual worksheet item info is correct")
    private boolean isIndividualWorksheetItemInfoCorrect(PayMultipleTransfer transfer) {
        String errorMsg = "Submitted Worksheet item %s did not contain the following information %s";
        //Checking individual worksheet transfer item for correct information
        String worksheetItem =
            worksheetItemDetails.stream()
                .filter(item -> item.getText().contains(transfer.getAmountStr()))
                .findFirst().orElseThrow(
                () -> new NoSuchElementException(String.format("No Worksheet Item found with amount $%s", transfer.getAmountStr())))
                .getText().replaceAll("\n", " ");
        try {
            checkIfTextContains(worksheetItem, errorMsg, transfer.getToAccount().getName());
            checkIfTextContains(worksheetItem, errorMsg, transfer.getFromAccount().getName());
            checkIfTextContains(worksheetItem, errorMsg, transfer.getScheduledDateString());
            checkIfTextContains(worksheetItem, errorMsg, transfer.getAmountStr());
            checkIfTextContains(worksheetItem, errorMsg,
                String.format("%s: %s", PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_MEMO_LABEL.getValue().toUpperCase(),
                    transfer.getNote()));
            checkIfTextContains(worksheetItem, errorMsg,
                PayMultipleL10N.Localization.SUBMITTED_WORKSHEET_SUMMARY_CONFIRMATION_NUMBER_LABEL.getValue().toUpperCase());
        } catch (TextNotContainedException e) {
            log.error("Individual worksheet item did not contain the correct information", e);
            return false;
        }
        return true;
    }


}