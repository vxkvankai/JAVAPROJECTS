package com.Vamsi.Java;

public class Penguin extends Bird {

    public Penguin(String name) {
        super(name);
    }

    @Override
    public void fly() {
        super.fly();
        System.out.println("I do not fly, i swim instead");
    }
}
