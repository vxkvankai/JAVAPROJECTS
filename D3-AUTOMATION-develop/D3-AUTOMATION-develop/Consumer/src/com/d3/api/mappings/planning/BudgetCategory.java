package com.d3.api.mappings.planning;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetCategory {
    private Integer id;
    private Category category;
    private Double actualAmount;
    private Double budgetAmount;
    private Double difference;
}
