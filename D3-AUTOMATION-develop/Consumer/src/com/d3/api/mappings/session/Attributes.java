
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("preferredAcctId")
    @Expose
    private String preferredAcctId;
    @SerializedName("bpEnrollmentStatus")
    @Expose
    private String bpEnrollmentStatus;

    public String getPreferredAcctId() {
        return preferredAcctId;
    }

    public void setPreferredAcctId(String preferredAcctId) {
        this.preferredAcctId = preferredAcctId;
    }

    public String getBpEnrollmentStatus() {
        return bpEnrollmentStatus;
    }

    public void setBpEnrollmentStatus(String bpEnrollmentStatus) {
        this.bpEnrollmentStatus = bpEnrollmentStatus;
    }

}
