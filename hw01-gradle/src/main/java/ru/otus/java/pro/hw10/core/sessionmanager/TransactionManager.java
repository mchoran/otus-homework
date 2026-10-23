package ru.otus.java.pro.hw10.core.sessionmanager;

import org.hibernate.Session;

import java.util.function.Function;

public interface TransactionManager {

    <T> T doInTransaction(Function<Session, T> action);
}
