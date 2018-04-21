package com.d3.support.wrappers;

import com.d3.support.D3Element;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;

import java.util.List;

public class D3Select extends D3Element implements Select {

    private org.openqa.selenium.support.ui.Select seleniumSelect;

    public D3Select(WebElement element) {
        super(element);
        seleniumSelect = new org.openqa.selenium.support.ui.Select(this);
    }

    @Override
    public Select selectByText(String text) {
        logger.info("Selecting option by full text: {}", text);
        seleniumSelect.selectByVisibleText(text);
        return this;
    }

    @Override
    public Select selectByValue(String value) {
        logger.info("Selecting option by value: {}", value);
        seleniumSelect.selectByValue(value);
        return this;
    }

    @Override
    public Select selectByTextContains(String textContains) {
        List<WebElement> candidates = this.findElements(By.xpath(String.format(".//option[contains(., %s)]", Quotes.escape(textContains))));
        logger.info("Selecting option by text contains: {}", textContains);
        if (candidates.isEmpty()) {
            logger.warn("Can't select option with text contains: {}, none found", textContains);
            return this;
        } else if (candidates.size() != 1) {
            logger.warn("Multiple options with the text: {}, selecting first one", textContains);
        }
        return selectByText(candidates.get(0).getText());
    }
    @Override
    public boolean optionEqualsText(String option) {
        List<WebElement> dropdown = seleniumSelect.getOptions();
        logger.info("Looking if dropdown contains option equal to {}", option);
        return dropdown.stream().peek(optionText -> logger.info("Found dropdown option {}", optionText.getText()))
                .anyMatch(optionText -> optionText.getText().equals(option));
    }
    
    @Override
    public boolean optionContainsText(String textContains) {
        List<WebElement> candidates = this.findElements(By.xpath(String.format(".//option[contains(., %s)]", Quotes.escape(textContains))));
        logger.info("Looking for option by text contains: {}", textContains);
        return !candidates.isEmpty();
     }
    
    
}
