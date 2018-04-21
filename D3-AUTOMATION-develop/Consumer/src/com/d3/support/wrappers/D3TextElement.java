package com.d3.support.wrappers;

import com.d3.support.D3Element;
import com.d3.support.wrappers.base.TextElement;
import org.openqa.selenium.WebElement;

public class D3TextElement extends D3Element implements TextElement {

    public D3TextElement(WebElement element) {
        super(element);
    }

    @Override
    public boolean isPresent() {
        return isDisplayed();
    }

}
