package com.d3.pages.consumer.settings.security;

import com.d3.exceptions.TextNotDisplayedException;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ChangeUserNameForm extends PageObjectBase {

    @FindBy(id = "new-username")
    private Element newUserName;

    @FindBy(id = "confirm-new-username")
    private Element confirmNewUserName;

    @FindBy(id = "password-new-username")
    private Element currentPassword;

    @FindBy(css = "button.btn.btn-default.btn-save")
    private  List<Element> saveButton;

    @FindBy(xpath = "//span[@class='user-name']")
    private Element displayedUserName;

    public ChangeUserNameForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ChangeUserNameForm me() {
        return this;
    }

    public ChangeUserNameForm enterNewUserName(String username) {
        newUserName.sendKeys(username);
        return this;
    }

    public ChangeUserNameForm confirmNewUserName(String username) {
        confirmNewUserName.sendKeys(username);
        return this;
    }

    public ChangeUserNameForm enterCurrentPassword(String password) {
        currentPassword.sendKeys(password);
        return this;
    }

    public Security clickSaveButton() {
        getDisplayedElement(saveButton).click();
        waitForSpinner();
        return Security.initialize(driver, Security.class);
    }

    public boolean verifyUserNameChanged(String newUserName) {

        try {
            checkIfTextEquals(getDisplayedUserName(), newUserName);
        } catch (TextNotDisplayedException e) {
            logger.warn("User Name was not validated", e);
            return false;
        }

        return true;
    }

    public String getDisplayedUserName() {
        return displayedUserName.getText();
    }
}
