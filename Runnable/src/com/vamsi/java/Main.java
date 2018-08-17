package com.vamsi.java;

import java.util.Random;

class MyCounter implements Runnable {

    private int threadNo;

    public MyCounter(int threadNo)
    {
        this.threadNo = threadNo;
    }

    @Override
    public void run()
    {
        Random random = new Random();
        for (int i = 0; i <= 9; i++)
            try
            {
                {          Thread.sleep(random.nextInt(500));
                    System.out.println("The value if i is: " + i + "and the thread number is: " + threadNo);
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
    }
}


public class Main {

    public static void main(String[] args)
    {
//        Thread thread1 = new Thread(new MyCounter(1));
//        Thread thread2 = new Thread(new MyCounter(2));
//
//        thread1.start();
//        thread2.start();
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            for (int i =1; i<10; i++){
                                System.out.println(i);
                            } ;
                        }
                    }

    }
}
