package com.d3.api.mappings.accounts;

import com.d3.datawrappers.account.ProductType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private Integer id;
    private String name;
    private String accountingClass;

    public Product offlineAccount(ProductType productType) {
        setAccountingClass(productType.toString());
        setId(productType.getAccountProductId());
        setName(productType.getName());
        return this;
    }

}
