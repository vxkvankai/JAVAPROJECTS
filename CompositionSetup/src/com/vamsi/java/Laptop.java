package com.vamsi.java;

public class Laptop {

    private float screen;
    private Processor processor;
    private String ram;
    private String hardDrive;
    private GraphicsCard graphicsCard;
    private String OpticalDrive;
    private String keyboard;

    public Laptop()
    {

        this.screen = 29.8f;
        this.processor = new Processor("brand1","xyz", 8, 4, 34, "testmemory", "myfrequency", "234", "uoy");
        this.ram = "290";
        this.hardDrive = "HDR 1";
        this.graphicsCard = new GraphicsCard("asusgraphics", 23,"234");
        OpticalDrive = "opticalDrive43";
        this.keyboard = "kbrd xp";
    }

    public Laptop(String screen, Processor processor, String ram, String hardDrive, GraphicsCard graphicsCard, String opticalDrive, String keyboard)
    {
        this.screen = 29.5f;
        this.processor = processor;
        this.ram = ram;
        this.hardDrive = hardDrive;
        this.graphicsCard = graphicsCard;
        OpticalDrive = opticalDrive;
        this.keyboard = keyboard;
    }

    @Override
    public String toString()
    {
        return "Laptop{" +
                "screen=" + screen +
                ", processor=" + processor +
                ", ram='" + ram + '\'' +
                ", hardDrive='" + hardDrive + '\'' +
                ", graphicsCard=" + graphicsCard +
                ", OpticalDrive='" + OpticalDrive + '\'' +
                ", keyboard='" + keyboard + '\'' +
                '}';
    }

    public float getScreen()
    {
        return screen;
    }

    public Processor getProcessor()
    {
        return processor;
    }

    public String getRam()
    {
        return ram;
    }

    public String getHardDrive()
    {
        return hardDrive;
    }

    public GraphicsCard getGraphicsCard()
    {
        return graphicsCard;
    }

    public String getOpticalDrive()
    {
        return OpticalDrive;
    }

    public String getKeyboard()
    {
        return keyboard;
    }

    
}
