package com.TimUdemy;

public class Car {
    private boolean engine;
    private int cyllinders;
    private String name;
    private int wheels;

    public Car(int cyllinders, String name) {
        this.cyllinders = cyllinders;
        this.name = name;
        this.wheels = 4;
        this.engine = true;
    }

    public int getCyllinders() {
        return cyllinders;
    }

    public String getName() {
        return name;
    }

    public String startEngine(){
        return " Car -> startEngine() ";
    }

    public String accelerate(){
        return " Car -> accelerate() ";
    }

    public String brake(){
        return " Car -> brake() ";
    }

    class Mithsubishi extends Car{
        public Mithsubishi(int cyllinders, String name) {
            super(cyllinders, name);
        }

        @Override
        public String startEngine() {
            return " Mitshubishi -> startEngine() ";
        }

        @Override
        public String accelerate() {
            return " Mitshubishi -> accelerate() ";
        }

        @Override
        public String brake() {
            return " Mitshubishi -> brake() ";
        }
    }



    }





