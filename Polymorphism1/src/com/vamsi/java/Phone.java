package com.vamsi.java;

public class Phone {

    private String model;

    public Phone(String model)
    {
        this.model = model;
    }

    public void features()
    {
        System.out.println("feature phone");
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }


}
