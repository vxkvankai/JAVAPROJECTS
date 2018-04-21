package com.TimUdemy;

public class Main {

        public static void main(String[] args) {
            Bank bank = new Bank("First National Bank");

            bank.addBranch("Omaha");

            bank.addCustomer("Omaha Downtown", "vamsi", 50.00);
            bank.addCustomer("Omaha MidTown","Tim", 170.00);
            bank.addCustomer("Bellevue", "Ben", 220.00);
            bank.addCustomer("Gretna", "Shawn", 23.00);

            bank.addBranch("Elkhorn");
            bank.addCustomer("Elkhorn","Bob", 45.89);

            bank.addCustomerTransaction("Papillion", "John", 34.00);
            bank.addCustomerTransaction("La Vista", "Ken", 78.89);
            bank.addCustomerTransaction("Chalo", "Henry", 23.90);

            bank.listCustomers("Elkhorn", false);
        }
    }