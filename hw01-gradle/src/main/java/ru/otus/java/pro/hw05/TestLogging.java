package ru.otus.java.pro.hw05;

public class TestLogging implements TestLoggingInterface {

    @Override
    @Log
    public void calculation(int param) {
        System.out.println("Inside calculation(int)");
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        System.out.println("Inside calculation(int, int)");
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.println("Inside calculation(int, int, String)");
    }

    @Override
    public void withoutLogging(int param) {
        System.out.println("Inside withoutLogging");
    }
}
