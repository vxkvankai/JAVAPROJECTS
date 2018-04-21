package com.d3.datawrappers;

import java.util.Arrays;
import java.util.List;

public enum CategoryType {
    EXPENSE_DISCRETIONARY,
    EXPENSE_UNCATEGORIZED,
    INCOME,
    INCOME_UNCATEGORIZED,
    SAVINGS,
    TRANSFER;


    public static List<CategoryType> getExpenseCategoryTypes() {
        return Arrays.asList(EXPENSE_DISCRETIONARY, EXPENSE_UNCATEGORIZED);
    }

    public static List<CategoryType> getIncomeCategoryTypes() {
        return Arrays.asList(INCOME, INCOME_UNCATEGORIZED);
    }
}
