package com.d3.datawrappers.recipient.base;

public enum RecipientWho {
    SELF("Self"),
    PERSON("Person"),
    COMPANY("Company"),
    ACCOUNT("Account");
    
    String who;
    
    RecipientWho(String who) {
        this.who=who;
   }

    public String value() {
        return who;
    }

}

