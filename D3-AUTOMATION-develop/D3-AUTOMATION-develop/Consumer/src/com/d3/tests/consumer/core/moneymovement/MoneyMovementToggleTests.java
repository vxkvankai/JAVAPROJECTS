package com.d3.tests.consumer.core.moneymovement;

import com.d3.database.AccountDatabaseHelper;
import com.d3.database.RecipientMMDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.recipient.base.RecipientWho;
import com.d3.datawrappers.transfers.BillPayRecurringTransfer;
import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.datawrappers.transfers.StandardRecurringTransfer;
import com.d3.datawrappers.user.D3User;
import com.d3.datawrappers.user.enums.UserType;
import com.d3.l10n.moneymovement.BillPayEnrollmentL10N;
import com.d3.l10n.moneymovement.ScheduleL10N;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.moneymovement.BillPayPage;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.Schedule;
import com.d3.pages.consumer.moneymovement.schedule.forms.add.SingleTransferForm;
import com.d3.tests.annotations.RunForUserTypes;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


@Epic("Money Movement")
public class MoneyMovementToggleTests extends MoneyMovementTestBase {

    private static final String TRANSFER_NOT_VALIDATED_ERROR = "Transfer was not validated, see logs";

    @Feature("Bill Pay")
    @Story("Bill Pay Enrollment - Funding Accounts Available")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyBillPayEnrollmentFundingAccounts(D3User user) {
        Dashboard dashboard = login(user);
        BillPayPage billpay = dashboard.getHeader().clickMoneyMovementButton()
            .getTabs().clickBillPayEnrollmentLink().clickEnrollNow();
        Assert.assertTrue(billpay.areFundingAccountsCorrect(AccountDatabaseHelper.getMaskedUserAccounts(user)));
    }

    @Feature("Bill Pay")
    @Story("Bill Pay Enrollment")
    @Issue("REL31-1423")
    @Test(dataProvider = "Get All User Types")
    public void verifyBillPayEnrollmentForToggleUsers(D3User user) {
        D3Account acct = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_CHECKING) : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);

        Dashboard dashboard = login(user);
        BillPayPage billpay = dashboard.getHeader().clickMoneyMovementButton()
            .getTabs().clickBillPayEnrollmentLink();
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.START.getValue()));

        billpay.clickEnrollNow().selectFundingAccount(acct.getName()).clickEnrollSaveButton();

        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_SUCCESS_TITLE.getValue()));
        Assert.assertTrue(billpay.isTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_SUCCESS_TEXT.getValue()));
    }

    @Feature("Schedule")
    @Story("Schedule - Internal User Accounts Available")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Get Specific User Types")
    public void verifyMoneyMovementAvailableInternalAccounts(D3User user) {
        D3Account accountToSelect = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_CHECKING) : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);

        SingleTransferForm form = login(user).getHeader().clickMoneyMovementButton()
            .clickScheduleButton();

        Assert.assertTrue(form.areDestinationAccountsCorrect(AccountDatabaseHelper.getMaskedUserAccounts(user)));
        form.selectToAccount(accountToSelect);
        Assert.assertTrue(form.areSourceAccountsCorrect(AccountDatabaseHelper.getMaskedUserAccounts(user)));
    }

    @Feature("Recurring Transfer")
    @Story("Recurring Transfer Between Internal Accounts")
    @Test(dataProvider = "Get All User Types")
    public void verifyRecurringTransferInternalAccounts(D3User user) {
        D3Account toAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_CHECKING) : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        StandardRecurringTransfer transfer = StandardRecurringTransfer.createRandomTransferDetails(toAccount, fromAccount);
        SingleTransferForm form = login(user).getHeader().clickMoneyMovementButton().clickScheduleButton();
        form.fillOutRecurringForm(transfer).clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(
            String.format(ScheduleL10N.Localization.RECURRING_MODAL_TEXT.getValue(), transfer.getAmountStr(),
                transfer.getToAccount().getName(), transfer.getFromAccount().getName(), transfer.getFrequency().getDropdownValue())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton();
        Assert.assertTrue(schedulePage.isRecurringTransferCorrect(transfer), TRANSFER_NOT_VALIDATED_ERROR);
    }

    @Flaky
    @Feature("Single Transfer")
    @Story("Single Transfer Between Internal Accounts")
    @Test(dataProvider = "Get All User Types")
    public void verifySingleTransferInternalAccountsToggle(D3User user) {

        D3Account toAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_CHECKING) : user.getFirstAccountByType(ProductType.DEPOSIT_CHECKING);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        SingleTransfer transfer = SingleTransfer.createRandomTransfer(toAccount, fromAccount);
        SingleTransferForm form = login(user).getHeader().clickMoneyMovementButton().clickScheduleButton();
        form.fillOutForm(transfer).clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), transfer.getAmountStr(),
            transfer.getToAccount().getName(), transfer.getFromAccount().getName(), transfer.getScheduledDateString())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton();
        Assert.assertTrue(schedulePage.isSingleTransferCorrect(transfer), TRANSFER_NOT_VALIDATED_ERROR);
    }

    @Feature("Recipients")
    @Story("Correct Recipients Displayed")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyRecipientsPageRecipients(D3User user) {

        Dashboard dashboard = login(user);
        RecipientsPage recipients = dashboard.getHeader().clickMoneyMovementButton().getTabs().clickRecipientsLink();
        Assert.assertTrue(recipients.areCorrectRecipientsDisplayed(user));
        dashboard.getHeader().changeToggleMode(user);
        Assert.assertTrue(recipients.isTextPresent("You have not created any recipients"));

    }

    @Feature("Schedule")
    @Story("Correct Recipients Available")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_TOGGLE, UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyMoneyMovementAvailableRecipients(D3User user) {
        List<String> userRecipients = RecipientMMDatabaseHelper.getDefaultUserRecipients(user);
        user.getRecipients().forEach(recipient -> userRecipients.add(recipient.getTransferableName()));

        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();
        Assert.assertTrue(form.areDestinationCompanyRecipientsCorrect(userRecipients));
        Assert.assertTrue(form.areDestinationPersonRecipientsCorrect(userRecipients));
    }

    @Feature("Single Payment")
    @Story("Single Payment to Company Recipient")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
        UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifySinglePaymentToCompanyRecipient(D3User user) {

        Recipient toAccount = user.getFirstRecipientByType(RecipientWho.COMPANY);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        SingleTransfer transfer = SingleTransfer.createRandomTransfer(toAccount, fromAccount);
        SingleTransferForm form = login(user).getHeader().clickMoneyMovementButton().clickScheduleButton();
        form.fillOutForm(transfer).clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), transfer.getAmountStr(),
            transfer.getToAccount().getTransferableName(), transfer.getFromAccount().getName(), transfer.getScheduledDateString())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton();
        Assert.assertTrue(schedulePage.isSingleTransferCorrect(transfer), TRANSFER_NOT_VALIDATED_ERROR);
    }

    @Feature("Recurring Payment")
    @Story("Recurring Payment to Company Recipient")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
        UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyRecurringPaymentToCompanyRecipient(D3User user) {

        Recipient toAccount = user.getFirstRecipientByType(RecipientWho.COMPANY);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        BillPayRecurringTransfer transfer = BillPayRecurringTransfer.createRandomTransferDetails(toAccount, fromAccount);
        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();

        form.fillOutRecurringForm(transfer)
            .clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(
            String.format(ScheduleL10N.Localization.RECURRING_MODAL_TEXT.getValue(), transfer.getAmountStr(),
                transfer.getToAccount().getTransferableName(), transfer.getFromAccount().getName(),
                transfer.getFrequency().getDropdownValue())));

        Schedule schedulePage = form.clickContinueButton()
            .clickOkButton();
        Assert.assertTrue(schedulePage.isRecurringTransferCorrect(transfer), TRANSFER_NOT_VALIDATED_ERROR);
    }

    // TODO: UserType.PRIMARY_BUSINESS_USER is flaky
    @Feature("Single Payment")
    @Story("Single Payment to Person Recipient")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER,
        //UserType.PRIMARY_BUSINESS_USER,
        UserType.PRIMARY_CONSUMER_TOGGLE,
        UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifySinglePaymentToPersonRecipient(D3User user) {
        // TODO: have this test take in a Transfer type as well as user

        Recipient toAccount = user.getFirstRecipientByType(RecipientWho.PERSON);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        SingleTransfer transfer = SingleTransfer.createRandomTransfer(toAccount, fromAccount);
        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();

        form.fillOutForm(transfer)
            .clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(String.format(ScheduleL10N.Localization.SINGLE_TRANSFER_MODAL_TEXT.getValue(), transfer.getAmountStr(),
            transfer.getToAccount().getTransferableName(), transfer.getFromAccount().getName(), transfer.getScheduledDateString())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton();
        Assert.assertTrue(schedulePage.isSingleTransferCorrect(transfer), TRANSFER_NOT_VALIDATED_ERROR);
    }

    @Flaky // Note (Jmoravec): Failing randomly on the disclosure ok button, doesn't seem to be clicking 100% of the time in Jenkins
    @Feature("Recurring Payment")
    @Story("Recurring Payment to Person Recipient")
    @RunForUserTypes(userTypes = {UserType.PRIMARY_CONSUMER_USER, UserType.PRIMARY_BUSINESS_USER, UserType.PRIMARY_CONSUMER_TOGGLE,
        UserType.PRIMARY_BUSINESS_TOGGLE, UserType.COMMINGLED})
    @Test(dataProvider = "Specific User with Bill Pay And Recipients")
    public void verifyRecurringPaymentToPersonRecipient(D3User user) {

        Recipient toAccount = user.getFirstRecipientByType(RecipientWho.PERSON);
        D3Account fromAccount = user.getUserType().isBusinessType() ? user.getFirstAccountByType(
            ProductType.BUSINESS_DEPOSIT_SAVINGS) : user.getFirstAccountByType(ProductType.DEPOSIT_SAVINGS);

        BillPayRecurringTransfer transfer = BillPayRecurringTransfer.createRandomTransferDetails(toAccount, fromAccount);

        SingleTransferForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .clickScheduleButton();

        form.fillOutRecurringForm(transfer)
            .clickSubmitButton();

        Assert.assertTrue(form.isTextDisplayed(
            String.format(ScheduleL10N.Localization.RECURRING_MODAL_TEXT.getValue(), transfer.getAmountStr(),
                transfer.getToAccount().getTransferableName(), transfer.getFromAccount().getName(),
                transfer.getFrequency().getDropdownValue())));

        Schedule schedulePage = form.clickContinueButton().clickOkButton();
        Assert.assertTrue(schedulePage.isRecurringTransferCorrect(transfer), TRANSFER_NOT_VALIDATED_ERROR);
    }
}
