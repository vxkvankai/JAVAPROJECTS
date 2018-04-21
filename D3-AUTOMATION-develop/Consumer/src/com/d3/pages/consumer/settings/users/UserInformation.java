package com.d3.pages.consumer.settings.users;

import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class UserInformation extends PageObjectBase {

    private String mobilePhoneValue;
    private String workPhoneValue;

    public String getStateValue() {
        return stateValue;
    }

    protected String stateValue;

    @FindBy(id = "firstName")
    private Element firstName;

    @FindBy(id = "middleName")
    private Element middleName;

    @FindBy(id = "lastName")
    private Element lastName;

    @FindBy(id = "line1")
    private Element physicalAddressLine1;

    @FindBy(name = "line2")
    private Element physicalAddressLine2;

    @FindBy(id = "city")
    private Element physicalAddressCity;

    @FindBy(id = "state")
    private Element physicalAddressState;

    @FindBy(id = "postalCode")
    private Element physicalAddressZipCode;

    @FindBy(id = "email")
    private Element email;

    @FindBy(id = "homePhone")
    private Element homePhone;

    @FindBy(id = "mobilePhone")
    private Element mobilePhone;

    @FindBy(id = "workPhone")
    private Element workPhone;

    @FindBy(id = "loginId")
    private Element loginId;

    @FindBy(id = "newPassword")
    private Element newPassword;

    @FindBy(id = "confirmNewPassword")
    private Element confirmNewPassword;

    @FindBy(css = "button.btn-next")
    private Element nextButton;

    public UserInformation(WebDriver driver) {
        super(driver);
    }

    @Override
    protected UserInformation me() {
        return this;
    }

    public UserInformation enterFirstName(String name) {
        firstName.sendKeys(name);
        return this;
    }

    public UserInformation enterMiddleName(String name) {
        middleName.sendKeys(name);
        return this;
    }

    public UserInformation enterLastName(String name) {
        lastName.sendKeys(name);
        return this;
    }

    public UserInformation enterAddressLine1(String address) {
        physicalAddressLine1.sendKeys(address);
        return this;
    }

    public UserInformation enterAddressLine2(String address) {
        physicalAddressLine2.sendKeys(address);
        return this;
    }

    public UserInformation enterEmail(String emailAddress) {
        email.sendKeys(emailAddress);
        return this;
    }

    public UserInformation enterHomePhone(String phone) {
        homePhone.sendKeys(phone);
        return this;
    }

    public UserInformation enterMobilePhone(String phone) {
        mobilePhone.sendKeys(phone);
        getMobilePhone();
        return this;
    }

    public UserInformation enterWorkPhone(String phone) {
        workPhone.sendKeys(phone);
        getWorkPhone();
        return this;
    }

    public UserInformation enterCity(String city) {
        physicalAddressCity.sendKeys(city);
        return this;
    }

    public UserInformation enterState(String state) {
        physicalAddressState.sendKeys(state);
        this.stateValue = physicalAddressState.getValueAttribute();
        return this;
    }

    public UserInformation enterZip(String zip) {
        physicalAddressZipCode.sendKeys(zip);
        return this;
    }

    public UserInformation enterLoginId(String login) {
        loginId.sendKeys(login);
        return this;
    }

    public UserInformation enterPassword(String password) {
        newPassword.sendKeys(password);
        return this;
    }

    public UserInformation confirmNewPassword(String password) {
        confirmNewPassword.sendKeys(password);
        return this;
    }

    public UserInformation enterSecondaryUserInfomation(D3SecondaryUser user) {
        enterFirstName(user.getFirstName())
                .enterMiddleName(user.getMiddleName())
                .enterLastName(user.getLastName())
                .enterAddressLine1(user.getAddress().getAddress1())
                .enterAddressLine2(user.getAddress().getAddress2())
                .enterEmail(user.getEmail())
                .enterHomePhone(user.getHomePhone().getValue())
                .enterMobilePhone(user.getMobilePhone().getValue())
                .enterWorkPhone(user.getWorkPhone().getValue())
                .enterCity(user.getAddress().getCity())
                .enterState(user.getAddress().getState())
                .enterZip(user.getAddress().getPostalCode())
                .enterLoginId(user.getLoginId())
                .enterPassword(user.getPassword())
                .confirmNewPassword(user.getPassword());
        return this;
    }

    public AccountServices continueToAccountServicesForm() {
        nextButton.click();
        return AccountServices.initialize(driver, AccountServices.class);
    }

    public void getMobilePhone() {
        this.mobilePhoneValue = mobilePhone.getValueAttribute();
    }

    public void getWorkPhone() {
        this.workPhoneValue = workPhone.getValueAttribute();
    }

    public String getMobilePhoneValue() {
        return mobilePhoneValue;
    }

    public String getWorkPhoneValue() {
        return workPhoneValue;
    }
}
