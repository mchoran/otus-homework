package ru.otus.java.pro.hw03.framework;


import ru.otus.java.pro.hw03.annotations.After;
import ru.otus.java.pro.hw03.annotations.Before;
import ru.otus.java.pro.hw03.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public final class ReflectionScanner {

    private ReflectionScanner() {
    }

    public static TestSuite scan(Class<?> clazz) {

        List<Method> beforeMethods =
                findAnnotatedMethods(clazz, Before.class);

        List<Method> testMethods =
                findAnnotatedMethods(clazz, Test.class);

        List<Method> afterMethods =
                findAnnotatedMethods(clazz, After.class);

        return new TestSuite(
                clazz,
                beforeMethods,
                testMethods,
                afterMethods
        );
    }

    private static List<Method> findAnnotatedMethods(
            Class<?> clazz,
            Class annotation) {

        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .toList();
    }

}