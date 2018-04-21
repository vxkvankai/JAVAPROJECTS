package com.TimUdemy;

class car {
    private boolean engine;
    private int cylinders;
    private String name;
    private int wheels;

    public car(int cylinders, String name) {
        this.cylinders = cylinders;
        this.name = name;
        this.wheels = 4;
        this.engine = true;
    }

    public int getCylinders() {
        return cylinders;
    }

    public String getName() {
        return name;
    }


    public String startEngine() {
        return "Car -> startEngine()";
    }

    public String accelerate() {
        return "Car -> accelerate()";
    }

    public String brake() {
        return "Car -> brake()";
    }


}

public class Main {

    public static void main(String[] args) {
        car cars = new car(8, "Base car");
        System.out.println();
        System.out.println();
        System.out.println(cars.startEngine());
        System.out.println(cars.accelerate());
        System.out.println(cars.brake());
        System.out.println();

        Mitshubishi mitshubishi = new Mitshubishi(6,"Outlander V6");
        System.out.println(mitshubishi.startEngine());
        System.out.println(mitshubishi.accelerate());
        System.out.println(mitshubishi.brake());
        System.out.println();

        Ford Ford = new Ford (6,"Ford V6");
        System.out.println(Ford.startEngine());
        System.out.println(Ford.accelerate());
        System.out.println(Ford.brake());
        System.out.println();

        Holden holden = new Holden (6,"Ford V6");
        System.out.println(holden.startEngine());
        System.out.println(holden.accelerate());
        System.out.println(holden.brake());
    }

    static class Mitshubishi extends car {

        public Mitshubishi(int cylinders, String name) {
            super(cylinders, name);
        }

        @Override
        public String startEngine() {
            return "Mitshubishi -> startEngine()";
        }

        @Override
        public String accelerate() {
            return "Mitshubishi -> accelerate()";
        }

        @Override
        public String brake() {
            return "Mitshubishi -> brake()";
        }
    }

    static class Ford extends car {

        public Ford (int cylinders, String name) {
            super(cylinders, name);
        }

        @Override
        public String startEngine() {
            return "Ford -> startEngine()";
        }


        @Override
        public String accelerate() {
            return "Ford -> accelerate()";
        }

        @Override
        public String brake() {
            return "Ford -> brake()";
        }
    }

    static class Holden extends car {

        public Holden (int cylinders, String name) {
            super(cylinders, name);
        }

        @Override
        public String startEngine() {
            return getClass().getSimpleName() + " -> startEngine()";
//            return "Holden -> startEngine()";
        }


        @Override
        public String accelerate() {
            return getClass().getSimpleName() + " -> accelerate()";

        }

        @Override
        public String brake() {
            return getClass().getSimpleName()+ " -> brake()";
        }
    }
}