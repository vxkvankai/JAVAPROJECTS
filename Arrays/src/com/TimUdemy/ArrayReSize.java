package com.TimUdemy;

import java.util.Scanner;

public class ArrayReSize {

    private static Scanner s = new Scanner(System.in);
    private static int[] baseData = new int[10];

    public static void main(String[] args){
        System.out.println("Enter 10 digits: ");
        getInput();
        printArray(baseData);
        resieArray();
        System.out.println("Enter 12 digits now: ");
        getInput();
        printArray(baseData);
    }

    private static void getInput(){
        for(int i = 0; i<baseData.length; i++){
            baseData[i] = s.nextInt();
        }
    }

    private static void printArray(int[] arr){
        for(int i=0; i<arr.length; i++ ){
            System.out.println(arr[i] + "  ");
            System.out.println();
        }

    }

    private static void resieArray(){
        int[] original = baseData;

        baseData = new int[12];
        for(int i = 0; i<original.length; i++){
            baseData[i] = original[i];

        }


    }





}
