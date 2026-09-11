package ru.otus.java.pro.hw03.framework;

import java.lang.reflect.Method;
import java.util.List;

public final class MethodInvoker {

    private MethodInvoker() {
    }

    public static void invokeAll(
            Object instance,
            List<Method> methods) throws Exception {

        for (Method method : methods) {
            method.setAccessible(true);
            method.invoke(instance);
        }

    }

}
