package com.d3.pages.consumer.settings.dashboard;

import com.d3.pages.consumer.BaseConsumerPage;
import org.openqa.selenium.WebDriver;


public class Dashboard extends BaseConsumerPage {

    public Dashboard(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Dashboard me() {
        return this;
    }


}
