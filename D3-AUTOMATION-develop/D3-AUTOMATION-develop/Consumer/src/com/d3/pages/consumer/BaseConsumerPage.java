package com.d3.pages.consumer;

import com.d3.pages.consumer.headers.Header;
import com.d3.support.PageObjectBase;
import org.openqa.selenium.WebDriver;


public abstract class BaseConsumerPage extends PageObjectBase {
    private Header header;

    /**
     * Inits the driver and header, don't use the constructor manually, use {@link com.d3.support.PageObjectBase#initialize(WebDriver, Class)}
     *
     * @param driver WebDriver object
     */
    public BaseConsumerPage(WebDriver driver) {
        super(driver);
        this.header = Header.initialize(driver, Header.class);
    }

    public Header getHeader() {
        return header;
    }
}
