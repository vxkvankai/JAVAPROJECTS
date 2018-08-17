package com.vamsi.java;

public class Processor {

    public String brand;
    private String series;
    private int generation;
    private int core;
    private int threads;
    private String cacheMemory;
    private String frequency;
    private String MinFrequency;
    private String MaxFrequency;
//
//    public Processor()
//    {
//
//        this.brand = "Asus";
//        this.series = "Series 29";
//        this.generation = 5;
//        this.core = 7;
//        this.threads = 10;
//        this.cacheMemory = "cache28";
//        this.frequency = "fr1";
//        MinFrequency = "min24Hzs";
//        MaxFrequency = "Max2897Hzs";
//    }

    public Processor(String brand, String series, int generation, int core, int threads, String cacheMemory, String frequency, String minFrequency, String maxFrequency)
    {
        this.brand = brand;
        this.series = series;
        this.generation = generation;
        this.core = core;
        this.threads = threads;
        this.cacheMemory = cacheMemory;
        this.frequency = frequency;
        MinFrequency = minFrequency;
        MaxFrequency = maxFrequency;
    }

    @Override
    public String toString()
    {
        return "Processor{" +
                "brand='" + brand + '\'' +
                ", series='" + series + '\'' +
                ", generation=" + generation +
                ", core=" + core +
                ", threads=" + threads +
                ", cacheMemory='" + cacheMemory + '\'' +
                ", frequency='" + frequency + '\'' +
                ", MinFrequency='" + MinFrequency + '\'' +
                ", MaxFrequency='" + MaxFrequency + '\'' +
                '}';
    }

    public String getBrand()
    {
        return brand;
    }

    public String getSeries()
    {
        return series;
    }

    public int getGeneration()
    {
        return generation;
    }

    public int getCore()
    {
        return core;
    }

    public int getThreads()
    {
        return threads;
    }

    public String getCacheMemory()
    {
        return cacheMemory;
    }

    public String getFrequency()
    {
        return frequency;
    }

    public String getMinFrequency()
    {
        return MinFrequency;
    }

    public String getMaxFrequency()
    {
        return MaxFrequency;
    }
}
