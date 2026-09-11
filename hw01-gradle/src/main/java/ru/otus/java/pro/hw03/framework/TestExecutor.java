package ru.otus.java.pro.hw03.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestExecutor {

    public TestReport execute(TestSuite suite) {

        TestReport report = new TestReport();

        for (Method testMethod : suite.getTestMethods()) {

            executeSingleTest(suite, testMethod, report);

        }

        return report;
    }

    private void executeSingleTest(
            TestSuite suite,
            Method testMethod,
            TestReport report) {

        Object testInstance = createTestInstance(suite);

        try {

            MethodInvoker.invokeAll(
                    testInstance,
                    suite.getBeforeMethods());

            invokeTest(testInstance, testMethod);

            report.registerSuccess();

            System.out.printf("[PASS] %s%n", testMethod.getName());

        } catch (Exception e) {

            report.registerFailure();

            System.out.printf(
                    "[FAIL] %s -> %s%n",
                    testMethod.getName(),
                    getRootCause(e).getMessage());

        } finally {

            runAfterMethods(testInstance, suite);

        }

    }

    private Object createTestInstance(TestSuite suite) {

        try {

            return suite.getTestClass()
                    .getDeclaredConstructor()
                    .newInstance();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Cannot create test instance",
                    e);

        }

    }

    private void invokeTest(
            Object instance,
            Method testMethod) throws Exception {

        testMethod.setAccessible(true);

        testMethod.invoke(instance);

    }

    private void runAfterMethods(
            Object instance,
            TestSuite suite) {

        try {

            MethodInvoker.invokeAll(
                    instance,
                    suite.getAfterMethods());

        } catch (Exception e) {

            System.out.println(
                    "After methods finished with error: "
                            + e.getMessage());

        }

    }

    private Throwable getRootCause(Throwable throwable) {

        if (throwable instanceof InvocationTargetException ite) {
            return ite.getTargetException();
        }

        return throwable;

    }

}
