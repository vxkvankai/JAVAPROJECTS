package com.d3.tests.consumer.core.messages;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.D3Alert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.messages.SecureMessage;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.RandomHelper;
import com.d3.l10n.common.CommonL10N;
import com.d3.l10n.messages.SecureMessagesL10N;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.messages.Notices;
import com.d3.pages.consumer.messages.SecureMessages;
import com.d3.tests.annotations.RunForSpecificAlerts;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Epic("Messages")
public class MessagesTests extends MessagesTestBase {

    @TmsLink("597980")
    @Feature("Secure Messages")
    @Story("Delete Secure Message")
    @Test(dataProvider = "Basic User With Secure Message")
    public void verifyDeleteSingleSecureMessage(D3User user, SecureMessage secureMessage) {
        SecureMessages secureMessages = login(user).getHeader().clickMessagesButton().getTabs()
            .clickSecureMessagesLink().deleteMessageWithSubject(secureMessage.getSubject());
        Assert.assertTrue(secureMessages.isTextDisplayed(SecureMessagesL10N.Localization.EMPTY.getValue()));
    }

    @TmsLink("288059")
    @Feature("Secure Messages")
    @Story("Write Secure Message")
    @Test(dataProvider = "Basic User")
    public void verifyWriteSecureMessage(D3User user) {
        SecureMessage secureMessage = SecureMessage.createRandomMessage(user);
        SecureMessages secureMessages = login(user).getHeader()
            .clickMessagesButton()
            .getTabs()
            .clickSecureMessagesLink()
            .writeSecureMessage(secureMessage).sendMessage();

        Assert.assertFalse(secureMessages.isTextDisplayed(SecureMessagesL10N.Localization.EMPTY.getValue()),
            "User has no secure messages. Message was not created");
        Assert.assertTrue(secureMessages.isMessageCreated(secureMessage));
    }

    @TmsLink("288058")
    @TmsLink("348383")
    @Feature("Notices")
    @Story("Delete Message")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class})
    @Test(dataProvider = "Basic User With Alert Triggered")
    public void deleteNoticeMessage(D3Alert alert) {
        D3User user = alert.getUser();
        Dashboard dashboard = login(user);
        Notices notices = dashboard.getHeader().clickMessagesButton();
        Assert.assertFalse(notices.deleteButtonIsEnabled());
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertMessageDetails(user)));
        notices.deleteMessageWithSubject(alert.getAlert().getMessageSubject());
        Assert.assertTrue(notices.userHasNoMessages());
    }

    @Flaky // TODO needs new dataprovider that give one user but multiple alerts
    @TmsLink("348384")
    @Feature("Notices")
    @Story("Delete Multiple Messages")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class, MerchantActivityAlert.class})
    @Test(dataProvider = "Basic User With Alert Triggered")
    public void validateDeleteMultipleMessages(D3Alert alert) {
        D3User user = alert.getUser();
        Notices notices = login(user).getHeader().clickMessagesButton();
        Assert.assertTrue(notices.getMessageCount() > 1, "User did not have more than 1 message available to delete");
        Assert.assertFalse(notices.deleteButtonIsEnabled(), "Delete Message button is enabled with no messages selected yet");

        notices.selectMultipleCheckboxes()
            .clickDeleteSelectedButton()
            .clickCancelButton()
            .selectMultipleCheckboxes()
            .clickDeleteSelectedButton()
            .clickDeleteMessageContinueButton();
        Assert.assertTrue(notices.userHasNoMessages(), "User still has messages displayed");
    }

    @TmsLink("348387")
    @Feature("Notices")
    @Story("Search by Message Subject")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class, MerchantActivityAlert.class})
    @Test(dataProvider = "Basic User With Alert Triggered")
    public void searchNoticeMessageBySubject(D3Alert alert) {
        Notices notices = login(alert.getUser()).getHeader()
            .clickMessagesButton()
            .searchForTerm(alert.getAlert().getMessageSubject())
            .searchButtonClick();
        Assert.assertEquals(notices.getMessageCount(), 1,
            String.format("Filtering by message subject: [%s] returned the incorrect number of results", alert.getAlert().getMessageSubject()));
        Assert.assertTrue(notices.isTextDisplayed(alert.getAlert().getMessageSubject()), "Alert message subject was not displayed");
    }

    @TmsLink("348382")
    @Feature("Notices")
    @Story("Filter Messages")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class, MerchantActivityAlert.class})
    // TODO: Add PaymentComingDueAlert
    @Test(dataProvider = "Basic User With Alert Triggered")
    public void validateFilteringMessagesByType(D3Alert alert) {
        D3User user = alert.getUser();
        Notices notices = login(user).getHeader()
            .clickMessagesButton()
            .clickMessageTypeDropdown()
            .filterMessagesByType(alert.getMessageFilter().name());
        Assert.assertEquals(notices.getCurrentFilter(), alert.getMessageFilter().getFilterText());
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertMessageDetails(user)));
    }

    @TmsLink("348390")
    @Feature("Secure Messages")
    @Story("Delete Multiple Messages")
    @Test(dataProvider = "Basic User With Multiple Secure Messages")
    public void verifyDeletionOfMultipleSecureMessage(D3User user, List<SecureMessage> secureMessages) {
        SecureMessages secureMessagePage = login(user).getHeader()
            .clickMessagesButton()
            .getTabs()
            .clickSecureMessagesLink();
        Assert.assertFalse(secureMessagePage.deleteButtonIsEnabled());

        secureMessagePage.deleteAllMessages();
        Assert.assertTrue(secureMessagePage.isTextDisplayed(SecureMessagesL10N.Localization.EMPTY.getValue()));
        Assert.assertNotNull(DatabaseUtils.getAuditRecordId(user, Audits.SECURE_MESSAGE_DELETE));
    }

    @TmsLink("348389")
    @Feature("Secure Messages")
    @Story("Delete Message")
    @Test(dataProvider = "Basic User With Secure Message")
    public void deleteSingleSecureMessage(D3User user, SecureMessage secureMessage) {
        SecureMessages secureMessages = login(user).getHeader()
            .clickMessagesButton()
            .getTabs()
            .clickSecureMessagesLink();
        Assert.assertFalse(secureMessages.deleteButtonIsEnabled());

        secureMessages.deleteMessageWithSubject(secureMessage.getSubject());
        Assert.assertTrue(secureMessages.isTextDisplayed(SecureMessagesL10N.Localization.EMPTY.getValue()));
    }

    @TmsLink("348386")
    @Feature("Notices")
    @Story("Search by Message Content")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class, MerchantActivityAlert.class})
    // TODO: Add PaymentComingDueAlert
    @Test(dataProvider = "Basic User With Alert Triggered")
    public void validateNoticesSearchByMessageContent(D3Alert alert) {
        D3User user = alert.getUser();
        String messageContent = alert.getTriggeredAlertMessageDetails(user);
        Notices notices = login(user).getHeader().clickMessagesButton()
            .searchForTerm(StringUtils.substring(messageContent, 5, 15)) //search substring of full message
            .searchButtonClick();
        Assert.assertEquals(notices.getMessageCount(), 1);
        Assert.assertTrue(notices.isTextDisplayed(messageContent));
    }

    @TmsLink("523127")
    @Feature("Secure Messages")
    @Story("Write Secure Message")
    @Test(dataProvider = "Basic User")
    public void writeSecureMessageWithFieldValidation(D3User user) {
        String charactersNotAllowed = "`<>^|\"";
        SecureMessage message = SecureMessage.createRandomMessage(user);
        SecureMessages secureMessages = login(user).getHeader()
            .clickMessagesButton()
            .getTabs()
            .clickSecureMessagesLink()
            .clickWriteSecureMessage()
            .sendMessage();
        Assert.assertTrue(secureMessages.requiredFieldMessagingDisplays());

        secureMessages.selectTopic(message.getTopic())
            .selectIssue(message.getIssue());

        for (char badChar : charactersNotAllowed.toCharArray()) {
            secureMessages.enterSubject(String.valueOf(badChar))
                .sendMessage();
            Assert.assertTrue(secureMessages.isTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue()));
        }
        secureMessages.enterSubject(message.getSubject());

        for (char badChar : charactersNotAllowed.toCharArray()) {
            secureMessages.enterMessage(String.valueOf(badChar))
                .sendMessage();
            Assert.assertTrue(secureMessages.isTextDisplayed(CommonL10N.Localization.CHARACTERS_NOT_ALLOWED.getValue()));
        }

        secureMessages.enterMessage(message.getMessageBody()).sendMessage();

        Assert.assertFalse(secureMessages.isTextDisplayed(SecureMessagesL10N.Localization.EMPTY.getValue()),
            "User has no secure messages. Message was not created");
        Assert.assertTrue(secureMessages.isMessageCreated(message));
    }

    @TmsLink("523128")
    @Feature("Secure Messages")
    @Story("Search Message by Different Fields")
    @Test(dataProvider = "Basic User With Multiple Secure Messages")
    public void verifySearchOfSecureMessage(D3User user, List<SecureMessage> secureMessages) {
        SecureMessage messageToSearch = secureMessages.get(ThreadLocalRandom.current().nextInt(secureMessages.size()));
        List<String> searchTerms =
            new ArrayList<>(Arrays.asList(messageToSearch.getTopic(), messageToSearch.getIssue(), messageToSearch.getSubject(),
                messageToSearch.getMessageBody()));
        String date = DateTimeFormat.forPattern("MM/dd/yyy").print(new DateTime());

        SecureMessages secureMessagePage = login(user).getHeader()
            .clickMessagesButton()
            .getTabs()
            .clickSecureMessagesLink()
            .clickOnDateRange()
            .enterStartDate(date)
            .enterEndDate(date)
            .clickOnSearchDateButton();
        Assert.assertTrue(secureMessagePage.isTextDisplayed(messageToSearch.getTopic()));
        secureMessagePage.clearDateRange();

        //Loop through searchTerms and verify correct messages are filtered
        searchTerms.forEach(term -> {
            secureMessagePage.enterSearchCriteria(term)
                .clickOnSearchButton();
            Assert.assertTrue(secureMessagePage.areCorrectMessagesDisplayed(term), "Secure Message message was not what was expected (see logs)");
        });
    }

    @TmsLink("308950")
    @Feature("Secure Messages")
    @Story("Replies sorted chronologically")
    @Test(dataProvider = "Basic User With Secure Message Replies")
    public void verifySecureMessageRepliesAreSortedChronologically(D3User user, SecureMessage secureMessage) {
        SecureMessages secureMessagePage = login(user).getHeader()
            .clickMessagesButton()
            .getTabs()
            .clickSecureMessagesLink()
            .expandSecureMessageWithSubject(secureMessage.getSubject());

        Assert.assertTrue(secureMessagePage.repliesAreInChronologicalOrder(), "Secure message page replies were not in Chronological Order");
    }

    @TmsLink("348385")
    @Feature("Notices")
    @Story("Filter by Date Range")
    @RunForSpecificAlerts(d3AlertType = {CheckClearedAlert.class, MerchantActivityAlert.class})
    @Test(dataProvider = "Basic User With Alert Triggered")
    public void validateSearchingNoticeMessagesByDateRange(D3Alert alert) {
        D3User user = alert.getUser();
        DateTimeFormatter df = DateTimeFormat.forPattern("MM/dd/yyy");

        //Verify no results when start date is a future date
        Notices notices = login(user).getHeader().clickMessagesButton()
            .clickDateRangeButton()
            .enterStartDate(df.print(RandomHelper.getRandomFutureDate()))
            .clickDateRangeSearchButton();
        Assert.assertTrue(notices.userHasNoMessages(), "User has messages when expecting none");

        //Verify no results when end date is a past date
        notices.clearSearch()
            .enterEndDate(df.print(RandomHelper.getRandomPastDate()))
            .clickDateRangeSearchButton();
        Assert.assertTrue(notices.userHasNoMessages(), "User has messages when expecting none");

        //Verify start date gets updated to end date if end date is before the entered start date
        notices.enterStartDate(df.print(RandomHelper.getRandomFutureDate()))
            .enterEndDate(df.print(DateTime.now()))
            .clickDateRangeSearchButton();
        // TODO: onchange issue here
        Assert.assertEquals(notices.getStartDateValue(), notices.getEndDateValue(), "Start date value does not equal end date value");

        notices.clearSearch();
        Assert.assertTrue(notices.isTextDisplayed(alert.getTriggeredAlertMessageDetails(user)), "Triggered Alert Message Details text was not "
            + "shown");

        notices.clickClearDatesButton();
        Assert.assertFalse(notices.areDateFieldsDisplayed(), "Date Fields are not empty as expected");
    }
}
