package com.d3.support;

import io.qameta.allure.TmsLink;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class D3TmsLink implements TmsLink {

    private String tmsId;

    public D3TmsLink(String tmsId) {
        this.tmsId = tmsId;
    }

    @Override
    public String value() {
        return tmsId;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return TmsLink.class;
    }
}
