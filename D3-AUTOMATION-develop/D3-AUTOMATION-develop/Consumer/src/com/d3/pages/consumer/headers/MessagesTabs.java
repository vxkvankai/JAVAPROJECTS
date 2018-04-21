package com.d3.pages.consumer.headers;


import com.d3.datawrappers.transfers.SingleTransfer;
import com.d3.pages.consumer.messages.SecureMessages;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class MessagesTabs extends HeaderBase {

    @FindBy(xpath = "//a[@href='#messages/notices']")
    private Element noticesLink;

    @FindBy(xpath = "//a[@href='#messages/secure']")
    private Element secureMessagesLink;

    @FindBy(css = "li.entity.message")
    private Element firstAlert;

    public MessagesTabs(WebDriver driver) {
        super(driver);
    }

    @Override
    protected MessagesTabs me() {
        return this;
    }

    public SecureMessages clickSecureMessagesLink() {
        secureMessagesLink.click();
        return SecureMessages.initialize(driver, SecureMessages.class);
    }

    public MessagesTabs clickFirstAlert() {
        firstAlert.click();
        return this;
    }

    //TODO: Update this method to use the ConsumerAlerts.getMessageDetails method to retrieve the message text instead of hardcoding it here
    public String getScheduleMessage(SingleTransfer transfer) {
        switch (transfer.getToAccount().getProviderOption()) {
            case CORE_ON_US_TRANSFER:
                return String.format("An On Us Transfer %s with the amount $%s from your account %s", transfer.getToAccount().getTransferableName(),
                        transfer.getAmountStr(), transfer.getFromAccount().getTransferableName());
            case FEDWIRE_TRANSFER:
                return String.format("A Fedwire transfer %s with the amount $%s from your account %s", transfer.getToAccount().getTransferableName(),
                        transfer.getAmountStr(), transfer.getFromAccount().getTransferableName());
            case ACH_TRANSFER:
                return String.format(" in the amount of $%s involving your account %s.", transfer.getAmountStr(), transfer.getFromAccount().getTransferableName());
            case REGULAR_PAYMENT:
                return String.format("Your account %s has a scheduled payment %s in the amount of $%s to be paid on",
                        transfer.getFromAccount().getTransferableName(),
                        transfer.getToAccount().getTransferableName(), transfer.getAmountStr());
            default:
                return String.format("An internal transfer %s with the amount $%s from your account %s was created for",
                        transfer.getToAccount().getTransferableName(), transfer.getAmountStr(), transfer.getFromAccount());
        }
    }
}
