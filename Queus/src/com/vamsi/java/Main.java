package com.vamsi.java;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args)
    {

        Map<Integer, String> student = new HashMap<>();
        student.put(3, "Priyanka");
        student.put(1, "Rahul");
        student.put(2, "Mia");
        student.put(4, "Angel");
        student.put(5, "vamsi");
        student.put(6, "Madan");

//        for (Map.Entry<Integer, String> entry : student.entrySet())
//
//        {
//            System.out.println("Key: " + entry.getKey() + " & Value: " + entry.getValue());
//
//        }
//
//        System.out.println(student.get(3));



        Iterator<Map.Entry<Integer, String>> entry = student.entrySet().iterator();

        while (entry.hasNext())
        {
            Map.Entry  <Integer,String > temp = entry.next();
            System.out.println("Key: " + temp.getKey() + " value: " + temp.getValue());

        }
    }
}

