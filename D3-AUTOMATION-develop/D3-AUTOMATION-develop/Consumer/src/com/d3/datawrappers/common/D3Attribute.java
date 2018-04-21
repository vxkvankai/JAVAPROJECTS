package com.d3.datawrappers.common;

import java.io.Serializable;

public interface D3Attribute extends Serializable {
    String getName();
    void setName(String name);
    String getAttributeType();
    void setAttributeType(AttributeType attributeType);
    Boolean isVisible();
    void setVisible(Boolean visible);
    Integer getDisplayOrder();
    void setDisplayOrder(Integer displayOrder);
    String getValue();
    void setValue(String value);
}
