package com.d3.datawrappers.user;

import static com.d3.helpers.AccountHelper.generatePassword;
import static com.d3.helpers.RandomHelper.getRandomNumberString;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class D3SecondaryUser {

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public D3Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public D3Contact getHomePhone() {
        return homePhone;
    }

    public D3Contact getMobilePhone() {
        return mobilePhone;
    }

    public D3Contact getWorkPhone() {
        return workPhone;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        if (middleName == null || middleName.isEmpty()) {
            return String.format("%s %s", firstName, lastName);
        } else {
            return String.format("%s %s %s", firstName, middleName, lastName);
        }
    }

    public Map<String, String> getEnabledServices() {
        return enabledServices;
    }

    String firstName;
    String middleName;
    String lastName;
    D3Address address;
    String email;
    D3Contact homePhone;
    D3Contact mobilePhone;
    D3Contact workPhone;
    String loginId;
    String password;
    private Map<String, String> enabledServices;

    public D3SecondaryUser() {
        Person person = Fairy.create().person();
        firstName = person.getFirstName();
        middleName = person.getMiddleName();
        lastName = person.getLastName();
        address = new D3Address(person.getAddress());
        email = person.getEmail();
        homePhone = new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.HOME, getRandomNumberString(10, true));
        mobilePhone = new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.MOBILE, getRandomNumberString(10, true));
        workPhone = new D3Contact(D3Contact.ContactType.PHONE, D3Contact.ContactLabel.WORK, getRandomNumberString(10, true));
        loginId = StringUtils.deleteWhitespace(firstName.toLowerCase());
        password = generatePassword();
        enabledServices = new HashMap<>();
        // TODO do this a little better/random, should probably make it an enums too
        enabledServices.put("Goals", "View Only");
    }
}
