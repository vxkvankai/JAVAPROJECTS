
package com.d3.api.mappings.session;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {

    @SerializedName("loginId")
    @Expose
    private String loginId;
    @SerializedName("enrolled")
    @Expose
    private Boolean enrolled;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("taxIdExists")
    @Expose
    private Boolean taxIdExists;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("emailAddresses")
    @Expose
    private List<EmailAddress> emailAddresses = null;
    @SerializedName("phoneNumbers")
    @Expose
    private List<Object> phoneNumbers = null;
    @SerializedName("inbox")
    @Expose
    private Inbox inbox;
    @SerializedName("push")
    @Expose
    private Push push;
    @SerializedName("physicalAddress")
    @Expose
    private PhysicalAddress physicalAddress;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("validEmailTypes")
    @Expose
    private List<ValidEmailType> validEmailTypes = null;
    @SerializedName("validPhoneTypes")
    @Expose
    private List<ValidPhoneType> validPhoneTypes = null;
    @SerializedName("validProfileModes")
    @Expose
    private List<String> validProfileModes = null;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("servicePermissions")
    @Expose
    private ServicePermissions servicePermissions;
    @SerializedName("profileType")
    @Expose
    private String profileType;
    @SerializedName("previousLogin")
    @Expose
    private String previousLogin;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Boolean getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Boolean enrolled) {
        this.enrolled = enrolled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getTaxIdExists() {
        return taxIdExists;
    }

    public void setTaxIdExists(Boolean taxIdExists) {
        this.taxIdExists = taxIdExists;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public List<Object> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<Object> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public void setInbox(Inbox inbox) {
        this.inbox = inbox;
    }

    public Push getPush() {
        return push;
    }

    public void setPush(Push push) {
        this.push = push;
    }

    public PhysicalAddress getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(PhysicalAddress physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public List<ValidEmailType> getValidEmailTypes() {
        return validEmailTypes;
    }

    public void setValidEmailTypes(List<ValidEmailType> validEmailTypes) {
        this.validEmailTypes = validEmailTypes;
    }

    public List<ValidPhoneType> getValidPhoneTypes() {
        return validPhoneTypes;
    }

    public void setValidPhoneTypes(List<ValidPhoneType> validPhoneTypes) {
        this.validPhoneTypes = validPhoneTypes;
    }

    public List<String> getValidProfileModes() {
        return validProfileModes;
    }

    public void setValidProfileModes(List<String> validProfileModes) {
        this.validProfileModes = validProfileModes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ServicePermissions getServicePermissions() {
        return servicePermissions;
    }

    public void setServicePermissions(ServicePermissions servicePermissions) {
        this.servicePermissions = servicePermissions;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getPreviousLogin() {
        return previousLogin;
    }

    public void setPreviousLogin(String previousLogin) {
        this.previousLogin = previousLogin;
    }

}
