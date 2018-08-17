package org.studyeasy.vehicles;

import org.studyeasy.parent.Vehicle;

public class Car extends Vehicle {

    private String steering;
    private String musicSystem;
    private String seatBelt;
    private String airConditioner;
    private String fridge;
    private String entertainmentSystem;

//    public Car()
//    {
//    }

    public Car(String steering, String musicSystem, String seatBelt, String airConditioner, String fridge, String entertainmentSystem)
    {
        this.steering = "square";
        this.musicSystem = "Panasonice";
        this.seatBelt = "3 type";
        this.airConditioner = "coolMan";
        this.fridge = "frigidaire";
        this.entertainmentSystem = "aginSony";
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

    public String getFridge()
    {
        return fridge;
    }

    public String getEntertainmentSystem()
    {
        return entertainmentSystem;
    }

    @Override
    public String toString()
    {
        return "Car{" +
                "steering='" + steering + '\'' +
                ", musicSystem='" + musicSystem + '\'' +
                ", seatBelt='" + seatBelt + '\'' +
                ", airConditioner='" + airConditioner + '\'' +
                ", fridge='" + fridge + '\'' +
                ", entertainmentSystem='" + entertainmentSystem + '\'' +
                "} " + super.toString();
    }

    @Override
    public void run()
    {
        System.out.println("car running");
        System.out.println(toString());
    }
}
