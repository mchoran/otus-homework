package ru.otus.java.pro.hw09.core.repository.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DbExecutor {

    <T> List<T> executeSelect(
            Connection connection,
            String sql,
            List<Object> parameters,
            ResultSetHandler<T> resultSetHandler);

    long executeStatement(Connection connection, String sql, List<Object> parameters);

    @FunctionalInterface
    interface ResultSetHandler<T> {
        T handle(ResultSet resultSet) throws SQLException;
    }
}
