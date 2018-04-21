package com.d3.datawrappers.company;

import com.d3.datawrappers.common.AttributeType;
import com.d3.datawrappers.common.D3Attribute;

public class D3CompanyAttribute implements D3Attribute {

    private String name;
    private AttributeType attributeType;
    private Boolean visible;
    private Integer displayOrder;
    private String value;

    public D3CompanyAttribute(String name, AttributeType attributeType, String value) {
        this.name = name;
        this.attributeType = attributeType;
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
        return attributeType == null ? null : attributeType.getConduitCode();
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
