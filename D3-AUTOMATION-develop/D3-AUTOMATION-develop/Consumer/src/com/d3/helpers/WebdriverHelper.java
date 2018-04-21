package com.d3.helpers;

import com.d3.support.D3Element;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebdriverHelper {

    private WebdriverHelper() {
    }

    /**
     * Create a new Webdriver wait object with the correct timeout
     */
    public static WebDriverWait getWebDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, getWebDriverTimeout());
    }

    /**
     * Get the value of the webdriver timeout
     */
    public static long getWebDriverTimeout() {
        return Long.valueOf(System.getProperty("WebDriverTimeout", "5"));
    }

    /**
     * Find the element, then wait until it is clickable
     *
     * @param driver Webdriver
     * @param by By method to find the element
     * @return The D3Element returned by the wait method
     */
    public static D3Element waitUntilClickableBy(WebDriver driver, By by) {
        return new D3Element(getWebDriverWait(driver).until(ExpectedConditions.elementToBeClickable(by)));
    }

    /**
     * Wait until the given element is visible
     *
     * @param driver Webdriver
     * @param by By method to find the element
     * @return The D3Element returned by the wait method
     */
    public static D3Element waitUntilVisible(WebDriver driver, By by) {
        return new D3Element(getWebDriverWait(driver).until(ExpectedConditions.visibilityOfElementLocated(by)));
    }

    /**
     * Checks what browser is being tested, and returns true if mobile browser is found
     *
     * @return true if browse contains Android or iOS, false otherwise
     */
    public static boolean isMobile() {
        return System.getProperty("browse").equalsIgnoreCase("mobile");
    }

    /**
     * Checks if the actual mobile app is being tested
     */
    public static boolean isMobileApp() {
        return System.getProperty("mobileBrowserType").toLowerCase().contains("app");
    }

    /**
     * Returns the correct tab key for the platform. Currently only supports desktop browser, but will be changed in the future for a different tab
     * key to be pressed for mobile users.
     */
    public static CharSequence getTab() {
        return Keys.TAB;
    }

    /**
     * Switch to the last tab in the getWindowHandles list
     *
     * @param driver WebDriver instance
     */
    @Step("Switch browser tabs")
    public static void switchToNewBrowserTab(WebDriver driver) {
        for (String newTab : driver.getWindowHandles()) {
            driver.switchTo().window(newTab);
        }
    }

    /**
     * When scrollIntoView isn't working, use the PAGE_DOWN key to get to bottom of page
     *
     * @param driver WebDriver instance
     */
    public static void goToBottomOfPage(WebDriver driver) {
        Actions action = new Actions(driver);
        StringBuilder pageDown = new StringBuilder();
        for (int i = 0; i < 100; ++i) {
            pageDown.append(Keys.PAGE_DOWN);
        }
        action.sendKeys(pageDown).perform();
    }

    /**
     * When scrollIntoView isn't working, use the PAGE_UP key to get to top of page
     *
     * @param driver WebDriver instance
     */
    public static void goToTopOfPage(WebDriver driver) {
        Actions action = new Actions(driver);
        StringBuilder pageUp = new StringBuilder();
        for (int i = 0; i < 100; ++i) {
            pageUp.append(Keys.PAGE_UP);
        }
        action.sendKeys(pageUp).perform();
    }
}
