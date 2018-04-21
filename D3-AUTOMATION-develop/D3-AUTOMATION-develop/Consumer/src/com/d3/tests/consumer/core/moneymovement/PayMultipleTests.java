package com.d3.tests.consumer.core.moneymovement;

import static com.d3.helpers.RandomHelper.getRandomString;

import com.d3.database.AuditDatabaseHelper;
import com.d3.datawrappers.transfers.D3Transfer;
import com.d3.datawrappers.transfers.PayMultipleTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.moneymovement.PayMultipleL10N;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.moneymovement.PayMultiple;
import com.d3.pages.consumer.moneymovement.Schedule;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Epic("Money Movement")
@Feature("Pay Multiple")
@Slf4j
public class PayMultipleTests extends MoneyMovementTestBase {
    private static final String FORMATTED_AMOUNT = "$%,.2f";
    private static final String TEMPLATE_STR = "Template";

    @Flaky
    @TmsLink("75574")
    @Story("Pay Multiple Template")
    @Test(dataProvider = "User with pay multiple template created")
    public void verifyEditingPayMultipleWorksheetAndSubmittingPayment(D3User user, List<PayMultipleTransfer> transfers) {
        String paymentTotal = String.format(FORMATTED_AMOUNT, transfers.stream().collect(Collectors.summingDouble(D3Transfer::getAmount)));
        PayMultipleTransfer transferToSearch = transfers.get(ThreadLocalRandom.current().nextInt(transfers.size()));

        PayMultiple payMultiple = login(user).getHeader()
                .clickMoneyMovementButton()
                .getTabs().clickPayMultipleLink()
            .selectWorksheetByName(TEMPLATE_STR)
                .fillSchedulePaymentInfo(transfers)
                .clickPaySelectedButton();
        Assert.assertTrue(payMultiple.isTextDisplayed(String.format(PayMultipleL10N.Localization.SUBMIT_PAYMENT_CONFIRM.getValue(), paymentTotal)));

        payMultiple.clickConfirmPaymentButton().expandSubmittedWorksheetItemDetails();
        Assert.assertTrue(payMultiple.areSubmittedWorksheetConfirmationDetailsCorrect(transfers));

        payMultiple.clickClosePaymentsScheduledModal();

        //TODO figure out if this is a timing issue. Sometimes will get modal with leave page message after closing payment scheduled confirmation
        try {
            payMultiple.clickLeavePageContinue();
        } catch (NoSuchElementException e) {
            log.warn("Leave page messaging not displayed for user: {}", user);
        }

        Assert.assertTrue(payMultiple.isCorrectTemplateSelected(PayMultipleL10N.Localization.DEFAULT_WORKSHEET_FILTER.getValue()));

        Schedule schedule = payMultiple.getHeader().clickMoneyMovementButton()
                .enterSearchTerm(transferToSearch.getFromAccount().getName()).clickSearchButton()
                .expandShownSummaryThatHasText(transferToSearch.getAmountStr());
        Assert.assertTrue(schedule.isTextDisplayed(transferToSearch.getNote()));
    }


    @Flaky
    @TmsLink("262687")
    @Story("Pay Multiple Template")
    @Test(dataProvider = "User with pay multiple transfers")
    public void verifyCreatingPayMultipleTemplate(D3User user, List<PayMultipleTransfer> transfers) {
        String templateName = String.format("Template!@$3?-%s", getRandomString(30));
        String paymentTotal = String.format(FORMATTED_AMOUNT, transfers.stream().collect(Collectors.summingDouble(D3Transfer::getAmount)));

        PayMultiple payMultiple = login(user).getHeader()
                .clickMoneyMovementButton()
                .getTabs().clickPayMultipleLink()
                .fillSchedulePaymentInfo(transfers)
                .createNewTemplate(templateName);

        Assert.assertTrue(payMultiple.isCorrectTemplateSelected(StringUtils.substring(templateName, 0, 32)));
        Assert.assertTrue(payMultiple.isPaymentTotalCorrect(paymentTotal));
    }

    @Flaky
    @TmsLink("75576")
    @Story("Pay Multiple Template")
    @Test(dataProvider = "User with pay multiple template created")
    public void verifyMessageWhenLeavingTemplatePageDuringEdit(D3User user, List<PayMultipleTransfer> transfers) {
        String paymentTotal = String.format(FORMATTED_AMOUNT, (Double) transfers.stream().mapToDouble(D3Transfer::getAmount).sum());
        PayMultiple payMultiple = login(user).getHeader()
                .clickMoneyMovementButton()
                .getTabs().clickPayMultipleLink()
            .selectWorksheetByName(TEMPLATE_STR)
                .fillSchedulePaymentInfo(transfers);
        payMultiple.getHeader().clickDashboardButton();

        Assert.assertTrue(payMultiple.isTextDisplayed(PayMultipleL10N.Localization.LEAVE_PAGE.getValue()));

        payMultiple.clickLeavePageCancel();
        Assert.assertTrue(payMultiple.isPaymentTotalCorrect(paymentTotal));
    }

    @Flaky
    @TmsLink("75556")
    @Story("Pay Multiple Template")
    @Test(dataProvider = "User with pay multiple template created")
    public void deleteWorksheetTemplate(D3User user, List<PayMultipleTransfer> transfers) {
        PayMultiple payMultiple = login(user).getHeader()
                .clickMoneyMovementButton()
            .getTabs().clickPayMultipleLink().selectWorksheetByName(TEMPLATE_STR).deleteTemplate();
        Assert.assertTrue(payMultiple.isTextDisplayed(PayMultipleL10N.Localization.DELETE_TEMPLATE_CONFIRM_TITLE.getValue()));
        Assert.assertTrue(payMultiple.isDeleteWorksheetConfirmTextDisplayed());
        payMultiple.clickConfirmDeleteTemplate();
        Assert.assertTrue(payMultiple.isTextDisplayed(PayMultipleL10N.Localization.DELETE_TEMPLATE_SUCCESS_TITLE.getValue()));
        Assert.assertTrue(payMultiple.isTextDisplayed(PayMultipleL10N.Localization.DELETE_TEMPLATE_SUCCESS_TEXT.getValue()));

        Integer auditId = AuditDatabaseHelper.getAuditRecordId(user, Audits.WORKSHEET_DELETED);
        Assert.assertNotNull(auditId);
        Assert.assertFalse(Integer.toString(auditId).isEmpty());
    }

    @TmsLink("75555")
    @Story("Pay Multiple Template")
    @Test(dataProvider = "User with pay multiple transfers")
    public void verifySaveTemplateNotActivatedUntilValidAmountsEntered(D3User user, List<PayMultipleTransfer> transfers) {
        transfers.forEach(transfer -> transfer.setAmount(0.00));

        PayMultiple payMultiple = login(user).getHeader()
                .clickMoneyMovementButton()
                .getTabs().clickPayMultipleLink();
        Assert.assertFalse(payMultiple.isPaySelectedButtonEnabled(), "Pay Selected is enabled, but shouldn't be");

        payMultiple.fillSchedulePaymentInfo(transfers);
        Assert.assertTrue(payMultiple.isTextDisplayed(String.format(ScheduleL10N.Localization.RANGE_VALIDATION_ERROR.getValue(), "1.00", "999,999,999.99")));
        Assert.assertFalse(payMultiple.isPaySelectedButtonEnabled(), "Pay Selected is enabled, but shouldn't be");

        transfers.forEach(transfer -> transfer.setAmount(RandomHelper.getRandomNumber(50, 99)));
        payMultiple.fillSchedulePaymentInfo(transfers);
        Assert.assertTrue(payMultiple.isPaySelectedButtonEnabled(), "Pay Selected is not enabled, but should be");

        payMultiple.clickPaySelectedDropdown();
        Assert.assertTrue(payMultiple.isSaveAsTemplateLinkEnabled(), "Save as Template link is not enabled");

    }

    @Flaky
    @TmsLink("75590")
    @Story("Pay Multiple Template")
    @Test(dataProvider = "User with pay multiple transfers")
    public void verifyWorksheetCanBeSubmittedFromDefaultBillPayRecipientsList(D3User user, List<PayMultipleTransfer> transfers) {
        String paymentTotal = String.format(FORMATTED_AMOUNT, transfers.stream().collect(Collectors.summingDouble(D3Transfer::getAmount)));

        PayMultiple payMultiple = login(user).getHeader()
                .clickMoneyMovementButton()
                .getTabs().clickPayMultipleLink()
                .fillSchedulePaymentInfo(transfers)
                .clickPaySelectedButton();
        Assert.assertTrue(payMultiple.isTextDisplayed(String.format(PayMultipleL10N.Localization.SUBMIT_PAYMENT_CONFIRM.getValue(), paymentTotal)));


        payMultiple.clickConfirmPaymentButton().expandSubmittedWorksheetItemDetails();
        Assert.assertTrue(payMultiple.areSubmittedWorksheetConfirmationDetailsCorrect(transfers));
        payMultiple.clickClosePaymentsScheduledModal();

        //TODO figure out if this is a timing issue. Sometimes will get modal with leave page message after closing payment scheduled confirmation
        try {
            payMultiple.clickLeavePageContinue();
        } catch (NoSuchElementException e) {
            log.warn("Leave page messaging not displayed for user: {}", user);
        }

        Assert.assertTrue(payMultiple.isCorrectTemplateSelected(PayMultipleL10N.Localization.DEFAULT_WORKSHEET_FILTER.getValue()));
    }

    @TmsLink("262699")
    @Story("Pay Multiple Template")
    @Test(dataProvider = "User with pay multiple template created")
    public void verifyMessageWhenTemplateNameAlreadyExists(D3User user, List<PayMultipleTransfer> transfers) {
        PayMultiple payMultiple = login(user).getHeader()
                .clickMoneyMovementButton()
                .getTabs()
                .clickPayMultipleLink()
            .selectWorksheetByName(TEMPLATE_STR);
        //Update amounts and try and save as template with existing name
        transfers.forEach(transfer -> transfer.setAmount(RandomHelper.getRandomNumber(50, 99)));
        String currentTemplate = payMultiple.getCurrentTemplateTitle();

        payMultiple.fillSchedulePaymentInfo(transfers)
                .clickPaySelectedDropdown()
                .clickSaveAsTemplateLink()
                .enterTemplateName(currentTemplate);
        Assert.assertTrue(payMultiple.isTextDisplayed(PayMultipleL10N.Localization.WORKSHEET_NAME_EXISTS.getValue()));
    }
}