package com.d3.tests.annotations;

import com.d3.datawrappers.user.enums.UserType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface RunForUserTypes {

    UserType[] userTypes();
}
