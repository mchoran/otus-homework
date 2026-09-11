package ru.otus.java.pro.hw06.operation;

import ru.otus.java.pro.hw06.domain.CashBundle;

public interface CashDispenser {

    CashBundle withdraw(int amount);
}
