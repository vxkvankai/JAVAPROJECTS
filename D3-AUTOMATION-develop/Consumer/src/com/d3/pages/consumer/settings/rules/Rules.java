package com.d3.pages.consumer.settings.rules;

import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class Rules extends PageObjectBase {

    @FindBy(css = "button.btn-delete-rule")
    private Element deleteRuleButton;

    @FindBy(css = "button.btn-delete-cancel")
    private Element deleteRuleCancelButton;

    @FindBy(css = "button.btn-delete-confirm")
    private Element deleteRuleConfirmButton;

    public Rules(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Rules me() {
        return this;
    }

    public Rules clickDeleteRuleButton() {
        deleteRuleButton.click();
        return this;
    }

    public Rules clickDeleteRuleCancelButton() {
        deleteRuleCancelButton.click();
        return this;
    }

    public Rules clickDeleteRuleConfirmButton() {
        deleteRuleConfirmButton.click();
        return this;
    }
}
