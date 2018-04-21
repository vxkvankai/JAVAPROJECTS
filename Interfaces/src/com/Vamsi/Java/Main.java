package com.Vamsi.Java;

public class Main {

    public static void main(String[] args) {
        ITelephone vamsisPhone;
        vamsisPhone = new DeskPhone(1234567);
        vamsisPhone.powerOn();
        vamsisPhone.callPhone(123321);
        vamsisPhone.answer();

        vamsisPhone = new MobilePhone(234234);
        vamsisPhone.powerOn();
        vamsisPhone.callPhone(234234);
        vamsisPhone.answer();



    }
}
