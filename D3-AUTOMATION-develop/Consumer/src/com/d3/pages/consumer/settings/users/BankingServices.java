package com.d3.pages.consumer.settings.users;

import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;


public class BankingServices extends PageObjectBase {

    @FindBy(css = "button.btn-submit")
    private Element submitButton;

    @FindBy(xpath = "//select[@name='accessValue']")
    private List<Element> bankingService;

    public BankingServices(WebDriver driver) {
        super(driver);
    }

    @Override
    protected BankingServices me() {
        return this;
    }

    public UsersPage clickSubmitButton() {
        submitButton.click();
        waitForSpinner();
        return UsersPage.initialize(driver, UsersPage.class);
    }

    public BankingServices enableBankingServices(Map<String, String> servicesToEnable) {
        for (Element service : bankingService) {
            String serviceName = service.findElement(By.xpath("./preceding-sibling::*[1]")).getText();
            if (servicesToEnable.containsKey(serviceName)) {
                Select selectDropdown = new Select(service);
                selectDropdown.selectByVisibleText(servicesToEnable.get(serviceName));
            }
        }
        return this;
    }

    public BankingServices disableBankingServicesExcluding(Map<String, String> servicesToExcludeFromDisable) {
        for (Element service : bankingService) {
            String serviceName = service.findElement(By.xpath("./preceding-sibling::*[1]")).getText();
                if (!servicesToExcludeFromDisable.containsKey(serviceName)) {
                    Select selectDropdown = new Select(service);
                    selectDropdown.selectByIndex(0);
                }
            }

        return this;

    }
}
