package com.d3.support.wrappers;

import com.d3.support.D3Element;
import com.d3.support.wrappers.base.Select;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;

import java.util.List;

@Slf4j
public class D3Select extends D3Element implements Select {

    private org.openqa.selenium.support.ui.Select seleniumSelect;

    public D3Select(WebElement element) {
        super(element);
        seleniumSelect = new org.openqa.selenium.support.ui.Select(this);
    }

    @Override
    public Select selectByText(String text) {
        log.info("Selecting option by full text: {}", text);
        seleniumSelect.selectByVisibleText(text);
        return this;
    }

    @Override
    public Select selectByValue(String value) {
        log.info("Selecting option by value: {}", value);
        seleniumSelect.selectByValue(value);
        return this;
    }

    @Override
    public Select selectByTextContains(String textContains) {
        List<WebElement> candidates = this.findElements(By.xpath(String.format(".//option[contains(., %s)]", Quotes.escape(textContains))));
        log.info("Selecting option by text contains: {}", textContains);
        if (candidates.isEmpty()) {
            log.warn("Can't select option with text contains: {}, none found", textContains);
            return this;
        } else if (candidates.size() != 1) {
            log.warn("Multiple options with the text: {}, selecting first one", textContains);
        }
        return selectByText(candidates.get(0).getText());
    }

    @Override
    public boolean optionEqualsText(String option) {
        List<WebElement> dropdown = seleniumSelect.getOptions();
        log.info("Looking if dropdown contains option equal to {}", option);
        return dropdown.stream().peek(optionText -> log.info("Found dropdown option {}", optionText.getText()))
            .anyMatch(optionText -> optionText.getText().equals(option));
    }

    @Override
    public boolean optionContainsText(String textContains) {
        List<WebElement> candidates = this.findElements(By.xpath(String.format(".//option[contains(., %s)]", Quotes.escape(textContains))));
        log.info("Looking for option by text contains: {}", textContains);
        return !candidates.isEmpty();
    }


}
