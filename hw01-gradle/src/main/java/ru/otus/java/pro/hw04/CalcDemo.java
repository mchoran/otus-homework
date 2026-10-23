package ru.otus.java.pro.hw04;

import java.time.LocalDateTime;

/*
    256 Мб - 1736 мс
    512 Мб - 1533 мс оптимальный
    768 Мб - 1741 мс
    1024 Мб - 1726 мс
    2048 Мб - 1781 мс
 */

public class CalcDemo {

    public static void main(String[] args) {
        long counter = 100_000_000;
        Summator summator = new Summator();
        long startTime = System.currentTimeMillis();

        for (int idx = 0; idx < counter; idx++) {
            Data data = new Data(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));
    }
}
