package com.d3.support;

import static com.d3.helpers.WebdriverHelper.getWebDriverWait;

import com.d3.helpers.WebdriverHelper;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class D3Element implements Element {

    private final WebElement element;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public D3Element(final WebElement element) {
        this.element = element;
    }

    @Override
    public void click() {
        scrollIntoView();
        waitUntilClickable();
        try {
            element.click();
        } catch (WebDriverException e) {
            logger.warn("Error clicking element, attempting again", e);
            element.click();
        }
    }

    @Override
    public void submit() {
        waitUntilVisible();
        element.submit();
    }

    /**
     * Clears then sends the keys to the element
     */
    @Override
    public void sendKeys(CharSequence... keysToSend) {
        sendKeys(true, true, true, keysToSend);
    }

    @Override
    public void sendKeys(boolean clearField, boolean waitUntilVisible, boolean addTabKey, CharSequence... keysToSend) {
        CharSequence keys;
        if (keysToSend.length > 1 && logger.isWarnEnabled()) {
            logger.warn("missing some keys, should fix the sendKeys method", Arrays.toString(keysToSend));
        }
        if (addTabKey) {
            keys = String.valueOf(keysToSend[0]) + WebdriverHelper.getTab();
        } else {
            keys = String.valueOf(keysToSend[0]);
        }

        if (waitUntilVisible) {
            waitUntilVisible();
        }
        if (clearField) {
            this.clear();
        } else {
            logger.info("Not clearing input field");
        }

        logger.info("Sending keys to element: {}", keys);
        element.sendKeys(keys);
        if (System.getProperty("browse").contains("IOS") || System.getProperty("browse").equalsIgnoreCase("safari")) {
            logger.info("firing event for safari/IOS after sending keys");
            JavascriptExecutor jse = (JavascriptExecutor) ((WrapsDriver) element).getWrappedDriver();
            jse.executeScript("var element=arguments[0];" +
                    "if ('createEvent' in document) {" +
                    "var evt = document.createEvent('HTMLEvents');" +
                    "evt.initEvent('change', true, true);" +
                    "element.dispatchEvent(evt);" +
                    "} else { " +
                    "element.fireEvent('onchange')" +
                    "};", element);
        }
    }

    @Override
    public Element waitUntilClickable() {
        Wait<WebDriver> wait = getWebDriverWait(getWrappedDriver()).ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.elementToBeClickable(this));
        return this;
    }

    @Override
    public Element waitUntilVisible() {
        Wait<WebDriver> wait = getWebDriverWait(getWrappedDriver()).ignoring(StaleElementReferenceException.class);
        wait.until(ExpectedConditions.visibilityOf(this));
        return this;
    }


    @Override
    public void clear() {
        this.clear(true);
    }

    @Override
    public void clear(boolean addBackspace) {
        waitUntilVisible();
        element.clear();
        if (addBackspace) {
            StringBuilder clearKeys = new StringBuilder();
            for (int i = 0; i < 50; ++i) {
                clearKeys.append(Keys.BACK_SPACE);
                clearKeys.append(Keys.DELETE);
            }
            element.sendKeys(clearKeys);
        }
    }

    @Override
    public String getTagName() {
        return element.getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return element.getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    @Override
    public String getText() {
        waitUntilVisible();
        return element.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    @Override
    public D3Element findElement(By by) {
        return new D3Element(element.findElement(by));
    }

    @Override
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return element.getLocation();
    }

    @Override
    public Dimension getSize() {
        return element.getSize();
    }

    @Override
    public Rectangle getRect() {
        return element.getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return element.getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
        return element.getScreenshotAs(target);
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) element).getCoordinates();
    }

    @Override
    public WebElement getWrappedElement() {
        return element;
    }

    @Override
    public String getValueAttribute() {
        return getAttribute("value");
    }

    private WebDriver getWrappedDriver() {
        return ((WrapsDriver) element).getWrappedDriver();
    }

    @Override
    public Element scrollIntoView() {
        ((JavascriptExecutor) getWrappedDriver()).executeScript("arguments[0].scrollIntoView(true);", this);
        waitUntilVisible();
        return this;
    }
}
