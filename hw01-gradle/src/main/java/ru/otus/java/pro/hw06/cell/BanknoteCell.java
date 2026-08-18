package ru.otus.java.pro.hw06.cell;

import ru.otus.java.pro.hw06.domain.Denomination;

public interface BanknoteCell {

    Denomination denomination();

    void put(int count);

    void take(int count);

    int count();

    default long balance() {
        return (long) count() * denomination().value();
    }
}
