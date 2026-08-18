package ru.otus.java.pro.hw06.operation;

import ru.otus.java.pro.hw06.domain.Denomination;

public interface CashReceiver {

    void deposit(Denomination denomination, int count);
}
