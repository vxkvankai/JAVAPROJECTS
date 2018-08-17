package com.vamsi.java;

public class Main {

    public static void main(String[] args)
    {

        //Door door = new Door();
        if (new Door().isLocked(args[0]))
        {
            System.out.println("Shop has close, please visit later");
        }
        else
        {
            System.out.println("Welcome, we are open: ");

        }
        System.out.println(args[0]);

    }
}
