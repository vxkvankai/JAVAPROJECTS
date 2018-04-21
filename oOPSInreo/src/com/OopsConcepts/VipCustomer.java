package com.OopsConcepts;

public class VipCustomer {
    private  String name;
    private double credit;
    private String email;

    public VipCustomer(){
        this("default name", 50000, "vk@gmail.com");
    }

    public VipCustomer(String name, double credit) {
        this.name = name;
        this.credit = credit;
    }

    public VipCustomer(String name, double credit, String email) {
        this.name = name;
        this.credit = credit;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public double getCredit() {
        return credit;
    }

    public String getEmail() {
        return email;
    }
}
