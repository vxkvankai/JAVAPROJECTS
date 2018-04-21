package com.d3.datawrappers.user.enums;

public enum UserType {

    PRIMARY_CONSUMER_USER(UserType.CONSUMER),
    PRIMARY_BUSINESS_USER(UserType.BUSINESS),
    PRIMARY_CONSUMER_TOGGLE(UserType.CONSUMER),
    PRIMARY_BUSINESS_TOGGLE(UserType.BUSINESS),
    COMMINGLED(UserType.CONSUMER);

    private String profileType;

    UserType(String profileType) {
        this.profileType = profileType;
    }

    public String getProfileType() {
        return profileType;
    }

    /**
     * Returns true if the user type is a business type, false otherwise
     */
    public boolean isBusinessType() {
        return profileType.equals(UserType.BUSINESS);
    }

    private static final String BUSINESS = "BUSINESS";
    private static final String CONSUMER = "CONSUMER";
}
