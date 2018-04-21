package com.d3.support;


import static org.awaitility.Awaitility.await;

import com.d3.api.helpers.banking.D3BankingApi;
import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.datawrappers.user.enums.ToggleMode;
import com.d3.exceptions.D3ApiException;
import com.d3.exceptions.TextNotContainedException;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.support.internal.Element;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * This class should be treated as an abstract one. Allowing it to be public lets us avoid a lot of boilerplate code in the sub page objects
 */
@Slf4j
public abstract class PageObjectBase<T extends PageObjectBase> {

    private static final String SPINNER_CLASS = "spinner";
    private static final String SLIDE_PANEL_CLASS = "slide-panel";
    private static final String MODAL_OPEN_CLASS = "modal-open";
    private static final int WAIT_TIMEOUT = 2;
    protected WebDriver driver;

    public PageObjectBase(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Inits the page object via {@link ElementFactory}
     *
     * @param driver Webdriver object
     * @param tClass Class that is being initialized (e.g. Header.class)
     * @return A initialized page object of the given class
     */
    public static <T> T initialize(WebDriver driver, Class<T> tClass) {
        return ElementFactory.initElements(driver, tClass);
    }

    /**
     * Checks if the text is present in the DOM
     *
     * @param what The text that will be checked
     */
    public boolean isTextPresent(String what) {
        if (!isTextDisplayed(what)) {
            log.warn("'{}' was not found on the DOM, checking source directly", what);
            if (driver.getPageSource().contains(what)) {
                log.warn("'{}' was found in the page source", what);
                return true;
            }
            log.error("'{}' was not found in the page source", what);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the text is present on the UI
     *
     * @param what The text that will be checked
     */
    @Step("See if text: '{what}' is displayed")
    public boolean isTextDisplayed(String what) {
        log.info("Checking {} is on the DOM", what);
        return waitUntilTextPresent(what);
    }


    /**
     * Checks if specific text is contained in a String value
     *
     * @param text The string value
     * @param what The value to check exists in the text
     */
    private boolean isTextContained(String text, String what) {
        log.info("Checking if the string value {} contains {}", text, what);
        return text.contains(what);
    }

    /**
     * Check if the what given is contained within the text given, if it is not, throw a TextNotContainedException This is just a helper method that
     * allows a common error message to be used as well as putting multiple of these validations in a try catch block
     *
     * @param what What to check for in the text (second %s to be sent to the error message)
     * @param errorMsg Error message (using String.format interpolation), needs to have two %s's
     * @param text first %s to be sent to the error message, will only be used for the error message
     */
    public void checkIfTextContains(String text, String errorMsg, String what) throws TextNotContainedException {
        if (!isTextContained(text, what)) {
            String msg = String.format(errorMsg, text, what);
            throw new TextNotContainedException(msg);
        }
    }

    /**
     * Check if the text given is on the screen, if it is not, throw a TextNotDisplayedException This is just a helper method that allows a common
     * error message to be used as well as putting multiple of these validations in a try catch block
     *
     * @param what What to check for on the screen (second %s to be sent to the error message)
     * @param errorMsg Error message (using String.format interpolation), needs to have two %s's
     * @param whatType first %s to be sent to the error message, will only be used for the error message
     */
    public void checkIfTextDisplayed(String what, String errorMsg, String whatType) throws TextNotDisplayedException {
        if (!isTextDisplayed(what)) {
            String msg = String.format(errorMsg, whatType, what);
            throw new TextNotDisplayedException(msg);
        }
    }

    /**
     * Check if the text given is on the screen, if it is not, throw a TextNotDisplayedException This is just a helper method that allows a common
     * error message to be used as well as putting multiple of these validations in a try catch block
     *
     * @param what What to check for on the screen (will be passed to the error message)
     * @param errorMsg Error message (using String.format interpolation), needs to have one %s
     * @throws TextNotDisplayedException Thrown when isTextDisplayed returns false
     */
    public void checkIfTextDisplayed(String what, String errorMsg) throws TextNotDisplayedException {
        if (!isTextDisplayed(what)) {
            String msg = String.format(errorMsg, what);
            throw new TextNotDisplayedException(msg);
        }
    }

    /**
     * Check if 2 strings are equal. This is a helper method that adds a common error message if the two do not equal, as well as allowing the user to
     * have multiple checks in one try/catch block
     *
     * @param expected Expected String to be checked against
     * @param actual Actual String that is being verified
     * @throws TextNotDisplayedException Thrown when the actual does not equal the expected
     */
    public void checkIfTextEquals(String actual, String expected) throws TextNotDisplayedException {
        log.info("Verifying {} == {}", actual, expected);
        if (!expected.equals(actual)) {
            String msg = String.format("Text did not match: Actual: %s, Expected: %s", actual, expected);
            throw new TextNotDisplayedException(msg);
        }
    }


    @Step("Check if text is not present: {what}")
    public boolean isTextNotPresent(String what) {
        log.info("Checking if {} is on the DOM", what);
        return waitUntilTextNotPresent(what);
    }

    public boolean isElementDisplayed(Element element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    /**
     * Checks if the link is present in the DOM
     *
     * @param what The link that will be checked
     */
    public boolean isLinkPresent(String what) {
        log.info("Checking for link {} on the DOM", what);
        return waitUntilLinkPresent(what);
    }

    /**
     * Waits for the Spinner on the screen to disappear
     */
    @Step("Wait for the spinner")
    protected void waitForSpinner() {
        await("Waiting until spinner is gone").atMost(15, TimeUnit.SECONDS).until(spinnerIsGone());
    }

    /**
     * Waits for the Schedule Modal on the screen to disappear
     */
    @Step("Wait for schedule modal to disappear")
    protected void waitForScheduleModalToClose() {
        await("Waiting until schedule modal is gone").atMost(15, TimeUnit.SECONDS).until(slidePanelIsGone());
    }

    /**
     * Waits for the Confirmation Modal on the screen to disappear
     */
    @Step("Wait for confirmation modal to disappear")
    protected void waitForConfirmationModalToClose() {
        await("Waiting until confirmation modal is gone").atMost(15, TimeUnit.SECONDS).until(confirmationModalIsClosed());
    }

    /**
     * Switch to the Web view (as opposed to Native) on mobile devices
     */
    public void switchToWebView() {
        String webviewContext = null;

        Set<String> availableContexts = ((AppiumDriver<?>) driver).getContextHandles();
        while (availableContexts.size() < 2) {
            availableContexts = ((AppiumDriver<?>) driver).getContextHandles();
        }

        for (String context : availableContexts) {
            if (context.contains("WEBVIEW")) {
                webviewContext = context;
                break;
            }
        }

        Assert.assertNotNull(webviewContext);
        ((AppiumDriver<?>) driver).context(webviewContext);
    }

    /**
     * Switch to the native view for mobile devices
     */
    public void switchToNativeView() {
        String nativeContext = null;

        Set<String> availableContexts = ((AppiumDriver<?>) driver).getContextHandles();
        for (String context : availableContexts) {
            if (context.contains("NATIVE")) {
                nativeContext = context;
                break;
            }
        }

        Assert.assertNotNull(nativeContext);
        ((AppiumDriver<?>) driver).context(nativeContext);
    }

    public boolean waitUntilTextPresent(String textToWaitFor) {
        log.info("Waiting for text to be present: '{}'", textToWaitFor);
        // Note (JMoravec): need to account for valid xpaths if a " or ' character is in the string to check (xpath can't escape chars)
        By by = textToWaitFor.contains("'") ? By.xpath(String.format("//*[contains(text(), \"%s\")]", textToWaitFor))
            : By.xpath(String.format("//*[contains(text(), '%s')]", textToWaitFor));
        List<WebElement> elements = driver.findElements(by);
        log.info("{} elements found with text {}, checking if any of them are visible", elements.size(), textToWaitFor);
        boolean present = false;
        for (int i = 0; i < elements.size(); ++i) {
            try {
                new WebDriverWait(driver, WAIT_TIMEOUT, 100).until(ExpectedConditions.visibilityOf(elements.get(i)));
                present = true;
                break;
            } catch (TimeoutException | NoSuchElementException ignored) {
                log.warn("'{}' is not displayed on the DOM, checking next element if it exists", textToWaitFor);
            } catch (StaleElementReferenceException e) {
                log.warn("Stale Element Reference on the element, finding elements again", e);
                // reset the loop
                elements = driver.findElements(by);
                i = -1; //im not proud of this
            }
        }
        if (present) {
            log.info("'{}' is on the DOM.", textToWaitFor);
        } else {
            log.warn("'{}' was still missing from the DOM", textToWaitFor);
        }
        return present;
    }


    public boolean waitUntilTextNotPresent(String textToWaitFor) {
        try {
            log.info("Waiting for text to go away: '{}'", textToWaitFor);
            By by = By.xpath(String.format("//*[contains(text(), '%s')]", textToWaitFor));

            List<WebElement> elements = driver.findElements(by);
            log.info("{} elements found with text {}, checking if any of them are still visible", elements.size(), textToWaitFor);
            new WebDriverWait(driver, WAIT_TIMEOUT, 100).until(ExpectedConditions.invisibilityOfAllElements(elements));
            log.info("{} was not found on the DOM", textToWaitFor);
            return true;
        } catch (TimeoutException e) {
            log.info("waitUntilTextNotPresent exception: ", e);
            log.info("{} was found on the DOM", textToWaitFor);
            return false;
        }
    }

    public boolean waitUntilLinkPresent(String linkToWaitFor) {
        try {
            By by = By.linkText(linkToWaitFor);
            new WebDriverWait(driver, WAIT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(by));
            log.info("Link with text {} was found on the DOM", linkToWaitFor);
            return true;
        } catch (TimeoutException | NoSuchElementException ignored) {
            log.info("Link with text {} was not found on the DOM", linkToWaitFor);
            return false;
        }
    }

    /**
     * Waits for the current url to be the given url
     */
    @Step("Wait for the url")
    public void waitForUrlToBe(String url) {
        await(String.format("Waiting until url is equal to <%s>", url)).atMost(10, TimeUnit.SECONDS).until(currentUrlEquals(url));

    }

    /**
     * Logs in to the banking app via the API, then adds the session cookies to the running webdriver instance
     *
     * @param username Username to log in as
     * @param password Password of the user
     * @param secretQuestion Secret Question answer that is asked
     * @param toggleMode Toggle Mode of the user
     * @param apiUrl Url for the api to login to
     */
    public void loginViaAPi(String username, String password, String secretQuestion, String apiUrl, String toggleMode)
        throws D3ApiException {
        // Note (Jmoravec) doesn't need to be Money movement
        D3BankingApi api = new MoneyMovementApiHelper(apiUrl);
        Map<String, String> cookies = api.login(username, password, secretQuestion);

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            driver.manage().addCookie(new Cookie(entry.getKey(), entry.getValue()));
        }

        if (toggleMode.equals(ToggleMode.BUSINESS.toString())) {
            api.switchToggleToBusinessMode();
        }

        driver.navigate().refresh();
        waitForSpinner();
    }

    /**
     * Go through a list of element and return the first element who's value matches the given value
     *
     * @param elements List of elements to go through
     * @param value Value to match against
     * @return Element if found, throw NoSuchElementException otherwise
     */
    protected Element getElementInListFromValue(List<Element> elements, String value) {
        return elements.stream()
            .filter(element -> element.getValueAttribute().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("No element found with value attribute equal to %s", value)));
    }

    /**
     * Go through a list of element and return the first element that contains the given text value
     *
     * @param elements List of elements to go through
     * @param text text to check if element contains
     * @return Element if found, throw NoSuchElementException otherwise
     */
    protected Element getElementInListByTextContains(List<Element> elements, String text) {
        return elements.stream().filter(element -> element.getText().contains(text)).findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("No element found that contained text %s", text)));

    }

    /**
     * Go through a list of element and return the first element that contains BOTH given text values
     *
     * @param elements List of elements to go through
     * @param text text to check if element contains
     * @param text2 text to check if element contains
     * @return Element if found, throw NoSuchElementException otherwise
     */
    protected Element getElementInListByMultipleTextContains(List<Element> elements, String text, String text2) {
        return elements.stream().filter(element -> element.getText().contains(text) && element.getText().contains(text2)).findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("No element found that contained text %s AND %s", text, text2)));

    }

    /**
     * Go through a list of element and return the first element that is displayed
     *
     * @param elements List of elements to go through
     * @return Element if found, otherwise throw a {@link NoSuchElementException}
     */
    protected Element getDisplayedElement(List<Element> elements) {
        return elements.stream().filter(Element::isDisplayed).findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("No Displayed Element found for %s", elements.toString())));
    }

    /**
     * Get the last item from a list of elements. Useful for dynamic lists, to get the current/latest element
     *
     * @param elements List of elements you are interacting with
     * @return Element The last item on the list
     */
    public Element getLastListValue(List<Element> elements) {
        return elements.get(elements.size() - 1);
    }

    protected abstract T me();

    public T reloadPage() {
        driver.navigate().refresh();
        waitForSpinner();
        return me();
    }


    /**
     * Method will check if the loading spinner is currently shown
     *
     * @return true if spinner is gone and ajax has finished loading
     */
    private Callable<Boolean> spinnerIsGone() {
        return () ->
            (boolean) ((JavascriptExecutor) driver).executeScript(String.format("return document.querySelector('.%s') == null", SPINNER_CLASS))
                && (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
    }

    /**
     * Method will check if Payment/Transfer modal has been closed
     *
     * @return true if modal slide panel is gone and ajax has finished loading
     */
    private Callable<Boolean> slidePanelIsGone() {
        return () ->
            (boolean) ((JavascriptExecutor) driver).executeScript(String.format("return document.querySelector('.%s') == null", SLIDE_PANEL_CLASS))
                && (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
    }

    /**
     * Method will check if Confirmation modal has been closed
     *
     * @return true if modal slide panel is gone and ajax has finished loading
     */
    private Callable<Boolean> confirmationModalIsClosed() {
        return () ->
            (boolean) ((JavascriptExecutor) driver).executeScript(String.format("return document.querySelector('.%s') == null", MODAL_OPEN_CLASS))
                && (boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
    }

    /**
     * Method will check if current url is equal to the given url
     *
     * @return true current url equals given url
     */
    private Callable<Boolean> currentUrlEquals(String url) {
        return () -> driver.getCurrentUrl().trim().equals(url);
    }
}
