package org.studyeasy.vehicles;

import org.studyeasy.parent.Vehicle;

public class Bike extends Vehicle {

    private String handle;

    public Bike()
    {
        super();
        this.handle = "short";
    }


    @Override
    public String toString()
    {
        return "Bike{" +
                "handle='" + handle + '\'' +
                "} " + super.toString();
    }

    public Bike(String handle, String engine, int wheels, int seats, int fulelTank, String lights)
    {
        super(engine, wheels, seats, fulelTank, lights);
        this.handle = handle;
    }



    public String getHandle()
    {
        return handle;
    }

    @Override
    public void run()
    {
        System.out.println("Running Bike");
       System.out.println(toString());
    }
}
