package com.d3.support.wrappers.base;

import com.d3.support.internal.Element;
import com.d3.support.internal.ImplementedBy;
import com.d3.support.wrappers.D3Checkbox;

@ImplementedBy(D3Checkbox.class)
public interface CheckBox extends Element {
    CheckBox toggle();
    CheckBox check();
    CheckBox uncheck();
    boolean isChecked();
}
