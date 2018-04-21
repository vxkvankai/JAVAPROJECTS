package com.d3.support.wrappers.base;

import com.d3.support.internal.Element;
import com.d3.support.internal.ImplementedBy;
import com.d3.support.wrappers.D3Radio;

@ImplementedBy(D3Radio.class)
public interface Radio extends Element {
    Radio select();
    Radio check();
    boolean isSelected();
}
