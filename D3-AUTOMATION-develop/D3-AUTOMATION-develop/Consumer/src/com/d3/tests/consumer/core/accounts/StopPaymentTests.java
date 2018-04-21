package com.d3.tests.consumer.core.accounts;

import com.d3.database.AttributeDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.account.enums.AccountProductAttributes;
import com.d3.datawrappers.account.stoppayment.D3StopPayment;
import com.d3.datawrappers.account.stoppayment.StopPaymentHistory;
import com.d3.datawrappers.account.stoppayment.StopPaymentRange;
import com.d3.datawrappers.account.stoppayment.StopPaymentReason;
import com.d3.datawrappers.account.stoppayment.StopPaymentSingle;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.accounts.AccountsL10N;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.pages.consumer.accounts.stoppayment.StopPaymentHistoryForm;
import com.d3.pages.consumer.accounts.stoppayment.StopPaymentRangeForm;
import com.d3.pages.consumer.accounts.stoppayment.StopPaymentSingleForm;
import com.d3.pages.consumer.accounts.stoppayment.base.StopPaymentForm;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Epic("Accounts")
@Feature("Stop Payment")
public class StopPaymentTests extends AccountsTestBase {


    @BeforeClass
    private void setProductAttributePermissions() {
        AttributeDatabaseHelper.updateAccountProductAttribute(ProductType.DEPOSIT_CHECKING, AccountProductAttributes.STOP_PAYMENT, true);
        AttributeDatabaseHelper.updateAccountProductAttribute(ProductType.DEPOSIT_SAVINGS, AccountProductAttributes.STOP_PAYMENT, true);
        AttributeDatabaseHelper.updateAccountProductAttribute(ProductType.CREDIT_CARD, AccountProductAttributes.STOP_PAYMENT, false);

    }

    /**
     * Logs in, goes to the account page, clicks on the account, clicks stop payment, selects the stop payment type, fills out the form, clicks save,
     * asserts the correct text is present, then completes the stop payment
     *
     * @param user User to login as
     * @param account Account to click
     * @param stopPaymentType Stop Payment Type to select from the drop down and fill out the form
     */
    private MyAccountsSection stopPaymentCommonSteps(D3User user, D3Account account, D3StopPayment stopPaymentType) {
        StopPaymentForm stopPaymentForm = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickStopPaymentButton()
            .selectStopPaymentType(stopPaymentType)
            .fillOutForm(stopPaymentType)
            .clickStopPaymentSaveButton();
        Assert.assertTrue(stopPaymentForm.isTextPresent(AccountsL10N.Localization.STOP_PAYMENT_CONFIRM.getValue()));
        return stopPaymentForm.clickStopPaymentConfirmButton();
    }

    @TmsLink("509945")
    @TmsLink("509947")
    @TmsLink("509957")
    @Story("Stop Payment - Valid Check Range")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifyStopPaymentOnValidCheckRange(D3User user, D3Account account) {
        StopPaymentRange stopPaymentRange = StopPaymentRange.createRandomStopPaymentRange(StopPaymentReason.getRandom());

        MyAccountsSection stopPayment = stopPaymentCommonSteps(user, account, stopPaymentRange);
        Assert.assertTrue(stopPayment.isTextDisplayed(
            String.format(AccountsL10N.Localization.STOP_PAYMENT_RANGE_SUCCESS.getValue(), stopPaymentRange.getStartCheckStr(),
                stopPaymentRange.getEndCheckStr(), stopPaymentRange.getTrackingNumber(), stopPaymentRange.getStatus())));
    }

    @TmsLink("509951")
    @Story("Stop Payment - Invalid Check Range")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifyStopPaymentOnInvalidCheckRange(D3User user, D3Account account) {
        StopPaymentRange stopPaymentRange = StopPaymentRange.createRandomInvalidStopPaymentRange(StopPaymentReason.getRandom());
        StopPaymentForm stopPayment = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickStopPaymentButton()
            .selectStopPaymentType(stopPaymentRange)
            .fillOutForm(stopPaymentRange)
            .clickStopPaymentSaveButton();

        Assert.assertTrue(stopPayment.isTextDisplayed(AccountsL10N.Localization.STOP_PAYMENT_RANGE_VALIDATE.getValue()));
    }

    @TmsLink("509962")
    @Story("Stop Payment - Check Range Already Posted To Account")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifyStopPaymentOnCheckRangeAlreadyPostedToAccount(D3User user, D3Account account) {

        StopPaymentRange stopPaymentRange = StopPaymentRange.createRandomStopPaymentRangeAlreadyPosted(StopPaymentReason.getRandom());

        MyAccountsSection stopPayment = stopPaymentCommonSteps(user, account, stopPaymentRange);
        Assert.assertTrue(stopPayment.isTextDisplayed(String.format(AccountsL10N.Localization.STOP_PAYMENT_RANGE_FAIL.getValue(),
            stopPaymentRange.getStartCheckStr(), stopPaymentRange.getEndCheckStr(), stopPaymentRange.getStatus())));
    }

    @TmsLink("509948")
    @Story("Stop Payment - Check Range Does Not Exist for Account")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifyStopPaymentOnCheckRangeThatDoesNotExist(D3User user, D3Account account) {
        StopPaymentRange stopPaymentRange = StopPaymentRange.createRandomNonExistentStopPaymentRange(StopPaymentReason.getRandom());

        MyAccountsSection stopPayment = stopPaymentCommonSteps(user, account, stopPaymentRange);
        Assert.assertTrue(stopPayment.isTextDisplayed(String.format(AccountsL10N.Localization.STOP_PAYMENT_RANGE_FAIL.getValue(),
            stopPaymentRange.getStartCheckStr(), stopPaymentRange.getEndCheckStr(), stopPaymentRange.getStatus())));
    }

    @TmsLink("509963")
    @TmsLink("509964")
    @Story("Stop Payment - Get Stop Payment History")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES, enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_HISTORY)
    public void verifyStopPaymentHistory(D3User user, D3Account account) {
        StopPaymentHistoryForm stopPaymentHistory = (StopPaymentHistoryForm) login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickStopPaymentButton()
            .selectStopPaymentType(new StopPaymentHistory());

        Assert.assertTrue(stopPaymentHistory.isCorrectInformationDisplayed());
    }

    @TmsLink("509958")
    @Story("Stop Payment - Single Check Request")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifyStopPaymentOnSingleCheck(D3User user, D3Account account) {
        StopPaymentSingle stopPaymentSingle = StopPaymentSingle.createRandomStopPayment(StopPaymentReason.getRandom());

        MyAccountsSection singleStopPayment = stopPaymentCommonSteps(user, account, stopPaymentSingle);
        Assert.assertTrue(singleStopPayment.isTextPresent(String.format(AccountsL10N.Localization.STOP_PAYMENT_SINGLE_SUCCESS.getValue(),
            stopPaymentSingle.getCheckNumberStr(), stopPaymentSingle.getPayee(), stopPaymentSingle.getStopPaymentAmountStr(),
            stopPaymentSingle.getTrackingNumber(), stopPaymentSingle.getStatus())));
    }

    @TmsLink("509955")
    @Story("Stop Payment - Reasons")
    @Test(dataProvider = "User with Stop Payment Reasons")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifyStopPaymentReasonsOnSingleCheck(D3User user, D3Account account, StopPaymentReason reason) {
        StopPaymentSingle stopPaymentSingle = StopPaymentSingle.createRandomStopPayment(reason);

        MyAccountsSection singleStopPayment = stopPaymentCommonSteps(user, account, stopPaymentSingle);
        Assert.assertTrue(singleStopPayment.isTextPresent(String.format(AccountsL10N.Localization.STOP_PAYMENT_SINGLE_SUCCESS.getValue(),
            stopPaymentSingle.getCheckNumberStr(), stopPaymentSingle.getPayee(), stopPaymentSingle.getStopPaymentAmountStr(),
            stopPaymentSingle.getTrackingNumber(), stopPaymentSingle.getStatus())));
    }

    @TmsLink("522448")
    @Story("Stop Payment - Account Product Permission Disabled")
    @Test(dataProvider = "Basic User")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    public void verifyStopPaymentNotAvailableWhenDisabledForAccountProduct(D3User user) {
        D3Account userAccount = user.getFirstAccountByType(ProductType.CREDIT_CARD);

        MyAccountsSection stopPayment = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(userAccount.getName());
        Assert.assertFalse(stopPayment.isStopPaymentButtonDisplayed());
    }

    @TmsLink("509961")
    @Story("Stop Payment - Single Check Required Fields")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifySingleStopPaymentRequiredFields(D3User user, D3Account account) {
        D3StopPayment type = StopPaymentSingle.createRandomStopPayment(StopPaymentReason.getRandom());

        StopPaymentSingleForm singleStopPayment = (StopPaymentSingleForm) login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickStopPaymentButton()
            .selectStopPaymentType(type).clickStopPaymentSaveButton();

        Assert.assertTrue(singleStopPayment.areRequiredFieldMessagesDisplayed());
    }

    @TmsLink("522420")
    @Story("Stop Payment - Check Range Required Fields")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    public void verifyStopPaymentRangeRequiredFields(D3User user, D3Account account) {

        StopPaymentRange stopPaymentRange = StopPaymentRange.createRandomStopPaymentRange(StopPaymentReason.getRandom());

        StopPaymentRangeForm stopPaymentForm = (StopPaymentRangeForm) login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickStopPaymentButton()
            .selectStopPaymentType(stopPaymentRange)
            .clickStopPaymentSaveButton();

        Assert.assertTrue(stopPaymentForm.areRequiredFieldMessagesDisplayed());
    }

    @TmsLink("509952")
    @Story("Stop Payment - Dropdown Type Options")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_HISTORY)
    public void verifyStopPaymentDropdownOptions(D3User user, D3Account account) {

        StopPaymentReason reason = StopPaymentReason.getRandom();
        D3StopPayment[] dropdownOptions = {StopPaymentSingle.createRandomStopPayment(reason),
            StopPaymentRange.createRandomStopPaymentRange(reason),
            new StopPaymentHistory()};

        MyAccountsSection stopPayment = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickStopPaymentButton();

        //loops through all of the dropdown options and verifies each stop payment type can be selected
        Stream.of(dropdownOptions).forEach(stopPayment::selectStopPaymentType);
    }

    @TmsLink("509956")
    @Story("Stop Payment - Disabled")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT, enabled = false)
    public void verifyStopPaymentNotAvailableWhenDisabled(D3User user, D3Account account) {
        MyAccountsSection stopPayment = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName());
        Assert.assertFalse(stopPayment.isStopPaymentButtonDisplayed());
    }

    @TmsLink("509946")
    @Story("Stop Payment - Range NOT Available When Disabled")
    @Test(dataProvider = "Basic User with existing Asset accounts")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_CHECK_RANGES, enabled = false)
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ACCOUNTS_STOP_PAYMENT_HISTORY)
    public void verifyStopPaymentRangeNotAvailableWhenDisabled(D3User user, D3Account account) {
        StopPaymentRange stopPaymentRange = StopPaymentRange.createRandomStopPaymentRange(StopPaymentReason.getRandom());

        MyAccountsSection stopPayment = login(user)
            .getHeader()
            .clickAccountsButton()
            .getMyAccountsSection()
            .clickAccountByAccountName(account.getName())
            .clickStopPaymentButton();

        Assert.assertFalse(stopPayment.isStopPaymentOptionAvailable(stopPaymentRange),
            String.format("Dropdown option: %s was available", stopPaymentRange.getType().toString()));
    }
}