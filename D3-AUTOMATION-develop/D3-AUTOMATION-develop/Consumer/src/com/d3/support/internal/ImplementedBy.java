package com.d3.support.internal;

import com.d3.support.D3Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplementedBy {
    Class<?> value() default D3Element.class;
}
