package com.vamsi.java;

public abstract class Person implements LiveLife, IsAlive{

    public void speak()
    {
        System.out.println("Shares her thoughts");
    }

    public abstract void eat();

    @Override
    public void breathe()
    {
        System.out.println("be alive; remain living");
    }



    @Override
    public void message()
    {
        System.out.println("life is a journey keep moving and meet new people");
    }
}
