package ru.otus.java.pro.hw05;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method targetMethod = target.getClass()
                .getMethod(method.getName(), method.getParameterTypes());

        if (targetMethod.isAnnotationPresent(Log.class)) {

            String parameters = formatParameters(args);

            System.out.println(
                    "executed method: "
                            + method.getName()
                            + ", param: "
                            + parameters
            );
        }

        return method.invoke(target, args);
    }

    private String formatParameters(Object[] args) {

        if (args == null || args.length == 0) {
            return "";
        }

        return Arrays.stream(args)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
