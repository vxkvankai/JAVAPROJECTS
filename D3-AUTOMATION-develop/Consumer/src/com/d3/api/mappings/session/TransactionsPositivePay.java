
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionsPositivePay {

    @SerializedName("access")
    @Expose
    private List<Object> access = null;

    public List<Object> getAccess() {
        return access;
    }

    public void setAccess(List<Object> access) {
        this.access = access;
    }

}
