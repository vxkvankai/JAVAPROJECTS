package com.vamsi.java;

public class Main {

    public static void main(String[] args)
    {

        Laptop lappy = new Laptop();
        //Processor processor = new Processor();
        //GraphicsCard graphicsCard = new GraphicsCard();
        //System.out.println(lappy.getProcessor().getBrand());

          Processor processor = new Processor("intel", "test", 7,5,4,"190M", "10mhz","234hzs", "234h");
Laptop laptop = new Laptop("234", processor, "ram", "259", new GraphicsCard("mybrand", 23, "tsettstst"),"test", "myKb");
        System.out.println(laptop);

    }
}
