package com.d3.tests.annotations;

import com.d3.datawrappers.common.D3ScheduledJobs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RunForSpecificAlerts {

    Class[] d3AlertType() default {};
    D3ScheduledJobs[] d3AlertTrigger() default {};
}
