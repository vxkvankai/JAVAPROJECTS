package com.TimUdemy;

public class Main {

    public static void main(String[] args) {

        Wall wall1 = new Wall("West");
        Wall wall2 = new Wall("West");
        Wall wall3 = new Wall("West");
        Wall wall4 = new Wall("West");

        Ceiling ceiling = new Ceiling(12, 55);

        Bed bed = new Bed("Modern", 5, 18, 5, 4);

        Lamp lamp = new Lamp("Classic", false, 75);

        Bedroom bedroom = new Bedroom("Vamsi's", wall1, wall2, wall3, wall4, ceiling, bed, lamp);

        bedroom.makeBed();
        bedroom.getLamp().turnOn();

        Printer printer = new Printer(50, true);
        System.out.println("Initial Page Count = " + printer.getPagesPrinted());
        int pagesPrinted = printer.printPages(4);
        System.out.println("Pages Printed was " + pagesPrinted + "new total print count for printer = " + printer.getPagesPrinted());
        pagesPrinted = printer.printPages(2);
        System.out.println("Pages Printed was " + pagesPrinted + "new total print count for printer = " + printer.getPagesPrinted());


    }
}
