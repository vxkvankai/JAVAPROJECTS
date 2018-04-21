package com.d3.api.mappings.planning;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Budget {

    private Integer id;
    private String name;
    private String type;
    private String periodType;
    private String periodStart;
    private String periodEnd;
    private Double budgetExpense;
    private Double actualExpense;
    private Double expenseDifference;
    private Double budgetIncome;
    private Double actualIncome;
    private Double incomeDifference;
    private List<BudgetCategory> budgetCategories;
}
