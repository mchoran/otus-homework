package ru.otus.java.pro.hw03.framework;

public class TestReport {

    private int total;
    private int passed;
    private int failed;

    public void registerSuccess() {
        total++;
        passed++;
    }

    public void registerFailure() {
        total++;
        failed++;
    }

    public void print() {

        System.out.println();
        System.out.println("========== TEST REPORT ==========");
        System.out.println("Total  : " + total);
        System.out.println("Passed : " + passed);
        System.out.println("Failed : " + failed);
    }
}