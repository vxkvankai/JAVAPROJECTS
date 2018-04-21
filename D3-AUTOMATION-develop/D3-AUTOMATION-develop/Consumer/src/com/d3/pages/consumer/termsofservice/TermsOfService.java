package com.d3.pages.consumer.termsofservice;

import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import com.d3.support.wrappers.base.TextElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class TermsOfService extends PageObjectBase {

    @FindBy(xpath = "//label[@for='tosText']")
    private TextElement tosText;

    @AndroidFindBy(className = "android.widget.CheckBox")
    @iOSFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name, 'Check to accept Terms of Service')]")
    @FindBy(id = "tosAccept")
    private CheckBox acceptCheckBox;

    @iOSFindBy(xpath = "//XCUIElementTypeButton[contains(@name, 'Save')]")
    @FindBy(xpath = "//button[@type='submit']")
    private Element saveButton;

    public TermsOfService(WebDriver driver) {
        super(driver);
    }

    @Override
    protected TermsOfService me() {
        return this;
    }

    @Step("Get the TOS Title Text")
    public String getTosTitleText() {
        return tosText.getText();
    }

    @Step("Toggle the Accept check box")
    public TermsOfService toggleAcceptCheckBox() {
        acceptCheckBox.toggle();
        return this;
    }

    @Step("Click the Submit button")
    public Dashboard clickSubmit() {
        saveButton.click();
        return Dashboard.initialize(driver, Dashboard.class);
    }
}
