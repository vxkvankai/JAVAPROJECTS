package com.vamsi.java;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class Names implements Comparable<Names> {
    private String name;

    public Names(String name)
    {
        this.name = name;
    }

    @Override
    public int compareTo(Names obj)
    {
        if (name.length() == obj.name.length())
        {
            return 0;
        }
        else if (name.length() < obj.name.length())
        {
            return -1;
        }
        else
        {
            return 1;
        }

    }

    @Override
    public String toString()
    {
        return "Names{" +
                "name='" + name + '\'' +
                '}';
    }
}


public class Main {

    public static void main(String[] args)
    {
        List<Object> elements = new LinkedList<>();
        elements.add(new Names("Vamsi"));
        elements.add(new Names("Chaand"));
        elements.add(new Names("john"));
        elements.add(new Names("Don"));
        elements.add(new Names("Don"));

        elements.add(1);
        elements.add(2.0);
        elements.add('c');
        elements.add('*');


        Main main = new Main();
        main.printList(elements);


    }

    void printList(List<Object> list)
    {
        ListIterator<Object> iterator = list.listIterator();
        while (iterator.hasNext())
        {
            System.out.println("Element: " + iterator.next());
        }
    }


}
