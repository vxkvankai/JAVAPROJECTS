package com.d3.pages.consumer.settings.businessprofile;

import com.d3.pages.consumer.settings.SettingsBasePage;
import com.d3.pages.consumer.settings.profile.ChangeAddressForm;
import com.d3.pages.consumer.settings.profile.ChangeContactInfoForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class BusinessProfile extends SettingsBasePage {

    @FindBy(css = "button.btn.btn-default.edit-address")
    private Element changeAddressButton;

    @FindBy(css = "button.btn.btn-default.edit-info")
    private Element changeContactInfoButton;

    public BusinessProfile(WebDriver driver) {
        super(driver);
    }

    @Override
    protected BusinessProfile me() {
        return this;
    }

    public boolean isChangeAddressButtonDisplayed() {
        try {
            return changeAddressButton.isDisplayed();
        } catch (NoSuchElementException e) {
            log.info("Error finding button", e);
            return false;
        }
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
