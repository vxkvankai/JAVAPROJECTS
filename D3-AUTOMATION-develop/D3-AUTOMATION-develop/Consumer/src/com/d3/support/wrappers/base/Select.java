package com.d3.support.wrappers.base;

import com.d3.support.internal.Element;
import com.d3.support.internal.ImplementedBy;
import com.d3.support.wrappers.D3Select;
import org.openqa.selenium.NoSuchElementException;

@ImplementedBy(D3Select.class)
public interface Select extends Element {

    /**
     * Select all options that display text matching the argument. That is, when given "Bar" this
     * would select an option like:
     *
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param text The visible text to match against
     * @throws NoSuchElementException If no matching option elements are found
     * @return The current select element (this)
     */
    Select selectByText(String text);

    /**
     * Select all options that have a value matching the argument. That is, when given "foo" this
     * would select an option like:
     *
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param value The value to match against
     * @throws NoSuchElementException If no matching option elements are found
     * @return The current select element (this)
     */
    Select selectByValue(String value);

    /**
     * Select the first option that has display text matching the argument. That is, when given Bar this
     * would select an option like:
     *
     * &lt;option value="foo"&gt;FooBar&lt;/option&gt;
     *
     * @param containsText The visible text that the option contains
     * @return The current select element (this)
     */
    Select selectByTextContains(String containsText);

    boolean optionEqualsText(String option);

    /**
    *Verify the text is present in the dropdown
    *
    *
    *@param textContains The visible text that the option contains
    *@return True if text is present or False if text is not present
    */
    boolean optionContainsText(String textContains);
}
