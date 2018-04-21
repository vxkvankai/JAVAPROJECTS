package com.d3.api.mappings.billpay;

import com.d3.datawrappers.user.D3Contact;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BillPayEnrollment {
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("transferMethod")
    @Expose
    private String transferMethod;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("preferredEndpointId")
    @Expose
    private String preferredEndpointId;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTransferMethod() {
        return transferMethod;
    }

    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPreferredEndpointId() {
        return preferredEndpointId;
    }

    public void setPreferredEndpointId(String preferredEndpointId) {
        this.preferredEndpointId = preferredEndpointId;
    }

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
