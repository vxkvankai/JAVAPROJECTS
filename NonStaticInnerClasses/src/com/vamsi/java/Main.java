package com.vamsi.java;

public class Main {

    public static void main(String[] args)
    {
        Door door = new Door();
        door.shopStatus();
        door.getLock().setLock(false);
        door.shopStatus();

    }
}
