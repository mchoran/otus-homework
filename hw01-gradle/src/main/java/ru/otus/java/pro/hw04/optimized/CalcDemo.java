package ru.otus.java.pro.hw04.optimized;

import java.time.LocalDateTime;

/*
    256 Мб - 793 мс оптимальный
    512 Мб - 713 мс
    768 Мб - 742 мс
    1024 Мб - 824 мс
    2048 Мб - 763 мс
 */

public class CalcDemo {

    public static void main(String[] args) {
        long counter = 100_000_000;
        Summator summator = new Summator();
        Data data = new Data(0);
        long startTime = System.currentTimeMillis();

        for (int idx = 0; idx < counter; idx++) {
            data.setValue(idx);
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
