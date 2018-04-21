package com.d3.tests.consumer.core.moneymovement;


import com.d3.database.UserDatabaseHelper;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.L10nCommon;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.recipients.OOBRecipientPage;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.tests.annotations.UseCompany;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Money Movement")
@Feature("Out Of Band")
@Slf4j
public class OutOfBandRecipientsTests extends MoneyMovementTestBase {

    @TmsLink("288054")
    @TmsLink("288055")
    @Story("OOB Adding Recipient")
    @UseCompany(companyId = "fi3")
    @Test(dataProvider = "User with bill pay and recipients")
    public void verifyOOBCodeRequiredForAddingRecipient(D3User user, Recipient recipient) {

        if (recipient instanceof CompanyBillPaySeedRecipient) {
            log.warn("This Test (Company Bill Pay Seed Recipient) will not work as expected due to REL31-225");
            return;
        }

        RecipientsPage recipientsPage = login(user).getHeader().clickMoneyMovementButton().getTabs().clickRecipientsLink();
        recipientsPage.clickAddRecipientButton().getToFormPage(recipient);
        OOBRecipientPage oobPage = OOBRecipientPage.getOOBPage(driver);
        Assert.assertTrue(oobPage.isTextDisplayed(L10nCommon.Localization.OOB_VERIFICATION_SENT.getValue()));

        String verificationCode = UserDatabaseHelper.getOOBVerificationCode(user);
        AddRecipientForm addRecipientForm = oobPage.enterVerificationCode(verificationCode).clickContinueButton(recipient);
        recipientsPage = addRecipientForm.enterRecipientInfo(recipient).clickSaveButton();

        Assert.assertTrue(recipientsPage.isTextDisplayed(recipient.getName()));
        // just validate that the buttons are there
        Assert.assertTrue(recipientsPage.isTextDisplayed("Schedule"));
        Assert.assertTrue(recipientsPage.isTextDisplayed("Delete"));
        Assert.assertTrue(recipientsPage.isTextDisplayed("Edit"));
    }
}
