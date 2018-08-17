package com.vamsi.java;

import java.util.ArrayList;

public class Main {
    static ArrayList<String> listNames = new ArrayList<String>();


    public static void main(String[] args)
    {
        listNames.add("Ganesha");
        listNames.add("Lakshmi");
        listNames.add("vishnu");
        listNames.add("Kubera");
        listNames.add("all Gods");
        listNames.add("non gods");
        listNames.add("temps");
        listNames.add("humans");
        listNames.add("computers");
        Main main = new Main();
        main.displayList(listNames);
        main.removeByNameString("non gods");
        main.removeNameByString(5);
        System.out.println(listNames);
        System.out.println(listNames.get(0));
        System.out.println(listNames.get(1));
        System.out.println(listNames.get(4));
             main.modifyName(5, "vamsi");
        System.out.println(listNames);
        
    }

    void displayList(ArrayList<String> names)
    {
        for (String name : names)
        {
            System.out.println(name);

        }

    }

    void removeNameByString(int position)
    {
        listNames.remove(position);
    }

    void removeByNameString(String name)
    {
        listNames.remove(6);
    }

    void modifyName(int position, String newName){
        listNames.set(position, newName);

    }
}
