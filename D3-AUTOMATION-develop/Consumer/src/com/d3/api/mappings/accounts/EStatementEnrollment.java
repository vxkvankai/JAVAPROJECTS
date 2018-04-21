package com.d3.api.mappings.accounts;

import com.d3.datawrappers.user.D3Contact;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class EStatementEnrollment {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userAccountId")
    @Expose
    private Integer userAccountId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public static List<EStatementEnrollment> enrollment(D3User user, Integer userAccountId) {
        EStatementEnrollment enrollSingleAccount = new EStatementEnrollment();
        enrollSingleAccount.setUserAccountId(userAccountId);
        enrollSingleAccount.setType("BOTH");
        D3Contact emailContact = user.getAnEmailContact();
        if (emailContact != null) {
            enrollSingleAccount.setEmail(emailContact.getValue());
        }
        return Collections.singletonList(enrollSingleAccount);
    }

}
