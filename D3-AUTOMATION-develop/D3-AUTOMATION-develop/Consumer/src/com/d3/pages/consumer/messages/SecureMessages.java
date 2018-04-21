package com.d3.pages.consumer.messages;

import com.d3.datawrappers.messages.SecureMessage;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.messages.SecureMessagesL10N;
import com.d3.pages.consumer.messages.base.MessageSection;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class SecureMessages extends MessageSection<SecureMessages> {

    @FindBy(css = "button.new-message")
    private Element writeSecureMessageButton;

    @FindBy(css = "button.send")
    private Element sendButton;

    @FindBy(css = "button.cancel")
    private Element cancelButton;

    @FindBy(id = "topic")
    private Select topic;

    @FindBy(id = "issue")
    private Select issue;

    @FindBy(id = "subject")
    private Element subject;

    @FindBy(id = "body")
    private Element messageBody;

    @FindBy(css = "div.entity-summary div.subject")
    private List<Element> messageSubject;

    @FindBy(css = "li.entity.message.active div.message-body div.row.message-line div.row:last-of-type")
    private List<Element> replyTimestamps;

    @FindBy(css = "li.entity.message.read-message")
    private List<Element> messages;

    public SecureMessages(WebDriver driver) {
        super(driver);
    }

    @Override
    protected SecureMessages me() {
        return this;
    }

    public SecureMessages clickWriteSecureMessage() {
        writeSecureMessageButton.click();
        return this;
    }

    public SecureMessages selectTopic(String messageTopic) {
        topic.selectByValue(messageTopic);
        return this;
    }

    public SecureMessages selectIssue(String messageIssue) {
        issue.selectByValue(messageIssue);
        return this;
    }

    public SecureMessages enterSubject(String subjectLine) {
        subject.sendKeys(subjectLine);
        return this;
    }

    public SecureMessages enterMessage(String message) {
        messageBody.sendKeys(message);
        return this;
    }

    public SecureMessages sendMessage() {
        sendButton.click();
        return this;
    }

    public SecureMessages writeSecureMessage(SecureMessage message) {
        return clickWriteSecureMessage().
                selectTopic(message.getTopic()).
                selectIssue(message.getIssue()).
                enterSubject(message.getSubject()).
                enterMessage(message.getMessageBody());
    }

    public boolean isMessageCreated(SecureMessage message) {
        String errMsg = "%s: %s was not found on the Dom.";
        try {
            checkIfTextDisplayed(message.getSubject(), errMsg, "Message with subject");
        } catch (TextNotDisplayedException e) {
            log.warn("Message subject not found", e);
            return false;
        }
        return true;
    }

    public boolean requiredFieldMessagingDisplays() {
        String errorMsg = "Required field text for Message %s: %s was not found on the DOM.";
        try {
            checkIfTextDisplayed(SecureMessagesL10N.Localization.TOPIC_REQUIRED.getValue(), errorMsg, "Topic");
            checkIfTextDisplayed(SecureMessagesL10N.Localization.ISSUE_REQUIRED.getValue(), errorMsg, "Issue");
            checkIfTextDisplayed(SecureMessagesL10N.Localization.SUBJECT_REQUIRED.getValue(), errorMsg, "Subject");
            checkIfTextDisplayed(SecureMessagesL10N.Localization.BODY_REQUIRED.getValue(), errorMsg, "Body");
        } catch (TextNotDisplayedException e) {
            log.warn("Required field messaging was not displayed", e);
            return false;
        }

        return true;
    }

    public SecureMessages expandSecureMessageWithSubject(String subjectText) {
        getElementInListByTextContains(messageSubject, subjectText).click();
        return this;
    }


    public boolean repliesAreInChronologicalOrder() {
        ArrayList<String> sortedList = new ArrayList<>();
        ArrayList<String> unsortedList = new ArrayList<>();
        replyTimestamps.forEach(element -> {
            sortedList.add(element.getText());
            unsortedList.add(element.getText());
        });

        Collections.sort(sortedList, Collections.reverseOrder());
        return sortedList.equals(unsortedList);
     }


    /**
     * This method will expand each secure message element and check that it contains the search term used to filter by
     *
     * @param searchTerm Term secure messages have been filtered by
     * @return true if all secure message text contains search term, otherwise false
     */
    public boolean areCorrectMessagesDisplayed(String searchTerm) {
        return messages.stream().peek(Element::click).peek(element -> log.info("Found Secure Message With Text: \n{}", element.getText()))
                .allMatch(secureMessage -> secureMessage.getText().contains(searchTerm));
    }
}
