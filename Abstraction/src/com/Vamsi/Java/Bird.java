package com.Vamsi.Java;

public abstract  class Bird extends Animal implements CanFly{
    public Bird(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println(getName() + " is Pecking");
    }

    @Override
    public void breathe() {
        System.out.println(getName() + ", Breath in, Breath out, Repeat");
    }

    @Override
    public void fly() {
        System.out.println(getName() + " is flapping iots wings");
    }
}


