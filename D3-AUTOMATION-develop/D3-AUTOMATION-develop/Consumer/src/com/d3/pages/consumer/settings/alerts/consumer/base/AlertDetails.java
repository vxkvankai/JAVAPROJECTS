package com.d3.pages.consumer.settings.alerts.consumer.base;


import com.d3.datawrappers.alerts.D3Alert;

public interface AlertDetails<A extends D3Alert> {

    boolean isAlertInformationCorrect(A alert);
}
