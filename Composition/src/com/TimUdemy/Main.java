package com.TimUdemy;

public class Main {

    public static void main(String[] args) {

        Dimensions dimensions = new Dimensions(20, 20, 5);

        Case theCase = new Case("220B", "Dell", "240", dimensions);

        Monitor theMonitor = new Monitor("27inc", "Acer", 27, new Resolution(2450, 1440));

        Motherboard theMoherBoard = new Motherboard("BJ-22", "Asus", 4, 6, "V2.2348");

        PC thePC = new PC(theCase, theMonitor, theMoherBoard);

       thePC.powerUp();




    }
}
