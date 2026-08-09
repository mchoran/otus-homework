package ru.otus.java.pro.hw05;

public class Demo {

    public static void main(String[] args) {

        TestLoggingInterface loggingProxy =
                LoggingProxy.create(
                        new TestLogging(),
                        TestLoggingInterface.class
                );

        loggingProxy.calculation(6);

        loggingProxy.calculation(6, 7);

        loggingProxy.calculation(6, 7, "hello");

        loggingProxy.withoutLogging(100);
    }
}