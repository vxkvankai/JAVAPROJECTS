package com.d3.pages.consumer.settings.security;

import com.d3.exceptions.TextNotDisplayedException;
import com.d3.support.D3Element;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ChangeSecurityQuestionsForm extends PageObjectBase {

    @FindBy(id = "password-security-questions")
    private Element currentPassword;

    @FindBy(className = "question")
    private List<Element> securityQuestionDropdown;


    private D3Element securityQuestion(String question) {
        By by = By.xpath(String.format("//a[@role='menuitem'][contains(text(), '%s')]", question));
        return new D3Element(driver.findElement(by));

    }

    @FindBy(name = "securityQuestion")
    private List<Element> securityQuestionAnswer;

    @FindBy(css = "button.btn.btn-default.btn-save")
    private List<Element> saveButton;

    public ChangeSecurityQuestionsForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ChangeSecurityQuestionsForm me() {
        return this;
    }

    public ChangeSecurityQuestionsForm enterCurrentPassword(String password) {
        currentPassword.sendKeys(password);
        return this;
    }

    public ChangeSecurityQuestionsForm changeFirstSecurityQuestion(String question) {
        securityQuestionDropdown.get(0).click();
        securityQuestion(question).click();
        return this;
    }

    public ChangeSecurityQuestionsForm changeSecondSecurityQuestion(String question) {
        securityQuestionDropdown.get(1).click();
        securityQuestion(question).click();
        return this;
    }

    public ChangeSecurityQuestionsForm changeThirdSecurityQuestion(String question) {
        securityQuestionDropdown.get(2).click();
        securityQuestion(question)
                .scrollIntoView()
                .waitUntilVisible()
                .click();
        return this;
    }

    public ChangeSecurityQuestionsForm changeFirstSecurityQuestionAnswer(String answer) {
        securityQuestionAnswer.get(0).sendKeys(answer);
        return this;
    }

    public ChangeSecurityQuestionsForm changeSecondSecurityQuestionAnswer(String answer) {
        securityQuestionAnswer.get(1).sendKeys(answer);
        return this;
    }

    public ChangeSecurityQuestionsForm changeThirdSecurityQuestionAnswer(String answer) {
        securityQuestionAnswer.get(2).sendKeys(answer);
        return this;
    }

    public boolean verifyQuestionsChanged(String question1, String question2, String question3) {
        try {
            checkIfTextEquals(getFirstSecurityQuestion(), question1);
            checkIfTextEquals(getSecondSecurityQuestion(), question2);
            checkIfTextEquals(getThirdSecurityQuestion(), question3);
        } catch (TextNotDisplayedException e) {
            logger.warn("Security questions not validated", e);
            return false;
        }

        return true;
    }

    public boolean verifyAnswersChanged(String answer) {

        try {
            checkIfTextEquals(getFirstSecurityAnswer(), answer);
            checkIfTextEquals(getSecondSecurityAnswer(), answer);
            checkIfTextEquals(getThirdSecurityAnswer(), answer);
        } catch (TextNotDisplayedException e) {
            logger.warn("Security Answers not validated", e);
            return false;
        }

        return true;
    }


    public Security clickSaveButton() {
        getDisplayedElement(saveButton).click();
        waitForSpinner();
        return Security.initialize(driver, Security.class);
    }

    public String getFirstSecurityQuestion() {
        return securityQuestionDropdown.get(0).getText();
    }

    public String getSecondSecurityQuestion() {
        return securityQuestionDropdown.get(1).getText();
    }

    public String getThirdSecurityQuestion() {
        return securityQuestionDropdown.get(2).getText();
    }

    public String getFirstSecurityAnswer() {
        return securityQuestionAnswer.get(0).getValueAttribute();
    }

    public String getSecondSecurityAnswer() {
        return securityQuestionAnswer.get(1).getValueAttribute();
    }

    public String getThirdSecurityAnswer() {
        return securityQuestionAnswer.get(2)
                .scrollIntoView()
                .waitUntilVisible()
                .getValueAttribute();
    }
}