package com.TimUdemy;

public class Account {

    private String Number;
    private double balance;
    private String customerName;
    private String customerEmailAddress;
    private String customerPhoneNumber;

    public Account(){
        System.out.println("Empty constructor called");
    }

    public Account(String number, double balance, String customerName, String customerEmailAddress, String customerPhoneNumber){


    }






    public void deposit (double depositAmount){
        this.balance += depositAmount;
    }

    public void withdrawal (double withdrawalAmount){
        if (this.balance - withdrawalAmount <0){
            System.out.println(" only " + this.balance + " avaialbe. Withdrawl not processed");
        } else {
            this.balance -= withdrawalAmount;
            System.out.println(" Withdrawal of " + withdrawalAmount + "processed. Remaining balance = " + this.balance);
        }

    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        balance = balance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    public void setCustomerEmailAddress(String customerEmailAddress) {
        this.customerEmailAddress = customerEmailAddress;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }
}
