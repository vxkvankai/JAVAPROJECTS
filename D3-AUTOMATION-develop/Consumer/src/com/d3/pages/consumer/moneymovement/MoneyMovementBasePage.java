package com.d3.pages.consumer.moneymovement;

import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.headers.MoneyMovementTabs;
import org.openqa.selenium.WebDriver;

public abstract class MoneyMovementBasePage extends BaseConsumerPage {
    private MoneyMovementTabs tabs;

    public MoneyMovementBasePage(WebDriver driver) {
        super(driver);
        tabs = MoneyMovementTabs.initialize(driver, MoneyMovementTabs.class);
    }

    public MoneyMovementTabs getTabs() {
        return tabs;
    }
}
