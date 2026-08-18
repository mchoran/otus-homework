package ru.otus.java.pro.hw06.exception;

public class CannotDispenseException extends RuntimeException {

    public CannotDispenseException(int amount) {
        super("Невозможно выдать сумму: " + amount);
    }
}
