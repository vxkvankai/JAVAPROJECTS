package com.vamsi.java;


interface Lambda {
    abstract public void demo();
}


public class Main {
    int x = 10;
    public static void main(String[] args)
    {

        Lambda lambda = new Lambda() {
            @Override
            public void demo()
            {
                System.out.println("Value of x: " +  new Main().x);
            }
        };

        lambda.demo();

    }
}
