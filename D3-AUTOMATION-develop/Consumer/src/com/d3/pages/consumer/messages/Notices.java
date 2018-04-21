package com.d3.pages.consumer.messages;

import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.messages.NoticesL10N;
import com.d3.pages.consumer.messages.base.MessageSection;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class Notices extends MessageSection<Notices> {

    @FindBy(css = "li.empty-results")
    private Element emptyResults;

    public Notices(WebDriver driver) {
        super(driver);
    }

    private D3Element messageFilter(String messageType) {
        By by = By.cssSelector(String.format("a[data-display-group='%s']", messageType));
        return new D3Element(driver.findElement(by));
    }

    @Override
    protected Notices me() {
        return this;
    }

    public boolean userHasNoMessages() {
        try {
            checkIfTextEquals(emptyResults.getText(), String.format(NoticesL10N.Localization.EMPTY.getValue(), "Settings"));
            return true;
        } catch (TextNotDisplayedException e) {
            logger.warn("User has no messages text not displayed", e);
            return false;
        }
    }

    public Notices filterMessagesByType(String type) {
        messageFilter(type).click();
        return this;
    }

}
