package com.d3.datawrappers.account;

import com.d3.datawrappers.common.AttributeType;
import com.d3.datawrappers.common.D3Attribute;

import java.io.Serializable;

public class D3AccountAttribute implements D3Attribute, Serializable {
    private String name;
    private AttributeType attributeType;
    private Boolean visible;
    private Integer displayOrder;
    private String value;

    public D3AccountAttribute(String name, AttributeType attributeType, Boolean visible, Integer displayOrder, String value) {
        this.name = name;
        this.attributeType = attributeType;
        this.visible = visible;
        this.displayOrder = displayOrder;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAttributeType() {
        return  attributeType == null ? null : attributeType.getConduitCode();
    }

    @Override
    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public Boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    @Override
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
