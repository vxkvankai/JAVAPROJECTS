package com.d3.pages.consumer.messages;


import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.headers.MessagesTabs;
import org.openqa.selenium.WebDriver;

public abstract class MessagesBasePage extends BaseConsumerPage {

    private MessagesTabs tabs;

    public MessagesBasePage(WebDriver driver) {
        super(driver);
        tabs = MessagesTabs.initialize(driver, MessagesTabs.class);
    }

    public MessagesTabs getTabs() {
        return tabs;
    }
}

