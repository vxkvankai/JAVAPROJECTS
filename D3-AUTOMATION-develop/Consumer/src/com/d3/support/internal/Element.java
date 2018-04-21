package com.d3.support.internal;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

@ImplementedBy
public interface Element extends WebElement, WrapsElement, Locatable {

    /**
     * Send Keys to the element
     *
     * @param clearField Set to true to clear the field first, false to keep the current text and append
     * @param waitUntilVisible Set to true to wait for the field to be visible before attempting to send keys to it
     * @param addTabKey Set to true to append a tab key to the end of the input (helpful for forcing an onchange event)
     * @param keysToSend Actual keys to send to the element
     */
    void sendKeys(boolean clearField, boolean waitUntilVisible, boolean addTabKey, CharSequence... keysToSend);

    Element waitUntilClickable();

    Element waitUntilVisible();

    String getValueAttribute();

    Element scrollIntoView();

    void clear(boolean addBackspace);
}
