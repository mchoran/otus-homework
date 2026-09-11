package ru.otus.java.pro.hw03.framework;

import java.lang.reflect.Method;
import java.util.List;

public class TestSuite {

    private final Class<?> testClass;
    private final List<Method> beforeMethods;
    private final List<Method> testMethods;
    private final List<Method> afterMethods;

    public TestSuite(
            Class<?> testClass,
            List<Method> beforeMethods,
            List<Method> testMethods,
            List<Method> afterMethods) {

        this.testClass = testClass;
        this.beforeMethods = beforeMethods;
        this.testMethods = testMethods;
        this.afterMethods = afterMethods;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public List<Method> getBeforeMethods() {
        return beforeMethods;
    }

    public List<Method> getTestMethods() {
        return testMethods;
    }

    public List<Method> getAfterMethods() {
        return afterMethods;
    }

}
