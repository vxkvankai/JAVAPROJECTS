package com.TimUdemy;

import java.util.ArrayList;
import java.util.Scanner;

public class GroceryList {

    private static Scanner scanner = new Scanner((System.in));
    private static GroceryList groceryList = new GroceryList();





    //private ArrayList<String> groceryList = new ArrayList<String>();

    public void addGroceryItems(String item) {
        groceryList.add(item);
    }

    public void printGroceryList() {
        System.out.println("You have " + groceryList.size() + " items in your grocery list");
        for (int i = 0; i < groceryList.size(); i++) {
            System.out.println((i + 1) + " . " + groceryList.get(i));

        }
    }

    public void modifyGroceryItem(int position, String newItem) {
        groceryList.set(position, newItem);
        System.out.println("Grocery Item " + (position + 1) + " has been modified.");

    }

    public void removeGroceryItem(int position) {
        String theItem = groceryList.get(position);
        groceryList.remove(position);

    }

    public String findItem(String searchItem) {
        boolean exists = groceryList.contains(searchItem);

        int position = groceryList.indexOf(searchItem);
        if (position >= 0) {
            return groceryList.get(position);

        }
        return null;
    }



    public static void main(String[] args) {


        boolean quit = false;
        int choice = 0;
        printInstructions();

        while (!quit) {
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0:
                    printInstructions();
                case 1:
                    groceryList.printGroceryList();
                case 2:
                    addItem();
                    break;
                case 3:
                    modifyItem();
                    break;
                case 4:
                    removeItem();
                    break;
                case 5:
                    searchForItem();
                    break;
                case 6:
                    quit = true;
                    break;
            }

        }


    }

    public static void printInstructions(){
        System.out.println("\n Press");
        System.out.println("\t 0 - To Print choice options. ");
        System.out.println("\t 1 - To Print list of grocery items. ");
        System.out.println("\t 2 - To add an item to the list. ");
        System.out.println("\t 3 - To modify an item in the list. ");
        System.out.println("\t 4 - To remove an item from the list ");
        System.out.println("\t 5 - To search for an item in the list. ");
        System.out.println("\t 6 - To quit the application. ");

    }

    public static void addItem(){
        System.out.println("Please enter the grocery item: ");
        groceryList.addGroceryItems(scanner.nextLine());
    }

    public static void modifyItem(){
        System.out.println("Enter item number: ");
        int itemNo = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter replacement Item: ");
        String newItem = scanner.nextLine();
        groceryList.modifyGroceryItem(itemNo-1, newItem);

    }

    public static void removeItem(){
        System.out.println("Enter item number: ");
        int itemNo = scanner.nextInt();
        scanner.nextLine();
        groceryList.removeGroceryItem(itemNo);
    }

    public static void searchForItem(){
        System.out.println("Enter item to search for: ");
        String searchItem = scanner.next();
        if(groceryList.findItem(searchItem) != null){
            System.out.println("Found " + searchItem + "in our grocery list");
        } else {
            System.out.println(searchItem + "is not in the shopping list");
        }
    }


}
