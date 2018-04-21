package com.d3.pages.consumer.headers;

import com.d3.pages.consumer.selfservice.FAQs;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class SelfServiceTabs extends HeaderBase {

    @FindBy(xpath = "//a[@href='#self-service/faq']")
    private Element faqLink;

    @FindBy(xpath = "//a[@href='#self-service/request-forms']")
    private Element requestForms;

    public SelfServiceTabs(WebDriver driver) {
        super(driver);
    }

    @Override
    protected SelfServiceTabs me() {
        return this;
    }

    public FAQs clickFAQsLink() {
        faqLink.click();
        return FAQs.initialize(driver, FAQs.class);
    }
}
