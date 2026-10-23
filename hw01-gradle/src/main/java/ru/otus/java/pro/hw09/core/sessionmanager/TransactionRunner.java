package ru.otus.java.pro.hw09.core.sessionmanager;

import java.sql.Connection;
import java.util.function.Function;

public interface TransactionRunner {

    <T> T doInTransaction(Function<Connection, T> action);
}
