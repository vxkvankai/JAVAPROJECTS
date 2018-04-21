package com.d3.pages.consumer.moneymovement;

import static com.d3.helpers.AccountHelper.verifyAccounts;

import com.d3.datawrappers.user.D3Contact;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.moneymovement.BillPayEnrollmentL10N;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class BillPayPage extends MoneyMovementBasePage {

    @FindBy(css = "button.btn.enroll")
    private Element enrollNowButton;

    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private Element billPayEnrollSaveButton;

    @FindBy(css = "button.cancel")
    private Element billPayCancelButton;

    @FindBy(id = "preferredEndpointId")
    private Select billPayFundingAcct;

    @FindBy(css = "#preferredEndpointId option")
    private List<Element> availableFundingAccounts;

    @FindBy(css = "div.billpay-info-needed")
    private Element billPayInfoNeeded;


    public BillPayPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected BillPayPage me() {
        return this;
    }

    public BillPayPage clickEnrollNow() {
        enrollNowButton.click();
        return this;
    }

    public BillPayPage selectFundingAccount(String account) {
        billPayFundingAcct.selectByTextContains(account);
        return this;
    }

    public BillPayPage clickEnrollSaveButton() {
        billPayEnrollSaveButton.click();
        return this;
    }

    public BillPayPage clickCancelButton() {
        billPayCancelButton.click();
        return this;
    }

    public boolean areFundingAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, availableFundingAccounts, Element::getText);
    }

    public boolean isBillPayMoreInfoNeededMessageDisplayed() {
        try {
            checkIfTextEquals(billPayInfoNeeded.getText(), String.format(BillPayEnrollmentL10N.Localization.INFO_NEEDED.getValue(), "Profile"));
        } catch (TextNotDisplayedException e) {
            log.warn("Bill Pay Info Needed message not displayed", e);
            return false;
        }

        return true;
    }

    public boolean isCorrectFormInformationDisplayed(D3User user) {
        String errMsg = "Bill pay enrollment form did not contain %s %s";
        try {
            checkIfTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_ADDRESS.getValue(), errMsg, "Address Label");
            checkIfTextDisplayed(user.getProfile().getPhysicalAdd().getAddress1(), errMsg, "Address Line 1");
            checkIfTextDisplayed(user.getProfile().getPhysicalAdd().getAddress2(), errMsg, "Address Line 2");
            checkIfTextDisplayed(
                    String.format("%s, %s %s", user.getProfile().getPhysicalAdd().getCity(), user.getProfile().getPhysicalAdd().getState(),
                            user.getProfile().getPhysicalAdd().getPostalCode()), errMsg, "City, State Zip");
            checkIfTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_EMAIL.getValue(), errMsg, "Email Label");
            D3Contact emailContact = user.getPrimaryContactForType(D3Contact.ContactType.EMAIL);
            if (emailContact == null) {
                log.error("Email Contact for user was null");
                return false;
            }
            checkIfTextDisplayed(emailContact.getValue(), errMsg, "Primary Email Address");
            checkIfTextDisplayed(BillPayEnrollmentL10N.Localization.ENROLL_PHONE_NUMBER.getValue(), errMsg, "Phone Number Label");

            D3Contact phoneContact = user.getPrimaryContactForType(D3Contact.ContactType.PHONE);
            if (phoneContact == null) {
                log.error("Phone contact for user was null");
                return false;
            }

            checkIfTextDisplayed(phoneContact.getFormattedPhoneNumber(), errMsg,
                    "Primary Phone Number");

        } catch (TextNotDisplayedException e) {
            log.warn("Correct Bill Pay Form information was not displayed", e);
            return false;
        }

        return true;
    }
}
