package com.d3.pages.consumer.settings.security;

import com.d3.pages.consumer.settings.SettingsBasePage;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class Security extends SettingsBasePage {

    @FindBy(css = "button.btn.btn-default.btn-password-toggle")
    protected Element changePasswordButton;

    @FindBy(css = "button.btn.btn-default.btn-username-toggle")
    protected Element changeUserNameButton;

    @FindBy(css = "button.btn.btn-default.btn-questions-toggle")
    protected Element changeSecurityQuestionsButton;

    public Security(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Security me() {
        return this;
    }

    public ChangePasswordForm clickChangePasswordButton() {
        changePasswordButton.click();
        return ChangePasswordForm.initialize(driver, ChangePasswordForm.class);
    }

    public ChangeUserNameForm clickChangeUserNameButton() {
        changeUserNameButton.click();
        return ChangeUserNameForm.initialize(driver, ChangeUserNameForm.class);
    }

    public ChangeSecurityQuestionsForm clickChangeSecurityQuestionsButton() {
        changeSecurityQuestionsButton.click();
        return ChangeSecurityQuestionsForm.initialize(driver, ChangeSecurityQuestionsForm.class);
    }
}
