package com.vamsi.java;

public class Vegan extends Person {

    @Override
    public void eat()
    {
        System.out.println("Vegan eats vegan");
    }

    @Override
    public String toString()
    {
        return "Vegan{} " + super.toString();
    }
}
