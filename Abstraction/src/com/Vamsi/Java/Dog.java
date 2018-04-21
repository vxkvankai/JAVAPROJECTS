package com.Vamsi.Java;

public class Dog extends Animal {
    private String name;
    
    public Dog(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println(getName() + " is eating");
    }

    @Override
    public void breathe() {
        System.out.println(getName() + ", Breathe in, Breather out, repeat");
    }
}
