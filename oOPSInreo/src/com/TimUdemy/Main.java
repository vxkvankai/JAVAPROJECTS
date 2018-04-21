package com.TimUdemy;


import com.OopsConcepts.VipCustomer;

public class Main {

    public static void main(String[] args) {

        Account vamsiAccount = new Account();
        vamsiAccount.withdrawal(100);
        vamsiAccount.deposit(50);
        vamsiAccount.withdrawal(100);
        vamsiAccount.deposit(51);
        vamsiAccount.withdrawal(100);
        vamsiAccount.setCustomerEmailAddress("v@v.com");
        vamsiAccount.setBalance(0);
        vamsiAccount.setNumber("1231231231");
        vamsiAccount.setCustomerPhoneNumber("4027875656");


        VipCustomer customer = new VipCustomer();
        System.out.println(customer.getName());

        VipCustomer customer1 = new VipCustomer("Bob", 25000);
        System.out.println(customer1.getName());

        VipCustomer customer3 = new VipCustomer("Tim", 100, "tim@tim.com");
        System.out.println(customer3.getName());






    }
}
