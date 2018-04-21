package com.d3.tests.annotations;

import com.d3.datawrappers.user.enums.AccountPermissions;
import com.d3.datawrappers.user.enums.UserServices;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WithSpecificServiceAndPermissions {

    UserServices[] withUserServices() default {};
    UserServices[] withoutUserServices() default {};
    AccountPermissions[] withAccountPermissions() default {};
    AccountPermissions[] withoutAccountPermissions() default {};


}
