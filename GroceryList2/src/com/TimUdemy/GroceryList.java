package com.TimUdemy;

import java.util.ArrayList;

public class GroceryList {

    private ArrayList<String> groceryList = new ArrayList<String>();

    public void addGroceryItem(String item) {
        groceryList.add(item);
    }

    public ArrayList<String> getGroceryList() {
        return groceryList;
    }

    public void printGroceryList() {
        System.out.println("You have " + groceryList.size() + " items in your grocery list");
        for (int i = 0; i < groceryList.size(); i++) {
            System.out.println((i + 1) + ".  " + groceryList.get(i));
        }
    }

    public void modifyGroceryItem(String CurretnItem, String newItem) {
        int position = findItem(CurretnItem);
        if (position >= 0) {
            modifyGroceryItem(position, newItem);

        }
    }

    private void modifyGroceryItem(int position, String newItem) {
        groceryList.set(position, newItem);
    }

    public void removeGroceryItem(String item) {
        int position = findItem(item);
        if (position >= 0) {
            removeGroceryItem(position);
        }

    }

    private void removeGroceryItem(int position) {
        groceryList.remove(position);
    }

    public int findItem(String searchItem) {
//        int position = groceryList.indexOf(searchItem);
//        if (position >= 0) {
//            return groceryList.get(position);
//        }
//        return null;
        return groceryList.indexOf(searchItem);
    }


    public boolean onFile(String searchItem) {
        int position = findItem(searchItem);
        if (position >= 0) {
            return true;

        }
        return false;
    }


}
