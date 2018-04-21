
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingsProfile {

    @SerializedName("access")
    @Expose
    private List<String> access = null;

    public List<String> getAccess() {
        return access;
    }

    public void setAccess(List<String> access) {
        this.access = access;
    }

}
