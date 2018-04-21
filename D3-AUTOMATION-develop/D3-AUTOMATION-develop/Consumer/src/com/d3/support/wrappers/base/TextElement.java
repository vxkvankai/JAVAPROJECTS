package com.d3.support.wrappers.base;

import com.d3.support.internal.Element;
import com.d3.support.internal.ImplementedBy;
import com.d3.support.wrappers.D3TextElement;

@ImplementedBy(D3TextElement.class)
public interface TextElement extends Element {
    boolean isPresent();
}
