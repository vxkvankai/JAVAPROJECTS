package com.d3.api.mappings.users;

import com.d3.datawrappers.user.D3SecondaryUser;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondaryUsers {
    private Object mailingAddress;
    private PhysicalAddress physicalAddress;
    private List<EmailAddress> emailAddresses;
    private List<PhoneNumber> phoneNumbers;
    private String emailAddress;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private List<Object> approvalPermissions = null;
    private List<AccountGroup> accountGroups;
    private List<ServiceGroup> serviceGroups;
    private String loginId;
    private Integer id;
    private String password;

    public SecondaryUsers(D3SecondaryUser secondaryUser, AccountServiceGroups accountServiceGroups) {
        this.firstName = secondaryUser.getFirstName();
        this.lastName = secondaryUser.getLastName();
        if (secondaryUser.getMiddleName() != null && !secondaryUser.getMiddleName().isEmpty()) {
            this.middleName = secondaryUser.getMiddleName();
        }
        this.loginId = secondaryUser.getLogin();
        this.password = secondaryUser.getPassword();
        this.physicalAddress = new PhysicalAddress(secondaryUser.getAddress());
        this.mailingAddress = null;
        this.emailAddresses = new ArrayList<>();
        this.emailAddresses.add(new EmailAddress(secondaryUser));
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(new PhoneNumber(secondaryUser.getHomePhone()));
        this.phoneNumbers.add(new PhoneNumber(secondaryUser.getMobilePhone()));
        this.phoneNumbers.add(new PhoneNumber(secondaryUser.getWorkPhone()));
        this.accountGroups = new ArrayList<>();
        this.accountGroups.add(accountServiceGroups.setAccountAccessAndLimits(secondaryUser));
        this.serviceGroups = accountServiceGroups.setServiceAccessLevel(secondaryUser);
    }

}
