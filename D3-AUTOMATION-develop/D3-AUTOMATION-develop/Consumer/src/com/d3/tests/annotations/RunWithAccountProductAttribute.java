package com.d3.tests.annotations;

import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.account.enums.AccountProductAttributes;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Repeatable(RunWithAccountProductAttributes.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunWithAccountProductAttribute {


    ProductType accountProduct();

    AccountProductAttributes attribute();

    boolean enabled() default true;

    String value() default "";

    boolean resetAfterTest() default false;


}
