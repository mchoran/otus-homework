package ru.otus.java.pro.hw06;

import ru.otus.java.pro.hw06.atm.Atm;
import ru.otus.java.pro.hw06.atm.AtmFactory;
import ru.otus.java.pro.hw06.domain.CashBundle;
import ru.otus.java.pro.hw06.domain.Denomination;
import ru.otus.java.pro.hw06.exception.CannotDispenseException;

public class AtmDemo {

    public static void main(String[] args) {
        Atm atm = AtmFactory.createDefault();

        atm.deposit(Denomination.FIVE_THOUSAND, 2);
        atm.deposit(Denomination.ONE_THOUSAND, 5);
        atm.deposit(Denomination.FIVE_HUNDRED, 4);
        atm.deposit(Denomination.ONE_HUNDRED, 10);
        atm.deposit(Denomination.FIFTY, 6);

        System.out.println("Баланс после внесения: " + atm.getBalance());

        CashBundle bundle = atm.withdraw(5800);
        System.out.println("Выдано 5800: " + bundle.banknotes());
        System.out.println("Количество банкнот: " + bundle.totalCount());
        System.out.println("Баланс после выдачи: " + atm.getBalance());

        try {
            atm.withdraw(30);
        } catch (CannotDispenseException e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage());
            System.out.println("Баланс не изменился: " + atm.getBalance());
        }
    }
}
