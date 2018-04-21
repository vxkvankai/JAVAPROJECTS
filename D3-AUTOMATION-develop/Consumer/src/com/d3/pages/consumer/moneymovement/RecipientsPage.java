package com.d3.pages.consumer.moneymovement;

import static com.d3.helpers.AccountHelper.verifyRecipients;

import com.d3.database.DatabaseUtils;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.moneymovement.recipients.add.AddRecipient;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditRecipients;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class RecipientsPage extends MoneyMovementBasePage {

    @FindBy(css = "button.add-recipients-button")
    private Element addRecipientsButton;

    @FindBy(css = "button.edit-recipient-btn")
    private Element editRecipientsButton;

    @FindBy(css = "button.delete-recipient-btn")
    private Element deleteRecipientsButton;

    @FindBy(css = "button.submit-one")
    private Element confirmDeleteRecipientButton;

    @FindBy(css = "li.entity")
    private List<Element> availableRecipients;

    private Element recipientDetail(String recipientName) {
        By by = By.xpath(String.format("//div[contains(text(), '%s')]", recipientName));
        return new D3Element(driver.findElement(by));
    }

    public RecipientsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected RecipientsPage me() {
        return this;
    }

    public AddRecipient clickAddRecipientButton() {
        addRecipientsButton.click();
        waitForSpinner();
        return AddRecipient.initialize(driver, AddRecipient.class);
    }

    public EditRecipients clickEditRecipientButton() {
        editRecipientsButton.click();
        waitForSpinner();
        return EditRecipients.initialize(driver, EditRecipients.class);
    }

    public RecipientsPage clickDeleteRecipientButton() {
        deleteRecipientsButton.click();
        return this;
    }

    public RecipientsPage clickConfirmDeleteRecipientButton() {
        confirmDeleteRecipientButton.click();
        waitForSpinner();
        return this;
    }

    public RecipientsPage clickRecipient(Recipient recipient) {
        recipientDetail(recipient.getTransferableName()).click();
        waitForSpinner();
        return this;
    }

    public boolean areCorrectRecipientsDisplayed(D3User user) {
        List<String> userRecipients = DatabaseUtils.getDefaultUserRecipients(user);
        user.getRecipients().forEach(recipient -> userRecipients
                .add(recipient.getNickname() != null ? String.format("%s (%s)", recipient.getNickname(), recipient.getName())
                        : recipient.getName()));
        return verifyRecipients(userRecipients, availableRecipients);
    }


    public boolean isRecipientCreated(Recipient recipient) {
        logger.info("Checking if the recipient is created properly");
        try {
            checkIfTextDisplayed(recipient.getName(), "New Recipient name is not listed in the recipient list");
            checkIfTextDisplayed(recipient.getNickname(), "New Recipient nickname is not present in the recipient list");
        } catch (TextNotDisplayedException e) {
            logger.warn("Recipient was not created properly", e);
            return false;
        }
        return true;
    }

}

