package com.d3.pages.consumer.moneymovement;

import org.openqa.selenium.WebDriver;

public class PopMoneyPage extends MoneyMovementBasePage {

    public PopMoneyPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PopMoneyPage me() {
        return this;
    }
}
