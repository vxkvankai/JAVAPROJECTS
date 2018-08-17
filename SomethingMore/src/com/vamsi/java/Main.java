package com.vamsi.java;

import java.util.*;

class Name implements Comparable<Name>{
    private String name;

    public Name(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

   

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Name))
        {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(getName(), name1.getName());
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(getName());
    }

    @Override
    public String toString()
    {
        return "Name{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Name o)
    {
        return getName().compareTo(o.getName());
    }
}

public class Main {

    public static void main(String[] args) {

        Set<Name> set = new HashSet<>();
        set.add(new Name("Chaand"));
        set.add(new Name ("John"));
        set.add(new Name ("Aafiya"));
        set.add(new Name ("Arman"));
        set.add(new Name ("Mia"));
        set.add(new Name ("Chaand"));

//        for (String name: set
//             )
//        {
//            System.out.println(name);
//
//        }
        List<Name> list = new ArrayList<Name>();
        list.addAll(set);
        Collections.sort(list);

        for (Name name: list
             )
        {
            System.out.println(name);

        }
        System.out.println(Collections.binarySearch(list, new Name("Mia")));

    }


}
