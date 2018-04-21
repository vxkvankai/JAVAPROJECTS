package com.d3.tests.consumer.core.moneymovement;

import com.d3.database.AuditDatabaseHelper;
import com.d3.datawrappers.ebills.D3EBill;
import com.d3.datawrappers.ebills.enums.AutoPay;
import com.d3.datawrappers.ebills.enums.Status;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.moneymovement.EBillsL10N;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.ebills.EBillsPage;
import com.d3.pages.consumer.moneymovement.ebills.forms.AutoPayForm;
import com.d3.pages.consumer.moneymovement.ebills.forms.FileForm;
import com.d3.pages.consumer.moneymovement.ebills.forms.PayNowForm;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

@Epic("Money Movement")
@Feature("E-Bills")
public class EBillTests extends MoneyMovementTestBase {

    @TmsLink("385365")
    @Story("Stop Paper Bills")
    @Test(dataProvider = "User with active e-billers")
    public void verifyStopReceivingPaperBills(D3User user, Recipient recipient) {
        EBillsPage ebills = login(user).getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink()
            .expandActiveEbiller(recipient.getName())
            .stopReceivingPaperBills()
            .clickGoPaperLessSaveButton();
        Assert.assertTrue(ebills.isTextDisplayed(EBillsL10N.Localization.SUPRESS_PAPER_STATEMENT_SUCCESS.getValue()));
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.PAPER_SUPPRESSION_SUBMITTED));

    }

    @TmsLink("523300")
    @Story("Stop E-Bills")
    @Test(dataProvider = "User with active e-billers")
    public void verifyStopEBills(D3User user, Recipient recipient) {
        EBillsPage ebills = login(user).getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink()
            .expandActiveEbiller(recipient.getName())
            .clickStopEbillsButton();
        Assert.assertTrue(ebills.isTextDisplayed(EBillsL10N.Localization.REMOVE_EBILLER_TITLE.getValue()));
        Assert.assertTrue(ebills.isTextDisplayed(EBillsL10N.Localization.REMOVE_EBILLER_TEXT.getValue()));

        ebills.confirmRemoveEBiller()
            .expandAddEbillerDropdown();
        Assert.assertTrue(ebills.isRecipientAvailableToAddAsEBiller(recipient));
    }

    @TmsLink("523319")
    @Story("File E-Bill")
    @Test(dataProvider = "User with active e-billers")
    public void verifyEbillFile(D3User user, Recipient recipient) {
        D3EBill ebillFiling = D3EBill.createRandomFiling();
        EBillsPage ebills = login(user).getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink();

        FileForm fileForm = ebills
            .expandEbillWithText(recipient.getName(), Status.UNPAID.getFormatted())
            .clickFileButton();
        Assert.assertTrue(fileForm.isFileFormCorrect());

        fileForm.selectFileReason(ebillFiling.getFileReason())
            .enterFillingNote(ebillFiling.getNote())
            .clickFileSaveButton()
            .expandEbillWithText(recipient.getName(), ebillFiling.getStatus().getFormatted());
        Assert.assertTrue(ebills.isEBillFormCorrect(ebillFiling), "Ebill form is not correct");
        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.EBILL_FILED));

    }

    @TmsLink("523322")
    @Story("Pay Now")
    @Test(dataProvider = "User with active e-billers")
    public void verifyEBillPayNow(D3User user, Recipient recipient) {
        Dashboard dashboard = login(user); //NOTE: need to log in before ebills are available in database
        D3EBill ebill = D3EBill.createRandomPayNowTransfer(user, recipient);
        PayNowForm payNow = dashboard.getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink()
            .expandEbillWithText(recipient.getName(), ebill.getTransfer().getAmountStr())
            .clickPayNowButton();
        Assert.assertTrue(payNow.isFormPopulatedWithCorrectValues(ebill.getTransfer()), "EBill Pay Now form not pre-populated with the correct values");

        payNow.fillOutForm(ebill.getTransfer()).clickSubmitButton();
        Assert.assertTrue(payNow.isTextDisplayed(
            String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), ebill.getTransfer().getAmountStr(),
                ebill.getTransfer().getToAccount().getTransferableName(), ebill.getTransfer().getFromAccount().getTransferableName(),
                ebill.getTransfer().getScheduledDateString())));
        payNow.clickContinueButton()
            .clickOkButton();

        Schedule schedulePage = dashboard.getHeader()
            .clickMoneyMovementButton()
            .getTabs()
            .clickScheduleLink()
            .expandTransferDetails(ebill.getTransfer().getAmountStr());
        Assert.assertTrue(schedulePage.isSingleTransferCorrect(ebill.getTransfer()), "Schedule payment/transfer details are not correct");

        EBillsPage ebills = dashboard.getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink()
            .expandEbillWithText(recipient.getName(), Status.PAID.getFormatted());
        Assert.assertTrue(ebills.isEBillFormCorrect(ebill), "Ebill form is not correct");

        Assert.assertNotNull(AuditDatabaseHelper.getAuditRecordId(user, Audits.PAYMENT_SUBMITTED), "PAYMENT_SUBMITTED Audit record not found for user");

    }


    @TmsLink("523314")
    @TmsLink("309131")
    @TmsLink("523316")
    @Story("Add Auto Pay")
    @Test(dataProvider = "User with active e-billers and auto pay data")
    public void verifyAddAutoPayAndRequiredFields(D3User user, Recipient recipient, D3EBill autoPay) {
        EBillsPage eBillsPage = login(user).getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink();
        AutoPayForm autoPayForm = eBillsPage
            .expandActiveEbiller(recipient.getName())
            .clickAddAutoPayButton();

        autoPayForm.clickAutoPaySaveButton();
        Assert.assertTrue(autoPayForm.verifyRequiredFields(Arrays.asList(EBillsL10N.Localization.AUTOPAY_VALIDATE_PAY_AMOUNT, EBillsL10N.Localization.AUTOPAY_VALIDATE_PAY_ON)));
        // Fix Amount and Lead Days are other non default required fields
        autoPayForm.selectAmountType(AutoPay.AmountType.FIXED_AMOUNT).selectPaymentDate(AutoPay.PayOn.LEAD_DAYS).clickAutoPaySaveButton();
        Assert.assertTrue(autoPayForm.verifyRequiredFields(Arrays.asList(EBillsL10N.Localization.AUTOPAY_VALIDATE_FIXED_AMOUNT, EBillsL10N.Localization.AUTOPAY_VALIDATE_LEAD_DAYS)));

        autoPayForm.fillOutForm(autoPay)
            .clickAutoPaySaveButton();
        Assert.assertTrue(eBillsPage.areAutoPayDetailsCorrect(autoPay));

    }

    @TmsLink("523317")
    @Story("Edit Auto Pay")
    @Test(dataProvider = "User with active e-billers enrolled in Auto Pay")
    public void verifyEditAutoPay(D3User user, Recipient recipient) {
        D3EBill newAutoPayDetails = D3EBill.createRandomAutoPay(user);
        EBillsPage autoPayForm = login(user).getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink()
            .expandActiveEbiller(recipient.getName())
            .clickEditAutoPayButton()
            .fillOutForm(newAutoPayDetails)
            .clickAutoPaySaveButton();
        Assert.assertTrue(autoPayForm.areAutoPayDetailsCorrect(newAutoPayDetails));
    }

    @TmsLink("523318")
    @Story("Delete Auto Pay")
    @Test(dataProvider = "User with active e-billers enrolled in Auto Pay")
    public void verifyDeleteAutoPay(D3User user, Recipient recipient) {
        EBillsPage eBillsPage = login(user).getHeader().clickMoneyMovementButton().getTabs().clickEbillsLink();

        AutoPayForm autoPayForm = eBillsPage
            .expandActiveEbiller(recipient.getName())
            .clickEditAutoPayButton()
            .clickAutoPayDeleteButton();
        Assert.assertTrue(autoPayForm.isTextDisplayed(EBillsL10N.Localization.AUTOPAY_DELETE_CONFIRM_TITLE.getValue()));
        Assert.assertTrue(autoPayForm.isTextDisplayed(EBillsL10N.Localization.AUTOPAY_DELETE_CONFIRM_TEXT.getValue()));

        autoPayForm.clickAutoPayDeleteConfirmButton()
            .expandActiveEbiller(recipient.getName());

        Assert.assertTrue(eBillsPage.isAddAutoPayButtonDisplayed());
    }

    @TmsLink("309112")
    @Story("Search/Filter Received E-Bills")
    @Test(dataProvider = "User with active e-billers")
    public void verifyAutoPaySearchResults(D3User user, Recipient recipient) {
        Dashboard dashboard = login(user); //NOTE: need to log in before ebills are available in database
        D3EBill transferDetails = D3EBill.createRandomPayNowTransfer(user, recipient);

        //Verify invalid text and date searches
        EBillsPage eBillsPage = dashboard
            .getHeader()
            .clickMoneyMovementButton()
            .getTabs()
            .clickEbillsLink()
            .enterSearchTerm(RandomHelper.getRandomString(10))
            .clickSearchButton();
        Assert.assertTrue(eBillsPage.isTextDisplayed(EBillsL10N.Localization.NO_EBILLS_RECEIVED.getValue()),
            "No Ebill Receive L10N not displayed");

        eBillsPage.clearSearchFilters()
            .clickDateRangeButton()
            .enterEndDate(DateTime.now())
            .clickDateRangeSearchButton();
        Assert.assertTrue(eBillsPage.isTextDisplayed(EBillsL10N.Localization.NO_EBILLS_RECEIVED.getValue()),
            "No Ebill Receive L10N not displayed");

        //Verify valid text and date searches
        eBillsPage.clickClearDateRangeButton()
            .enterSearchTerm(recipient.getName())
            .clickSearchButton();
        Assert.assertTrue(eBillsPage.filteredEbillsContainText(recipient.getName()),
            "Received Ebill does not contain the filtered text");

        eBillsPage.clickDateRangeButton()
            .enterStartDate(transferDetails.getTransfer().getScheduledDate())
            .enterEndDate(transferDetails.getTransfer().getScheduledDate())
            .clickDateRangeSearchButton();
        Assert.assertTrue(eBillsPage.filteredEbillsContainText(transferDetails.getTransfer().getAmountStr()),
            "Received Ebill does not contain the filtered text");
    }
}
