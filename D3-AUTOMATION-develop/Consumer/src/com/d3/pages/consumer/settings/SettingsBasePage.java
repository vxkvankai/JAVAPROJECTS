package com.d3.pages.consumer.settings;

import com.d3.pages.consumer.BaseConsumerPage;
import com.d3.pages.consumer.headers.SettingsTabs;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public abstract class SettingsBasePage extends BaseConsumerPage {
    private SettingsTabs tabs;
    protected static Random random = new Random();

    public SettingsBasePage(WebDriver driver) {
        super(driver);
        tabs = SettingsTabs.initialize(driver, SettingsTabs.class);
    }

    public SettingsTabs getTabs() {
        return tabs;
    }
}
