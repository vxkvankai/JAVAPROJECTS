package com.d3.tests.consumer.core.moneymovement;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.l10n.common.CommonL10N;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.messages.Notices;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.recipients.add.AddRecipient;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditRecipients;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import com.d3.tests.annotations.RunWithCompanyAttribute;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.openqa.selenium.InvalidElementStateException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;


@Epic("Money Movement")
@Feature("Recipients")
public class RecipientTests extends MoneyMovementTestBase {

    @TmsLink("288016")
    @TmsLink("522807")
    @Story("Delete Recipient")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ON_US_TRANSFER_ENABLED)
    @Test(dataProvider = "User with bill pay and already added recipients and no seeded")
    public void verifyConsumerDeletingRecipient(D3User user, Recipient recipient) {
        RecipientsPage recipientsPage = login(user).getHeader().clickMoneyMovementButton().getTabs().clickRecipientsLink()
            .clickRecipient(recipient)
            .clickDeleteRecipientButton();
        Assert.assertTrue(recipientsPage.isTextDisplayed(recipient.getDeleteText()));

        recipientsPage.clickConfirmDeleteRecipientButton();
        Assert.assertFalse(recipientsPage.isTextPresent(recipient.getName()), "The recipient was not deleted");

        Notices notices = recipientsPage.getHeader().clickMessagesButton();
        Assert.assertTrue(
            notices.isTextDisplayed(String.format("A %s recipient named %s was deleted on", recipient.getWho().value(), recipient.getName())),
            "The alert message is not displayed properly ");

        Map<String, String> events = DatabaseUtils.getAuditRecordAttributes(user, Audits.RECIPIENT_DELETED);

        Assert.assertFalse(events == null || events.isEmpty(), "The audit event was not created");
        Assert.assertTrue(events.get(AuditAttribute.RECIPIENT_NAME.getAttributeName()).equals(recipient.getName()),
            "The recipient name in the audit event is not correct");
    }

    @TmsLink("288009")
    @Story("Recipient Form Validation")
    @Test(dataProvider = "Bill Pay User with Bad new Recipients and no seeded")
    public void verifyAddingCheckFreeRecipientPersonSpecialChars(D3User user, List<Recipient> recipients) {
        RecipientsPage recipientPage = login(user).getHeader().clickMoneyMovementButton().getTabs().clickRecipientsLink();
        AddRecipient addRecipientPage = recipientPage.clickAddRecipientButton();
        AddRecipientForm form = addRecipientPage.createNewRecipientNoSave(recipients.get(0));
        Assert.assertTrue(form.isTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue()));
        for (Recipient recipient : recipients.subList(1, recipients.size())) {
            form.enterRecipientInfo(recipient).clickSaveButton();
            Assert.assertTrue(form.isTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue()));
        }


    }

    @Issue("DPD-1906") // TODO remove try block after fixed
    @TmsLink("522805")
    @Story("Edit Recipient")
    @Test(dataProvider = "User with bill pay and already added recipients and no seeded")
    public void verifyEditingRecipient(D3User user, Recipient existingRecipient) {
        Recipient newRecipientDetails = existingRecipient.getNewRandomVersion();

        RecipientsPage recipientsPage = login(user).getHeader()
            .clickMoneyMovementButton()
            .getTabs()
            .clickRecipientsLink();
        EditRecipients editPage =
            recipientsPage.clickRecipient(existingRecipient).clickEditRecipientButton()
                .enterNickname(newRecipientDetails.getNickname());

        Assert.assertFalse(editPage.isNameFieldEditable(), "The name field is editable");

        if (existingRecipient.isEditable()) {
            try { // TODO remove this try block after test is fixed
                EditRecipientsForm form = editPage.clickEditButton(existingRecipient, false);
                //noinspection unchecked
                recipientsPage = form.fillOutForm(newRecipientDetails)
                    .clickSaveButton()
                    .clickSaveButton();
            } catch (InvalidElementStateException e) {
                Assert.fail("Error filling out form", e);
            }
        } else {
            recipientsPage = editPage.clickSaveButton();
        }
        editPage = recipientsPage.clickEditRecipientButton();
        Assert.assertTrue(editPage.isRecipientInformationCorrect(newRecipientDetails, true),
            "Recipient information was not as expected");

        existingRecipient.getEditRecipientsForm(driver).clickCancelButton(existingRecipient.isEditable());

        Notices notices = editPage.getHeader().clickMessagesButton();
        Assert.assertTrue(notices.isTextDisplayed(String.format("A %s recipient named %s was updated on", newRecipientDetails.getWho().value(),
            existingRecipient.getName())), "The alert message is not displayed properly");

        Map<String, String> audit = DatabaseUtils.getAuditRecordAttributes(user, Audits.RECIPIENT_UPDATED);
        Assert.assertFalse(audit.isEmpty(), "The audit event was not created");
    }


    @Issue("DPD-2156")
    @TmsLink("522800")
    @Story("Add Recipient")
    @RunWithCompanyAttribute(attribute = CompanyAttribute.ON_US_TRANSFER_ENABLED)
    @Test(dataProvider = "User with bill pay and recipients")
    public void verifyAddRecipient(D3User user, Recipient newRecipient) {

        AddRecipientForm form = login(user).getHeader()
            .clickMoneyMovementButton()
            .getTabs()
            .clickRecipientsLink()
            .clickAddRecipientButton()
            .createNewRecipientNoSave(newRecipient);

        Assert.assertTrue(form.isOptionTextDisplayed(newRecipient));

        RecipientsPage recipientsPage = form.clickSaveButton();

        Assert.assertTrue(recipientsPage.isRecipientCreated(newRecipient));
        EditRecipients editRecipientsPage = recipientsPage.clickEditRecipientButton();

        Assert.assertTrue(editRecipientsPage.isAddRecipientInformationCorrect(newRecipient),
            String.format("Recipient (type: %s, who: %s) information was not as expected",
                newRecipient.getType(),
                newRecipient.getWho()));

        Notices notices = editRecipientsPage.getRecipientForm(newRecipient)
            .clickCancelButton(newRecipient.isEditable())
            .getHeader()
            .clickMessagesButton();

        Assert.assertTrue(
            notices.isTextDisplayed(String.format("A %s recipient named %s was added on", newRecipient.getWho().value(), newRecipient.getName())),
            "The alert message is not displayed properly ");

        Assert.assertTrue(newRecipient.isAddingAuditRecordCreated(user), "The audit event was not created or was not validated");
    }

    @TmsLink("288010")
    @Story("Add CheckFree Recipient")
    @Test(dataProvider = "Basic User With Bill Pay")
    public void verifyCheckFreeAddingRecipientCompany(D3User user) {

        CompanyBillPaySeedRecipient recipient = CompanyBillPaySeedRecipient.createRandomExistingRecipient("Best Buy");

        RecipientsPage recipientsPage = login(user).getHeader()
            .clickMoneyMovementButton()
            .getTabs()
            .clickRecipientsLink()
            .clickAddRecipientButton()
            .createNewRecipient(recipient);

        Assert.assertTrue(recipientsPage.isTextDisplayed(recipient.getName()),
            "New Recipient name is not listed in the recipient list");
        Assert.assertTrue(recipientsPage.isTextDisplayed(recipient.getNickname()),
            "New Recipient nickname is not present in the recipient list");
        EditRecipients editRecipientsPage = recipientsPage.clickEditRecipientButton();
        Assert.assertTrue(editRecipientsPage.isRecipientInformationCorrect(recipient), "Recipient information was not as expected");
    }
}
