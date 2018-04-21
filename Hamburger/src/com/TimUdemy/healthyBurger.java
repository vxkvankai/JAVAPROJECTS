package com.TimUdemy;

public class healthyBurger extends Hamburger{

    private String healthyExtr1Nname;
    private double healthyExtr1Price;

    private String healthyExtr2Nname;
    private double healthyExtr2Price;

    public healthyBurger(String meat, double price) {
        super("Healthy", meat, price, "Brown rye");

    }

    public void addHealthyAddition1 (String name, double price){
        this.healthyExtr1Nname = name;
        this.healthyExtr1Price = price;

    }

    public void addHealthyAddition2 (String name, double price){
        this.healthyExtr2Nname = name;
        this.healthyExtr2Price = price;

    }

    @Override
    public double itemizedHamburger() {
        double hamburgerPrice = super.itemizedHamburger();
        if(this.healthyExtr1Nname != null){
            hamburgerPrice += this.healthyExtr1Price;
            System.out.println(" Added " + this.healthyExtr1Nname + " for an extra" + this.healthyExtr1Price);
        }

        if(this.healthyExtr2Nname != null){
            hamburgerPrice += this.healthyExtr2Price;
            System.out.println(" Added " + this.healthyExtr2Nname + " for an extra" + this.healthyExtr2Price);

        }
        return hamburgerPrice;
    }


}
