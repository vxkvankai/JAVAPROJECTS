package org.studyeasy.vehicles;

import org.studyeasy.parent.Vehicle;

public class Truck extends Vehicle {
    private String steering;
    private String musicSystem;
    private String seatBelt;
    private String airConditioner;
    private int container;

//    public Truck()
//    {
//    }

    public Truck(String steering, String musicSystem, String seatBelt, String airConditioner, int container)
    {
        this.steering = "Circular";
        this.musicSystem = "Sony";
        this.seatBelt = "type 2";
        this.airConditioner = "lennoxx";
        this.container = 4;
    }

    public String getSteering()
    {
        return steering;
    }

    public String getMusicSystem()
    {
        return musicSystem;
    }

    public String getSeatBelt()
    {
        return seatBelt;
    }

    public String getAirConditioner()
    {
        return airConditioner;
    }

    public int getContainer()
    {
        return container;
    }

    @Override
    public String toString()
    {
        return "Truck{" +
                "steering='" + steering + '\'' +
                ", musicSystem='" + musicSystem + '\'' +
                ", seatBelt='" + seatBelt + '\'' +
                ", airConditioner='" + airConditioner + '\'' +
                ", container=" + container +
                "} " + super.toString();
    }

    @Override
    public void run()
    {
        System.out.println("Truck Running");
        System.out.println(toString());
    }
}

