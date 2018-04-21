package com.d3.pages.asserts.login;

import com.d3.pages.consumer.AssertionsBase;
import com.d3.pages.consumer.login.LoginPage;

public class LoginAssert extends AssertionsBase<LoginAssert, LoginPage> {
    private static final String LOGIN = "#login";
    private static final String LOGOUT = "#logout";

    public LoginAssert(LoginPage loginPage) {
        super(loginPage, LoginAssert.class);
    }

    @Override
    public LoginAssert atPage() {
        if (!getDriver().getCurrentUrl().contains(LOGIN) && !getDriver().getCurrentUrl().contains(LOGOUT)) {
            failWithMessage("User was not on the login page. Expected URL to contain <%s> or <%s>, but was on <%s>", LOGIN, LOGOUT, getDriver().getCurrentUrl());
        }
        return myself;
    }
}
