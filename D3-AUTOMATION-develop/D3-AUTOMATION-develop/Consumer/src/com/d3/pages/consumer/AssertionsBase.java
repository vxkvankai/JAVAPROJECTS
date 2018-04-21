package com.d3.pages.consumer;

import static org.assertj.core.util.introspection.FieldSupport.extraction;

import com.d3.pages.asserts.dashboard.DashboardAssert;
import com.d3.pages.asserts.dashboard.DashboardPlanAssert;
import com.d3.pages.asserts.login.LoginAssert;
import com.d3.pages.asserts.login.TermsOfServiceAssert;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import com.d3.pages.consumer.login.LoginPage;
import com.d3.pages.consumer.termsofservice.TermsOfService;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.assertj.core.api.AbstractObjectAssert;
import org.openqa.selenium.WebDriver;

import java.util.List;


/**
 * An abstract class providing entry point for all of our custom page assertions
 * NOTE: import com.d3.pages.consumer.AssertionsBase.assertThat; in test classes to be able to access asserts
 */
public abstract class AssertionsBase<S extends AssertionsBase<S, A>, A extends PageObjectBase> extends AbstractObjectAssert<S, A> {

    public AssertionsBase(A a, Class<?> selfType) {
        super(a, selfType);
    }

    public static DashboardAssert assertThat(Dashboard actual) {
        return new DashboardAssert(actual);
    }

    public static DashboardPlanAssert assertThat(DashboardPlan actual) {
        return new DashboardPlanAssert(actual);
    }

    public static LoginAssert assertThat(LoginPage actual) {
        return new LoginAssert(actual);
    }

    public static TermsOfServiceAssert assertThat(TermsOfService actual) {
        return new TermsOfServiceAssert(actual);
    }

    /**
     * Abstract method to check if user is on correct page
     * NOTE: Use checkPage method from Assert classes
     *
     * @return Assert Page object
     */
    public abstract S atPage();

    /**
     * Checks the current url contains the given url
     *
     * @param url URL the user should be on
     * @return Assert Page object if true, else fail with the given message
     */
    protected S checkPageUrl(String url) {
        if (!getDriver().getCurrentUrl().contains(url)) {
            failWithMessage("User was not on the correct page. Expected URL to contain <%s>, but was on <%s>", url, getDriver().getCurrentUrl());
        }
        return myself;
    }

    public S displaysText(String text) {
        if (!actual.isTextDisplayed(text)) {
            failWithMessage("Expected text <%s> to be displayed, but it was not found on the DOM", text);
        }
        return myself;
    }

    public S doesNotDisplayText(String text) {
        if (actual.isTextDisplayed(text)) {
            failWithMessage("Was not expecting text <%s> to be displayed, but currently displayed", text);
        }
        return myself;
    }

    /**
     * Return Element from page object class with the given locatorName
     *
     * @param locatorName page object mapping locator name
     * @return Element
     */
    protected Element getElement(String locatorName) {
        return extraction().fieldValue(locatorName, Element.class, actual);
    }

    /**
     * Return List<Element> from page object class with the given locatorName
     *
     * @param locatorName page object mapping locator name
     * @return List<Element>
     */
    protected List<Element> getListElement(String locatorName) {
        return extraction().fieldValue(locatorName, List.class, actual);
    }

    protected WebDriver getDriver() {
        return extraction().fieldValue("driver", WebDriver.class, actual);
    }

    protected S elementDisplays(String locatorName, String errorMessage) {
        if (!actual.isElementDisplayed(getElement(locatorName))) {
            failWithMessage(errorMessage);
        }
        return myself;
    }

    protected S elementNotDisplayed(String locatorName, String errorMessage) {
        if (actual.isElementDisplayed(getElement(locatorName))) {
            failWithMessage(errorMessage);
        }
        return myself;
    }

}
