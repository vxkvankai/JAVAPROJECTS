package com.d3.pages.consumer.planning;

import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.headers.PlanningTabs;
import org.openqa.selenium.WebDriver;

public abstract class PlanningBasePage extends BaseConsumerPage {
    private PlanningTabs tabs;

    public PlanningBasePage(WebDriver driver) {
        super(driver);
        tabs = PlanningTabs.initialize(driver, PlanningTabs.class);
    }

    public PlanningTabs getTabs() {
        return tabs;
    }

}
