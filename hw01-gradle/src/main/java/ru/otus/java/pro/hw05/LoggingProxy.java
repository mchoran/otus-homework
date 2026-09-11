package ru.otus.java.pro.hw05;

import java.lang.reflect.Proxy;

public final class LoggingProxy {

    private LoggingProxy() {
    }

    public static <T> T create(T target, Class<T> interfaceClass) {

        Object proxy = Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class<?>[]{interfaceClass},
                new LoggingInvocationHandler(target)
        );

        return interfaceClass.cast(proxy);
    }
}