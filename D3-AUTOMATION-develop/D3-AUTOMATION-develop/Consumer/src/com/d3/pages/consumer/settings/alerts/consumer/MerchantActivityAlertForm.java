package com.d3.pages.consumer.settings.alerts.consumer;

import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertDetails;
import com.d3.pages.consumer.settings.alerts.consumer.base.AlertForm;
import com.d3.support.internal.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class MerchantActivityAlertForm extends AlertForm<MerchantActivityAlertForm, MerchantActivityAlert>
        implements AlertDetails<MerchantActivityAlert> {


    @FindBy(id = "merchantName")
    private Element merchantNameInput;

    public MerchantActivityAlertForm(WebDriver driver) {
        super(driver);

    }

    public MerchantActivityAlertForm enterMerchantName(String name) {
        merchantNameInput.sendKeys(name);
        return this;
    }

    @Override
    protected MerchantActivityAlertForm me() {
        return this;
    }

    @Override
    public AlertForm fillOutForm(MerchantActivityAlert alert) {
        enterMerchantName(alert.getMerchantName());
        return this;
    }

    @Override
    public boolean isAlertInformationCorrect(MerchantActivityAlert alert) {
        String errorMsg = "%s: %s for Merchant Activity Alert was not found on the DOM.";
        try {
            checkIfTextDisplayed(alert.getAlert().getDescription(), errorMsg, "Alert Description");
            checkIfTextDisplayed(alert.getMerchantName(), errorMsg, "Merchant Name");
        } catch (TextNotDisplayedException e) {
            log.warn("Merchant Activity Alert was not validated", e);
            return false;
        }

        return true;
    }
}
