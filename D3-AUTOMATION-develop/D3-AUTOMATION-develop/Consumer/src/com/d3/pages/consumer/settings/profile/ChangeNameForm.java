package com.d3.pages.consumer.settings.profile;

import com.d3.exceptions.TextNotDisplayedException;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import io.codearte.jfairy.producer.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class ChangeNameForm extends PageObjectBase {

    @FindBy(name = "firstName")
    private Element firstNameInput;

    @FindBy(name = "middleName")
    private Element middleNameInput;

    @FindBy(name = "lastName")
    private Element lastNameInput;

    @FindBy(xpath = "//button[.='Cancel']")
    private List<Element> cancelButton;

    @FindBy(xpath = "//button[.='Save']")
    private List<Element> saveButton;

    public ChangeNameForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ChangeNameForm me() {
        return this;
    }

    public ChangeNameForm enterFirstName(String name) {
        log.info("Entering First Name: {}", name);
        firstNameInput.sendKeys(name);
        return this;
    }

    public ChangeNameForm enterMiddleName(String name) {
        log.info("Entering Middle Name: {}", name);
        middleNameInput.sendKeys(name);
        return this;
    }

    public ChangeNameForm enterLastName(String name) {
        log.info("Entering Last Name: {}", name);
        lastNameInput.sendKeys(name);
        return this;
    }

    public ChangeNameForm changeNameFields(Person person) {
        enterFirstName(person.getFirstName());
        enterMiddleName(person.getMiddleName());
        enterLastName(person.getLastName());
        return this;
    }

    public ChangeNameForm clickCancelButton() {
        getDisplayedElement(cancelButton).click();
        return this;
    }

    public Profile clickSaveButton() {
        getDisplayedElement(saveButton).click();
        return Profile.initialize(driver, Profile.class);
    }

    public boolean isNameCorrect(Person person) {

        try {
            checkIfTextEquals(getFirstName(), person.getFirstName());
            checkIfTextEquals(getMiddleName(), person.getMiddleName());
            checkIfTextEquals(getLastName(), person.getLastName());
        } catch (TextNotDisplayedException e) {
            log.warn("Name was not validated", e);
            return false;
        }

        return true;
    }

    public String getFirstName() {
        return firstNameInput.getValueAttribute();
    }

    public String getMiddleName() {
        return middleNameInput.getValueAttribute();
    }

    public String getLastName() {
        return lastNameInput.getValueAttribute();
    }

}
