package com.d3.pages.consumer.transactions;

import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.support.PageObjectBase;
import org.openqa.selenium.WebDriver;

public abstract class TransactionsBasePage extends BaseConsumerPage {

    /**
     * Inits the driver and header, don't use the constructor manually, use {@link PageObjectBase#initialize(WebDriver, Class)}
     *
     * @param driver WebDriver object
     */
    public TransactionsBasePage(WebDriver driver) {
        super(driver);
    }
}
