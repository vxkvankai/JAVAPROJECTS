package com.d3.pages.consumer.moneymovement.recipients.edit;

import com.d3.datawrappers.recipient.base.OnUsAccountIOwnRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.transfers.ProviderOption;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.RecipientsPage;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class EditRecipients extends MoneyMovementBasePage {

    @FindBy(id = "name")
    private Element nameInput;

    @FindBy(id = "nickname")
    private Element nicknameInput;

    @FindBy(css = "button.save-recipient")
    private Element saveButton;

    @FindBy(xpath = "//a[contains(text(),'Bill Pay Details')]")
    private Element billPayDetails;
    
    @FindBy(css = "button.btn-cancel")
    private Element cancelButton;

    @FindBy(xpath = "//a[contains(text(), 'On Us Details')]")
    private Element onUsDetails;

    private D3Element editButtonElement(By by) {
        return new D3Element(driver.findElement(by));
    }

    public EditRecipients(WebDriver driver) {
        super(driver);
    }

    @Override
    protected EditRecipients me() {
        return this;
    }

    public RecipientsPage clickSaveButton() {
        saveButton.click();
        return RecipientsPage.initialize(driver, RecipientsPage.class);
    }

    public String getName() {
        return nameInput.getValueAttribute();
    }

    public String getNickname() {
        return nicknameInput.getValueAttribute();
    }

    public EditRecipients expandBillPayDetails() {
        billPayDetails.click();
        return this;
    }

    public EditRecipients expandOnUsDetails() {
        onUsDetails.click();
        return this;
    }

    public boolean areOnUsDetailsCorrect(OnUsAccountIOwnRecipient recipient) {
        try {
            checkIfTextDisplayed(recipient.getRoutingNumber(), "Routing Number: {} not displayed");
            checkIfTextDisplayed(recipient.getFinancialInstitution(), "Name: {} not displayed");
            checkIfTextDisplayed(recipient.getMaskedAccountNumber(), "Account number: {} not displayed");
            checkIfTextDisplayed(recipient.getAccountType().name(), "Account type: {} not displayed");
            return true;
        } catch (TextNotDisplayedException e) {
            return false;
        }
    }

    public EditRecipients enterNickname(String nickname) {
        nicknameInput.sendKeys(nickname);
        return this;
    } 
    

    /**
     * Click the edit button of the given recipient type if the recipient is editable (else set forceClick to true to always attempt)
     *
     * @param recipient Recipient rail to click the edit button
     * @param forceClick Always attempt to click the button (even if expected to fail)
     */
    public EditRecipientsForm clickEditButton(Recipient recipient, boolean forceClick) {
        if (recipient.isEditable() || forceClick) {
            log.info("Recipient is editable (or forceClick was set to true) , clicking button");
            editButtonElement(recipient.getEditButtonBy()).click();
        } else {
            log.info("Recipient is not editable, not clicking button");
        }
        return recipient.getEditRecipientsForm(driver);
    }

    
    public EditRecipientsForm getRecipientForm(Recipient recipient) {
        return recipient.getEditRecipientsForm(driver);
    }

 

    public boolean isAddRecipientInformationCorrect(Recipient recipient) {
        log.info("Checking if recipient information is correct");
        try {
            checkIfTextEquals(getName(),recipient.getName());
            checkIfTextEquals(getNickname(),recipient.getNickname());
            
        }catch (TextNotDisplayedException e) {
            log.warn("Recipient information is not correct");
            return false;
        }
         
        if(recipient.isEditable()) {
            editButtonElement(recipient.getEditButtonBy()).click();
        }
        return recipient.isFormValid(driver);
    }
    
    
    public boolean isRecipientInformationCorrect(Recipient recipient) {
        return isRecipientInformationCorrect(recipient, false);
    }
    
    public boolean isRecipientInformationCorrect(Recipient recipient, boolean skipNameVerification) {
        String errorMsg = "{}: {} on the Dom does not equal the recipient {}";
        
        if (!skipNameVerification) {
            String name = getName();
            log.info("Checking if name: {} is on the form", name);
            if (!name.equals(recipient.getName())) {
                log.info(errorMsg, "Name", name, recipient.getName());
                return false;
            }
        }

        String nickName = getNickname();
        log.info("Checking if nickname: {} is on the form", nickName);
         if (!nickName.contentEquals(recipient.getNickname())) {
             log.info(errorMsg, "Nickname", nickName, recipient.getNickname());
             return false;
        }

        if(recipient.isEditable() ) {
            editButtonElement(recipient.getEditButtonBy()).click();  
        } 
        if(recipient.getProviderOption().equals(ProviderOption.CORE_ON_US_TRANSFER))
            return true;
                   
        return recipient.isFormValid(driver); 
        
     }
    
public boolean isNameFieldEditable() {
    return nameInput.isEnabled();
}
}

