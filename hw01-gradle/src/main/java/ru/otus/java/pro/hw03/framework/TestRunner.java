package ru.otus.java.pro.hw03.framework;

public final class TestRunner {

    private TestRunner() {
    }

    public static void run(String className) {

        try {

            Class<?> clazz = Class.forName(className);

            TestSuite suite =
                    ReflectionScanner.scan(clazz);

            TestExecutor executor =
                    new TestExecutor();

            TestReport report =
                    executor.execute(suite);

            report.print();

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(
                    "Test class not found: " + className,
                    e);

        }

    }

}
