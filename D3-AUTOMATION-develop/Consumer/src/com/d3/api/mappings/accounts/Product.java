package com.d3.api.mappings.accounts;

import com.d3.datawrappers.account.ProductType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("accountingClass")
    @Expose
    private String accountingClass;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountingClass() {
        return accountingClass;
    }

    public void setAccountingClass(String accountingClass) {
        this.accountingClass = accountingClass;
    }

    public Product offlineAccount(ProductType productType) {
        setAccountingClass(productType.toString());
        setId(productType.getAccountProductId());
        setName(productType.getName());
        return this;
    }

}
