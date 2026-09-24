package ru.otus.java.pro.hw09.core.sessionmanager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class TransactionRunnerJdbc implements TransactionRunner {

    private final DataSource dataSource;

    public TransactionRunnerJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T doInTransaction(Function<Connection, T> action) {
        try (var connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                var result = action.apply(connection);
                connection.commit();
                return result;
            } catch (RuntimeException e) {
                rollback(connection, e);
                throw e;
            }
        } catch (SQLException e) {
            throw new TransactionRunnerException(e);
        }
    }

    private static void rollback(Connection connection, RuntimeException originalException) {
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }
}
