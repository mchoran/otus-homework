package ru.otus.java.pro.hw06.atm;

import ru.otus.java.pro.hw06.operation.BalanceProvider;
import ru.otus.java.pro.hw06.operation.CashDispenser;
import ru.otus.java.pro.hw06.operation.CashReceiver;

public interface Atm extends CashReceiver, CashDispenser, BalanceProvider {
}
