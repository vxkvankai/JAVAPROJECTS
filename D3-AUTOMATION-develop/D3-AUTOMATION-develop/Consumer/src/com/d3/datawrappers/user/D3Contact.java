package com.d3.datawrappers.user;

import static com.d3.helpers.RandomHelper.getRandomString;

import java.io.Serializable;
import java.util.Optional;

public class D3Contact implements Serializable {
    public String getUid() {
        return uid;
    }

    public ContactType getType() {
        return type;
    }

    public String getTypeValue() {
        return type == null ? "" : type.toString().toLowerCase();
    }

    public void setType(ContactType type) {
        this.type = type;
    }


    public String getLabelValue() {
        return label == null ? "" : label.toString().toLowerCase();
    }

    public ContactLabel getLabel() {
        return label;
    }

    public void setLabel(ContactLabel label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getOutOfBandValue() {
        return Optional.ofNullable(outOfBand).orElse(false);
    }

    public void setOutOfBand(Boolean outOfBand) {
        this.outOfBand = outOfBand;
    }

    public boolean isPrimary() {
        return Optional.ofNullable(primary).orElse(false);
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public boolean getSms() {
        return Optional.ofNullable(sms).orElse(false);
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    /**
     *
     * @return Phone number in the following format : (###) ###-####
     */
    public String getFormattedPhoneNumber() {
        return String.format("(%s) %s-%s", value.substring(0, 3), value.substring(3,6), value.substring(6,10));
    }


    public enum ContactType {
        PHONE,
        EMAIL,
        INBOX
    }

    public enum ContactLabel {
        HOME,
        MOBILE,
        WORK,
        PRIMARY,
        ALTERNATE,
        ADDITIONAL,
        INBOX
    }

    private String uid; // Required
    private ContactType type; // Required
    private ContactLabel label; // Required
    private String value; // Required
    private Boolean outOfBand; // Optional
    private Boolean primary; // Optional
    private Boolean sms; // Optional

    public D3Contact(ContactType type, ContactLabel label, String value) {
        this.uid = getRandomString(10);
        this.type = type;
        this.label = label;
        this.value = value;
        if (label == ContactLabel.PRIMARY) {
            this.primary = true;
            if (type == ContactType.EMAIL) {
                this.outOfBand = true;
            }
        }
    }

}
