package com.vamsi.java;

public class Main {

    static public void main(String vamsi[]) {
        Statictest firstInstance = new Statictest("Ist Instance");
        System.out.println(firstInstance.getName() + " is instance number" + firstInstance.getNumInstances());
        Statictest secondInstance = new Statictest("2nd instance");
        System.out.println(secondInstance.getName() + "is instance number " + secondInstance.getName());


    }
}
