package com.d3.datawrappers.recipient.base;

import com.d3.datawrappers.user.D3Address;

public abstract class BillAddressRecipient extends Recipient {

    protected String add1;
    protected String add2;
    protected String city;
    protected String state;
    protected String zipCode;
    protected String phoneNumberOriginal;
    protected String phoneNumber;
  

    public String getAdd1() {
        return add1;
    }

    public String getAdd2() {
        return add2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumberOriginal() {
        return phoneNumberOriginal;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumberOriginal = phoneNumber;
        this.phoneNumber = phoneNumber;
    }

    public BillAddressRecipient(String name) {
        super(name);
    }

    public BillAddressRecipient(String name, D3Address address, String phoneNumber, String nickname) {
        super(name);
        type = RecipientType.BILL_AND_ADDRESS;
        this.add1 = address.getAddress1();
        this.add2 = address.getAddress2();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getPostalCode();
        this.phoneNumberOriginal = phoneNumber;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
    }

    @Override
    public String getAuditTransferString() {
        return TransferMethod.BILL_PAY.toString();
    }
}
