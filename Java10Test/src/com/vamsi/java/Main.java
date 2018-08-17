package com.vamsi.java;

public class Main {

    public static void main(String[] args) {
        System.out.println("Ganesha - Java 10 Test.");
        ProcessHandle currProcess = ProcessHandle.current();
        ProcessHandle.Info currProcessInfo = currProcess.info();
        System.out.println(currProcess.children());
        System.out.println(currProcessInfo);
        System.out.println(currProcess.isAlive());;
    }
}
