package com.vamsi.java;

public class Statictest {

    private int numInstances = 0;
    private String name;

    public Statictest(String name) {
        this.name = name;
        numInstances++;
        return;
    }

    public Statictest(int numInstances, String name) {
        this.numInstances = numInstances;
        this.name = name;
        return;
    }

    public int getNumInstances() {
        return numInstances;
    }

    public String getName() {
        return name;
    }
}
