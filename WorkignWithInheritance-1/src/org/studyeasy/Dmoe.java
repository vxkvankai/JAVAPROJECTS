package org.studyeasy;


import org.studyeasy.parent.Vehicle;
import org.studyeasy.vehicles.Bike;
import org.studyeasy.vehicles.Car;
import org.studyeasy.vehicles.Truck;

public class Dmoe {

    public static void main(String[] args)
    {
        Bike bike = new Bike("long", "Diesel", 4, 4, 40, "LED");

        Car car = new Car("test1", "ecoone", "vamsiCool", "MyEntertainement", "test", "sony");


        // System.out.println(car);
        Vehicle vehicle = new Vehicle();
        //vehicle.run();
        //bike.run();
        //vehicle.run();
        //car.run();
        Truck truck = new Truck("heavy Streering", "Randome", "three type", "coolaire", 4);
        truck.run();
    }
}