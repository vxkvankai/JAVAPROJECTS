package com.d3.support;

import static com.d3.helpers.WebdriverHelper.isMobile;

import com.d3.exceptions.D3FactoryException;
import com.d3.pages.consumer.login.LoginPage;
import com.d3.support.internal.ElementDecorator;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ElementFactory extends PageFactory {

    public static <T> T initElements(WebDriver driver, Class<T> pageClassToProxy) {
        try {
            T page = pageClassToProxy.getConstructor(WebDriver.class).newInstance(driver);
            if (pageClassToProxy.getName().equals(LoginPage.class.getName())) { // NOSONAR (pageClassToProxy is Class type, not LoginPage)
                if (isMobile()) {
                    PageFactory.initElements(new AppiumFieldDecorator(driver), page);
                } else {
                    PageFactory.initElements(new DefaultFieldDecorator(new DefaultElementLocatorFactory(driver)), page);
                }
            } else {
                PageFactory.initElements(new ElementDecorator(new DefaultElementLocatorFactory(driver)), page);
            }
            return page;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new D3FactoryException(e);
        } catch (NoSuchMethodException e) {
            log.error("Does the page object have a public constructor?");
            throw new D3FactoryException(e);
        }
    }
}
