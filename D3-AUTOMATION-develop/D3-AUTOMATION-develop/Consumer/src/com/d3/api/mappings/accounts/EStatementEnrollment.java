package com.d3.api.mappings.accounts;

import com.d3.datawrappers.user.D3Contact;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EStatementEnrollment {

    private String type;
    private String email;
    private Integer userAccountId;

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
