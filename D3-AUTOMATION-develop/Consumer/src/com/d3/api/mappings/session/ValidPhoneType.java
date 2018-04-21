
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidPhoneType {

    @SerializedName("interfaceName")
    @Expose
    private String interfaceName;
    @SerializedName("internalName")
    @Expose
    private String internalName;
    @SerializedName("repeatable")
    @Expose
    private Boolean repeatable;
    @SerializedName("mobileType")
    @Expose
    private Boolean mobileType;
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

    public Boolean getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Boolean repeatable) {
        this.repeatable = repeatable;
    }

    public Boolean getMobileType() {
        return mobileType;
    }

    public void setMobileType(Boolean mobileType) {
        this.mobileType = mobileType;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

}
