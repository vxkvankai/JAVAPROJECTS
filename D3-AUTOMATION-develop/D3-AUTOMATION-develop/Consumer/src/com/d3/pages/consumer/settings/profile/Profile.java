package com.d3.pages.consumer.settings.profile;

import com.d3.pages.consumer.settings.SettingsBasePage;
import com.d3.support.internal.Element;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class Profile extends SettingsBasePage {

    @FindBy(css = "button.edit-name")
    private Element changeNameButton;

    @FindBy(css = "button.btn.btn-default.edit-address")
    private Element changeAddressButton;

    @FindBy(css = "button.btn.btn-default.edit-info")
    private Element changeContactInfoButton;


    public Profile(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Profile me() {
        return this;
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isChangeAddressButtonDisplayed() {
        try {
            return changeAddressButton.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    public ChangeNameForm getChangeNameForm() {
        changeNameButton.click();
        return ChangeNameForm.initialize(driver, ChangeNameForm.class);
    }

    public ChangeAddressForm getChangeAddressForm() {
        changeAddressButton.click();
        return ChangeAddressForm.initialize(driver, ChangeAddressForm.class);
    }

    public ChangeContactInfoForm getChangeContactInfoForm() {
        changeContactInfoButton.click();
        return ChangeContactInfoForm.initialize(driver, ChangeContactInfoForm.class);
    }


}
