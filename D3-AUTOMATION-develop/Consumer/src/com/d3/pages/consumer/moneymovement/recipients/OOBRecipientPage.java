package com.d3.pages.consumer.moneymovement.recipients;

import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class OOBRecipientPage extends MoneyMovementBasePage {

    @FindBy(id = "code")
    private Element codeInput;

    @FindBy(css = "button.save")
    private Element continueButton;

    public OOBRecipientPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected OOBRecipientPage me() {
        return this;
    }

    public static OOBRecipientPage getOOBPage(WebDriver driver) {
        return initialize(driver, OOBRecipientPage.class);
    }

    public OOBRecipientPage enterVerificationCode(String verificationCode) {
        codeInput.sendKeys(verificationCode);
        return this;
    }

    public AddRecipientForm clickContinueButton(Recipient recipient) {
        continueButton.click();
        return recipient.getAddRecipientForm(driver);
    }
}
