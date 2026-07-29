package ru.otus.java.pro.hw03.demo;


import ru.otus.java.pro.hw03.annotations.After;
import ru.otus.java.pro.hw03.annotations.Before;
import ru.otus.java.pro.hw03.annotations.Test;

public class CalculatorTest {

    private Calculator calculator;

    @Before
    public void init() {

        calculator = new Calculator();

        System.out.println("Before");

    }

    @After
    public void destroy() {

        System.out.println("After");

    }

    @Test
    public void sumTest() {

        int result = calculator.sum(5, 3);

        if (result != 8) {
            throw new RuntimeException("Wrong sum");
        }

    }

    @Test
    public void multiplyTest() {

        int result = calculator.multiply(4, 7);

        if (result != 28) {
            throw new RuntimeException("Wrong multiply");
        }

    }

    @Test
    public void divideTest() {

        int result = calculator.divide(20, 5);

        if (result != 4) {
            throw new RuntimeException("Wrong divide");
        }

    }

    @Test
    public void failedTest() {

        calculator.divide(10, 0);

    }

}