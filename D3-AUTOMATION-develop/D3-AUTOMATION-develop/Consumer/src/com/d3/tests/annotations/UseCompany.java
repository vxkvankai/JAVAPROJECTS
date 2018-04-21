package com.d3.tests.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UseCompany {

    String companyId() default "fi1";
    String toggleFi() default "fi2";


}
