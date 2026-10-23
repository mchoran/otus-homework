package ru.otus.java.pro.hw09.core.repository.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbExecutorImpl implements DbExecutor {

    @Override
    public <T> List<T> executeSelect(
            Connection connection,
            String sql,
            List<Object> parameters,
            ResultSetHandler<T> resultSetHandler) {
        try (var preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, parameters);
            try (var resultSet = preparedStatement.executeQuery()) {
                var result = new ArrayList<T>();
                while (resultSet.next()) {
                    result.add(resultSetHandler.handle(resultSet));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DbExecutorException(e);
        }
    }

    @Override
    public long executeStatement(Connection connection, String sql, List<Object> parameters) {
        try (var preparedStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(preparedStatement, parameters);
            var affectedRows = preparedStatement.executeUpdate();
            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                return generatedKeys.next() ? generatedKeys.getLong(1) : affectedRows;
            }
        } catch (SQLException e) {
            throw new DbExecutorException(e);
        }
    }

    private static void setParameters(
            PreparedStatement preparedStatement,
            List<Object> parameters) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            preparedStatement.setObject(index + 1, parameters.get(index));
        }
    }
}
