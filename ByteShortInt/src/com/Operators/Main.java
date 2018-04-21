package com.Operators;

public class Main {

    public static void main(String[] args) {
        // int has a width of 32
	    int myMinValue = -2_147_483_468;
        int myMaxValue = 2_147_483_468;
        int myTotal = (myMinValue/2);
        System.out.println("myTotal = " +myTotal);
        // byte has a width of 8
        byte myByteValue = -128;
        byte myNewByteValue = (byte) (myByteValue/2);
        System.out.println("myNewByteValue: " + myNewByteValue);

        // short has a width of 16
        short myShortValue = -32768;
short myNewShortValue = (short)(myShortValue / 2);

        // long has width of 64 bits
        long myLongValue = 100L;

        byte b = 5;
        short s = 10;
        int i = 22;
        long l = 50000L + (10L * (b + s + i));
        System.out.println(l);


    }
}
