package com.d3.tests.annotations;

import com.d3.datawrappers.company.CompanyAttribute;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Repeatable(RunWithCompanyAttributes.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunWithCompanyAttribute {

    CompanyAttribute attribute();

    boolean enabled() default true;

    String value() default "";

    boolean resetAfterTest() default false;

}
