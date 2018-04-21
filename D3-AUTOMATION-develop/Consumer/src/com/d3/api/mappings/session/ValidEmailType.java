
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidEmailType {

    @SerializedName("interfaceName")
    @Expose
    private String interfaceName;
    @SerializedName("internalName")
    @Expose
    private String internalName;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("repeatable")
    @Expose
    private Boolean repeatable;
    @SerializedName("destinationType")
    @Expose
    private String destinationType;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Boolean repeatable) {
        this.repeatable = repeatable;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

}
