package com.vamsi.java;

public class Main {

    public static void main(String[] args)
    {
//        Phone phone = new Phone("Nokia 33");
//        System.out.println(phone.getModel());
//        phone.features();
//
//        Samsumg note8 = new Samsumg("Note8");

//        System.out.println(note8.getModel());
//          note8.features();


        Phone Nokia3310 = new Main().phone(1); //
        //Phone Nokia3310 = new Nokia("Nokia 3310");
        System.out.println(Nokia3310.getModel());
        Nokia3310.features();

        Phone note8 = new Main().phone(2);
                //Samsumg("Note8");
        System.out.println(note8.getModel());
        note8.features();


    }

    public Phone phone(int dailyDriver){
        switch (dailyDriver){
            case 1: return new Nokia("3310");
            case 2: return new Samsumg("Note 5");
        }
            return null;
    }
}
