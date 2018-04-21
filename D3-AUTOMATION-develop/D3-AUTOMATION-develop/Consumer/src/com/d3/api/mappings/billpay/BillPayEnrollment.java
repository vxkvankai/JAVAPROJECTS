package com.d3.api.mappings.billpay;

import com.d3.datawrappers.user.D3Contact;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPayEnrollment {
    private Address address;
    private String transferMethod;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private String preferredEndpointId;

    public BillPayEnrollment(D3User user, int accountId) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        this.address = Address.addressFromD3Address(user.getProfile().getMailingAdd());
        if (this.address.getCountryCode() == null || this.address.getCountryCode().isEmpty()) {
            this.address.setCountryCode("US");
        }
        this.dateOfBirth = dtf.print(user.getProfile().getDateOfBirth());

        D3Contact emailContact = user.getAnEmailContact();
        if (emailContact != null) {
            this.email = emailContact.getValue();
        }

        D3Contact phoneContact = user.getAPhoneNumberContact();
        if (phoneContact != null) {
            this.phoneNumber = phoneContact.getValue();
        }
        this.transferMethod = "BILL_PAY";
        this.preferredEndpointId = "INTERNAL:" + Integer.toString(accountId);
    }
}
