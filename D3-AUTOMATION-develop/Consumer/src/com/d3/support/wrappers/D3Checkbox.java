package com.d3.support.wrappers;

import com.d3.support.D3Element;
import com.d3.support.wrappers.base.CheckBox;
import org.openqa.selenium.WebElement;

public class D3Checkbox extends D3Element implements CheckBox {

    public D3Checkbox(WebElement element) {
        super(element);
    }

    @Override
    public CheckBox toggle() {
        click();
        return this;
    }

    @Override
    public CheckBox check() {
        if (!isChecked()) {
            toggle();
        }
        return this;
    }

    @Override
    public CheckBox uncheck() {
        if (isChecked()) {
            toggle();
        }
        return this;
    }

    @Override
    public boolean isChecked() {
        return getWrappedElement().isSelected();
    }
}
