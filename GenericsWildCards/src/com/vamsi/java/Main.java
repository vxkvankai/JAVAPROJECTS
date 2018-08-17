package com.vamsi.java;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

class Vehicle {
    private int VehicleId;

    public String getVehicleModel()
    {
        return VehicleModel;
    }

    @Override
    public String toString()
    {
        return "Vehicle{" +
                "VehicleId=" + VehicleId +
                ", VehicleModel='" + VehicleModel + '\'' +
                '}';
    }

    public Vehicle(String vehicleModel)
    {
        VehicleModel = vehicleModel;
    }

    private String VehicleModel;


    public Vehicle()
    {
    }

    public Vehicle(int vehicleId)
    {
        VehicleId = vehicleId;
    }

    public int getVehicleId()
    {
        return VehicleId;
    }

    void info(){
        System.out.println("Vehicle model is "+ getVehicleModel());
    }


}

@Builder
class Car extends Vehicle {
    private String CarModel;

    public Car(int vehicleId, String carModel)
    {
        super(vehicleId);
        CarModel = carModel;
    }

    public String getCarModel()
    {
        return CarModel;
    }

    @Override
    public String
    toString()
    {
        return "Car{" +
                "CarModel='" + CarModel + '\'' +
                "} " + super.toString();
    }
    void info(){
        System.out.println("Car model is "+ getCarModel());
    }
}

public class Main {

    public static void main(String[] args)
    {
        List<Vehicle> list = new ArrayList<>();
        list.add(new Vehicle(10));
        list.add(new Vehicle(11));
        list.add(new Vehicle(12));
        list.add(new Vehicle(13));
        list.add(new Car(14, "A14"));
        

        display(list);
    }

    public static void display(List<? super Car> list)
    {
        for (Object element : list)
        {
            System.out.println(element);
        }

    }
}
