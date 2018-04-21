package com.d3.pages.consumer.login;

import static com.d3.helpers.WebdriverHelper.isMobileApp;

import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.exceptions.D3FactoryException;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.termsofservice.TermsOfService;
import com.d3.support.PageObjectBase;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class LoginPage extends PageObjectBase {

    private String baseUrl;
    private String apiVersion;

    @AndroidFindBy(xpath = "//*[@text='Username']")
    @iOSFindBy(xpath = "//XCUIElementTypeTextField[contains(@value, 'USER_NAME')]")
    @FindBy(id = "challengeUsername")
    private WebElement username;

    @AndroidFindBy(xpath = "//*[@password='true']") // NOSONAR
    @iOSFindBy(xpath = "//XCUIElementTypeSecureTextField[contains(@value, 'PASSWORD')]")
    @FindBy(id = "challengePassword")
    private WebElement password;

    @AndroidFindBy(xpath = "//*[@text='Secret answer']")
    @iOSFindBy(xpath = "//XCUIElementTypeSecureTextField[contains(@value, 'SECRET_QUESTION')]")
    @FindBy(id = "secretQuestionAnswer")
    private List<WebElement> secretQuestion;

    @iOSFindBy(xpath = "//XCUIElementTypeButton[contains(@name, 'Submit')]")
    @FindBy(css = "button.btn-submit")
    private WebElement submit;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected LoginPage me() {
        return this;
    }

    @Step("Enter Username: {loginId}")
    public LoginPage enterUserName(String loginId) {
        username.sendKeys(loginId);
        return this;
    }

    @Step("Enter Password: {userpassword}")
    public LoginPage enterPassword(String userpassword) {
        password.sendKeys(userpassword);
        return this;
    }

    @Step("Click the Submit button")
    public LoginPage clickSubmitButton() {
        log.info("Clicking the submit button for login");
        waitUntilTextPresent("Submit");
        submit.click();
        return this;
    }

    @Step("Enter the secret question answer: {answer}")
    public LoginPage enterSecretQuestionAnswers(String answer) {
        log.info("Entering secret answer for questions: {}", answer);
        for (WebElement question : secretQuestion) {
            question.sendKeys(answer);
        }
        return this;
    }

    /**
     * Logs into the application manually
     *
     * @param userName Username to log in as
     * @param passWord Password of the user
     * @param secretQuestion Secret question answer for security question
     */
    @Step("Login manually as: {userName}/{passWord}: {secretQuestion}")
    private void loginManually(String userName, String passWord, String secretQuestion) {
        log.info("Entering username into field: {}", userName);
        enterUserName(userName);
        log.info("Entering password into field: {}", passWord);
        enterPassword(passWord);
        clickSubmitButton();

        enterSecretQuestionAnswers(secretQuestion);

        waitUntilTextPresent("Submit");
        log.info("Clicking the submit button for secret answers");
        clickSubmitButton();

        if (isMobileApp()) {
            switchToWebView();
        }
        waitForSpinner();
    }

    /**
     * Login to the Banking app. The page object needs to have the baseUrl (and apiVersion if logging in via api) set before logging in
     *
     * @param username Username to log in as
     * @param password Password of the user
     * @param secretQuestion Secret question answer for the security question
     * @param toggleMode Toggle Mode of the user
     * @param loginManually If true, log into the ap manually, otherwise use the API
     * @throws D3FactoryException Thrown if baseUrl is not set, or if apiVersion is needed and not set
     */
    public void login(String username, String password, String secretQuestion, boolean loginManually, String toggleMode) throws D3ApiException {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new D3FactoryException("Base Url is not set for the login page");
        }

        if (isMobileApp()) {
            loginMobile(username, password, secretQuestion, toggleMode);
        } else {
            driver.get(baseUrl);

            if (loginManually) {
                loginManually(username, password, secretQuestion);
            } else {
                if (apiVersion == null || apiVersion.isEmpty()) {
                    throw new D3FactoryException("ApiVersion is not set");
                }
                loginViaAPi(username, password, secretQuestion, baseUrl + apiVersion, toggleMode);
            }
        }
    }

    /**
     * Login for the mobile platform
     *
     * @param username Username to log in as
     * @param password Password of the user
     * @param secretQuestion Secret question answer for the security question
     * @param toggleMode Toggle Mode of the user
     * @throws D3FactoryException Thrown if baseUrl is not set, or if apiVersion is needed and not set
     */
    private void loginMobile(String username, String password, String secretQuestion, String toggleMode) throws D3ApiException {
        if (apiVersion == null || apiVersion.isEmpty()) {
            throw new D3FactoryException("ApiVersion is not set");
        }
        loginViaAPi(username, password, secretQuestion, baseUrl + apiVersion, toggleMode);
    }

    public TermsOfService loginTOS(D3User user) throws D3ApiException {
        return loginTOS(user, true);
    }

    public TermsOfService loginTOS(D3SecondaryUser secondaryUser, boolean loginManually) throws D3ApiException {
        login(secondaryUser.getLogin(), secondaryUser.getLoginPsswd(), secondaryUser.getSecretQuestion(), loginManually, secondaryUser.getToggleMode().toString());
        return TermsOfService.initialize(driver, TermsOfService.class);
    }

    public TermsOfService loginTOS(D3SecondaryUser secondaryUser) throws D3ApiException {
        return loginTOS(secondaryUser, true);
    }

    public TermsOfService loginTOS(D3User user, boolean loginManually) throws D3ApiException {
        login(user.getLogin(), user.getPassword(), user.getSecretQuestion(), loginManually, user.getToggleMode().toString());
        return TermsOfService.initialize(driver, TermsOfService.class);
    }

    public Dashboard login(D3User user) throws D3ApiException {
        return login(user, false, user.getToggleMode().toString());
    }

    public Dashboard login(D3SecondaryUser secondaryUser) throws D3ApiException {
        return login(secondaryUser, false, secondaryUser.getToggleMode().toString());
    }


    public Dashboard login(D3User user, boolean loginManually, String toggleMode) throws D3ApiException {
        boolean reallyLoginManually = loginManually;
        if (!reallyLoginManually && isMobileApp()) {
            reallyLoginManually = true;
        }
        login(user.getLogin(), user.getPassword(), user.getSecretQuestion(), reallyLoginManually, toggleMode);
        return Dashboard.initialize(driver, Dashboard.class);
    }

    public Dashboard login(D3SecondaryUser secondaryUser, boolean loginManually, String toggleMode) throws D3ApiException {
        boolean reallyLoginManually = loginManually;
        if (!reallyLoginManually && isMobileApp()) {
            reallyLoginManually = true;
        }
        login(secondaryUser.getLogin(), secondaryUser.getLoginPsswd(), secondaryUser.getSecretQuestion(), reallyLoginManually, toggleMode);
        return Dashboard.initialize(driver, Dashboard.class);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
