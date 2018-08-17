package com.vamsi.java;

import java.util.ArrayList;
import java.util.List;

class Data {
    public <E> void printListData(List<E> list)
    {
        for (E element : list)
        {
            System.out.println(element);
        }

    }

    public <E> void printArrayData(E[] arrayData)
    {
        for (E element : arrayData
                )

        {
            System.out.println(element);
        }

    }

}


public class Main {

    public static void main(String[] args)
    {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        Data data = new Data();
        System.out.println("Printing integer list");
        data.printListData(list);

        List<String> listA = new ArrayList<>();
        listA.add("one");
        listA.add("two");
        listA.add("three");
        listA.add("four");
        data.printListData(listA);

        String[] stringArray = {"One", "Two", "Three"};
        Integer[] integerArray = {1, 2, 3, 4};
        Data data1 = new Data();
        data1.printArrayData(stringArray);
        data1.printArrayData(integerArray);
        Double[] doubleArray = {1.0, 2.0, 3.0, 4.0};
        data1.printArrayData(doubleArray);

    }
}
