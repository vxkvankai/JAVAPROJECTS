package com.d3.tests.consumer.core.moneymovement;

import com.d3.database.AuditDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.moneymovement.BillPayEnrollmentL10N;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.moneymovement.BillPayPage;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.recipients.add.AddRecipient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Money Movement")
@Feature("Bill Pay")
@Slf4j
public class BillPayTests extends MoneyMovementTestBase {

    /**
     * Logs in, goes to the money movement page, clicks on the Bill Pay Enrollment link, clicks the enroll now button, and selects a funding account
     *
     * @param user User to login as
     * @param account Account to click
     */
    private BillPayPage billPayEnrollmentCommonSteps(D3User user, D3Account account) {
        BillPayPage billpay = login(user).getHeader().clickMoneyMovementButton()
            .getTabs().clickBillPayEnrollmentLink();
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.START.getValue()));

        return billpay.clickEnrollNow().selectFundingAccount(account.getName());
    }

    //TODO needs TC number
    @Story("Bill Pay Enrollment")
    @Test(dataProvider = "Basic User")
    public void verifyBillPayEnrollmentCancel(D3User user) {
        D3Account acct = user.getUserAccounts().get(0).getValue0();

        // This recipient is just to test to make sure that the add company bill pay recipient button doesn't show up
        Recipient testRecipient = CompanyBillPaySeedRecipient.createRandomExistingRecipient("Best Buy");

        BillPayPage billpay = billPayEnrollmentCommonSteps(user, acct).clickCancelButton();
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.START.getValue()));
        RecipientsPage recipientsPage = billpay.getTabs().clickRecipientsLink();
        AddRecipient addRecipientPage = recipientsPage.clickAddRecipientButton().clickCompanyToPayButton();

        log.info("Verifying the Add Company Recipient button does not show: {}", user);
        Assert.assertThrows(NoSuchElementException.class, () -> addRecipientPage.clickBillAddressInfoButton(testRecipient));
    }

    @TmsLink("288007")
    @Story("Bill Pay Enrollment")
    @Test(dataProvider = "Basic User")
    public void verifyBillPayEnrollment(D3User user) {
        D3Account acct = user.getUserAccounts().get(0).getValue0();
        BillPayPage billpay = billPayEnrollmentCommonSteps(user, acct)
            .clickEnrollSaveButton();

        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_SUCCESS_TITLE.getValue()));
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_SUCCESS_TEXT.getValue()));
    }

    @TmsLink("307975")
    @Story("Bill Pay Enroll - Invalid Address")
    @Test(dataProvider = "Basic User with Invalid Address")
    public void verifyBillPayEnrollmentFailureWithInvalidAddress(D3User user) {
        D3Account acct = user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        BillPayPage billpay = billPayEnrollmentCommonSteps(user, acct)
            .clickEnrollSaveButton();
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_ERROR.getValue()));
    }

    @TmsLink("307974")
    @Story("Bill Pay Enroll - User with no address")
    @Test(dataProvider = "Basic User with No Address")
    public void verifyBillPayEnrollmentNotAvailableForUserWithNoAddress(D3User user) {
        verifyEnrollmentNotAvailable(user);
    }

    @TmsLink("307976")
    @Story("Bill Pay Enroll - User with without US address")
    @Test(dataProvider = "Basic User without US Address")
    public void verifyBillPayEnrollmentNotAvailableForUserWithoutUSAddress(D3User user) {
        verifyEnrollmentNotAvailable(user);
    }

    private void verifyEnrollmentNotAvailable(D3User user) {
        BillPayPage billpay = login(user)
            .getHeader()
            .clickMoneyMovementButton()
            .getTabs()
            .clickBillPayEnrollmentLink()
            .clickEnrollNow();
        Assert.assertTrue(billpay.isBillPayMoreInfoNeededMessageDisplayed());
    }


    @TmsLink("307959")
    @Story("Bill Pay Enrollment")
    @Test(dataProvider = "Basic User")
    public void verifyRequiredFieldsAndAuditRecord(D3User user) {
        BillPayPage billpay = login(user).getHeader().clickMoneyMovementButton()
            .getTabs().clickBillPayEnrollmentLink();
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.START.getValue()));

        billpay.clickEnrollNow().clickEnrollSaveButton();
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_VALIDATE_ACCOUNT.getValue()));
        Assert.assertTrue(billpay.isCorrectFormInformationDisplayed(user));

        billpay.selectFundingAccount(user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING).getName()).clickEnrollSaveButton();
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_SUCCESS_TITLE.getValue()));
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_SUCCESS_TEXT.getValue()));

        //verify audit record gets created for Bill Pay Enrollment
        String billPayEnrollmentId = AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.MM_USER_ENROLL)
            .getOrDefault(AuditAttribute.BILL_PAY_ENROLLMENT_ID.getAttributeName(), "");

        Assert.assertNotNull(billPayEnrollmentId);
        Assert.assertFalse(billPayEnrollmentId.isEmpty());
    }
}
