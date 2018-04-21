package com.TimUdemy;


public class Main {

    public static void main(String[] args) {

        Hamburger hamburger = new Hamburger("Basic", "Sausage", 3.56, "White");

        double price = hamburger.itemizedHamburger();
hamburger.addHamburgerAddition1("Tomato", 0.27);
hamburger.addHamburgerAddition2("Lettuce", 0.75);
hamburger.addHamburgerAddition3("Cheese", 1.13);
        System.out.println("Totla Burger Price is " + hamburger.itemizedHamburger() );
        healthyBurger healthyBurger = new healthyBurger("Bacon", 5.67);
        healthyBurger.itemizedHamburger();
        healthyBurger.addHamburgerAddition1("Egg", 5.43);
        healthyBurger.addHamburgerAddition1("Lentils", 3.41);
        System.out.println("Total Healthy Burger Price is " + healthyBurger.itemizedHamburger());

        deluxeBurger deluxeBurger = new deluxeBurger();
        deluxeBurger.itemizedHamburger();
        deluxeBurger.addHamburgerAddition1("should not do this", 50.33);
        deluxeBurger.itemizedHamburger();

    }


}
