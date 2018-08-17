package org.studyeasy.parent;

public class Vehicle {

    private String engine;
    private int wheels;
    private int seats;
    private int fulelTank;
    private String lights;

    public Vehicle()
    {
        this.engine = "gasoline";
        this.wheels = 4;
        this.seats = 4;
        this.fulelTank = 35;
        this.lights = "LED";
    }

    public Vehicle(String engine, int wheels, int seats, int fuleTank, String lights)
    {
        this.engine = engine;
        this.wheels = wheels;
        this.seats = seats;
        this.fulelTank = fuleTank;
        this.lights = lights;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Vehicle{");
        sb.append("engine='").append(engine).append('\'');
        sb.append(", wheels=").append(wheels);
        sb.append(", seats=").append(seats);
        sb.append(", fulelTank=").append(fulelTank);
        sb.append(", lights='").append(lights).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getEngine()
    {
        return engine;
    }

    public int getWheels()
    {
        return wheels;
    }

    public int getSeats()
    {
        return seats;
    }

    public int getFuleTank()
    {
        return fulelTank;
    }

    public String getLights()
    {
        return lights;
    }

    public void run()
    {
        System.out.println("Running Vehicle");
        System.out.println(toString());
    }
}
