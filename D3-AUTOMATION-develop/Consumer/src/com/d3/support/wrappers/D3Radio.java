package com.d3.support.wrappers;

import com.d3.support.D3Element;
import com.d3.support.wrappers.base.Radio;
import org.openqa.selenium.WebElement;

public class D3Radio extends D3Element implements Radio {

    public D3Radio(WebElement element) {
        super(element);
    }

    @Override
    public Radio select() {
        click();
        return this;
    }

    @Override
    public Radio check() {
        if (!isSelected()) {
            select();
        }
        return this;
    }

    @Override
    public boolean isSelected() {
        return getWrappedElement().isSelected();
    }
}
