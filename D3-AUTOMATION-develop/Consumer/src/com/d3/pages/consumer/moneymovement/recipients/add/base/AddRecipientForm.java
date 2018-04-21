package com.d3.pages.consumer.moneymovement.recipients.add.base;

import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.l10n.moneymovement.RecipientsL10N;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public abstract class AddRecipientForm<T extends AddRecipientForm, R extends Recipient> extends PageObjectBase {

    @FindBy(id = "name")
    private Element nameInput;

    @FindBy(id = "nickname")
    private Element nickNameInput;

    @FindBy(className = "save-recipient")
    private Element saveButton;
    
    
    public AddRecipientForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected abstract T me();

    public T enterName(String name) {
        logger.info("Entering name: {}", name);
        nameInput.sendKeys(name);
        return me();
    }

    public T enterNickname(String nickname) {
        logger.info("Entering nickname: {}", nickname);
        nickNameInput.sendKeys(nickname);
        return me();
    }
    
    
    

    public RecipientsPage clickSaveButton() {
        saveButton.click();
        waitForSpinner();
        return RecipientsPage.initialize(driver, RecipientsPage.class);
    }

    public abstract AddRecipientForm enterRecipientInfo(R recipient);
    
    public boolean isOptionTextDisplayed(Recipient recipient) {
        switch (recipient.getType()) {
            case ACCOUNT_I_OWN:
                return isTextDisplayed(String.format(RecipientsL10N.Localization.PRE_NOTE_TEXT.getValue(),"3"));
            case BANK_ACCOUNT:
               return isTextDisplayed(RecipientsL10N.Localization.OPTION_ON_US_TEXT.getValue());
            case WIRE:
                return isTextDisplayed(RecipientsL10N.Localization.FEDWIRE_OPTION_TEXT.getValue());
            default:
                return true;
        }   
    }
}
