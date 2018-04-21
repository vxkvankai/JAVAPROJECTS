package com.d3.pages.consumer.accounts.stoppayment.base;

import com.d3.datawrappers.account.stoppayment.D3StopPayment;
import com.d3.datawrappers.account.stoppayment.StopPaymentReason;
import com.d3.pages.consumer.accounts.MyAccountsSection;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public abstract class StopPaymentForm<T extends StopPaymentForm, P extends D3StopPayment> extends PageObjectBase {

    @Override
    protected abstract T me();

    @FindBy(id = "amount")
    private Element stopPaymentAmount;

    @FindBy(id = "payee")
    private Element stopPaymentPayee;

    @FindBy(id = "reason")
    private Select stopPaymentReason;

    @FindBy(css = "button.cancel")
    private Element stopPaymentCancelButton;

    @FindBy(css = "button.save")
    private Element stopPaymentSaveButton;

    @FindBy(css = "button.submit-one")
    private Element stopPaymentConfirmButton;

    public StopPaymentForm(WebDriver driver) {
        super(driver);
    }

    @Step("Enter {amount} as the amount")
    public T enterAmount(String amount) {
        stopPaymentAmount.sendKeys(amount);
        return me();
    }

    @Step("Enter {name} as the payee")
    public T enterPayee(String name) {
        stopPaymentPayee.sendKeys(name);
        return me();
    }

    @Step("Select {reason} as the reason")
    public T selectReason(StopPaymentReason reason) {
        stopPaymentReason.selectByText(reason.toString());
        return me();
    }

    @Step("Click the stop payment save button")
    public StopPaymentForm clickStopPaymentSaveButton() {
        stopPaymentSaveButton.click();
        return this;
    }

    @Step("Click the stop payment confirm button")
    public MyAccountsSection clickStopPaymentConfirmButton() {
        stopPaymentConfirmButton.click();
        return MyAccountsSection.initialize(driver, MyAccountsSection.class);
    }

    @Step("Fill out the stop payment form")
    public abstract StopPaymentForm fillOutForm(P stopPayment);

    @Step("Do common steps for filling out a stop payment form")
    protected T fillOutFormCommon(P stopPayment) {
        enterPayee(stopPayment.getPayee());
        selectReason(stopPayment.getReason());
        return me();
    }
}
