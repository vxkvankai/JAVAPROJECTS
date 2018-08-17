package com.vamsi.java;

class Data<K extends Integer, V extends Main> {
    private K key;
    private V value;

    public Data(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        return key;
    }

    public V getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "Data{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public <E extends Character, N extends Number> void display(E element, N number)
    {
        System.out.println("Element: " + element + " Number: " + number);
    }

}


public class Main {

    public static void main(String[] args)
    {
        Data<Integer, Main> data = new Data<Integer, Main>(1, new Main());
        data.display('#', 1.2f);
        data.getValue().test();
        System.out.println(data.getKey());

    }

    public void test()
    {
        System.out.println("Testing .......");
    }
}
