package com.vamsi.java;

public class Main {

    public static void main(String[] args)
    {

        Person pooja = new Person("Pooja", 27, "Female");
        System.out.println(pooja);
        pooja.setAge(40);
        System.out.println(pooja);

    }
}
