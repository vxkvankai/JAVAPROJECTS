package com.d3.datawrappers.user;

import com.d3.datawrappers.company.JFairyCompany;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class D3UserProfile implements Serializable {

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmployeeValue() {
        if (employee == null) {
            return "";
        }
        return employee ? "1" : "0";
    }

    public void setEmployee(boolean employee) {
        this.employee = employee;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTaxIdType() {
        return taxIdType != null ? taxIdType.getConduitCode() : "";
    }

    public void setTaxIdType(TaxIDType taxIdType) {
        this.taxIdType = taxIdType;
    }

    public String getMobileValue() {
        if (mobile == null) {
            return "";
        }
        return mobile ? "1" : "0";
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public String getEmailOptOutValue() {
        if (emailOptOut == null) {
            return "";
        }
        return emailOptOut ? "1" : "0";
    }

    public void setEmailOptOut(boolean emailOptOut) {
        this.emailOptOut = emailOptOut;
    }

    public DateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender != null ? gender.getConduitCode() : "";
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public Date getCreditScoreDate() {
        return creditScoreDate;
    }

    public void setCreditScoreDate(Date creditScoreDate) {
        this.creditScoreDate = creditScoreDate;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public D3Address getPhysicalAdd() {
        return physicalAdd;
    }

    public void setPhysicalAdd(D3Address physicalAdd) {
        this.physicalAdd = physicalAdd;
    }

    public D3Address getMailingAdd() {
        return mailingAdd;
    }

    public void setMailingAdd(D3Address mailingAdd) {
        this.mailingAdd = mailingAdd;
    }

    public enum ProfileType {
        CONSUMER("consumer"),
        BUSINESS("business");
        private final String conduitCode;

        ProfileType(String conduitCode) {
            this.conduitCode = conduitCode;
        }

        public String getConduitCode() {
            return this.conduitCode;
        }
    }

    public enum TaxIDType {
        INTERNATIONAL("i"),
        SSN("s"),
        TIN("t");

        private final String conduitCode;

        TaxIDType(String conduitCode) {
            this.conduitCode = conduitCode;
        }

        public String getConduitCode() {
            return this.conduitCode;
        }
    }

    public enum Gender {
        MALE("m"),
        FEMALE("f");

        private final String conduitCode;

        Gender(String conduitCode) {
            this.conduitCode = conduitCode;
        }

        public String getConduitCode() {
            return this.conduitCode;
        }
    }

    private ProfileType type; // Required
    private String firstName; // Optional (but really required)
    private String middleName; // Optional
    private String lastName; // Required if Profile type is consumer
    private String businessName; // Required if profile type is business
    private Boolean employee; // Optional (probably)
    private String taxId; // Optional
    private TaxIDType taxIdType; // Required if taxid present
    private Boolean mobile; // Optional
    private Boolean emailOptOut; // Optional
    private DateTime dateOfBirth; // Optional YYYY-MM-DD
    private Gender gender; // Optional
    private Integer creditScore; // Optional
    private Date creditScoreDate; // Optional
    private String userClass; // Optional
    private D3Address physicalAdd; // Optional
    private D3Address mailingAdd; // Optional

    public ProfileType getType() {
        return type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public D3UserProfile(String firstName, String lastName) {
        this.type = ProfileType.CONSUMER;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public D3UserProfile(Person person) {
        type = ProfileType.CONSUMER;
        firstName = person.getFirstName();
        lastName = person.getLastName();
        middleName = person.getMiddleName();
        if (person.getSex() == Person.Sex.MALE) {
            gender = Gender.MALE;
        } else {
            gender = Gender.FEMALE;
        }
        D3Address add = new D3Address(person.getAddress());
        physicalAdd = add;
        mailingAdd = add;
        dateOfBirth = person.getDateOfBirth();
        taxId = person.getNationalIdentificationNumber();
        taxIdType = TaxIDType.SSN;
    }

    public D3UserProfile(JFairyCompany company) {
        type = ProfileType.BUSINESS;
        businessName = company.getName();
        D3Address add = new D3Address(Fairy.create().person().getAddress());
        taxId = company.getVatIdentificationNumber().replace("-","");
        taxIdType = TaxIDType.TIN;
        physicalAdd = add;
        mailingAdd = add;
    }
}
