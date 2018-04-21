package com.d3.pages.consumer.settings.security;

import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ChangePasswordForm extends PageObjectBase {

    @FindBy(id = "password-change")
    private Element currentPassword;

    @FindBy(id = "new-password-change")
    private Element newPassword;

    @FindBy(id= "confirm-new-password-change")
    private Element confirmNewPassword;

    @FindBy(css = "button.btn.btn-default.btn-save")
    private List<Element> saveButton;

    public ChangePasswordForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ChangePasswordForm me() {
        return this;
    }

    public ChangePasswordForm enterCurrentPassword(String password) {
        currentPassword.sendKeys(password);
        return this;
    }

    public ChangePasswordForm enterNewPassword(String password) {
        newPassword.sendKeys(password);
        return this;
    }

    public ChangePasswordForm confirmCurrentPassword(String password) {
        confirmNewPassword.sendKeys(password);
        return this;
    }

    public Security clickSaveButton() {
        getDisplayedElement(saveButton).click();
        return Security.initialize(driver, Security.class);
    }
}
