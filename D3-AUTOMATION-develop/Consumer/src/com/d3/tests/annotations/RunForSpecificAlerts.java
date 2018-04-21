package com.d3.tests.annotations;

import com.d3.datawrappers.alerts.D3Alert;
import com.d3.pages.consumer.settings.alerts.consumer.base.ConsumerAlerts;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RunForSpecificAlerts {

    Class[] d3AlertType() default {};
}
